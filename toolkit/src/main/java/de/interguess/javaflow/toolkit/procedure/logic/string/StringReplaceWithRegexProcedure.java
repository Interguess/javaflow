package de.interguess.javaflow.toolkit.procedure.logic.string;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "string.replaceWithRegex",
        description = "Replaces a substring in a string using a regex pattern",
        shorthand = "replace regex %pattern% in %string% with %replace%",
        input = {
                @CustomProcedure.Field(
                        name = "string",
                        description = "The string to replace in",
                        type = String.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "pattern",
                        description = "The regex pattern to search for",
                        type = String.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "replace",
                        description = "The substring to replace with",
                        type = String.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "replaced",
                        description = "The string with the replaced substring",
                        type = String.class
                )
        }
)
public class StringReplaceWithRegexProcedure implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input) {
        final String string = input.required(workflow, String.class, "string");
        final String pattern = input.required(workflow, String.class, "pattern");
        final String replace = input.required(workflow, String.class, "replace");

        return Outputs.getInstance().create()
                .with("replaced", string.replaceAll(pattern, replace));
    }
}
