package de.interguess.javaflow.common.workflow;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.index.WorkflowIndex;
import de.interguess.javaflow.api.workflow.WorkflowProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class WorkflowProviderImpl extends WorkflowProvider {

    private final List<WorkflowIndex> workflows;

    public WorkflowProviderImpl() {
        this.workflows = new ArrayList<>();

        WorkflowProvider.setInstance(this);
    }

    @Override
    public void registerWorkflow(@NotNull WorkflowIndex workflowIndex) {
        workflows.add(workflowIndex);
    }

    @Override
    public @NotNull List<WorkflowIndex> getWorkflows() {
        return List.copyOf(workflows);
    }
}
