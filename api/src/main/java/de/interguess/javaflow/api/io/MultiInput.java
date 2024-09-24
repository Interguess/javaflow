package de.interguess.javaflow.api.io;

import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MultiInput {

    private static final MultiInput EMPTY = new MultiInput(Map.of()); // Not modifiable

    protected final Map<String, Object> values;

    public static @NotNull MultiInput.Builder create() {
        return new Builder(new HashMap<>());
    }

    public @NotNull Map<String, Object> getValues() {
        return values;
    }

    public static @NotNull MultiInput empty() {
        return EMPTY;
    }

    protected MultiInput(@NotNull Map<String, Object> values) {
        this.values = values;
    }

    public <T> @NotNull T required(@NotNull Class<T> clazz, @NotNull String key) {
        Object obj = values.get(key);

        if (obj == null) {
            throw new IllegalArgumentException("No value found for key '" + key + "'");
        }

        if (!clazz.isInstance(obj)) {
            throw new IllegalArgumentException("Value for key '" + key + "' is not of type " + clazz.getName());
        }

        return clazz.cast(obj);
    }

    public <T> @Nullable T optional(@NotNull Class<T> clazz, @NotNull String key) {
        Object obj = values.get(key);

        if (obj == null) {
            return null;
        }

        if (!clazz.isInstance(obj)) {
            throw new IllegalArgumentException("Value for key '" + key + "' is not of type " + clazz.getName());
        }

        return clazz.cast(obj);
    }

    public boolean has(@NotNull String key) {
        return values.containsKey(key);
    }

    public static class Builder extends MultiInput {

        protected Builder(@NotNull Map<String, Object> values) {
            super(values);
        }

        public @NotNull MultiInput.Builder with(@NotNull String key, @NotNull Object value) {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");

            values.put(key, value);

            return this;
        }
    }
}
