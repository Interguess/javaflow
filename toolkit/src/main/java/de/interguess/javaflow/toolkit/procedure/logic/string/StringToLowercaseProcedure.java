package de.interguess.javaflow.toolkit.procedure.logic.string;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "string.toLowercase",
        description = "Converts a string to lowercase",
        shorthand = "lowercase %string%",
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
                        name = "lowercase",
                        description = "The lowercase string",
                        type = String.class
                )
        }
)
public class StringToLowercaseProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final String string = input.required(String.class, "string");

        return MultiOutput.create()
                .with("lowercase", string.toLowerCase());
    }
}
