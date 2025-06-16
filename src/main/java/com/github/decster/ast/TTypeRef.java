package com.github.decster.ast;

// Helper class for resolving type references
public class TTypeRef extends TType {
  public TTypeRef(TProgram program, String name) {
    setProgram(program);
    setName(name);
  }
}
