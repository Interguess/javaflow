package de.interguess.javaflow.toolkit.procedure.logic;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "logic.equals",
        description = "Checks if two values are equal",
        shorthand = "%first% == %second%",
        input = {
                @CustomProcedure.Field(
                        name = "first",
                        description = "The first value",
                        type = Object.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "second",
                        description = "The second value",
                        type = Object.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "equals",
                        description = "Whether the values are equal",
                        type = Boolean.class
                )
        }
)
public class EqualsProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final Object first = input.required(Object.class, "first");
        final Object second = input.required(Object.class, "second");

        return MultiOutput.create()
                .with("equals", first.equals(second));
    }
}
