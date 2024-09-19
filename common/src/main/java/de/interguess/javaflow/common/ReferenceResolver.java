package de.interguess.javaflow.common;

import de.interguess.javaflow.api.workflow.Workflow;

import java.util.concurrent.atomic.AtomicReference;

public class ReferenceResolver {

    private static final String PATTERN = "${ %s }";

    private static final String PATTERN_REGEX = "\\$\\{\\s*(\\w+)\\s*\\}";

    public static Object resolve(Workflow workflow, String input) {
        final AtomicReference<String> result = new AtomicReference<>(input);

        if (input.matches(PATTERN_REGEX)) {
            final String key = result.get()
                    .replace("${", "")
                    .replace("}", "")
                    .trim();

            return workflow.getVariable(key);
        } else {
            workflow.getVariables().forEach((key, value) -> {
                result.set(result.get().replace(String.format(PATTERN, key), value.toString()));
            });

            return result.get();
        }
    }
}
