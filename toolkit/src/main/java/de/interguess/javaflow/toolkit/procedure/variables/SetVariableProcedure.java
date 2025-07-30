package de.interguess.javaflow.toolkit.procedure.variables;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CustomProcedure(
        id = "variables.set",
        description = "Sets or creates a variable",
        shorthand = "set %key% to %value%",
        input = {
                @CustomProcedure.Field(
                        name = "key",
                        description = "The key of the variable",
                        type = String.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "value",
                        description = "The value of the variable",
                        type = Object.class,
                        required = true
                )
        }
)
public class SetVariableProcedure implements Procedure {

    @Override
    public @Nullable MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final String name = input.required(workflow, String.class, "key");
        final Object value = input.required(workflow, Object.class, "value");

        workflow.setVariable(name, value);

        return null;
    }
}
