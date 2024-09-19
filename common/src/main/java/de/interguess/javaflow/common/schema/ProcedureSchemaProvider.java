package de.interguess.javaflow.common.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.schema.SchemaProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ProcedureSchemaProvider implements SchemaProvider<Procedure> {

    @Override
    public @NotNull String provideSchema(@NotNull List<Procedure> procedures) {
        final JsonArray schema = new JsonArray();

        procedures.forEach(function -> {
            final JsonObject conditionSchema = new JsonObject();

            final CustomProcedure customProcedure = function.getClass().getAnnotation(CustomProcedure.class);

            if (customProcedure == null) {
                throw new IllegalArgumentException("Function must be annotated with @CustomProcedure");
            }

            conditionSchema.addProperty("id", customProcedure.id());
            conditionSchema.addProperty("description", customProcedure.description());

            final JsonObject inputSchema = new JsonObject();

            Arrays.stream(customProcedure.input()).forEach(field -> {
                final JsonObject fieldSchema = new JsonObject();

                fieldSchema.addProperty("type", field.type().getSimpleName());
                fieldSchema.addProperty("description", field.description());
                fieldSchema.addProperty("required", field.required());

                inputSchema.add(field.name(), fieldSchema);
            });

            conditionSchema.add("input", inputSchema);

            if (customProcedure.output() != null) {
                final JsonObject outputSchema = new JsonObject();

                Arrays.stream(customProcedure.output()).forEach(field -> {
                    final JsonObject fieldSchema = new JsonObject();

                    fieldSchema.addProperty("type", field.type().getSimpleName());
                    fieldSchema.addProperty("description", field.description());
                    fieldSchema.addProperty("required", field.required());

                    outputSchema.add(field.name(), fieldSchema);
                });

                conditionSchema.add("output", outputSchema);
            }

            schema.add(conditionSchema);
        });

        return schema.toString();
    }
}
