package com.github.decster.ast;

/**
 * Represents a union definition in a Thrift document.
 */
public class UnionNode extends StructLikeNode {
    public UnionNode(String name) {
        super(name);
    }
}
