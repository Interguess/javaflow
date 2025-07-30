package de.interguess.javaflow.api.construct.procedure;

import de.interguess.javaflow.api.io.input.MultiInput;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Procedure {

    @Nullable MultiOutput execute(@NotNull Workflow workflow, @NotNull MultiInput input);
}
