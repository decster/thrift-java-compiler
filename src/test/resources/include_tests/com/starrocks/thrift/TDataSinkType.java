/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public enum TDataSinkType implements org.apache.thrift.TEnum {
  DATA_STREAM_SINK(0),
  RESULT_SINK(1),
  DATA_SPLIT_SINK(2),
  MYSQL_TABLE_SINK(3),
  EXPORT_SINK(4),
  OLAP_TABLE_SINK(5),
  MEMORY_SCRATCH_SINK(6),
  MULTI_CAST_DATA_STREAM_SINK(7),
  SCHEMA_TABLE_SINK(8),
  ICEBERG_TABLE_SINK(9),
  HIVE_TABLE_SINK(10),
  TABLE_FUNCTION_TABLE_SINK(11),
  BLACKHOLE_TABLE_SINK(12),
  DICTIONARY_CACHE_SINK(13),
  MULTI_OLAP_TABLE_SINK(14),
  SPLIT_DATA_STREAM_SINK(15);

  private final int value;

  private TDataSinkType(int value) {
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
  public static TDataSinkType findByValue(int value) { 
    switch (value) {
      case 0:
        return DATA_STREAM_SINK;
      case 1:
        return RESULT_SINK;
      case 2:
        return DATA_SPLIT_SINK;
      case 3:
        return MYSQL_TABLE_SINK;
      case 4:
        return EXPORT_SINK;
      case 5:
        return OLAP_TABLE_SINK;
      case 6:
        return MEMORY_SCRATCH_SINK;
      case 7:
        return MULTI_CAST_DATA_STREAM_SINK;
      case 8:
        return SCHEMA_TABLE_SINK;
      case 9:
        return ICEBERG_TABLE_SINK;
      case 10:
        return HIVE_TABLE_SINK;
      case 11:
        return TABLE_FUNCTION_TABLE_SINK;
      case 12:
        return BLACKHOLE_TABLE_SINK;
      case 13:
        return DICTIONARY_CACHE_SINK;
      case 14:
        return MULTI_OLAP_TABLE_SINK;
      case 15:
        return SPLIT_DATA_STREAM_SINK;
      default:
        return null;
    }
  }
}
