package de.interguess.javaflow.api.task;

import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

public interface ExecutableElement {

    @NotNull MultiOutput execute(@NotNull Workflow workflow);
}
