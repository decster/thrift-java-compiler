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
    private StructNode parameters;
    private StructNode exceptions;
    private List<TypeAnnotationNode> annotations;

    public FunctionNode(String name, TypeNode returnType) {
        this.name = name;
        this.returnType = returnType;
        this.oneway = Oneway.SYNC;
        this.parameters = new StructNode(name + "_args");
        this.exceptions = new StructNode(name + "_exceptions");
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
        return parameters.getFields();
    }

    public void addParameter(FieldNode parameter) {
        this.parameters.addField(parameter);
    }

    public List<FieldNode> getExceptions() {
        return exceptions.getFields();
    }

    public void addException(FieldNode exception) {
        this.exceptions.addField(exception);
    }

    /**
     * Returns the documentation string for this function.
     * @return the documentation string, or null if no documentation is available
     */
    public String getDocString() {
        return null;
    }

    /**
     * Returns the list of type annotations for this function.
     * @return the list of type annotations
     */
    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    /**
     * Adds a type annotation to this function.
     * @param annotation the annotation to add
     */
    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }
}
