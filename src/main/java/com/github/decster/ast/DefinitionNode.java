package com.github.decster.ast;

/**
 * Base class for all definition elements in a Thrift document.
 */
public abstract class DefinitionNode extends Node {
    private String name;

    public DefinitionNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
