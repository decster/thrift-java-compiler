package io.github.decster.ast;

/**
 * A thrift base type, which must be one of the defined enumerated types.
 * Corresponds to t_base_type.h in the C++ implementation.
 */
public class TBaseType extends TType {
  /**
   * Enumeration of thrift base types
   */
  public enum Base { TYPE_VOID, TYPE_STRING, TYPE_UUID, TYPE_BOOL, TYPE_I8, TYPE_I16, TYPE_I32, TYPE_I64, TYPE_DOUBLE }

  private Base base;
  private boolean binary;

  public TBaseType(String name, Base base) {
    this.setName(name);
    this.base = base;
    this.binary = false;
  }

  public TBaseType(Base base) {
    this.setName(base.name().substring(5));
    this.base = base;
    this.binary = false;
  }

  public Base getBase() { return base; }

  public void setBase(Base base) { this.base = base; }

  @Override
  public boolean isBinary() {
    return binary;
  }

  public void setBinary(boolean binary) { this.binary = binary; }

  @Override
  public boolean isBaseType() {
    return true;
  }

  @Override
  public boolean isString() {
    return base == Base.TYPE_STRING;
  }

  @Override
  public boolean isBool() {
    return base == Base.TYPE_BOOL;
  }

  @Override
  public boolean isVoid() {
    return base == Base.TYPE_VOID;
  }

  @Override
  public boolean isUUID() {
    return base == Base.TYPE_UUID;
  }

  @Override
  public String toString() {
    return "base(" + base.name() + ")";
  }
}
