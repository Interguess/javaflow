package de.interguess.javaflow.common.schema.member;

import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.schema.SchemaProperty;

import java.lang.reflect.Field;

public class AnnotatedMemberMapper {

    public static AnnotatedMember map(CustomProcedure.Field customProcedure) {
        return AnnotatedMember.builder()
                .name(customProcedure.name())
                .description(customProcedure.description())
                .required(customProcedure.required())
                .type(customProcedure.type())
                .build();
    }

    public static AnnotatedMember map(Field field) {
        if (!field.isAnnotationPresent(SchemaProperty.class)) {
            throw new IllegalArgumentException("Field must be annotated with @SchemaProperty");
        }

        final SchemaProperty schemaProperty = field.getAnnotation(SchemaProperty.class);

        assert schemaProperty != null; //cant be null, we checked above

        return AnnotatedMember.builder()
                .name(field.getName())
                .description(schemaProperty.description())
                .required(schemaProperty.required())
                .type(field.getType())
                .build();
    }

    public static boolean fieldMappable(Field field) {
        return field.isAnnotationPresent(SchemaProperty.class);
    }
}
