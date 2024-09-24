package de.interguess.javaflow.api.index.executable;

import de.interguess.javaflow.api.index.ExecutableIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
public record RouterIndex(
        String id,
        String type,
        Map<Object, List<ExecutableIndex>> routes,
        Object input
) implements ExecutableIndex {}
