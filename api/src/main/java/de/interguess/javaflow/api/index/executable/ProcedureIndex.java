package de.interguess.javaflow.api.index.executable;

import de.interguess.javaflow.api.index.ExecutableIndex;
import de.interguess.javaflow.api.io.MultiInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ProcedureIndex extends ExecutableIndex {

    private final String id;

    private final String type;

    private final MultiInput input;
}
