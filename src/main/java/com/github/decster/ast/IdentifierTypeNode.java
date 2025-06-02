package com.github.decster.ast;

public class IdentifierTypeNode implements TypeNode {
    public final String identifier;
    public IdentifierTypeNode(String identifier) { this.identifier = identifier; }
    @Override public String getTypeName() { return identifier; }
    @Override public String toString() { return "IdentifierType(" + identifier + ")"; }
}
