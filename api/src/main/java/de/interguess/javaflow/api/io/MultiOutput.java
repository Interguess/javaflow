package de.interguess.javaflow.api.io;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class MultiOutput {

    public static @NotNull MultiOutput create() {
        return new MultiOutput(new HashMap<>());
    }

    private final Map<String, Object> values;

    protected MultiOutput(@NotNull Map<String, Object> values) {
        this.values = values;
    }

    public @NotNull MultiOutput with(@NotNull String key, @NotNull Object value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        values.put(key, value);

        return this;
    }
}
