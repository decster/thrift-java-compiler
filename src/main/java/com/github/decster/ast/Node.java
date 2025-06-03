package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Base class for all AST nodes in the Thrift compiler.
 */
public abstract class Node {
    private int line;
    private int column;

    public Node() {
    }

    public Node(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
