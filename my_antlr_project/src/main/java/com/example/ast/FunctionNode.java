package com.example.ast;

import java.util.List;
import java.util.ArrayList;

public class FunctionNode implements Node {
    public final boolean oneway;
    public final TypeNode returnType; // Could be a special "VoidTypeNode" or null
    public final String name;
    public final List<FieldNode> arguments = new ArrayList<>();
    public final List<FieldNode> exceptions = new ArrayList<>(); // Throws list

    public FunctionNode(boolean oneway, TypeNode returnType, String name) {
        this.oneway = oneway;
        this.returnType = returnType;
        this.name = name;
    }
    public void addArgument(FieldNode arg) { this.arguments.add(arg); }
    public void addException(FieldNode ex) { this.exceptions.add(ex); }
    @Override public String toString() { return "Function(" + name + ")"; }
}
