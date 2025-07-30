package de.interguess.javaflow.common.io.input;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.io.input.Inputs;
import de.interguess.javaflow.api.io.input.MultiInput;
import org.jetbrains.annotations.NotNull;

@Singleton
public class InputsImpl extends Inputs {

    public InputsImpl() {
        Inputs.setInstance(this);
    }

    @Override
    public @NotNull MultiInput.Builder create() {
        return new MultiInputImpl.Builder();
    }
}
