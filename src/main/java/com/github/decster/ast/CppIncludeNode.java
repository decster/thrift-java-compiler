package com.github.decster.ast;

/**
 * Represents a cpp_include statement in a Thrift document.
 */
public class CppIncludeNode extends HeaderNode {
    private String path;

    public CppIncludeNode(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
