package de.interguess.javaflow.api.construct.procedure;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ProcedureProvider {

    @Getter
    @Setter
    private static ProcedureProvider instance;

    public static boolean hasInstance() {
        return instance != null;
    }

    public abstract void registerProcedures(@NotNull String packageName);

    public abstract @Nullable Procedure getProcedureById(@NotNull String id);
}
