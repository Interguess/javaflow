package de.interguess.javaflow.api.construct.trigger;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomTrigger {

    @NotNull String id();

    @NotNull String description();

    @NotNull Field[] input();

    @interface Field {

        @NotNull String name();

        @NotNull String description();

        @NotNull Class<?> type();

        boolean required() default false;
    }
}
