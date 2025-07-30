package de.interguess.javaflow.common.io.output;

import de.interguess.javaflow.api.io.output.MultiOutput;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MultiOutputImpl implements MultiOutput {

    private final Map<String, Object> data;

    public MultiOutputImpl(@NotNull Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public @NotNull MultiOutput with(@NotNull String key, @NotNull Object value) {
        data.put(key, value);

        return this;
    }

    @Override
    public @NotNull Object get(@NotNull String key) {
        return data.getOrDefault(key, null);
    }

    @Override
    public boolean contains(@NotNull String key) {
        return data.containsKey(key);
    }

    @Override
    public @NotNull List<String> getKeys() {
        return List.copyOf(data.keySet());
    }

    @Override
    public @NotNull List<Object> getValues() {
        return List.copyOf(data.values());
    }

    @Override
    public @NotNull Map<String, Object> getData() {
        return Map.copyOf(data);
    }
}
