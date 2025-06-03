package com.github.decster.ast;

/**
 * Represents a custom type identified by name in a Thrift document.
 * This could be a typedef or a user-defined type like an enum, struct, etc.
 */
public class IdentifierTypeNode extends TypeNode {
    private String name;

    public IdentifierTypeNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
