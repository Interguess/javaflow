package de.interguess.javaflow.common.schema.vertexai.dto.parameter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public enum ParameterType {

    STRING,
    NUMBER,
    INTEGER,
    BOOLEAN,
    OBJECT,
    ENUM,
    ARRAY;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static ParameterType fromClass(@NotNull Class<?> clazz) {
        final Map<List<Class<?>>, ParameterType> typeMap = Map.of(
                List.of(String.class), STRING,
                List.of(Number.class, double.class, float.class, Long.class, Double.class, Float.class, long.class), NUMBER,
                List.of(Integer.class, int.class), INTEGER,
                List.of(Boolean.class, boolean.class), BOOLEAN,
                List.of(), OBJECT,
                List.of(Enum.class), ENUM,
                List.of(List.class), ARRAY
        );

        if (clazz.isArray()) {
            System.out.println(clazz + " -> " + ARRAY);

            return ARRAY;
        }

        for (Map.Entry<List<Class<?>>, ParameterType> entry : typeMap.entrySet()) {
            if (entry.getKey().contains(clazz)) {
                System.out.println(clazz + " -> " + entry.getValue());
                return entry.getValue();
            }
        }

        System.out.println(clazz + " -> " + OBJECT);

        return OBJECT;
    }
}
