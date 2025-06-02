package com.github.decster.ast;

public class FieldNode implements Node {
    public final Integer fieldId; // Optional in some contexts, but usually present
    public final String requiredness; // "required", "optional", or null for default
    public final TypeNode fieldType;
    public final String name;
    // public final ConstValueNode defaultValue; // TODO
    // public final List<AnnotationNode> annotations; // TODO

    public FieldNode(Integer fieldId, String requiredness, TypeNode fieldType, String name) {
        this.fieldId = fieldId;
        this.requiredness = requiredness;
        this.fieldType = fieldType;
        this.name = name;
    }
    @Override public String toString() { return "Field(" + name + ": " + fieldType.getTypeName() + ")"; }
}
