package de.interguess.javaflow.common.schema.vertexai;

import com.google.gson.*;
import de.interguess.javaflow.common.schema.vertexai.dto.VertexAIAllParams;
import de.interguess.javaflow.common.schema.vertexai.dto.parameter.VertexAIFunctionParameter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class VertexAIAllParamsSerializer implements JsonSerializer<VertexAIAllParams> {

    @Override
    public JsonElement serialize(VertexAIAllParams src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        // Fixer Typ
        json.addProperty("type", src.getType());

        // Properties nur wenn nicht leer
        Map<String, VertexAIFunctionParameter> props = src.getProperties();
        if (props != null && !props.isEmpty()) {
            JsonObject propertiesJson = new JsonObject();
            for (Map.Entry<String, VertexAIFunctionParameter> entry : props.entrySet()) {
                JsonElement value = context.serialize(entry.getValue());
                if (value != null && value.isJsonObject() && !value.getAsJsonObject().entrySet().isEmpty()) {
                    propertiesJson.add(entry.getKey(), value);
                }
            }

            if (!propertiesJson.entrySet().isEmpty()) {
                json.add("properties", propertiesJson);
            }
        }

        // Required nur wenn nicht leer
        List<String> required = src.getRequired();
        if (required != null && !required.isEmpty()) {
            JsonArray requiredArray = new JsonArray();
            required.forEach(requiredArray::add);
            json.add("required", requiredArray);
        }

        return json;
    }
}
