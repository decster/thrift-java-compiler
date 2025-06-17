package io.github.decster.ast;

/**
 * A const is a constant value defined across languages that has a type and
 * a value. The trick here is that the declared type might not match the type
 * of the value object, since that is not determined until after parsing the
 * whole thing out.
 * Corresponds to t_const.h in the C++ implementation.
 */
public class TConst extends TDoc {
  private TType type;
  private String name;
  private TConstValue value;

  public TConst(TType type, String name, TConstValue value) {
    this.type = type;
    this.name = name;
    this.value = value;
  }

  public void setType(TType type) { this.type = type; }

  public TType getType() { return type; }

  public String getName() { return name; }

  public TConstValue getValue() { return value; }
}
