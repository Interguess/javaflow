package de.interguess.javaflow.toolkit.procedure.logic.string;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "string.toUppercase",
        description = "Converts a string to uppercase",
        shorthand = "uppercase %string%",
        input = {
                @CustomProcedure.Field(
                        name = "string",
                        description = "The string to convert",
                        type = String.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "uppercase",
                        description = "The uppercase string",
                        type = String.class
                )
        }
)
public class StringToUppercaseProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final String string = input.required(String.class, "string");

        return MultiOutput.create()
                .with("uppercase", string.toUpperCase());
    }
}
