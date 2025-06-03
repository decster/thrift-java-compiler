package com.github.decster.ast;

/**
 * Represents a constant definition in a Thrift document.
 */
public class ConstNode extends DefinitionNode {
    private TypeNode type;
    private Object value;  // Can be various constant types

    public ConstNode(String name, TypeNode type, Object value) {
        super(name);
        this.type = type;
        this.value = value;
    }

    public TypeNode getType() {
        return type;
    }

    public void setType(TypeNode type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
