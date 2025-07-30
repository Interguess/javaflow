package de.interguess.javaflow.api.construct.trigger;

import de.interguess.javaflow.api.io.input.MultiInput;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class TriggerProvider {

    @Getter
    @Setter
    private static TriggerProvider instance;

    public static boolean hasInstance() {
        return instance != null;
    }

    public abstract void registerTriggers(@NotNull String packageName);

    public abstract void callTrigger(@NotNull Trigger trigger, @NotNull MultiInput input);
}
