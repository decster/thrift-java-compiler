package com.example.ast;

import java.util.List;
import java.util.ArrayList;

public class ServiceNode implements DefinitionNode {
    public final String name;
    public final String extendsService; // Null if not extending
    public final List<FunctionNode> functions = new ArrayList<>();

    public ServiceNode(String name, String extendsService) {
        this.name = name;
        this.extendsService = extendsService;
    }
    public void addFunction(FunctionNode func) { this.functions.add(func); }
    @Override public String toString() { return "Service(" + name + (extendsService != null ? " extends " + extendsService : "") + ")"; }
}
