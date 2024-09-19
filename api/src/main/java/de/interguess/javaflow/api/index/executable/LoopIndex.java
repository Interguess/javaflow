package de.interguess.javaflow.api.index.executable;

import de.interguess.javaflow.api.index.ExecutableIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LoopIndex extends ExecutableIndex {

    private final String id;

    private final String type;

    private final ProcedureIndex condition;

    private final List<ExecutableIndex> tasks;
}
