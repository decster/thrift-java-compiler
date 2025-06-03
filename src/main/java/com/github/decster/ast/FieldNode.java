package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a field in a struct, union, exception, or function parameter in a Thrift document.
 */
public class FieldNode extends Node {
    public enum Requirement {
        REQUIRED,
        OPTIONAL,
        DEFAULT
    }

    private Integer id;  // Optional field ID
    private Requirement Requirement;
    private TypeNode type;
    private String name;
    private Object defaultValue;  // Optional default value
    private List<TypeAnnotationNode> annotations;

    public FieldNode(TypeNode type, String name) {
        this.type = type;
        this.name = name;
        this.Requirement = Requirement.DEFAULT;
        this.annotations = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Requirement getRequirement() {
        return Requirement;
    }

    public void setRequirement(Requirement Requirement) {
        this.Requirement = Requirement;
    }

    public TypeNode getType() {
        return type;
    }

    public void setType(TypeNode type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
