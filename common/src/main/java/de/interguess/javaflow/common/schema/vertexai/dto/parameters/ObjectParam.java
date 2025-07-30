package de.interguess.javaflow.common.schema.vertexai.dto.parameters;

import de.interguess.javaflow.common.schema.vertexai.dto.parameter.ParameterType;
import de.interguess.javaflow.common.schema.vertexai.dto.parameter.VertexAIFunctionParameter;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ObjectParam extends VertexAIFunctionParameter {

    private final Map<String, VertexAIFunctionParameter> properties;

    private final List<String> required;

    public ObjectParam(String name, String description, Map<String, VertexAIFunctionParameter> properties, List<String> required) {
        super(name, ParameterType.OBJECT.toString(), description);

        this.properties = properties;
        this.required = required;
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "type", getType(),
                //"name", getName(),
                "description", getDescription(),
                "properties", properties,
                "required", required
        );
    }
}
