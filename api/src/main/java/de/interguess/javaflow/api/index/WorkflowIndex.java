package de.interguess.javaflow.api.index;

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
public class WorkflowIndex extends Index {

    private final String name;

    private final Map<String, Object> variables;

    private final List<TriggerIndex> triggers;
}
