package de.interguess.javaflow.common.indexer;

import de.interguess.javaflow.api.index.ExecutableIndex;
import de.interguess.javaflow.api.index.TriggerIndex;
import de.interguess.javaflow.api.index.WorkflowIndex;
import de.interguess.javaflow.api.index.executable.LoopIndex;
import de.interguess.javaflow.api.index.executable.ProcedureIndex;
import de.interguess.javaflow.api.index.executable.RouterIndex;
import de.interguess.javaflow.api.indexer.WorkflowIndexer;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.common.util.NullUtil;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlIndexer implements WorkflowIndexer {

    @Override
    public @NotNull WorkflowIndex index(@NotNull String workflow) {
        try {
            return indexWorkflow(YamlConfiguration.loadConfigurationFromString(workflow));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WorkflowIndex indexWorkflow(ConfigurationSection section) {
        final String name = NullUtil.defaultIfNull(section.getString("name"), "Unnamed Workflow");

        final Map<String, Object> variables = new HashMap<>();

        final ConfigurationSection variablesSection = section.getConfigurationSection("variables");

        if (variablesSection != null) {
            for (String key : variablesSection.getKeys(false)) {
                variables.put(key, variablesSection.get(key));
            }
        }

        final List<TriggerIndex> triggers = new ArrayList<>();

        for (Object triggerElement : section.getList("triggers")) {
            final ConfigurationSection triggerSection = new YamlConfiguration().createSection("section", (Map<?, ?>) triggerElement);

            triggers.add(indexTrigger(triggerSection));
        }

        return WorkflowIndex.builder()
                .name(name)
                .variables(variables)
                .triggers(triggers)
                .build();
    }

    private TriggerIndex indexTrigger(ConfigurationSection section) {
        final String id = section.getString("id");
        final String type = section.getString("type");

        return TriggerIndex.builder()
                .id(id)
                .type(type)
                .tasks(indexExecutableIndexList(section, "tasks"))
                .build();
    }

    private ExecutableIndex indexExecutableElement(ConfigurationSection section) {
        final String id = section.getString("id");
        final String type = section.getString("type");

        if (type.equals("router")) {
            final Map<Object, List<ExecutableIndex>> routes = new HashMap<>();

            final ConfigurationSection routesSection = section.getConfigurationSection("routes");

            for (final String key : routesSection.getKeys(false)) {
                routes.put(key, indexExecutableIndexList(routesSection, key));
            }

            final Object inputObject = section.get("input");

            return RouterIndex.builder()
                    .id(id)
                    .type(type)
                    .routes(routes)
                    .input(inputObject)
                    .build();
        } else if (type.equals("loop")) {
            final ProcedureIndex condition = (ProcedureIndex) indexExecutableElement(section.getConfigurationSection("condition")); //todo: throw exception if type mismatch

            return LoopIndex.builder()
                    .id(id)
                    .type(type)
                    .condition(condition)
                    .tasks(indexExecutableIndexList(section, "tasks"))
                    .build();
        } else {
            final MultiInput.Builder input = MultiInput.create();

            final ConfigurationSection inputSection = section.getConfigurationSection("input");

            for (final String key : inputSection.getKeys(false)) {
                input.with(key, inputSection.get(key));
            }

            return ProcedureIndex.builder()
                    .id(id)
                    .type(type)
                    .input(input)
                    .build();
        }
    }

    private List<ExecutableIndex> indexExecutableIndexList(ConfigurationSection section, String sectionKey) {
        final List<ExecutableIndex> tasks = new ArrayList<>();

        for (final Object task : section.getList(sectionKey)) {
            final ConfigurationSection taskSection = new YamlConfiguration().createSection("section", (Map<?, ?>) task);

            tasks.add(indexExecutableElement(taskSection));
        }

        return tasks;
    }
}
