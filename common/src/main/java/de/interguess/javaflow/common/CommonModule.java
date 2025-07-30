package de.interguess.javaflow.common;

import com.google.inject.AbstractModule;
import de.interguess.javaflow.api.construct.procedure.ProcedureProvider;
import de.interguess.javaflow.api.construct.trigger.TriggerProvider;
import de.interguess.javaflow.api.io.input.Inputs;
import de.interguess.javaflow.api.io.output.Outputs;
import de.interguess.javaflow.api.reference.ReferenceResolver;
import de.interguess.javaflow.api.workflow.WorkflowProvider;
import de.interguess.javaflow.common.io.input.InputsImpl;
import de.interguess.javaflow.common.io.output.OutputsImpl;
import de.interguess.javaflow.common.procedure.ProcedureProviderImpl;
import de.interguess.javaflow.common.reference.ReferenceResolverImpl;
import de.interguess.javaflow.common.trigger.TriggerProviderImpl;
import de.interguess.javaflow.common.workflow.WorkflowProviderImpl;

public class CommonModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ProcedureProvider.class).to(ProcedureProviderImpl.class).asEagerSingleton();
        bind(TriggerProvider.class).to(TriggerProviderImpl.class).asEagerSingleton();
        bind(WorkflowProvider.class).to(WorkflowProviderImpl.class).asEagerSingleton();

        bind(Inputs.class).to(InputsImpl.class).asEagerSingleton();
        bind(Outputs.class).to(OutputsImpl.class).asEagerSingleton();

        bind(ReferenceResolver.class).to(ReferenceResolverImpl.class).asEagerSingleton();
    }
}
