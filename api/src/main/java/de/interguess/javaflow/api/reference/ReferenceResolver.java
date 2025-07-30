package de.interguess.javaflow.api.reference;

import de.interguess.javaflow.api.workflow.Workflow;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class ReferenceResolver {

    @Getter
    @Setter
    private static ReferenceResolver instance;

    public static boolean isInitialized() {
        return instance != null;
    }

    public abstract @NotNull Object resolve(@NotNull Workflow context, @NotNull String reference);
}
