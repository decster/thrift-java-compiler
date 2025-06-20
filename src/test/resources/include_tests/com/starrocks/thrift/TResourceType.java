/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public enum TResourceType implements org.apache.thrift.TEnum {
  TRESOURCE_CPU_SHARE(0),
  TRESOURCE_IO_SHARE(1),
  TRESOURCE_SSD_READ_IOPS(2),
  TRESOURCE_SSD_WRITE_IOPS(3),
  TRESOURCE_SSD_READ_MBPS(4),
  TRESOURCE_SSD_WRITE_MBPS(5),
  TRESOURCE_HDD_READ_IOPS(6),
  TRESOURCE_HDD_WRITE_IOPS(7),
  TRESOURCE_HDD_READ_MBPS(8),
  TRESOURCE_HDD_WRITE_MBPS(9);

  private final int value;

  private TResourceType(int value) {
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
  public static TResourceType findByValue(int value) { 
    switch (value) {
      case 0:
        return TRESOURCE_CPU_SHARE;
      case 1:
        return TRESOURCE_IO_SHARE;
      case 2:
        return TRESOURCE_SSD_READ_IOPS;
      case 3:
        return TRESOURCE_SSD_WRITE_IOPS;
      case 4:
        return TRESOURCE_SSD_READ_MBPS;
      case 5:
        return TRESOURCE_SSD_WRITE_MBPS;
      case 6:
        return TRESOURCE_HDD_READ_IOPS;
      case 7:
        return TRESOURCE_HDD_WRITE_IOPS;
      case 8:
        return TRESOURCE_HDD_READ_MBPS;
      case 9:
        return TRESOURCE_HDD_WRITE_MBPS;
      default:
        return null;
    }
  }
}
