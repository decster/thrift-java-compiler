package io.github.decster.ast;

/**
 * A map is a lightweight container type that maps from one data type to another.
 * Corresponds to t_map.h in the C++ implementation.
 */
public class TMap extends TContainer {
  private TType keyType;
  private TType valType;

  public TMap(TType keyType, TType valType) {
    this.keyType = keyType;
    this.valType = valType;
  }

  public TType getKeyType() { return keyType; }

  public TType getValType() { return valType; }

  public void setKeyType(TType keyType) { this.keyType = keyType; }

  public void setValType(TType valueType) { this.valType = valueType; }

  @Override
  public boolean isMap() {
    return true;
  }

  @Override
  public void validate() {
    // Add validation logic here if needed
  }

  @Override
  public String toString() {
    return "map<" + keyType + "," + valType + ">";
  }
}
