package com.example.ast;

public class VoidTypeNode implements TypeNode {
    @Override public String getTypeName() { return "void"; }
    @Override public String toString() { return "VoidType"; }
}
