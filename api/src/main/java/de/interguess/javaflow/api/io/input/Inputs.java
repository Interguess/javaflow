package de.interguess.javaflow.api.io.input;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class Inputs {

    @Getter
    @Setter
    private static Inputs instance;

    public static boolean hasInstance() {
        return instance != null;
    }

    public abstract @NotNull MultiInput.Builder create();
}
