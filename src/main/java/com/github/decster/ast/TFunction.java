package com.github.decster.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of a function. Key parts are return type, function name,
 * optional modifiers, and an argument list, which is implemented as a thrift
 * struct.
 * Corresponds to t_function.h in the C++ implementation.
 */
public class TFunction extends TDoc {
  private TType returnType;
  private String name;
  private TStruct arglist;
  private TStruct xceptions;
  private boolean ownXceptions;
  private boolean oneway;

  public TFunction(TType returnType, String name, TStruct arglist) { this(returnType, name, arglist, false); }

  public TFunction(TType returnType, String name, TStruct arglist, boolean oneway) {
    this.returnType = returnType;
    this.name = name;
    this.arglist = arglist;
    this.xceptions = new TStruct(null);
    this.xceptions.setMethodXcepts(true);
    this.ownXceptions = true;
    this.oneway = oneway;

    if (oneway && !returnType.isVoid()) {
      // Warning: Oneway methods should return void
    }
  }

  public TFunction(TType returnType, String name, TStruct arglist, TStruct xceptions, boolean oneway) {
    this.returnType = returnType;
    this.name = name;
    this.arglist = arglist;
    this.xceptions = xceptions;
    this.ownXceptions = false;
    this.oneway = oneway;

    if (oneway && !returnType.isVoid()) {
      // Warning: Oneway methods should return void
    }
  }

  public void setReturnType(TType returnType) { this.returnType = returnType; }

  public TType getReturnType() { return returnType; }

  public String getName() { return name; }

  public TStruct getArglist() { return arglist; }

  public TStruct getXceptions() { return xceptions; }

  public void setXceptions(TStruct xceptions) { this.xceptions = xceptions; }

  public boolean isOneway() { return oneway; }

  public void setOneWay(boolean oneway) { this.oneway = oneway; }

  private Map<String, List<String>> annotations;

  public void setAnnotations(Map<String, List<String>> annotations) { this.annotations = annotations; }

  public Map<String, List<String>> getAnnotations() { return annotations; }

  public boolean hasAnnotations() { return annotations != null && !annotations.isEmpty(); }
}
