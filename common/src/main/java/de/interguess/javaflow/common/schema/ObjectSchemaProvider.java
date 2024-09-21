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
    public @NotNull String provideSchema(@NotNull List<Object> objects) {
        final JsonArray schema = new JsonArray();

        objects.forEach(object -> {
            final JsonObject objectSchema = new JsonObject();

            final JsonArray fields = new JsonArray();

            final Class<?> dtoClass = object.getClass();

            Arrays.stream(dtoClass.getDeclaredFields()).forEach(field -> {
                final JsonObject fieldSchema = new JsonObject();

                fieldSchema.addProperty("key", field.getName());
                fieldSchema.addProperty("type", field.getType().getSimpleName());

                if (field.isAnnotationPresent(SchemaProperty.class)) {
                    final SchemaProperty schemaProperty = field.getAnnotation(SchemaProperty.class);

                    fieldSchema.addProperty("required", schemaProperty.required());
                    fieldSchema.addProperty("description", schemaProperty.description());
                } else {
                    fieldSchema.addProperty("required", false);
                    fieldSchema.addProperty("description", "No description provided");
                }

                fields.add(fieldSchema);
            });

            objectSchema.addProperty("name", String.format("$%s", dtoClass.getSimpleName()));
            objectSchema.add("fields", fields);

            schema.add(objectSchema);
        });

        return schema.toString();
    }
}
