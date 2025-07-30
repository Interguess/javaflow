package de.interguess.javaflow.common.io.input;

import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.reference.ReferenceResolver;
import de.interguess.javaflow.api.workflow.Workflow;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MultiInputImpl implements MultiInput {

    @Getter
    protected final Map<String, Object> values;

    protected MultiInputImpl(@NotNull Map<String, Object> values) {
        this.values = values;
    }

    public <T> @NotNull T required(@NotNull Workflow workflow, @NotNull Class<T> clazz, @NotNull String key) {
        Object obj = values.get(key);

        if (obj == null) {
            throw new IllegalArgumentException("No value found for key '" + key + "'"); //todo: no IllegalArgumentException
        }

        obj = ReferenceResolver.getInstance().resolve(workflow, obj.toString());

        if (clazz.equals(Integer.class)) {
            return clazz.cast(Integer.parseInt(obj.toString()));
        } else if (clazz.equals(Long.class)) {
            return clazz.cast(Long.parseLong(obj.toString()));
        } else if (clazz.equals(Double.class)) {
            return clazz.cast(Double.parseDouble(obj.toString()));
        } else if (clazz.equals(Float.class)) {
            return clazz.cast(Float.parseFloat(obj.toString()));
        } else if (clazz.equals(Boolean.class)) {
            return clazz.cast(Boolean.parseBoolean(obj.toString()));
        } else if (clazz.equals(Number.class)) {
            return clazz.cast(Double.parseDouble(obj.toString())); //todo
        }

        return clazz.cast(obj);
    }

    //todo: is this good?
    public <T> @Nullable T optional(@NotNull Workflow workflow, @NotNull Class<T> clazz, @NotNull String key) {
        try {
            if (!values.containsKey(key)) {
                return null; // No value found for the key
            }

            return required(workflow, clazz, key);
        } catch (IllegalArgumentException e) {
            // If the value cannot be resolved, return null instead of throwing an exception
            return null;
        }
    }

    @Override
    public @NotNull Map<String, Object> getData(@NotNull Workflow workflow) {
        final Map<String, Object> data = new java.util.HashMap<>(values);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            final String key = entry.getKey();

            Object value = entry.getValue();

            try {
                value = ReferenceResolver.getInstance().resolve(workflow, value.toString());
            } catch (Exception e) {
                throw new IllegalArgumentException("Value for key '" + key + "' cannot be resolved: " + value, e);
            }

            entry.setValue(value);
        }

        return data;
    }

    public boolean has(@NotNull String key) {
        return values.containsKey(key);
    }

    public static class Builder implements MultiInput.Builder {

        private final Map<String, Object> values = new HashMap<>();

        public @NotNull MultiInput.Builder with(@NotNull String key, @NotNull Object value) {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");

            values.put(key, value);

            return this;
        }

        @Override
        public @NotNull MultiInput build() {
            return new MultiInputImpl(values);
        }
    }
}
