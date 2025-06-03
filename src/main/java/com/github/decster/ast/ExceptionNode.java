package com.github.decster.ast;

/**
 * Represents an exception definition in a Thrift document.
 */
public class ExceptionNode extends StructLikeNode {
    public ExceptionNode(String name) {
        super(name);
    }
}
