package de.interguess.javaflow.toolkit.procedure.math;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "math.addition",
        description = "Adds two numbers",
        shorthand = "%first% + %second%",
        input = {
                @CustomProcedure.Field(
                        name = "first",
                        description = "The first number",
                        type = Number.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "second",
                        description = "The second number",
                        type = Number.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "result",
                        description = "The result of the addition",
                        type = Number.class
                )
        }
)
public class AdditionProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final double first = input.required(workflow, Double.class, "first");
        final double second = input.required(workflow, Double.class, "second");

        return Outputs.getInstance().create()
                .with("result", first + second);
    }
}
