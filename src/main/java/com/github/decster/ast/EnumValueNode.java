package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents an enum value in a Thrift document.
 */
public class EnumValueNode extends Node {
    private String name;
    private Integer value;  // Optional integer value
    private List<TypeAnnotationNode> annotations;

    public EnumValueNode(String name) {
        this.name = name;
        this.annotations = new ArrayList<>();
    }

    public EnumValueNode(String name, Integer value) {
        this.name = name;
        this.value = value;
        this.annotations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
