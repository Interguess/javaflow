package de.interguess.javaflow.common.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.interguess.javaflow.api.schema.SchemaProvider;
import de.interguess.javaflow.api.construct.trigger.CustomTrigger;
import de.interguess.javaflow.api.construct.trigger.Trigger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TriggerSchemaProvider implements SchemaProvider<Trigger> {

    @Override
    public @NotNull String provideSchema(@NotNull List<Trigger> triggers) {
        JsonArray schema = new JsonArray();

        triggers.forEach(function -> {
            final JsonObject conditionSchema = new JsonObject();

            final CustomTrigger customTrigger = function.getClass().getAnnotation(CustomTrigger.class);

            if (customTrigger == null) {
                throw new IllegalArgumentException("Function must be annotated with @CustomTrigger");
            }

            conditionSchema.addProperty("id", customTrigger.id());
            conditionSchema.addProperty("description", customTrigger.description());

            final JsonObject inputSchema = new JsonObject();

            Arrays.stream(customTrigger.input()).forEach(field -> {
                final JsonObject fieldSchema = new JsonObject();

                fieldSchema.addProperty("type", field.type().getSimpleName());
                fieldSchema.addProperty("description", field.description());
                fieldSchema.addProperty("required", field.required());

                inputSchema.add(field.name(), fieldSchema);
            });

            conditionSchema.add("input", inputSchema);

            schema.add(conditionSchema);
        });

        return schema.toString();
    }
}
