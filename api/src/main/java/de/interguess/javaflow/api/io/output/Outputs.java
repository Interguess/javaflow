package de.interguess.javaflow.api.io.output;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class Outputs {

    @Getter
    @Setter
    private static Outputs instance;

    public static boolean hasInstance() {
        return instance != null;
    }

    public abstract @NotNull MultiOutput create();
}
