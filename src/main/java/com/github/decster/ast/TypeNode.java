package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Base class for all types in a Thrift document.
 */
public abstract class TypeNode extends Node {
    private List<TypeAnnotationNode> annotations;

    public TypeNode() {
        this.annotations = new ArrayList<>();
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }

    public abstract String getName();

    /**
     * Check if this type is a struct
     */
    public boolean isStruct() {
        return false;
    }

    /**
     * Check if this type is an exception
     */
    public boolean isException() {
        return false;
    }

    /**
     * Check if this type is a container (list, set, map)
     */
    public boolean isContainer() {
        return false;
    }

    /**
     * Check if this type is a list
     */
    public boolean isList() {
        return false;
    }

    /**
     * Check if this type is a set
     */
    public boolean isSet() {
        return false;
    }

    /**
     * Check if this type is a map
     */
    public boolean isMap() {
        return false;
    }

    /**
     * Check if this type is an enum
     */
    public boolean isEnum() {
        return false;
    }

    /**
     * Check if this type is a binary type
     */
    public boolean isBinary() {
        return false;
    }

    /**
     * Check if this type is a typedef
     */
    public boolean isTypedef() {
        return false;
    }

    /**
     * Check if this type is a base type (primitive)
     */
    public boolean isBaseType() {
        return false;
    }

    /**
     * Get child nodes for container types
     */
    public List<TypeNode> getChildNodes() {
        return new ArrayList<>();
    }
}
