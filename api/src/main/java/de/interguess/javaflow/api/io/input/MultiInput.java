package de.interguess.javaflow.api.io.input;

import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface MultiInput {

    <T> @NotNull T required(@NotNull Workflow workflow, @NotNull Class<T> clazz, @NotNull String key);

    <T> @Nullable T optional(@NotNull Workflow workflow, @NotNull Class<T> clazz, @NotNull String key);

    @NotNull Map<String, Object> getData(@NotNull Workflow workflow);

    interface Builder {

        @NotNull Builder with(@NotNull String key, @NotNull Object value);

        @NotNull MultiInput build();
    }
}
