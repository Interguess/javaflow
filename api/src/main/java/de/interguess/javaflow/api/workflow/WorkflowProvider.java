package de.interguess.javaflow.api.workflow;

import de.interguess.javaflow.api.index.WorkflowIndex;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public abstract class WorkflowProvider {

    @Getter
    @Setter
    private static WorkflowProvider instance;

    public static boolean hasInstance() {
        return instance != null;
    }

    public abstract void registerWorkflow(@NotNull WorkflowIndex workflowIndex);

    public abstract @NotNull @Unmodifiable List<WorkflowIndex> getWorkflows();
}
