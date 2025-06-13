package com.github.decster.ast;

/**
 * A map is a lightweight container type that maps from one data type to another.
 * Corresponds to t_map.h in the C++ implementation.
 */
public class TMap extends TContainer {
    private TType keyType;
    private TType valueType;

    public TMap(TType keyType, TType valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public TType getKeyType() {
        return keyType;
    }

    public TType getValueType() {
        return valueType;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public void validate() {
        // Add validation logic here if needed
    }

    @Override
    public String toString() {
        return "map<" + keyType + "," + valueType + ">";
    }
}
