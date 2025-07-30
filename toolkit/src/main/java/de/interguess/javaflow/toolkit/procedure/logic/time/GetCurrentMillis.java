package de.interguess.javaflow.toolkit.procedure.logic.time;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CustomProcedure(
        id = "time.currentMillis",
        description = "Gets the current time in milliseconds since the epoch",
        shorthand = "current time in milliseconds",
        output = {
                @CustomProcedure.Field(
                        name = "currentMillis",
                        description = "The current time in milliseconds since the epoch",
                        type = Long.class
                )
        }
)
public class GetCurrentMillis implements Procedure {

    @Override
    public @Nullable MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        return Outputs.getInstance().create()
                .with("currentMillis", System.currentTimeMillis());
    }
}
