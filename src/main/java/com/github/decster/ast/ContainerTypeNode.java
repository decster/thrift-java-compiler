package com.github.decster.ast;

/**
 * Base class for all container types in a Thrift document.
 */
public abstract class ContainerTypeNode extends TypeNode {
    // Common properties for all container types
    private String cppType;

    public String getCppType() {
        return cppType;
    }

    public void setCppType(String cppType) {
        this.cppType = cppType;
    }

    @Override
    public boolean isContainer() {
        return true;
    }
}
