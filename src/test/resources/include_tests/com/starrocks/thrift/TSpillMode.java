/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public enum TSpillMode implements org.apache.thrift.TEnum {
  AUTO(0),
  FORCE(1),
  NONE(2),
  RANDOM(3);

  private final int value;

  private TSpillMode(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  @Override
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  @org.apache.thrift.annotation.Nullable
  public static TSpillMode findByValue(int value) { 
    switch (value) {
      case 0:
        return AUTO;
      case 1:
        return FORCE;
      case 2:
        return NONE;
      case 3:
        return RANDOM;
      default:
        return null;
    }
  }
}
