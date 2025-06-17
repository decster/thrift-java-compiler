package com.github.decster.ast;

/**
 * Base class for container types (list, set, map).
 * Corresponds to t_container.h in the C++ implementation.
 */
public class TContainer extends TType {
  private String cppName;
  private boolean hasCppName;

  public TContainer() { this.hasCppName = false; }

  public void setCppName(String cppName) {
    this.cppName = cppName;
    this.hasCppName = true;
  }

  public boolean hasCppName() { return hasCppName; }

  public String getCppName() { return cppName; }

  @Override
  public boolean isContainer() {
    return true;
  }
}
