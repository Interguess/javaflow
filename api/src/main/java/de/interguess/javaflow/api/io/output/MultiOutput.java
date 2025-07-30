package de.interguess.javaflow.api.io.output;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface MultiOutput {

    @NotNull MultiOutput with(@NotNull String key, @NotNull Object value);

    @NotNull Object get(@NotNull String key);

    boolean contains(@NotNull String key);

    @NotNull List<String> getKeys();

    @NotNull List<Object> getValues();

    @NotNull Map<String, Object> getData();
}
