package de.interguess.javaflow.common.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.interguess.javaflow.api.schema.SchemaProperty;
import de.interguess.javaflow.api.schema.SchemaProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ObjectSchemaProvider implements SchemaProvider<Object> {

    @Override
    public @NotNull JsonArray provideSchema(@NotNull List<Object> objects) {
        return objects.stream()
                .map(Object::getClass)
                .map(ObjectSchemaProvider::toJson)
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    //todo
    public static JsonObject toJson(Class<?> clazz) {
        final JsonObject objectSchema = new JsonObject();

        final JsonArray fields = new JsonArray();

        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            final JsonObject fieldSchema = new JsonObject();

            fieldSchema.addProperty("key", field.getName());
            fieldSchema.addProperty("type", field.getType().getSimpleName());

            if (field.isAnnotationPresent(SchemaProperty.class)) {
                final SchemaProperty schemaProperty = field.getAnnotation(SchemaProperty.class);

                assert schemaProperty != null;

                fieldSchema.addProperty("required", schemaProperty.required());
                fieldSchema.addProperty("description", schemaProperty.description());
            } else {
                fieldSchema.addProperty("required", false);
                fieldSchema.addProperty("description", "No description provided");
            }

            fields.add(fieldSchema);
        });

        objectSchema.addProperty("name", String.format("$%s", clazz.getSimpleName()));
        objectSchema.add("fields", fields);

        return objectSchema;
    }
}
