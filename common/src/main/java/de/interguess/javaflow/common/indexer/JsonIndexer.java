package de.interguess.javaflow.common.indexer;

import com.google.gson.*;
import de.interguess.javaflow.api.exception.WorkflowLoadException;
import de.interguess.javaflow.api.index.ExecutableIndex;
import de.interguess.javaflow.api.index.TriggerIndex;
import de.interguess.javaflow.api.index.WorkflowIndex;
import de.interguess.javaflow.api.index.executable.LoopIndex;
import de.interguess.javaflow.api.index.executable.ProcedureIndex;
import de.interguess.javaflow.api.index.executable.RouterIndex;
import de.interguess.javaflow.api.indexer.WorkflowIndexer;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.common.util.NullUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * GsonIndexer implements the WorkflowIndexer interface to parse JSON workflow definitions
 * and create corresponding Java objects representing the workflow structure.
 */
public class JsonIndexer implements WorkflowIndexer {

    // Gson instance for JSON parsing and object conversion
    private final Gson gson = new GsonBuilder().create();

    /**
     * Indexes a workflow from a JSON string.
     * <p/>
     * @param workflow The JSON string representation of the workflow
     * @return Returns the parsed {@link WorkflowIndex} from the json
     */
    @Override @NotNull
    public WorkflowIndex index(@NotNull String workflow) {
        try {
            JsonObject jsonObject = JsonParser.parseString(workflow).getAsJsonObject();
            return this.indexWorkflow(jsonObject);
        } catch (JsonSyntaxException exception) {
            // File contains invalid json syntax
            throw new WorkflowLoadException("Error parsing syntax of workflow", exception);
        }
    }

    /**
     * Indexes a workflow from a json object.
     * <p/>
     * @param json The {@link JsonObject} representing the workflow
     * @return Returns the {@link WorkflowIndex} object
     */
    @NotNull
    private WorkflowIndex indexWorkflow(@NotNull JsonObject json) {
        final String name = NullUtil.defaultIfNull(json.has("name") ? json.get("name").getAsString() : null, "Unnamed Workflow");

        // Parse variables
        final Map<String, Object> variables = this.parseVariables(json);

        // Parse triggers
        final List<TriggerIndex> triggers = this.parseTriggers(json);

        // Build and return the WorkflowIndex
        return WorkflowIndex.builder()
                .name(name)
                .variables(variables)
                .triggers(triggers)
                .build();
    }

    /**
     * Parses variables from the workflow JSON.
     * <p/>
     * @param json The {@link JsonObject} representing the workflow
     * @return Map of variable names to their values
     */
    @NotNull
    private Map<String, Object> parseVariables(@NotNull JsonObject json) {
        final Map<String, Object> variables = new HashMap<>();
        if (json.has("variables")) {
            JsonObject variablesJson = json.getAsJsonObject("variables");
            for (Map.Entry<String, JsonElement> entry : variablesJson.entrySet())
                variables.put(entry.getKey(), gson.fromJson(entry.getValue(), Object.class));
        }
        return variables;
    }

    /**
     * Parses triggers from the workflow JSON.
     * <p/>
     * @param json The {@link JsonObject} representing the workflow
     * @return Returns a list of {@link JsonObject} objects
     */
    @NotNull
    private List<TriggerIndex> parseTriggers(@NotNull  JsonObject json) {
        final List<TriggerIndex> triggers = new ArrayList<>();
        if (json.has("triggers")) {
            JsonArray triggersJson = json.getAsJsonArray("triggers");
            for (JsonElement triggerElement : triggersJson) {
                triggers.add(this.indexTrigger(triggerElement.getAsJsonObject()));
            }
        }
        return triggers;
    }

    /**
     * Indexes a single trigger from a {@link JsonObject}.
     * <p/>
     * @param json The {@link JsonObject} representing the trigger
     * @return Returns the indexed {@link TriggerIndex} object
     */
    @NotNull
    private TriggerIndex indexTrigger(@NotNull JsonObject json) {
        final String id = json.get("id").getAsString();
        final String type = json.get("type").getAsString();

        final List<ExecutableIndex> tasks = new ArrayList<>();
        if (json.has("tasks")) {
            JsonArray tasksJson = json.getAsJsonArray("tasks");
            for (JsonElement taskElement : tasksJson) {
                tasks.add(this.indexExecutableElement(taskElement.getAsJsonObject()));
            }
        }

        return TriggerIndex.builder()
                .id(id)
                .type(type)
                .tasks(tasks)
                .build();
    }

    /**
     * Indexes an executable element (Router, Loop, or Procedure) from a JsonObject.
     * <p/>
     * @param json JsonObject representing the executable element
     * @return Returns the {@link ExecutableIndex} object (RouterIndex, LoopIndex, or ProcedureIndex)
     */
    @NotNull
    private ExecutableIndex indexExecutableElement(@NotNull JsonObject json) {
        final String id = json.get("id").getAsString();
        final String type = json.get("type").getAsString();

        return switch (type) {
            case "router" -> this.indexRouter(json, id, type);
            case "loop" -> this.indexLoop(json, id, type);
            default -> this.indexProcedure(json, id, type);
        };
    }

    /**
     * Indexes a router executable element.
     * <p/>
     * @param json The {@link JsonObject} representing the router
     * @param id The router id
     * @param type The router type
     * @return Returns the {@link RouterIndex} object
     */
    @NotNull
    private RouterIndex indexRouter(@NotNull JsonObject json, @NotNull String id, @NotNull String type) {
        final Map<Object, List<ExecutableIndex>> routes = new HashMap<>();
        JsonObject routesJson = json.getAsJsonObject("routes");
        for (Map.Entry<String, JsonElement> entry : routesJson.entrySet()) {
            List<ExecutableIndex> routeTasks = new ArrayList<>();
            JsonArray routeTasksJson = entry.getValue().getAsJsonArray();
            for (JsonElement taskElement : routeTasksJson)
                routeTasks.add(this.indexExecutableElement(taskElement.getAsJsonObject()));
            routes.put(entry.getKey(), routeTasks);
        }

        final Object inputObject = gson.fromJson(json.get("input"), Object.class);

        return RouterIndex.builder()
                .id(id)
                .type(type)
                .routes(routes)
                .input(inputObject)
                .build();
    }

    /**
     * Indexes a loop executable element.
     * <p/>
     * @param json The {@link JsonObject} representing the loop
     * @param id The loop id
     * @param type The loop type
     * @return Returns the {@link LoopIndex}
     */
    @NotNull
    private LoopIndex indexLoop(@NotNull JsonObject json, @NotNull String id, @NotNull String type) {
        final ProcedureIndex condition = (ProcedureIndex) this.indexExecutableElement(json.getAsJsonObject("condition"));

        final List<ExecutableIndex> tasks = new ArrayList<>();
        JsonArray tasksJson = json.getAsJsonArray("tasks");
        for (JsonElement taskElement : tasksJson)
            tasks.add(this.indexExecutableElement(taskElement.getAsJsonObject()));

        return LoopIndex.builder()
                .id(id)
                .type(type)
                .condition(condition)
                .tasks(tasks)
                .build();
    }

    /**
     * Indexes a procedure executable element.
     * <p/>
     * @param json The {@link JsonObject} representing the procedure
     * @param id The procedure id
     * @param type The procedure type
     * @return The {@link ProcedureIndex} object
     */
    @NotNull
    private ProcedureIndex indexProcedure(@NotNull JsonObject json, @NotNull String id, @NotNull String type) {
        final MultiInput.Builder input = MultiInput.create();
        JsonObject inputJson = json.getAsJsonObject("input");
        for (Map.Entry<String, JsonElement> entry : inputJson.entrySet())
            input.with(entry.getKey(), gson.fromJson(entry.getValue(), Object.class));

        return ProcedureIndex.builder()
                .id(id)
                .type(type)
                .input(input)
                .build();
    }
}