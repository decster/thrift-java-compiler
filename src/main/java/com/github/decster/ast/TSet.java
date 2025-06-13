package com.github.decster.ast;

/**
 * A set is a lightweight container type that just wraps another data type.
 * Corresponds to t_set.h in the C++ implementation.
 */
public class TSet extends TContainer {
    private TType elemType;

    public TSet(TType elemType) {
        this.elemType = elemType;
    }

    public TType getElemType() {
        return elemType;
    }

    @Override
    public boolean isSet() {
        return true;
    }

    @Override
    public void validate() {
        // Add validation logic here if needed
    }

    @Override
    public String toString() {
        return "set<" + elemType + ">";
    }
}
