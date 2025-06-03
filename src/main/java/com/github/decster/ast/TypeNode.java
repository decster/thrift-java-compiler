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
}
