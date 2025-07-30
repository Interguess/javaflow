package de.interguess.javaflow.toolkit.procedure.logic;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CustomProcedure(
        id = "console.print",
        description = "Prints a message to the console",
        shorthand = "print %message%",
        input = {
                @CustomProcedure.Field(
                        name = "message",
                        description = "The message to print",
                        type = String.class,
                        required = true
                )
        }
)
public class PrintConsoleMessageProcedure implements Procedure {

    @Override
    public @Nullable MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final String message = input.required(workflow, String.class, "message");

        System.out.println(message);

        return Outputs.getInstance().create();
    }
}
