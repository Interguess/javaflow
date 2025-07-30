package de.interguess.javaflow.toolkit.procedure.variables;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "variables.exists",
        description = "Checks if a variable exists",
        shorthand = "exists %key%",
        input = {
                @CustomProcedure.Field(
                        name = "key",
                        description = "The key of the variable",
                        type = String.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "exists",
                        description = "Whether the variable exists",
                        type = Boolean.class
                )
        }
)
public class ExistsVariableProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final String name = input.required(workflow, String.class, "key");

        return Outputs.getInstance().create()
                .with("exists", workflow.hasVariable(name));
    }
}
