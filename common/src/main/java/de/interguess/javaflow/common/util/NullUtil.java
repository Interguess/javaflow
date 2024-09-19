package de.interguess.javaflow.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NullUtil {

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
