package de.interguess.javaflow.toolkit.procedure.math;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "math.multiplication",
        description = "Multiplies two numbers",
        shorthand = "%first% * %second%",
        input = {
                @CustomProcedure.Field(
                        name = "first",
                        description = "The first number",
                        type = Double.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "second",
                        description = "The second number",
                        type = Double.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "result",
                        description = "The result of the multiplication",
                        type = Double.class
                )
        }
)
public class MultiplicationProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final double first = input.required(Double.class, "first");
        final double second = input.required(Double.class, "second");

        return MultiOutput.create()
                .with("result", first * second);
    }
}
