package de.interguess.javaflow.api.construct.procedure;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomProcedure {

    @NotNull String id();

    @NotNull String description();

    @NotNull String shorthand();

    @NotNull Field[] input() default {};

    @NotNull Field[] output() default {};

    @interface Field {

        String name();

        String description();

        Class<?> type();

        boolean required() default false;

        RegexValidation regex() default @RegexValidation(regex = "", errorMessage = "");
    }

    @interface RegexValidation {

        String regex();

        String errorMessage();
    }
}
