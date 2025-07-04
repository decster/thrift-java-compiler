/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public enum TFileFormatType implements org.apache.thrift.TEnum {
  FORMAT_UNKNOWN(-1),
  FORMAT_CSV_PLAIN(0),
  FORMAT_CSV_GZ(1),
  FORMAT_CSV_LZO(2),
  FORMAT_CSV_BZ2(3),
  FORMAT_CSV_LZ4_FRAME(4),
  FORMAT_CSV_LZOP(5),
  FORMAT_PARQUET(6),
  FORMAT_CSV_DEFLATE(7),
  FORMAT_ORC(8),
  FORMAT_JSON(9),
  FORMAT_CSV_ZSTD(10),
  FORMAT_AVRO(11);

  private final int value;

  private TFileFormatType(int value) {
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
  public static TFileFormatType findByValue(int value) { 
    switch (value) {
      case -1:
        return FORMAT_UNKNOWN;
      case 0:
        return FORMAT_CSV_PLAIN;
      case 1:
        return FORMAT_CSV_GZ;
      case 2:
        return FORMAT_CSV_LZO;
      case 3:
        return FORMAT_CSV_BZ2;
      case 4:
        return FORMAT_CSV_LZ4_FRAME;
      case 5:
        return FORMAT_CSV_LZOP;
      case 6:
        return FORMAT_PARQUET;
      case 7:
        return FORMAT_CSV_DEFLATE;
      case 8:
        return FORMAT_ORC;
      case 9:
        return FORMAT_JSON;
      case 10:
        return FORMAT_CSV_ZSTD;
      case 11:
        return FORMAT_AVRO;
      default:
        return null;
    }
  }
}
