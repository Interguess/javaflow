package de.interguess.javaflow.toolkit.procedure.variables;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
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
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final String name = input.required(String.class, "key");

        return MultiOutput.create()
                .with("exists", workflow.hasVariable(name));
    }
}
