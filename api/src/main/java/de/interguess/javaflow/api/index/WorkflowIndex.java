package de.interguess.javaflow.api.index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
public record WorkflowIndex(
        String name,
        Map<String, Object> variables,
        List<TriggerIndex> triggers
) implements Index {}