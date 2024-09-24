package de.interguess.javaflow.api.index.executable;

import de.interguess.javaflow.api.index.ExecutableIndex;
import de.interguess.javaflow.api.io.MultiInput;
import lombok.Builder;

@Builder
public record ProcedureIndex(
        String id,
        String type,
        MultiInput input
) implements ExecutableIndex {}
