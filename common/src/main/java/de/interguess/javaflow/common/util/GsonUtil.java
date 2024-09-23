package de.interguess.javaflow.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

@UtilityClass
public class GsonUtil {

    private static final Gson GSON = new Gson();

    public static @NotNull Map<String, Serializable> deepJsonToMap(@NotNull Map<String, Serializable> map, @NotNull Object object) {
        final JsonObject jsonObject = GSON.toJsonTree(object).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                deepJsonToMap(map, entry.getValue().getAsJsonObject());
            } else {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }

        return map;
    }
}
