package de.interguess.javaflow.common.trigger;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.construct.procedure.ProcedureProvider;
import de.interguess.javaflow.api.construct.trigger.CustomTrigger;
import de.interguess.javaflow.api.construct.trigger.Trigger;
import de.interguess.javaflow.api.construct.trigger.TriggerProvider;
import de.interguess.javaflow.api.exception.TriggerIndexingException;
import de.interguess.javaflow.api.index.ExecutableIndex;
import de.interguess.javaflow.api.index.TriggerIndex;
import de.interguess.javaflow.api.index.WorkflowIndex;
import de.interguess.javaflow.api.index.executable.LoopIndex;
import de.interguess.javaflow.api.index.executable.ProcedureIndex;
import de.interguess.javaflow.api.index.executable.RouterIndex;
import de.interguess.javaflow.api.io.input.Inputs;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.reference.ReferenceResolver;
import de.interguess.javaflow.api.workflow.Workflow;
import de.interguess.javaflow.api.workflow.WorkflowProvider;
import de.interguess.javaflow.common.util.ClassSearcherUtil;
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
                    final CustomTrigger customTrigger = clazz.getAnnotation(CustomTrigger.class);

                    indexes.put((Class<? extends Trigger>) clazz, customTrigger);
                });
    }

    @Override
    public void callTrigger(@NotNull Trigger trigger, @NotNull MultiInput input) {
        final CustomTrigger customTrigger = indexes.get(trigger.getClass());

        if (customTrigger == null) {
            throw new TriggerIndexingException("Trigger class " + trigger.getClass().getName() + " is not indexed!");
        }

        for (WorkflowIndex workflowIndex : WorkflowProvider.getInstance().getWorkflows()) {
            for (TriggerIndex triggerIndex : workflowIndex.triggers()) {
                if (!triggerIndex.type().equals(customTrigger.id())) {
                    continue;
                }

                final Workflow workflow = new WorkflowImpl();

                input.getData(workflow).forEach((key, value) -> {
                    final String triggerId = triggerIndex.id() + "." + key;

                    workflow.setVariable(triggerId, value);
                });

                for (ExecutableIndex executableIndex : triggerIndex.tasks()) {
                    executeFunction(workflow, executableIndex);
                }
            }
        }
    }

    private @Nullable MultiOutput dExecProcedure(@NotNull Workflow workflow, @NotNull ProcedureIndex procedureIndex) {
        final Procedure procedure = ProcedureProvider.getInstance().getProcedureById(procedureIndex.type());

        if (procedure == null) {
            throw new TriggerIndexingException("The procedure with ID '" + procedureIndex.id() + "' of type '" + procedureIndex.type() + "' is not registered.");
        }

        final MultiInput.Builder input = Inputs.getInstance().create();

        final CustomProcedure customProcedureA = procedure.getClass().getAnnotation(CustomProcedure.class);

        String s = customProcedureA.shorthand();

        for (Map.Entry<String, Object> entry : procedureIndex.input().getData(workflow).entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();

            if (value == null) {
                //Todo: sure it cant be null?
                throw new IllegalArgumentException("Value for key '" + key + "' cannot be resolved because it is null.");
            }

            s = s.replace("%" + key + "%", value.toString());

            input.with(key, value);
        }

        final MultiOutput output = procedure.execute(workflow, procedureIndex.input());

        storeOutputInWorkflow(workflow, output, procedureIndex.id());

        return output;
    }

    private void dExecLoop(@NotNull Workflow workflow, @NotNull LoopIndex loopIndex) {
        final ProcedureIndex[] condition = loopIndex.condition();

        final String conditionOutputId = "_result"; //todo: improve loop conditions

        for (ProcedureIndex task : condition) {
            dExecProcedure(workflow, task);
        }

        while (String.valueOf(workflow.getVariable(conditionOutputId)).equals("true")) {
            for (final ExecutableIndex task : loopIndex.tasks()) {
                final MultiOutput output = executeFunction(workflow, task);

                storeOutputInWorkflow(workflow, output, task.getId());
            }

            for (final ExecutableIndex task : condition) {
                final MultiOutput output = executeFunction(workflow, task);

                storeOutputInWorkflow(workflow, output, task.getId());
            }
        }
    }

    private void dExecRouter(@NotNull Workflow workflow, @NotNull RouterIndex routerIndex) {
        String input = routerIndex.input().toString();

        try {
            input = ReferenceResolver.getInstance().resolve(workflow, input).toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Input cannot be resolved: " + input, e);
        }

        if (!routerIndex.routes().containsKey(input)) {
            throw new TriggerIndexingException("No route found for input: " + input);
        }

        for (final ExecutableIndex task : routerIndex.routes().get(input)) {
            final MultiOutput output = executeFunction(workflow, task);

            storeOutputInWorkflow(workflow, output, task.getId());
        }
    }

    private @Nullable MultiOutput executeFunction(@NotNull Workflow workflow, @NotNull ExecutableIndex index) {
        switch (index) {
            case LoopIndex loopIndex -> {
                dExecLoop(workflow, loopIndex);

                return null;
            }

            case ProcedureIndex procedureIndex -> {
                return dExecProcedure(workflow, procedureIndex);
            }

            case RouterIndex routerIndex -> {
                dExecRouter(workflow, routerIndex);

                return null;
            }

            default ->
                    throw new TriggerIndexingException("Unknown executable index type: " + index.getClass().getName());
        }
    }

    private void storeOutputInWorkflow(@NotNull Workflow workflow, @Nullable MultiOutput output, @NotNull String stepId) {
        if (output == null) return;

        final Object raw = output.get("_result");

        //todo raw null check not working

        if (raw instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() instanceof String key && entry.getValue() instanceof Serializable value) {
                    workflow.setVariable(key, value);
                }
            }
        }

        workflow.setVariable("_result", raw);

        for (Map.Entry<String, Object> entry : output.getData().entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();

            if (value instanceof Serializable serializable) {
                final String variableKey = stepId + "." + key;

                workflow.setVariable(variableKey, serializable);
            } else {
                throw new IllegalArgumentException("Value for key '" + key + "' in output is not serializable: " + value);
            }
        }
    }
}
