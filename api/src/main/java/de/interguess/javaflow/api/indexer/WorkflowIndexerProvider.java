package de.interguess.javaflow.api.indexer;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class WorkflowIndexerProvider {

    @Getter
    @Setter
    private static WorkflowIndexerProvider instance;

    public static boolean isInitialized() {
        return instance != null;
    }

    public abstract @Nullable WorkflowIndexer getIndexerById(@NotNull String id);

    public abstract void registerIndexer(@NotNull String id, @NotNull WorkflowIndexer indexer);
}
