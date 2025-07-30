package de.interguess.javaflow.api.index;

public sealed interface Index permits ExecutableIndex, TriggerIndex, WorkflowIndex {

    String getId();
}
