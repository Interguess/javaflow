package de.interguess.javaflow.common.schema.vertexai.dto.parameters;

import de.interguess.javaflow.common.schema.vertexai.dto.parameter.ParameterType;
import de.interguess.javaflow.common.schema.vertexai.dto.parameter.VertexAIFunctionParameter;

import java.util.Map;

public class ArrayParam extends VertexAIFunctionParameter {

    private final Map<String, Object> items;

    private final transient VertexAIFunctionParameter arrayType;

    public ArrayParam(String name, String description, VertexAIFunctionParameter arrayType) {
        super(name, ParameterType.ARRAY.toString(), description);

        this.items = arrayType.toMap();
        this.arrayType = arrayType;
    }

    public Map<String, Object> toMap() {

        return Map.of(
                "type", arrayType.toString(),
                "name", arrayType.getName(),
                "description", getDescription(),
                "items", items
        );
    }
}
