package de.interguess.javaflow.api.index.executable;

import de.interguess.javaflow.api.index.ExecutableIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RouterIndex extends ExecutableIndex {

    private final String id;

    private final String type;

    private final Map<Object, List<ExecutableIndex>> routes;

    private final Object input;
}
