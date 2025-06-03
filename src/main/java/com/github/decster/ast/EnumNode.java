package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents an enum definition in a Thrift document.
 */
public class EnumNode extends DefinitionNode {
    private List<EnumValueNode> values;
    private List<TypeAnnotationNode> annotations;

    public EnumNode(String name) {
        super(name);
        this.values = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public List<EnumValueNode> getValues() {
        return values;
    }

    public void addValue(EnumValueNode value) {
        this.values.add(value);
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
