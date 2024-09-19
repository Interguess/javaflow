package de.interguess.javaflow.api.schema;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SchemaProvider<T> {

    @NotNull String provideSchema(@NotNull List<T> data);
}
