package de.interguess.javaflow.api.schema;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SchemaProvider<T> {

    @NotNull JsonElement provideSchema(@NotNull List<T> data);
}
