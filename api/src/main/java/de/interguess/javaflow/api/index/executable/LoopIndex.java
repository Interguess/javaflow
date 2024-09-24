package de.interguess.javaflow.api.index.executable;

import de.interguess.javaflow.api.index.ExecutableIndex;
import lombok.Builder;

import java.util.List;

@Builder
public record LoopIndex(
        String id,
        String type,
        ProcedureIndex condition,
        List<ExecutableIndex> tasks
) implements ExecutableIndex {}
