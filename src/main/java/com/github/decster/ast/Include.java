package com.github.decster.ast;

/**
 * Represents an include statement in a Thrift document.
 */
public class Include extends HeaderNode {
    private String path;

    public Include(String path) {
        this.path = path;
    }
}
