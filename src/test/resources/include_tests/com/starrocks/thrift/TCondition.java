/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TCondition implements org.apache.thrift.TBase<TCondition, TCondition._Fields>, java.io.Serializable, Cloneable, Comparable<TCondition> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TCondition");

  private static final org.apache.thrift.protocol.TField COLUMN_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("column_name", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField CONDITION_OP_FIELD_DESC = new org.apache.thrift.protocol.TField("condition_op", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField CONDITION_VALUES_FIELD_DESC = new org.apache.thrift.protocol.TField("condition_values", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField IS_INDEX_FILTER_ONLY_FIELD_DESC = new org.apache.thrift.protocol.TField("is_index_filter_only", org.apache.thrift.protocol.TType.BOOL, (short)20);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TConditionStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TConditionTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String column_name; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String condition_op; // required
  public @org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> condition_values; // required
  public boolean is_index_filter_only; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    COLUMN_NAME((short)1, "column_name"),
    CONDITION_OP((short)2, "condition_op"),
    CONDITION_VALUES((short)3, "condition_values"),
    IS_INDEX_FILTER_ONLY((short)20, "is_index_filter_only");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // COLUMN_NAME
          return COLUMN_NAME;
        case 2: // CONDITION_OP
          return CONDITION_OP;
        case 3: // CONDITION_VALUES
          return CONDITION_VALUES;
        case 20: // IS_INDEX_FILTER_ONLY
          return IS_INDEX_FILTER_ONLY;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    @Override
    public short getThriftFieldId() {
      return _thriftId;
    }

    @Override
    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __IS_INDEX_FILTER_ONLY_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.IS_INDEX_FILTER_ONLY};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.COLUMN_NAME, new org.apache.thrift.meta_data.FieldMetaData("column_name", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CONDITION_OP, new org.apache.thrift.meta_data.FieldMetaData("condition_op", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CONDITION_VALUES, new org.apache.thrift.meta_data.FieldMetaData("condition_values", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.IS_INDEX_FILTER_ONLY, new org.apache.thrift.meta_data.FieldMetaData("is_index_filter_only", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TCondition.class, metaDataMap);
  }

  public TCondition() {
  }

  public TCondition(
    java.lang.String column_name,
    java.lang.String condition_op,
    java.util.List<java.lang.String> condition_values)
  {
    this();
    this.column_name = column_name;
    this.condition_op = condition_op;
    this.condition_values = condition_values;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TCondition(TCondition other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetColumn_name()) {
      this.column_name = other.column_name;
    }
    if (other.isSetCondition_op()) {
      this.condition_op = other.condition_op;
    }
    if (other.isSetCondition_values()) {
      java.util.List<java.lang.String> __this__condition_values = new java.util.ArrayList<java.lang.String>(other.condition_values);
      this.condition_values = __this__condition_values;
    }
    this.is_index_filter_only = other.is_index_filter_only;
  }

  @Override
  public TCondition deepCopy() {
    return new TCondition(this);
  }

  @Override
  public void clear() {
    this.column_name = null;
    this.condition_op = null;
    this.condition_values = null;
    setIs_index_filter_onlyIsSet(false);
    this.is_index_filter_only = false;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getColumn_name() {
    return this.column_name;
  }

  public TCondition setColumn_name(@org.apache.thrift.annotation.Nullable java.lang.String column_name) {
    this.column_name = column_name;
    return this;
  }

  public void unsetColumn_name() {
    this.column_name = null;
  }

  /** Returns true if field column_name is set (has been assigned a value) and false otherwise */
  public boolean isSetColumn_name() {
    return this.column_name != null;
  }

  public void setColumn_nameIsSet(boolean value) {
    if (!value) {
      this.column_name = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getCondition_op() {
    return this.condition_op;
  }

  public TCondition setCondition_op(@org.apache.thrift.annotation.Nullable java.lang.String condition_op) {
    this.condition_op = condition_op;
    return this;
  }

  public void unsetCondition_op() {
    this.condition_op = null;
  }

  /** Returns true if field condition_op is set (has been assigned a value) and false otherwise */
  public boolean isSetCondition_op() {
    return this.condition_op != null;
  }

  public void setCondition_opIsSet(boolean value) {
    if (!value) {
      this.condition_op = null;
    }
  }

  public int getCondition_valuesSize() {
    return (this.condition_values == null) ? 0 : this.condition_values.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<java.lang.String> getCondition_valuesIterator() {
    return (this.condition_values == null) ? null : this.condition_values.iterator();
  }

  public void addToCondition_values(java.lang.String elem) {
    if (this.condition_values == null) {
      this.condition_values = new java.util.ArrayList<java.lang.String>();
    }
    this.condition_values.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<java.lang.String> getCondition_values() {
    return this.condition_values;
  }

  public TCondition setCondition_values(@org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> condition_values) {
    this.condition_values = condition_values;
    return this;
  }

  public void unsetCondition_values() {
    this.condition_values = null;
  }

  /** Returns true if field condition_values is set (has been assigned a value) and false otherwise */
  public boolean isSetCondition_values() {
    return this.condition_values != null;
  }

  public void setCondition_valuesIsSet(boolean value) {
    if (!value) {
      this.condition_values = null;
    }
  }

  public boolean isIs_index_filter_only() {
    return this.is_index_filter_only;
  }

  public TCondition setIs_index_filter_only(boolean is_index_filter_only) {
    this.is_index_filter_only = is_index_filter_only;
    setIs_index_filter_onlyIsSet(true);
    return this;
  }

  public void unsetIs_index_filter_only() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __IS_INDEX_FILTER_ONLY_ISSET_ID);
  }

  /** Returns true if field is_index_filter_only is set (has been assigned a value) and false otherwise */
  public boolean isSetIs_index_filter_only() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __IS_INDEX_FILTER_ONLY_ISSET_ID);
  }

  public void setIs_index_filter_onlyIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __IS_INDEX_FILTER_ONLY_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case COLUMN_NAME:
      if (value == null) {
        unsetColumn_name();
      } else {
        setColumn_name((java.lang.String)value);
      }
      break;

    case CONDITION_OP:
      if (value == null) {
        unsetCondition_op();
      } else {
        setCondition_op((java.lang.String)value);
      }
      break;

    case CONDITION_VALUES:
      if (value == null) {
        unsetCondition_values();
      } else {
        setCondition_values((java.util.List<java.lang.String>)value);
      }
      break;

    case IS_INDEX_FILTER_ONLY:
      if (value == null) {
        unsetIs_index_filter_only();
      } else {
        setIs_index_filter_only((java.lang.Boolean)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case COLUMN_NAME:
      return getColumn_name();

    case CONDITION_OP:
      return getCondition_op();

    case CONDITION_VALUES:
      return getCondition_values();

    case IS_INDEX_FILTER_ONLY:
      return isIs_index_filter_only();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  @Override
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case COLUMN_NAME:
      return isSetColumn_name();
    case CONDITION_OP:
      return isSetCondition_op();
    case CONDITION_VALUES:
      return isSetCondition_values();
    case IS_INDEX_FILTER_ONLY:
      return isSetIs_index_filter_only();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TCondition)
      return this.equals((TCondition)that);
    return false;
  }

  public boolean equals(TCondition that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_column_name = true && this.isSetColumn_name();
    boolean that_present_column_name = true && that.isSetColumn_name();
    if (this_present_column_name || that_present_column_name) {
      if (!(this_present_column_name && that_present_column_name))
        return false;
      if (!this.column_name.equals(that.column_name))
        return false;
    }

    boolean this_present_condition_op = true && this.isSetCondition_op();
    boolean that_present_condition_op = true && that.isSetCondition_op();
    if (this_present_condition_op || that_present_condition_op) {
      if (!(this_present_condition_op && that_present_condition_op))
        return false;
      if (!this.condition_op.equals(that.condition_op))
        return false;
    }

    boolean this_present_condition_values = true && this.isSetCondition_values();
    boolean that_present_condition_values = true && that.isSetCondition_values();
    if (this_present_condition_values || that_present_condition_values) {
      if (!(this_present_condition_values && that_present_condition_values))
        return false;
      if (!this.condition_values.equals(that.condition_values))
        return false;
    }

    boolean this_present_is_index_filter_only = true && this.isSetIs_index_filter_only();
    boolean that_present_is_index_filter_only = true && that.isSetIs_index_filter_only();
    if (this_present_is_index_filter_only || that_present_is_index_filter_only) {
      if (!(this_present_is_index_filter_only && that_present_is_index_filter_only))
        return false;
      if (this.is_index_filter_only != that.is_index_filter_only)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetColumn_name()) ? 131071 : 524287);
    if (isSetColumn_name())
      hashCode = hashCode * 8191 + column_name.hashCode();

    hashCode = hashCode * 8191 + ((isSetCondition_op()) ? 131071 : 524287);
    if (isSetCondition_op())
      hashCode = hashCode * 8191 + condition_op.hashCode();

    hashCode = hashCode * 8191 + ((isSetCondition_values()) ? 131071 : 524287);
    if (isSetCondition_values())
      hashCode = hashCode * 8191 + condition_values.hashCode();

    hashCode = hashCode * 8191 + ((isSetIs_index_filter_only()) ? 131071 : 524287);
    if (isSetIs_index_filter_only())
      hashCode = hashCode * 8191 + ((is_index_filter_only) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(TCondition other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetColumn_name(), other.isSetColumn_name());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetColumn_name()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.column_name, other.column_name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetCondition_op(), other.isSetCondition_op());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCondition_op()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.condition_op, other.condition_op);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetCondition_values(), other.isSetCondition_values());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCondition_values()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.condition_values, other.condition_values);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetIs_index_filter_only(), other.isSetIs_index_filter_only());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIs_index_filter_only()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.is_index_filter_only, other.is_index_filter_only);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  @Override
  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  @Override
  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TCondition(");
    boolean first = true;

    sb.append("column_name:");
    if (this.column_name == null) {
      sb.append("null");
    } else {
      sb.append(this.column_name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("condition_op:");
    if (this.condition_op == null) {
      sb.append("null");
    } else {
      sb.append(this.condition_op);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("condition_values:");
    if (this.condition_values == null) {
      sb.append("null");
    } else {
      sb.append(this.condition_values);
    }
    first = false;
    if (isSetIs_index_filter_only()) {
      if (!first) sb.append(", ");
      sb.append("is_index_filter_only:");
      sb.append(this.is_index_filter_only);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (column_name == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'column_name' was not present! Struct: " + toString());
    }
    if (condition_op == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'condition_op' was not present! Struct: " + toString());
    }
    if (condition_values == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'condition_values' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TConditionStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TConditionStandardScheme getScheme() {
      return new TConditionStandardScheme();
    }
  }

  private static class TConditionStandardScheme extends org.apache.thrift.scheme.StandardScheme<TCondition> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TCondition struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // COLUMN_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.column_name = iprot.readString();
              struct.setColumn_nameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CONDITION_OP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.condition_op = iprot.readString();
              struct.setCondition_opIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // CONDITION_VALUES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list126 = iprot.readListBegin();
                struct.condition_values = new java.util.ArrayList<java.lang.String>(_list126.size);
                @org.apache.thrift.annotation.Nullable java.lang.String _elem127;
                for (int _i128 = 0; _i128 < _list126.size; ++_i128)
                {
                  _elem127 = iprot.readString();
                  struct.condition_values.add(_elem127);
                }
                iprot.readListEnd();
              }
              struct.setCondition_valuesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 20: // IS_INDEX_FILTER_ONLY
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.is_index_filter_only = iprot.readBool();
              struct.setIs_index_filter_onlyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, TCondition struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.column_name != null) {
        oprot.writeFieldBegin(COLUMN_NAME_FIELD_DESC);
        oprot.writeString(struct.column_name);
        oprot.writeFieldEnd();
      }
      if (struct.condition_op != null) {
        oprot.writeFieldBegin(CONDITION_OP_FIELD_DESC);
        oprot.writeString(struct.condition_op);
        oprot.writeFieldEnd();
      }
      if (struct.condition_values != null) {
        oprot.writeFieldBegin(CONDITION_VALUES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.condition_values.size()));
          for (java.lang.String _iter129 : struct.condition_values)
          {
            oprot.writeString(_iter129);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.isSetIs_index_filter_only()) {
        oprot.writeFieldBegin(IS_INDEX_FILTER_ONLY_FIELD_DESC);
        oprot.writeBool(struct.is_index_filter_only);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TConditionTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TConditionTupleScheme getScheme() {
      return new TConditionTupleScheme();
    }
  }

  private static class TConditionTupleScheme extends org.apache.thrift.scheme.TupleScheme<TCondition> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TCondition struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.column_name);
      oprot.writeString(struct.condition_op);
      {
        oprot.writeI32(struct.condition_values.size());
        for (java.lang.String _iter130 : struct.condition_values)
        {
          oprot.writeString(_iter130);
        }
      }
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetIs_index_filter_only()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetIs_index_filter_only()) {
        oprot.writeBool(struct.is_index_filter_only);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TCondition struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.column_name = iprot.readString();
      struct.setColumn_nameIsSet(true);
      struct.condition_op = iprot.readString();
      struct.setCondition_opIsSet(true);
      {
        org.apache.thrift.protocol.TList _list131 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRING);
        struct.condition_values = new java.util.ArrayList<java.lang.String>(_list131.size);
        @org.apache.thrift.annotation.Nullable java.lang.String _elem132;
        for (int _i133 = 0; _i133 < _list131.size; ++_i133)
        {
          _elem132 = iprot.readString();
          struct.condition_values.add(_elem132);
        }
      }
      struct.setCondition_valuesIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.is_index_filter_only = iprot.readBool();
        struct.setIs_index_filter_onlyIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

