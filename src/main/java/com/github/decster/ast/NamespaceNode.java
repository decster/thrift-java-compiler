package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a namespace declaration in a Thrift document.
 */
public class NamespaceNode extends HeaderNode {
    private String scope;
    private String name;
    private List<TypeAnnotationNode> annotations;

    public NamespaceNode(String scope, String name) {
        this.scope = scope;
        this.name = name;
        this.annotations = new ArrayList<>();
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
