package de.interguess.javaflow.api.index;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record TriggerIndex(
        String id,
        String type,
        List<ExecutableIndex> tasks
) implements Index {

    public TriggerIndex {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or blank");
        }
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
    }

    @Override
    public String getId() {
        return id;
    }
}