package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a service definition in a Thrift document.
 */
public class ServiceNode extends DefinitionNode {
    private String extendsService;  // Optional parent service
    private List<FunctionNode> functionNodes;
    private List<TypeAnnotationNode> annotations;
    private String parent;

    public ServiceNode(String name) {
        super(name);
        this.functionNodes = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public ServiceNode(String name, String extendsService) {
        super(name);
        this.extendsService = extendsService;
        this.functionNodes = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public String getExtendsService() {
        return extendsService;
    }

    public void setExtendsService(String extendsService) {
        this.extendsService = extendsService;
    }

    public List<FunctionNode> getFunctions() {
        return functionNodes;
    }

    public void addFunction(FunctionNode functionNode) {
        this.functionNodes.add(functionNode);
    }

    public List<TypeAnnotationNode> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(TypeAnnotationNode annotation) {
        this.annotations.add(annotation);
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
