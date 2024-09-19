package de.interguess.javaflow.api.workflow;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public interface Workflow {

    @NotNull @Unmodifiable Map<String, Object> getVariables();

    @Nullable Object getVariable(@NotNull String key);

    void setVariable(@NotNull String key, @Nullable Object value);

    void removeVariable(@NotNull String key);

    boolean hasVariable(@NotNull String key);
}
