package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Base class for structure-like definitions (struct, union, exception) in a Thrift document.
 */
public abstract class StructLikeNode extends DefinitionNode {
    private List<FieldNode> fieldNodes;
    private List<TypeAnnotationNode> annotations;

    public StructLikeNode(String name) {
        super(name);
        this.fieldNodes = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public List<FieldNode> getFields() {
        return fieldNodes;
    }

    public void addField(FieldNode fieldNode) {
        this.fieldNodes.add(fieldNode);
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
