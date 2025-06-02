package com.example.ast;

public class BaseTypeNode implements TypeNode {
    public final String typeName; // e.g., "i32", "string"
    public BaseTypeNode(String typeName) { this.typeName = typeName; }
    @Override public String getTypeName() { return typeName; }
    @Override public String toString() { return "BaseType(" + typeName + ")"; }
}
