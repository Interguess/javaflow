package de.interguess.javaflow.common.io.output;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.io.output.MultiOutput;
import de.interguess.javaflow.api.io.output.Outputs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Singleton
public class OutputsImpl extends Outputs {

    public OutputsImpl() {
        Outputs.setInstance(this);
    }

    @Override
    public @NotNull MultiOutput create() {
        return new MultiOutputImpl(new HashMap<>());
    }
}
