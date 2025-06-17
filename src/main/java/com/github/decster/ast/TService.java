package com.github.decster.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * A service consists of a set of functions.
 * Corresponds to t_service.h in the C++ implementation.
 */
public class TService extends TType {
  private TService extends_;
  private List<TFunction> functions;

  public TService(TProgram program) {
    setProgram(program);
    this.extends_ = null;
    this.functions = new ArrayList<>();
  }

  @Override
  public boolean isService() {
    return true;
  }

  public void setExtends(TService extends_) { this.extends_ = extends_; }

  public TService getExtends() { return extends_; }

  public void addFunction(TFunction func) {
    if (getFunctionByName(func.getName()) != null) {
      throw new RuntimeException("Function " + func.getName() + " is already defined");
    }
    functions.add(func);
  }

  public List<TFunction> getFunctions() { return functions; }

  public TFunction getFunctionByName(String name) {
    for (TFunction func : functions) {
      if (func.getName().equals(name)) {
        return func;
      }
    }
    return null;
  }

  public void validateUniqueMembers() {
    // Validate that there are no name conflicts with super class
    if (extends_ != null) {
      for (TFunction func : functions) {
        if (extends_.getFunctionByName(func.getName()) != null) {
          throw new RuntimeException("Function " + func.getName() + " conflicts with function in base service " +
                                     extends_.getName());
        }
      }
    }
  }
}
