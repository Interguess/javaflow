package de.interguess.javaflow.api.index.executable;

import de.interguess.javaflow.api.index.ExecutableIndex;
import lombok.Builder;

import java.util.List;

@Builder
public record LoopIndex(
        String id,
        String type,
        ProcedureIndex[] condition,
        List<ExecutableIndex> tasks
) implements ExecutableIndex {

    public LoopIndex {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("LoopIndex id cannot be null or blank");
        }

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("LoopIndex type cannot be null or blank");
        }

        if (condition == null) {
            throw new IllegalArgumentException("LoopIndex condition cannot be null");
        }

        if (tasks == null || tasks.isEmpty()) {
            throw new IllegalArgumentException("LoopIndex tasks cannot be null or empty");
        }
    }

    @Override
    public String getId() {
        return id;
    }
}
