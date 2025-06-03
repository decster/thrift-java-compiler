package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a function definition in a service in a Thrift document.
 */
public class FunctionNode extends Node {
    public enum Oneway {
        SYNC,
        ONEWAY,
        ASYNC
    }

    private Oneway oneway;
    private TypeNode returnType;  // Can be null for void functions
    private String name;
    private List<FieldNode> parameters;
    private List<FieldNode> exceptions;
    private List<TypeAnnotationNode> annotations;

    public FunctionNode(String name, TypeNode returnType) {
        this.name = name;
        this.returnType = returnType;
        this.oneway = Oneway.SYNC;
        this.parameters = new ArrayList<>();
        this.exceptions = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public Oneway getOneway() {
        return oneway;
    }

    public void setOneway(Oneway oneway) {
        this.oneway = oneway;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeNode returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FieldNode> getParameters() {
        return parameters;
    }

    public void addParameter(FieldNode parameter) {
        this.parameters.add(parameter);
    }

    public List<FieldNode> getExceptions() {
        return exceptions;
    }

    public void addException(FieldNode exception) {
        this.exceptions.add(exception);
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
