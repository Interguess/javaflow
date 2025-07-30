package de.interguess.javaflow.common.schema;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.schema.SchemaProvider;
import de.interguess.javaflow.common.schema.member.AnnotatedMember;
import de.interguess.javaflow.common.schema.member.AnnotatedMemberMapper;
import de.interguess.javaflow.common.schema.vertexai.ObjectParamSerializer;
import de.interguess.javaflow.common.schema.vertexai.VertexAIAllParamsSerializer;
import de.interguess.javaflow.common.schema.vertexai.dto.VertexAIAllParams;
import de.interguess.javaflow.common.schema.vertexai.dto.VertexAIFunction;
import de.interguess.javaflow.common.schema.vertexai.dto.parameter.ParameterType;
import de.interguess.javaflow.common.schema.vertexai.dto.parameter.VertexAIFunctionParameter;
import de.interguess.javaflow.common.schema.vertexai.dto.parameters.ArrayParam;
import de.interguess.javaflow.common.schema.vertexai.dto.parameters.ObjectParam;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VertexAISchemaProvider implements SchemaProvider<Procedure> {

    @Override
    public @NotNull JsonArray provideSchema(@NotNull List<Procedure> procedures) {
        return procedures.stream().map(procedure -> {
            final CustomProcedure customProcedure = procedure.getClass().getAnnotation(CustomProcedure.class);

            if (customProcedure == null) {
                throw new IllegalArgumentException("Procedure must be annotated with @CustomProcedure");
            }

            final List<String> requiredParameters = new ArrayList<>();

            System.out.println("Processing procedure: " + customProcedure.id());

            final Map<String, VertexAIFunctionParameter> functionParameters = Arrays.stream(customProcedure.input())
                    .map(field -> {
                        final ParameterType parameterType = ParameterType.fromClass(field.type());

                        VertexAIFunctionParameter parameter;
                        if (parameterType.equals(ParameterType.OBJECT)) {
                            parameter = serialize(field.name(), field.type()); //todo: check if Field#getName or just name
                        } else if (parameterType.equals(ParameterType.ARRAY)) {
                            System.out.println("Field.name: " + field.name());
                            parameter = new ArrayParam(
                                    field.name(),
                                    field.description(),
                                    VertexAIFunctionParameter.builder()
                                            .type(parameterType.toString())
                                            .description(field.description())
                                            .build()
                            );
                        } else {
                            parameter = VertexAIFunctionParameter.builder()
                                    .name(field.name())
                                    .type(parameterType.toString())
                                    .description(field.description())
                                    .build();
                        }
                        System.out.println("CALLING FIELD: " + field.name());

                        if (field.required()) {
                            requiredParameters.add(parameter.getName());
                        }

                        return parameter;
                    })
                    .collect(Collectors.toMap(VertexAIFunctionParameter::getName, Function.identity()));

            final VertexAIAllParams allParams = VertexAIAllParams.builder()
                    .properties(functionParameters)
                    .required(requiredParameters)
                    .build();

            final VertexAIFunction vertexAIFunction = VertexAIFunction.builder()
                    .name(customProcedure.id())
                    .description(customProcedure.description())
                    .parameters(allParams)
                    .build();

            return new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .registerTypeAdapter(ObjectParam.class, new ObjectParamSerializer())
                    .registerTypeAdapter(VertexAIAllParams.class, new VertexAIAllParamsSerializer())
                    .create()
                    .toJsonTree(vertexAIFunction);
        }).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    public ObjectParam serialize(String name, Class<?> clazz) {
        System.out.println("SERIALIZING CLASS: " + clazz.getSimpleName());
        final Map<String, VertexAIFunctionParameter> properties = new HashMap<>();

        final List<String> required = new ArrayList<>();

        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    final ParameterType parameterType = ParameterType.fromClass(field.getType());

                    VertexAIFunctionParameter parameter;

                    if (!AnnotatedMemberMapper.fieldMappable(field)) {
                        return; //Field is not mappable
                    }

                    AnnotatedMember annotatedMember = AnnotatedMemberMapper.map(field);

                    if (parameterType.equals(ParameterType.OBJECT)) {
                        System.out.println("SERIALIZING OBJECT: " + field.getType().getSimpleName());
                        parameter = serialize(field.getName(), field.getType()); //todo: check if Field#getName or just name
                    } else if (parameterType.equals(ParameterType.ARRAY)) {
                        System.out.println("SERIALIZING ARRAY: " + field.getType().getSimpleName());
                        parameter = new ArrayParam(
                                field.getName(),
                                annotatedMember.getDescription(),
                                VertexAIFunctionParameter.builder()
                                        .type(parameterType.toString())
                                        .description(annotatedMember.getDescription())
                                        .build()
                        );
                    } else {
                        System.out.println("SERIALIZING PRIMITIVE: " + field.getType().getSimpleName());
                        parameter = VertexAIFunctionParameter.builder()
                                .name(field.getName())
                                .type(parameterType.toString())
                                .description(annotatedMember.getDescription())
                                .build();
                    }

                    properties.put(field.getName(), parameter);

                    if (annotatedMember.isRequired()) {
                        required.add(field.getName());
                    }
                });

        return new ObjectParam(
                name,
                String.format("Object of type %s", clazz.getSimpleName()),
                properties,
                required
        );
    }
}
