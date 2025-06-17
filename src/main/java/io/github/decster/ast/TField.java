package io.github.decster.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to represent a field in a thrift structure. A field has a data type,
 * a symbolic name, and a numeric identifier.
 * Corresponds to t_field.h in the C++ implementation.
 */
public class TField extends TDoc {
  public enum Requirement { REQUIRED, OPTIONAL, OPT_IN_REQ_OUT }

  private TStruct struct;
  private TType type;
  private String name;
  private int key;
  private Requirement req;
  private TConstValue value;
  private boolean xsdOptional;
  private boolean xsdNillable;
  private TStruct xsdAttrs;
  private boolean reference;
  private Map<String, List<String>> annotations;

  public TField(TType type, String name) {
    this.type = type;
    this.name = name;
    this.key = 0;
    this.req = Requirement.OPT_IN_REQ_OUT;
    this.value = null;
    this.xsdOptional = false;
    this.xsdNillable = false;
    this.xsdAttrs = null;
    this.reference = false;
    this.annotations = new HashMap<>();
  }

  public TField(TType type, String name, int key) {
    this(type, name);
    this.key = key;
  }

  public TField(TType type, String name, int key, TConstValue value) {
    this(type, name, key);
    this.value = value;
  }

  public TStruct getStruct() { return struct; }

  public void setStruct(TStruct struct) { this.struct = struct; }

  public void setType(TType type) { this.type = type; }

  public TType getType() { return type; }

  public String getName() { return name; }

  public int getKey() { return key; }

  public void setKey(int key) { this.key = key; }

  public Requirement getReq() { return req; }

  public void setReq(Requirement req) { this.req = req; }

  public TConstValue getValue() { return value; }

  public void setValue(TConstValue value) { this.value = value; }

  public boolean hasValue() { return value != null; }

  public boolean isXsdOptional() { return xsdOptional; }

  public void setXsdOptional(boolean xsdOptional) { this.xsdOptional = xsdOptional; }

  public boolean isXsdNillable() { return xsdNillable; }

  public void setXsdNillable(boolean xsdNillable) { this.xsdNillable = xsdNillable; }

  public TStruct getXsdAttrs() { return xsdAttrs; }

  public void setXsdAttrs(TStruct xsdAttrs) { this.xsdAttrs = xsdAttrs; }

  public boolean isReference() { return reference; }

  public void setReference(boolean reference) { this.reference = reference; }

  public void setAnnotations(Map<String, List<String>> annotations) { this.annotations = annotations; }

  public Map<String, List<String>> getAnnotations() { return annotations; }

  public boolean hasAnnotations() { return annotations != null && !annotations.isEmpty(); }

  public boolean isRequired() { return req == Requirement.REQUIRED; }

  public boolean isOptional() { return req == Requirement.OPTIONAL; }

  @Override
  public String toString() {
    return "field[" + key + ":" + name + "]";
  }
}
