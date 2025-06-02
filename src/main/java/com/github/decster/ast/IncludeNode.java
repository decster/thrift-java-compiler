package com.github.decster.ast;

public class IncludeNode implements HeaderNode {
    public final String path;
    public IncludeNode(String path) { this.path = path; }
    @Override public String toString() { return "Include(" + path + ")"; }
}
