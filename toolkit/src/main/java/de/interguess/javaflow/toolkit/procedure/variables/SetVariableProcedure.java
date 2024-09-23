package de.interguess.javaflow.toolkit.procedure.variables;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@CustomProcedure(
        id = "variables.set",
        description = "Sets a variable",
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
                        type = Serializable.class,
                        required = true
                )
        }
)
public class SetVariableProcedure implements Procedure {

    @Override
    public @Nullable MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final String name = input.required(String.class, "key");
        final Serializable value = input.required(Serializable.class, "value");

        workflow.setVariable(name, value);

        return null;
    }
}
