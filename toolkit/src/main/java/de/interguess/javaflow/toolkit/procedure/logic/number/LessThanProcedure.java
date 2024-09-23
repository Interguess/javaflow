package de.interguess.javaflow.toolkit.procedure.logic.number;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "number.lessThan",
        description = "Checks if a number is less than another number",
        shorthand = "%number% < %other%",
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
                        name = "less",
                        description = "True if the number is less than the other number",
                        type = Boolean.class
                )
        }
)
public class LessThanProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final Number number = input.required(Number.class, "number");
        final Number other = input.required(Number.class, "other");

        return MultiOutput.create()
                .with("less", number.doubleValue() < other.doubleValue());
    }
}
