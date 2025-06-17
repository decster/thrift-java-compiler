package io.github.decster.ast;

/**
 * A list is a lightweight container type that just wraps another data type.
 * Corresponds to t_list.h in the C++ implementation.
 */
public class TList extends TContainer {
  private TType elemType;

  public TList(TType elemType) { this.elemType = elemType; }

  public TType getElemType() { return elemType; }

  public void setElemType(TType elemType) { this.elemType = elemType; }

  @Override
  public boolean isList() {
    return true;
  }

  @Override
  public void validate() {
    // Add validation logic here if needed
  }

  @Override
  public String toString() {
    return "list<" + elemType + ">";
  }
}
