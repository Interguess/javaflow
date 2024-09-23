package de.interguess.javaflow.toolkit.procedure.logic.number;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "number.greaterOrEqual",
        description = "Checks if a number is greater or equal to another number",
        shorthand = "%number% >= %other%",
        input = {
                @CustomProcedure.Field(
                        name = "number",
                        description = "The number to check",
                        type = Number.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "other",
                        description = "The other number to compare with",
                        type = Number.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "greaterOrEqual",
                        description = "True if the number is greater or equal to the other number",
                        type = Boolean.class
                )
        })
public class GreaterOrEqualProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final Number number = input.required(Number.class, "number");
        final Number other = input.required(Number.class, "other");

        return MultiOutput.create()
                .with("greaterOrEqual", number.doubleValue() >= other.doubleValue());
    }
}
