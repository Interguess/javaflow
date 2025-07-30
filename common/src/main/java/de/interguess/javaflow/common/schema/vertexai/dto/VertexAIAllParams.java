package de.interguess.javaflow.common.schema.vertexai.dto;

import de.interguess.javaflow.common.schema.vertexai.dto.parameter.VertexAIFunctionParameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class VertexAIAllParams {

    private final String type = "object";

    private final Map<String, VertexAIFunctionParameter> properties;

    private final List<String> required;
}
