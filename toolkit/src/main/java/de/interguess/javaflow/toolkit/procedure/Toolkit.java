package de.interguess.javaflow.toolkit.procedure;

import de.interguess.javaflow.api.construct.procedure.ProcedureProvider;
import de.interguess.javaflow.api.construct.trigger.TriggerProvider;
import org.jetbrains.annotations.NotNull;

public class Toolkit {

    private static final String PACKAGE = "de.interguess.javaflow.toolkit.%s";

    public static void initialize(@NotNull TriggerProvider triggerProvider, @NotNull ProcedureProvider procedureProvider) {
        //triggerProvider.registerTriggers(String.format(PACKAGE, "trigger")); // we don't have triggers in this toolkit yet

        procedureProvider.registerProcedures(String.format(PACKAGE, "procedure"));
    }
}
