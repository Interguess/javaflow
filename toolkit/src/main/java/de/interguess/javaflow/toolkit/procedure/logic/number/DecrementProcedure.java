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
        id = "number.decrement",
        description = "Decrements a number by 1",
        shorthand = "%number%--",
        input = {
                @CustomProcedure.Field(
                        name = "number",
                        description = "The number to decrement",
                        type = Number.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "value",
                        description = "The number to decrement by (default is 1)",
                        type = Number.class
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "decremented",
                        description = "The decremented number",
                        type = Number.class
                )
        }
)
public class DecrementProcedure implements Procedure {

    @Override
    public @Nullable MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final Number number = input.required(workflow, Number.class, "number");
        final Number value = input.optional(workflow, Number.class, "value");

        if (value == null) {
            return Outputs.getInstance().create()
                    .with("decremented", number.doubleValue() - 1);
        } else {
            return Outputs.getInstance().create()
                    .with("decremented", number.doubleValue() - value.doubleValue());
        }
    }
}
