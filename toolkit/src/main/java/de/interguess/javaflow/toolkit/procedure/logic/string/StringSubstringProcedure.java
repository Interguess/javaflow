package de.interguess.javaflow.toolkit.procedure.logic.string;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "string.substring",
        description = "Extracts a substring from a string",
        shorthand = "part of %string% from %start% to %end%",
        input = {
                @CustomProcedure.Field(
                        name = "string",
                        description = "The string to extract from",
                        type = String.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "start",
                        description = "The start index of the substring",
                        type = Integer.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "end",
                        description = "The end index of the substring",
                        type = Integer.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "substring",
                        description = "The extracted substring",
                        type = String.class
                )
        }
)
public class StringSubstringProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final String string = input.required(String.class, "string");
        final int start = input.required(Integer.class, "start");
        final int end = input.required(Integer.class, "end");

        return MultiOutput.create()
                .with("substring", string.substring(start, end));
    }
}
