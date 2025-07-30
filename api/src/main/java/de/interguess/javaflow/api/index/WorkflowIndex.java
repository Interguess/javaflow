package de.interguess.javaflow.api.index;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record WorkflowIndex(
        String name,
        Map<String, Object> variables,
        List<TriggerIndex> triggers
) implements Index {

    @Override
    public String getId() {
        return name;
    }
}