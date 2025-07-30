package de.interguess.javaflow.common.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.interguess.javaflow.api.schema.SchemaProvider;
import de.interguess.javaflow.api.construct.trigger.CustomTrigger;
import de.interguess.javaflow.api.construct.trigger.Trigger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TriggerSchemaProvider implements SchemaProvider<Trigger> {

    @Override
    public @NotNull JsonArray provideSchema(@NotNull List<Trigger> triggers) {
        return triggers.stream().map(trigger -> {
            final JsonObject triggerSchema = new JsonObject();

            final CustomTrigger customTrigger = trigger.getClass().getAnnotation(CustomTrigger.class);

            if (customTrigger == null) {
                throw new IllegalArgumentException("Trigger must be annotated with @CustomTrigger");
            }

            triggerSchema.addProperty("id", customTrigger.id());
            triggerSchema.addProperty("description", customTrigger.description());

            final JsonObject inputSchema = new JsonObject();

            Arrays.stream(customTrigger.input()).forEach(field -> {
                final JsonObject fieldSchema = new JsonObject();

                fieldSchema.addProperty("type", field.type().getSimpleName());
                fieldSchema.addProperty("description", field.description());
                fieldSchema.addProperty("required", field.required());

                inputSchema.add(field.name(), fieldSchema);
            });

            triggerSchema.add("input", inputSchema);

            return triggerSchema;
        }).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }
}
