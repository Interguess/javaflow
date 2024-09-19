package de.interguess.javaflow.api;

import de.interguess.javaflow.api.workflow.Workflow;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class Reference<T> {

    private Class<T> type;

    private final String key;

    public Reference(Class<T> type, String key) {
        this.key = key;
    }

    public Object resolve(@NotNull Workflow workflow) {
        return workflow.getVariable(key);
    }
}
