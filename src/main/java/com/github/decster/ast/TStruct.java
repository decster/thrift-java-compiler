package com.github.decster.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * A struct is a container for a set of member fields that has a name. Structs
 * are also used to implement exception types.
 * Corresponds to t_struct.h in the C++ implementation.
 */
public class TStruct extends TType {
  private boolean isXception;
  private boolean isUnion;
  private boolean isMethodXcepts;
  private boolean unionValidated;
  private boolean xceptsValidated;
  private int membersWithValue;
  private boolean xsdAll;
  private List<TField> members;
  private int curNegKey = -1;

  public TStruct(TProgram program) {
    this.setProgram(program);
    this.isXception = false;
    this.isUnion = false;
    this.isMethodXcepts = false;
    this.unionValidated = false;
    this.xceptsValidated = false;
    this.membersWithValue = 0;
    this.xsdAll = false;
    this.members = new ArrayList<>();
  }

  public TStruct(TProgram program, String name) {
    this(program);
    this.setName(name);
  }

  public boolean isXception() { return isXception; }

  public void setXception(boolean xception) { isXception = xception; }

  public boolean isUnion() { return isUnion; }

  public void setUnion(boolean union) { isUnion = union; }

  public boolean isMethodXcepts() { return isMethodXcepts; }

  public void setMethodXcepts(boolean methodXcepts) { isMethodXcepts = methodXcepts; }

  public List<TField> getMembers() { return members; }

  public List<TField> getSortedMembers() {
    members.sort((f1, f2) -> Integer.compare(f1.getKey(), f2.getKey()));
    return members;
  }

  public void append(TField field) {
    if (field.getKey() == Integer.MIN_VALUE) {
      field.setKey(curNegKey--);
    } else if (field.getKey() < 0) {
      throw new IllegalArgumentException("Negative keys are not allowed for struct fields.");
    }
    members.add(field);
    field.setStruct(this);
  }

  @Override
  public boolean isStruct() {
    return !isXception();
  }

  @Override
  public String toString() {
    return "struct:" + getName();
  }

  @Override
  public void validate() {
    // Add validation logic as needed
  }

  public TField getFieldByName(String fieldName) {
    for (TField field : members) {
      if (field.getName().equals(fieldName)) {
        return field;
      }
    }
    return null;
  }
}
