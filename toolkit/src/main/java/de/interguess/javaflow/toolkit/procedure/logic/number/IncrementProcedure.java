package de.interguess.javaflow.toolkit.procedure.logic.number;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CustomProcedure(
        id = "number.increment",
        description = "Increments a number by the given value",
        shorthand = "increment %number% by %value%",
        input = {
                @CustomProcedure.Field(
                        name = "number",
                        description = "The number to increment",
                        type = Number.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "value",
                        description = "The value to increment by (The default is 1)",
                        type = Number.class
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "incremented",
                        description = "The incremented number",
                        type = Number.class
                )
        }
)
public class IncrementProcedure implements Procedure {

    @Override
    public @Nullable MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final Number number = input.required(workflow, Number.class, "number");
        final Number value = input.optional(workflow, Number.class, "value");

        if (value == null) {
            return Outputs.getInstance().create()
                    .with("incremented", number.doubleValue() + 1);
        } else {
            return Outputs.getInstance().create()
                    .with("incremented", number.doubleValue() + value.doubleValue());
        }
    }
}
