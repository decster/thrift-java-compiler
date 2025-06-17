package com.github.decster.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A constant. These are used inside of enum definitions. Constants are just
 * symbol identifiers that may or may not have an explicit value associated
 * with them.
 * Corresponds to t_enum_value.h in the C++ implementation.
 */
public class TEnumValue extends TDoc {
  private String name;
  private int value;
  private Map<String, List<String>> annotations;

  public TEnumValue(String name, int value) {
    this.name = name;
    this.value = value;
    this.annotations = new HashMap<>();
  }

  public String getName() { return name; }

  public int getValue() { return value; }

  public Map<String, List<String>> getAnnotations() { return annotations; }

  public void addAnnotation(String key, String value) {
    annotations.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
  }

  public void setAnnotations(Map<String, List<String>> annotations) { this.annotations = annotations; }
}
