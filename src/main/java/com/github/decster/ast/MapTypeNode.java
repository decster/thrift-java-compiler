package com.github.decster.ast;

/**
 * Represents a map type in a Thrift document.
 */
public class MapTypeNode extends ContainerTypeNode {
    private TypeNode keyType;
    private TypeNode valueType;

    public MapTypeNode(TypeNode keyType, TypeNode valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public TypeNode getKeyType() {
        return keyType;
    }

    public void setKeyType(TypeNode keyType) {
        this.keyType = keyType;
    }

    public TypeNode getValueType() {
        return valueType;
    }

    public void setValueType(TypeNode valueType) {
        this.valueType = valueType;
    }

    @Override
    public String getName() {
        return "map<" + keyType.getName() + "," + valueType.getName() + ">";
    }
}
