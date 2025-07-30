package de.interguess.javaflow.common.reference;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.reference.ReferenceResolver;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Singleton
public class ReferenceResolverImpl extends ReferenceResolver {

    public ReferenceResolverImpl() {
        ReferenceResolver.setInstance(this);
    }

    @Override
    public @NotNull Object resolve(@NotNull Workflow context, @NotNull String reference) {
        final Map<String, Object> vars = context.getVariables();

        String out = "";

        for (int i = 0; i < reference.length(); ) {
            final char c = reference.charAt(i);

            if (c == '$' && i + 1 < reference.length() && reference.charAt(i + 1) == '{') {
                final int end = reference.indexOf('}', i + 2);

                if (end > 0) {
                    final String inside = reference.substring(i + 2, end).trim();

                    final Object value = vars.get(inside);

                    if (value != null) {
                        out = out.concat(value.toString());
                    } else {
                        out = out.concat(reference.substring(i, end + 1));
                    }

                    i = end + 1;

                    continue;
                }
            }

            out = out.concat(String.valueOf(c));

            i++;
        }

        return out;
    }
}
