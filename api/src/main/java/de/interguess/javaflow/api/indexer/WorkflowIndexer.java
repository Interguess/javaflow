package de.interguess.javaflow.api.indexer;

import de.interguess.javaflow.api.index.WorkflowIndex;
import org.jetbrains.annotations.NotNull;

public interface WorkflowIndexer {

    @NotNull WorkflowIndex index(@NotNull String workflow);
}
