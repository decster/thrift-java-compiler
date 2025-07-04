/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TBrokerCheckPathExistResponse implements org.apache.thrift.TBase<TBrokerCheckPathExistResponse, TBrokerCheckPathExistResponse._Fields>, java.io.Serializable, Cloneable, Comparable<TBrokerCheckPathExistResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TBrokerCheckPathExistResponse");

  private static final org.apache.thrift.protocol.TField OP_STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("opStatus", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField IS_PATH_EXIST_FIELD_DESC = new org.apache.thrift.protocol.TField("isPathExist", org.apache.thrift.protocol.TType.BOOL, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TBrokerCheckPathExistResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TBrokerCheckPathExistResponseTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable TBrokerOperationStatus opStatus; // required
  public boolean isPathExist; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    OP_STATUS((short)1, "opStatus"),
    IS_PATH_EXIST((short)2, "isPathExist");

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
        case 1: // OP_STATUS
          return OP_STATUS;
        case 2: // IS_PATH_EXIST
          return IS_PATH_EXIST;
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
  private static final int __ISPATHEXIST_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.OP_STATUS, new org.apache.thrift.meta_data.FieldMetaData("opStatus", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TBrokerOperationStatus.class)));
    tmpMap.put(_Fields.IS_PATH_EXIST, new org.apache.thrift.meta_data.FieldMetaData("isPathExist", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TBrokerCheckPathExistResponse.class, metaDataMap);
  }

  public TBrokerCheckPathExistResponse() {
  }

  public TBrokerCheckPathExistResponse(
    TBrokerOperationStatus opStatus,
    boolean isPathExist)
  {
    this();
    this.opStatus = opStatus;
    this.isPathExist = isPathExist;
    setIsPathExistIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TBrokerCheckPathExistResponse(TBrokerCheckPathExistResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetOpStatus()) {
      this.opStatus = new TBrokerOperationStatus(other.opStatus);
    }
    this.isPathExist = other.isPathExist;
  }

  @Override
  public TBrokerCheckPathExistResponse deepCopy() {
    return new TBrokerCheckPathExistResponse(this);
  }

  @Override
  public void clear() {
    this.opStatus = null;
    setIsPathExistIsSet(false);
    this.isPathExist = false;
  }

  @org.apache.thrift.annotation.Nullable
  public TBrokerOperationStatus getOpStatus() {
    return this.opStatus;
  }

  public TBrokerCheckPathExistResponse setOpStatus(@org.apache.thrift.annotation.Nullable TBrokerOperationStatus opStatus) {
    this.opStatus = opStatus;
    return this;
  }

  public void unsetOpStatus() {
    this.opStatus = null;
  }

  /** Returns true if field opStatus is set (has been assigned a value) and false otherwise */
  public boolean isSetOpStatus() {
    return this.opStatus != null;
  }

  public void setOpStatusIsSet(boolean value) {
    if (!value) {
      this.opStatus = null;
    }
  }

  public boolean isIsPathExist() {
    return this.isPathExist;
  }

  public TBrokerCheckPathExistResponse setIsPathExist(boolean isPathExist) {
    this.isPathExist = isPathExist;
    setIsPathExistIsSet(true);
    return this;
  }

  public void unsetIsPathExist() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ISPATHEXIST_ISSET_ID);
  }

  /** Returns true if field isPathExist is set (has been assigned a value) and false otherwise */
  public boolean isSetIsPathExist() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ISPATHEXIST_ISSET_ID);
  }

  public void setIsPathExistIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ISPATHEXIST_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case OP_STATUS:
      if (value == null) {
        unsetOpStatus();
      } else {
        setOpStatus((TBrokerOperationStatus)value);
      }
      break;

    case IS_PATH_EXIST:
      if (value == null) {
        unsetIsPathExist();
      } else {
        setIsPathExist((java.lang.Boolean)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case OP_STATUS:
      return getOpStatus();

    case IS_PATH_EXIST:
      return isIsPathExist();

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
    case OP_STATUS:
      return isSetOpStatus();
    case IS_PATH_EXIST:
      return isSetIsPathExist();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TBrokerCheckPathExistResponse)
      return this.equals((TBrokerCheckPathExistResponse)that);
    return false;
  }

  public boolean equals(TBrokerCheckPathExistResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_opStatus = true && this.isSetOpStatus();
    boolean that_present_opStatus = true && that.isSetOpStatus();
    if (this_present_opStatus || that_present_opStatus) {
      if (!(this_present_opStatus && that_present_opStatus))
        return false;
      if (!this.opStatus.equals(that.opStatus))
        return false;
    }

    boolean this_present_isPathExist = true;
    boolean that_present_isPathExist = true;
    if (this_present_isPathExist || that_present_isPathExist) {
      if (!(this_present_isPathExist && that_present_isPathExist))
        return false;
      if (this.isPathExist != that.isPathExist)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetOpStatus()) ? 131071 : 524287);
    if (isSetOpStatus())
      hashCode = hashCode * 8191 + opStatus.hashCode();

    hashCode = hashCode * 8191 + ((isPathExist) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(TBrokerCheckPathExistResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetOpStatus(), other.isSetOpStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOpStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.opStatus, other.opStatus);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetIsPathExist(), other.isSetIsPathExist());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIsPathExist()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.isPathExist, other.isPathExist);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TBrokerCheckPathExistResponse(");
    boolean first = true;

    sb.append("opStatus:");
    if (this.opStatus == null) {
      sb.append("null");
    } else {
      sb.append(this.opStatus);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("isPathExist:");
    sb.append(this.isPathExist);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (opStatus == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'opStatus' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'isPathExist' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
    if (opStatus != null) {
      opStatus.validate();
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TBrokerCheckPathExistResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TBrokerCheckPathExistResponseStandardScheme getScheme() {
      return new TBrokerCheckPathExistResponseStandardScheme();
    }
  }

  private static class TBrokerCheckPathExistResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<TBrokerCheckPathExistResponse> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TBrokerCheckPathExistResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // OP_STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.opStatus = new TBrokerOperationStatus();
              struct.opStatus.read(iprot);
              struct.setOpStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // IS_PATH_EXIST
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.isPathExist = iprot.readBool();
              struct.setIsPathExistIsSet(true);
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
      if (!struct.isSetIsPathExist()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'isPathExist' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, TBrokerCheckPathExistResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.opStatus != null) {
        oprot.writeFieldBegin(OP_STATUS_FIELD_DESC);
        struct.opStatus.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(IS_PATH_EXIST_FIELD_DESC);
      oprot.writeBool(struct.isPathExist);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TBrokerCheckPathExistResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TBrokerCheckPathExistResponseTupleScheme getScheme() {
      return new TBrokerCheckPathExistResponseTupleScheme();
    }
  }

  private static class TBrokerCheckPathExistResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<TBrokerCheckPathExistResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TBrokerCheckPathExistResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.opStatus.write(oprot);
      oprot.writeBool(struct.isPathExist);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TBrokerCheckPathExistResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.opStatus = new TBrokerOperationStatus();
      struct.opStatus.read(iprot);
      struct.setOpStatusIsSet(true);
      struct.isPathExist = iprot.readBool();
      struct.setIsPathExistIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

