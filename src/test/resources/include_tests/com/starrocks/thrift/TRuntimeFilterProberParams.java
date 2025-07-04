/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TRuntimeFilterProberParams implements org.apache.thrift.TBase<TRuntimeFilterProberParams, TRuntimeFilterProberParams._Fields>, java.io.Serializable, Cloneable, Comparable<TRuntimeFilterProberParams> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TRuntimeFilterProberParams");

  private static final org.apache.thrift.protocol.TField FRAGMENT_INSTANCE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("fragment_instance_id", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField FRAGMENT_INSTANCE_ADDRESS_FIELD_DESC = new org.apache.thrift.protocol.TField("fragment_instance_address", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TRuntimeFilterProberParamsStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TRuntimeFilterProberParamsTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TUniqueId fragment_instance_id; // optional
  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TNetworkAddress fragment_instance_address; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    FRAGMENT_INSTANCE_ID((short)1, "fragment_instance_id"),
    FRAGMENT_INSTANCE_ADDRESS((short)2, "fragment_instance_address");

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
        case 1: // FRAGMENT_INSTANCE_ID
          return FRAGMENT_INSTANCE_ID;
        case 2: // FRAGMENT_INSTANCE_ADDRESS
          return FRAGMENT_INSTANCE_ADDRESS;
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
  private static final _Fields optionals[] = {_Fields.FRAGMENT_INSTANCE_ID,_Fields.FRAGMENT_INSTANCE_ADDRESS};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.FRAGMENT_INSTANCE_ID, new org.apache.thrift.meta_data.FieldMetaData("fragment_instance_id", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TUniqueId.class)));
    tmpMap.put(_Fields.FRAGMENT_INSTANCE_ADDRESS, new org.apache.thrift.meta_data.FieldMetaData("fragment_instance_address", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TNetworkAddress.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TRuntimeFilterProberParams.class, metaDataMap);
  }

  public TRuntimeFilterProberParams() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TRuntimeFilterProberParams(TRuntimeFilterProberParams other) {
    if (other.isSetFragment_instance_id()) {
      this.fragment_instance_id = new com.starrocks.thrift.TUniqueId(other.fragment_instance_id);
    }
    if (other.isSetFragment_instance_address()) {
      this.fragment_instance_address = new com.starrocks.thrift.TNetworkAddress(other.fragment_instance_address);
    }
  }

  @Override
  public TRuntimeFilterProberParams deepCopy() {
    return new TRuntimeFilterProberParams(this);
  }

  @Override
  public void clear() {
    this.fragment_instance_id = null;
    this.fragment_instance_address = null;
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TUniqueId getFragment_instance_id() {
    return this.fragment_instance_id;
  }

  public TRuntimeFilterProberParams setFragment_instance_id(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TUniqueId fragment_instance_id) {
    this.fragment_instance_id = fragment_instance_id;
    return this;
  }

  public void unsetFragment_instance_id() {
    this.fragment_instance_id = null;
  }

  /** Returns true if field fragment_instance_id is set (has been assigned a value) and false otherwise */
  public boolean isSetFragment_instance_id() {
    return this.fragment_instance_id != null;
  }

  public void setFragment_instance_idIsSet(boolean value) {
    if (!value) {
      this.fragment_instance_id = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TNetworkAddress getFragment_instance_address() {
    return this.fragment_instance_address;
  }

  public TRuntimeFilterProberParams setFragment_instance_address(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TNetworkAddress fragment_instance_address) {
    this.fragment_instance_address = fragment_instance_address;
    return this;
  }

  public void unsetFragment_instance_address() {
    this.fragment_instance_address = null;
  }

  /** Returns true if field fragment_instance_address is set (has been assigned a value) and false otherwise */
  public boolean isSetFragment_instance_address() {
    return this.fragment_instance_address != null;
  }

  public void setFragment_instance_addressIsSet(boolean value) {
    if (!value) {
      this.fragment_instance_address = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case FRAGMENT_INSTANCE_ID:
      if (value == null) {
        unsetFragment_instance_id();
      } else {
        setFragment_instance_id((com.starrocks.thrift.TUniqueId)value);
      }
      break;

    case FRAGMENT_INSTANCE_ADDRESS:
      if (value == null) {
        unsetFragment_instance_address();
      } else {
        setFragment_instance_address((com.starrocks.thrift.TNetworkAddress)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case FRAGMENT_INSTANCE_ID:
      return getFragment_instance_id();

    case FRAGMENT_INSTANCE_ADDRESS:
      return getFragment_instance_address();

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
    case FRAGMENT_INSTANCE_ID:
      return isSetFragment_instance_id();
    case FRAGMENT_INSTANCE_ADDRESS:
      return isSetFragment_instance_address();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TRuntimeFilterProberParams)
      return this.equals((TRuntimeFilterProberParams)that);
    return false;
  }

  public boolean equals(TRuntimeFilterProberParams that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_fragment_instance_id = true && this.isSetFragment_instance_id();
    boolean that_present_fragment_instance_id = true && that.isSetFragment_instance_id();
    if (this_present_fragment_instance_id || that_present_fragment_instance_id) {
      if (!(this_present_fragment_instance_id && that_present_fragment_instance_id))
        return false;
      if (!this.fragment_instance_id.equals(that.fragment_instance_id))
        return false;
    }

    boolean this_present_fragment_instance_address = true && this.isSetFragment_instance_address();
    boolean that_present_fragment_instance_address = true && that.isSetFragment_instance_address();
    if (this_present_fragment_instance_address || that_present_fragment_instance_address) {
      if (!(this_present_fragment_instance_address && that_present_fragment_instance_address))
        return false;
      if (!this.fragment_instance_address.equals(that.fragment_instance_address))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetFragment_instance_id()) ? 131071 : 524287);
    if (isSetFragment_instance_id())
      hashCode = hashCode * 8191 + fragment_instance_id.hashCode();

    hashCode = hashCode * 8191 + ((isSetFragment_instance_address()) ? 131071 : 524287);
    if (isSetFragment_instance_address())
      hashCode = hashCode * 8191 + fragment_instance_address.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TRuntimeFilterProberParams other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetFragment_instance_id(), other.isSetFragment_instance_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFragment_instance_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fragment_instance_id, other.fragment_instance_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetFragment_instance_address(), other.isSetFragment_instance_address());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFragment_instance_address()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fragment_instance_address, other.fragment_instance_address);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TRuntimeFilterProberParams(");
    boolean first = true;

    if (isSetFragment_instance_id()) {
      sb.append("fragment_instance_id:");
      if (this.fragment_instance_id == null) {
        sb.append("null");
      } else {
        sb.append(this.fragment_instance_id);
      }
      first = false;
    }
    if (isSetFragment_instance_address()) {
      if (!first) sb.append(", ");
      sb.append("fragment_instance_address:");
      if (this.fragment_instance_address == null) {
        sb.append("null");
      } else {
        sb.append(this.fragment_instance_address);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (fragment_instance_id != null) {
      fragment_instance_id.validate();
    }
    if (fragment_instance_address != null) {
      fragment_instance_address.validate();
    }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TRuntimeFilterProberParamsStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TRuntimeFilterProberParamsStandardScheme getScheme() {
      return new TRuntimeFilterProberParamsStandardScheme();
    }
  }

  private static class TRuntimeFilterProberParamsStandardScheme extends org.apache.thrift.scheme.StandardScheme<TRuntimeFilterProberParams> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TRuntimeFilterProberParams struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // FRAGMENT_INSTANCE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.fragment_instance_id = new com.starrocks.thrift.TUniqueId();
              struct.fragment_instance_id.read(iprot);
              struct.setFragment_instance_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // FRAGMENT_INSTANCE_ADDRESS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.fragment_instance_address = new com.starrocks.thrift.TNetworkAddress();
              struct.fragment_instance_address.read(iprot);
              struct.setFragment_instance_addressIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TRuntimeFilterProberParams struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.fragment_instance_id != null) {
        if (struct.isSetFragment_instance_id()) {
          oprot.writeFieldBegin(FRAGMENT_INSTANCE_ID_FIELD_DESC);
          struct.fragment_instance_id.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.fragment_instance_address != null) {
        if (struct.isSetFragment_instance_address()) {
          oprot.writeFieldBegin(FRAGMENT_INSTANCE_ADDRESS_FIELD_DESC);
          struct.fragment_instance_address.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TRuntimeFilterProberParamsTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TRuntimeFilterProberParamsTupleScheme getScheme() {
      return new TRuntimeFilterProberParamsTupleScheme();
    }
  }

  private static class TRuntimeFilterProberParamsTupleScheme extends org.apache.thrift.scheme.TupleScheme<TRuntimeFilterProberParams> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TRuntimeFilterProberParams struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetFragment_instance_id()) {
        optionals.set(0);
      }
      if (struct.isSetFragment_instance_address()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetFragment_instance_id()) {
        struct.fragment_instance_id.write(oprot);
      }
      if (struct.isSetFragment_instance_address()) {
        struct.fragment_instance_address.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TRuntimeFilterProberParams struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.fragment_instance_id = new com.starrocks.thrift.TUniqueId();
        struct.fragment_instance_id.read(iprot);
        struct.setFragment_instance_idIsSet(true);
      }
      if (incoming.get(1)) {
        struct.fragment_instance_address = new com.starrocks.thrift.TNetworkAddress();
        struct.fragment_instance_address.read(iprot);
        struct.setFragment_instance_addressIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

