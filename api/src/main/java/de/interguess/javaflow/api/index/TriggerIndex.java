package de.interguess.javaflow.api.index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class TriggerIndex extends Index {

    private final String id;

    private final String type;

    private final List<ExecutableIndex> tasks;
}
