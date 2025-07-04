/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public enum TAggregationType implements org.apache.thrift.TEnum {
  SUM(0),
  MAX(1),
  MIN(2),
  REPLACE(3),
  HLL_UNION(4),
  NONE(5),
  BITMAP_UNION(6),
  REPLACE_IF_NOT_NULL(7),
  PERCENTILE_UNION(8),
  AGG_STATE_UNION(9);

  private final int value;

  private TAggregationType(int value) {
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
  public static TAggregationType findByValue(int value) { 
    switch (value) {
      case 0:
        return SUM;
      case 1:
        return MAX;
      case 2:
        return MIN;
      case 3:
        return REPLACE;
      case 4:
        return HLL_UNION;
      case 5:
        return NONE;
      case 6:
        return BITMAP_UNION;
      case 7:
        return REPLACE_IF_NOT_NULL;
      case 8:
        return PERCENTILE_UNION;
      case 9:
        return AGG_STATE_UNION;
      default:
        return null;
    }
  }
}
