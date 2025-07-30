package de.interguess.javaflow.common.schema.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnnotatedMember {

    private final String name;

    private final String description;

    private final boolean required;

    private final Class<?> type;
}