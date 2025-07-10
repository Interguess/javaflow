# Javaflow Framework

![image](https://github.com/user-attachments/assets/200c2189-6bd3-4795-b02a-fdb32386329c)

> [!WARNING]  
> Javaflow is currently still under active development, we do not yet have a version that is fully operational and performant.<br>
> You can use javaflow yourself to test it or support us in the development. There will still be some changes and whole structures can be revised.

A robust Java framework specifically designed to efficiently execute a wide range of tasks based on different conditions or triggers, providing seamless integration with AI systems. It has an advanced, automated schema generation process that simplifies the configuration of complex workflows. This framework is ideal for projects that require dynamic decision making, machine learning capabilities and automation of intelligent processes in different environments.

## How to install

### Use with gradle
If you are using gradle, add this snippet to your build.gradle.

```groovy
repositories {
    maven {
        url 'https://maven.pkg.github.com/interguess/javaflow'
    }
}
```

```groovy
dependencies {
    compileOnly 'de.interguess:javaflow:VERSION'
}
```

### Use with maven
If you are using maven, add this snippet to your pom.xml.

```xml
<repositories>
    <repository>
        <id>javaflow-github-packages</id>
        <name>Github Packages for javaflow repository</name>
        <url>https://maven.pkg.github.com/interguess/javaflow</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>de.interguess</groupId>
        <artifactId>javaflow</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

## How to use

### Creating a custom procedures

To create your own procedure, you first need a class in which you define the procedure with an annotation and write an action for it. to register it, you must register the package in which it is located via the ProcedureProvider. To do this, use `ProcedureProvider.getInstance().registerProcedures(“my.package.name”);`

```java
import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.io.MultiInput;
import de.interguess.javaflow.api.io.MultiOutput;
import de.interguess.javaflow.api.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

@CustomProcedure(
        id = "math.addition",
        description = "Adds two numbers",
        shorthand = "%first% + %second%",
        input = {
                @CustomProcedure.Field(
                        name = "first",
                        description = "The first number",
                        type = Number.class,
                        required = true
                ),
                @CustomProcedure.Field(
                        name = "second",
                        description = "The second number",
                        type = Number.class,
                        required = true
                )
        },
        output = {
                @CustomProcedure.Field(
                        name = "result",
                        description = "The result of the addition",
                        type = Number.class
                )
        }
)
public class AdditionFunction implements Procedure {

    @Override
    public @NotNull MultiOutput execute(@NotNull Workflow workflow, MultiInput input) {
        final double first = input.required(Double.class, "first");
        final double second = input.required(Double.class, "second");

        return MultiOutput.create()
                .with("result", first + second);
    }
}
```

### Creating a custom trigger

Triggers are events to which workflows can react. To create your own trigger, you first need a class in which you define the trigger with an annotation. To register the trigger, you must register the package in which it is located via the TriggerProvider. To do this, use `TriggerProvider.getInstance().registerProcedures(“my.package.name”);`

```java
import de.interguess.javaflow.api.construct.trigger.CustomTrigger;
import de.interguess.javaflow.api.construct.trigger.Trigger;

@CustomTrigger(
        id = "trigger.my",
        description = "My trigger",
        input = {
                @CustomTrigger.Field(
                        name = "myInput",
                        description = "My input",
                        type = String.class,
                        required = true
                )
        }
)
public class MyTrigger implements Trigger {
}
```

### Calling a custom trigger
```java
final MultiInput input = MultiInput.create()
                .with("myInput", "Hello, World!");
        
TriggerProvider.getInstance().callTrigger(new MyTrigger(), input);
```


![Made with Java](http://ForTheBadge.com/images/badges/made-with-java.svg)
