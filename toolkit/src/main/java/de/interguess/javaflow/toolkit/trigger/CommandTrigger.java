package de.interguess.javaflow.toolkit.trigger;

import de.interguess.javaflow.api.construct.trigger.CustomTrigger;
import de.interguess.javaflow.api.construct.trigger.Trigger;

@CustomTrigger(
        id = "command",
        description = "Trigger that is called when a command is executed",
        input = {
                @CustomTrigger.Field(
                        name = "command",
                        description = "The command that was executed",
                        type = String.class,
                        required = true
                ),
                @CustomTrigger.Field(
                        name = "value",
                        description = "The first argument of the command",
                        type = Number.class,
                        required = true
                )
        }

)
public class CommandTrigger implements Trigger {

}
