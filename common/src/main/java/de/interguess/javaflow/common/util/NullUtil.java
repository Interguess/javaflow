package de.interguess.javaflow.common.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class NullUtil {

    public <T> T defaultIfNull(T value, T defaultValue) {
        return Objects.requireNonNullElse(value, defaultValue);
    }
}