package com.github.decster.ast;

/**
 * Class to represent a Thrift annotation (key-value pair).
 */
public class TAnnotation {
  private String key;
  private String value;

  public TAnnotation(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() { return key; }

  public void setKey(String key) { this.key = key; }

  public String getValue() { return value; }

  public void setValue(String value) { this.value = value; }

  @Override
  public String toString() {
    return key + "=" + value;
  }
}
