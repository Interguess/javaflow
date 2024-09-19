package de.interguess.javaflow.common;

import com.google.inject.AbstractModule;
import de.interguess.javaflow.api.construct.procedure.ProcedureProvider;
import de.interguess.javaflow.api.construct.trigger.TriggerProvider;
import de.interguess.javaflow.api.workflow.WorkflowProvider;
import de.interguess.javaflow.common.procedure.ProcedureProviderImpl;
import de.interguess.javaflow.common.trigger.TriggerProviderImpl;
import de.interguess.javaflow.common.workflow.WorkflowProviderImpl;

public class CommonModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ProcedureProvider.class).to(ProcedureProviderImpl.class).asEagerSingleton();
        bind(TriggerProvider.class).to(TriggerProviderImpl.class).asEagerSingleton();
        bind(WorkflowProvider.class).to(WorkflowProviderImpl.class).asEagerSingleton();
    }
}
