package com.github.decster.ast;

/**
 * Represents a list type in a Thrift document.
 */
public class ListTypeNode extends ContainerTypeNode {
    private TypeNode elementType;

    public ListTypeNode(TypeNode elementType) {
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
        return "list<" + elementType.getName() + ">";
    }
}
