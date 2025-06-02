package com.example.ast;

import java.util.List;
import java.util.ArrayList;

public class StructNode implements DefinitionNode {
    public final String name;
    public final List<FieldNode> fields = new ArrayList<>();
    // public final List<AnnotationNode> annotations; // TODO

    public StructNode(String name) { this.name = name; }
    public void addField(FieldNode field) { this.fields.add(field); }
    @Override public String toString() { return "Struct(" + name + ", fields=" + fields.size() + ")"; }
}
