package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a typedef definition in a Thrift document.
 */
public class TypedefNode extends DefinitionNode {
    private TypeNode type;
    private List<TypeAnnotationNode> annotations;

    public TypedefNode(String name, TypeNode type) {
        super(name);
        this.type = type;
        this.annotations = new ArrayList<>();
    }

    public TypeNode getType() {
        return type;
    }

    public void setType(TypeNode type) {
        this.type = type;
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
