package com.github.decster.ast;

/**
 * Represents a type annotation in a Thrift document.
 */
public class TypeAnnotationNode extends Node {
    private String name;
    private Object value;  // Can be an Integer or String

    public TypeAnnotationNode(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
