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
        id = "time.parseMillisToTimestamp",
        description = "Parses millisecond that is provided to a timestamp",
        shorthand = "parse %millis% to timestamp",
        input = {
                @CustomProcedure.Field(
                        name = "millis",
                        description = "The milliseconds to parse",
                        type = Long.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "year",
                        description = "The year of the timestamp",
                        type = Integer.class
                ),
                @CustomProcedure.Field(
                        name = "month",
                        description = "The month of the timestamp",
                        type = Integer.class
                ),
                @CustomProcedure.Field(
                        name = "dayOfYear",
                        description = "The day of the year of the timestamp",
                        type = Integer.class
                ),
                @CustomProcedure.Field(
                        name = "dayOfMonth",
                        description = "The day of the month of the timestamp",
                        type = Integer.class
                ),
                @CustomProcedure.Field(
                        name = "hour",
                        description = "The hour of the day of the timestamp",
                        type = Integer.class
                ),
                @CustomProcedure.Field(
                        name = "minute",
                        description = "The minute of the hour of the timestamp",
                        type = Integer.class
                ),
                @CustomProcedure.Field(
                        name = "second",
                        description = "The second of the minute of the timestamp",
                        type = Integer.class
                )
        }
)
public class ParseMillisToTimestamp implements Procedure {

    @Override
    public @Nullable MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final long millis = input.required(workflow, Long.class, "millis");

        return Outputs.getInstance().create()
                .with("year", (int) (millis / 1000 / 60 / 60 / 24 / 365))
                .with("month", (int) (millis / 1000 / 60 / 60 / 24 % 12))
                .with("dayOfYear", (int) (millis / 1000 / 60 / 60 / 24))
                .with("dayOfMonth", (int) (millis / 1000 / 60 / 60 % 24))
                .with("hour", (int) (millis / 1000 / 60 % 60))
                .with("minute", (int) (millis / 1000 % 60))
                .with("second", (int) (millis % 1000));
    }
}
