package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

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

    @Override
    public boolean isSet() {
        return true;
    }

    @Override
    public List<TypeNode> getChildNodes() {
        List<TypeNode> children = new ArrayList<>();
        children.add(elementType);
        return children;
    }
}
