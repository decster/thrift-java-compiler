package com.github.decster.ast;

public class NamespaceNode implements HeaderNode {
    public final String scope;
    public final String name;
    public NamespaceNode(String scope, String name) {
        this.scope = scope;
        this.name = name;
    }
    @Override public String toString() { return "Namespace(" + scope + " " + name + ")"; }
}
