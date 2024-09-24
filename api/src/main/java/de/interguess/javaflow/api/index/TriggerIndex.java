package de.interguess.javaflow.api.index;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record TriggerIndex(
        String id,
        String type,
        List<ExecutableIndex> tasks
) implements Index {}