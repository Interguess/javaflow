package de.interguess.javaflow.common.schema.vertexai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VertexAIFunction {

    private final String name;

    private final String description;

    private final VertexAIAllParams parameters;
}
