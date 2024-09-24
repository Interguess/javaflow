package de.interguess.javaflow.common.trigger;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.construct.procedure.ProcedureProvider;
import de.interguess.javaflow.api.construct.trigger.CustomTrigger;
import de.interguess.javaflow.api.construct.trigger.Trigger;
import de.interguess.javaflow.api.construct.trigger.TriggerProvider;
import de.interguess.javaflow.api.exception.TriggerIndexingException;
import de.interguess.javaflow.api.index.ExecutableIndex;
import de.interguess.javaflow.api.index.executable.LoopIndex;
import de.interguess.javaflow.api.index.executable.ProcedureIndex;
import de.interguess.javaflow.api.index.executable.RouterIndex;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import de.interguess.javaflow.api.workflow.WorkflowProvider;
import de.interguess.javaflow.common.ReferenceResolver;
import de.interguess.javaflow.common.util.ClassSearcherUtil;
import de.interguess.javaflow.common.util.GsonUtil;
import de.interguess.javaflow.common.workflow.WorkflowImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class TriggerProviderImpl extends TriggerProvider {

    private final Map<Class<? extends Trigger>, CustomTrigger> indexes;

    public TriggerProviderImpl() {
        this.indexes = new HashMap<>();

        TriggerProvider.setInstance(this);
    }

    @Override
    public void registerTriggers(@NotNull String packageName) {
        ClassSearcherUtil.listAllClasses(ClassLoader.getSystemClassLoader(), packageName).stream()
                .filter(clazz -> clazz.isAnnotationPresent(CustomTrigger.class))
                .forEach(clazz -> {
                    CustomTrigger customTrigger = clazz.getAnnotation(CustomTrigger.class);

                    indexes.put((Class<? extends Trigger>) clazz, customTrigger);
                });
    }

    @Override
    public void callTrigger(@NotNull Trigger trigger, @NotNull MultiInput input) {
        CustomTrigger customTrigger = indexes.get(trigger.getClass());

        if (customTrigger == null) {
            throw new TriggerIndexingException("Trigger class " + trigger.getClass().getName() + " is not indexed!");
        }

        WorkflowProvider.getInstance().getWorkflows().forEach(workflowIndex -> {
            workflowIndex.triggers().stream()
                    .filter(triggerIndex -> triggerIndex.type().equals(customTrigger.id()))
                    .forEach(triggerIndex -> {
                        final Workflow workflow = new WorkflowImpl();

                        triggerIndex.tasks().forEach(task -> {
                            executeFunction(workflow, task);
                        });
                    });
        });
    }

    private @Nullable MultiOutput executeFunction(@NotNull Workflow workflow, @NotNull ExecutableIndex index) {
        switch (index) {
            case LoopIndex loopIndex -> {
                final ProcedureIndex condition = loopIndex.condition();

                while (((boolean) executeFunction(workflow, condition).getValues().get("_result"))) {
                    loopIndex.tasks().forEach(task -> {
                        executeFunction(workflow, task);
                    });
                }

                return null;
            }

            case ProcedureIndex procedureIndex -> {
                Procedure procedure = ProcedureProvider.getInstance().getProcedureById(procedureIndex.type());

                if (procedure == null) {
                    throw new TriggerIndexingException("Procedure not found: " + procedureIndex.type());
                }

                final MultiInput input = MultiInput.create();

                procedureIndex.input().getValues().forEach((key, value) -> {
                    if (value instanceof String string) {
                        input.getValues().put(key, ReferenceResolver.resolve(workflow, string));
                    } else {
                        input.getValues().put(key, value);
                    }
                });

                final MultiOutput output = procedure.execute(workflow, procedureIndex.input());

                if (output != null) {
                    final Map<String, Serializable> outputMap = GsonUtil.deepJsonToMap(new HashMap<>(), output.getValues());

                    for (Map.Entry<String, Serializable> entry : outputMap.entrySet()) {
                        workflow.setVariable(entry.getKey(), entry.getValue());
                    }

                    return output;
                }

                return null;
            }

            case RouterIndex routerIndex -> {
                Object input = routerIndex.input();

                if (input instanceof String string) {
                    input = ReferenceResolver.resolve(workflow, string);
                }

                if (!routerIndex.routes().containsKey(input)) {
                    throw new TriggerIndexingException("No route found for input: " + input);
                }

                routerIndex.routes().get(input).forEach(task -> {
                    executeFunction(workflow, task);
                });

                return null;
            }

            default -> throw new TriggerIndexingException("Unknown executable index type: " + index.getClass().getName());
        }
    }
}
