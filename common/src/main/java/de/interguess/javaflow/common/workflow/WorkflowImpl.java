package de.interguess.javaflow.common.workflow;

import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorkflowImpl implements Workflow {

    private final Map<String, Object> variables;

    public WorkflowImpl() {
        this.variables = new HashMap<>();
    }

    @Override
    public @NotNull @Unmodifiable Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    @Override
    public @Nullable Object getVariable(@NotNull String key) {
        final Object value = variables.get(key);

        if (value == null) {
            throw new IllegalArgumentException("Variable '" + key + "' does not exist in the workflow.");
        }

        return value;
    }

    @Override
    public void setVariable(@NotNull String key, @Nullable Object value) {
        variables.put(key, value);
    }

    @Override
    public void removeVariable(@NotNull String key) {
        variables.remove(key);
    }

    @Override
    public boolean hasVariable(@NotNull String key) {
        return variables.containsKey(key);
    }
}
