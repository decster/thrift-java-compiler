/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
public class DescriptorsConstants {

  public static final java.util.Map<java.lang.String,THdfsCompression> COMPRESSION_MAP = new java.util.HashMap<java.lang.String,THdfsCompression>();
  static {
    COMPRESSION_MAP.put("", com.starrocks.thrift.THdfsCompression.NONE);
    COMPRESSION_MAP.put("bzip2", com.starrocks.thrift.THdfsCompression.BZIP2);
    COMPRESSION_MAP.put("deflate", com.starrocks.thrift.THdfsCompression.DEFAULT);
    COMPRESSION_MAP.put("gzip", com.starrocks.thrift.THdfsCompression.GZIP);
    COMPRESSION_MAP.put("none", com.starrocks.thrift.THdfsCompression.NONE);
    COMPRESSION_MAP.put("snappy", com.starrocks.thrift.THdfsCompression.SNAPPY);
  }

}
