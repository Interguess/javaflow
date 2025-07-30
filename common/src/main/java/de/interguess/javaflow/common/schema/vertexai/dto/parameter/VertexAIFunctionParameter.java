package de.interguess.javaflow.common.schema.vertexai.dto.parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class VertexAIFunctionParameter {

    private final transient String name;

    private final String type;

    private final String description;

    public Map<String, Object> toMap() {
        return Map.of(
                "name", name,
                "type", type,
                "description", description
        );
    }
}
