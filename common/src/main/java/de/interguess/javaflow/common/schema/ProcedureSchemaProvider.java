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
            final JsonObject procedureSchema = new JsonObject();

            final CustomProcedure customProcedure = function.getClass().getAnnotation(CustomProcedure.class);

            if (customProcedure == null) {
                throw new IllegalArgumentException("Procedure must be annotated with @CustomProcedure");
            }

            procedureSchema.addProperty("id", customProcedure.id());
            procedureSchema.addProperty("description", customProcedure.description());

            final JsonObject inputSchema = new JsonObject();

            Arrays.stream(customProcedure.input()).forEach(field -> {
                final JsonObject fieldSchema = new JsonObject();

                fieldSchema.addProperty("type", field.type().getSimpleName());
                fieldSchema.addProperty("description", field.description());
                fieldSchema.addProperty("required", field.required());

                inputSchema.add(field.name(), fieldSchema);
            });

            procedureSchema.add("input", inputSchema);

            if (customProcedure.output() != null) {
                final JsonObject outputSchema = new JsonObject();

                Arrays.stream(customProcedure.output()).forEach(field -> {
                    final JsonObject fieldSchema = new JsonObject();

                    fieldSchema.addProperty("type", field.type().getSimpleName());
                    fieldSchema.addProperty("description", field.description());
                    fieldSchema.addProperty("required", field.required());

                    outputSchema.add(field.name(), fieldSchema);
                });

                procedureSchema.add("output", outputSchema);
            }

            schema.add(procedureSchema);
        });

        return schema.toString();
    }
}
