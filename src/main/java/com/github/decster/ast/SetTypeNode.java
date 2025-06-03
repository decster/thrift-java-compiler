package com.github.decster.ast;

/**
 * Represents a set type in a Thrift document.
 */
public class SetTypeNode extends ContainerTypeNode {
    private TypeNode elementType;

    public SetTypeNode(TypeNode elementType) {
        this.elementType = elementType;
    }

    public TypeNode getElementType() {
        return elementType;
    }

    public void setElementType(TypeNode elementType) {
        this.elementType = elementType;
    }

    @Override
    public String getName() {
        return "set<" + elementType.getName() + ">";
    }
}
