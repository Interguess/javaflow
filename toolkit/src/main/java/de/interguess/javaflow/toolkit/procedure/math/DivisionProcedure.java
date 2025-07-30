package de.interguess.javaflow.toolkit.procedure.math;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "math.division",
        description = "Divides two numbers",
        shorthand = "%dividend% / %divisor%",
        input = {
                @CustomProcedure.Field(
                        name = "dividend",
                        description = "The dividend",
                        type = Double.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "divisor",
                        description = "The divisor",
                        type = Double.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "result",
                        description = "The result of the division",
                        type = Double.class
                )
        }
)
public class DivisionProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final double dividend = input.required(workflow, Double.class, "dividend");
        final double divisor = input.required(workflow, Double.class, "divisor");

        return Outputs.getInstance().create()
                .with("result", dividend / divisor);
    }
}
