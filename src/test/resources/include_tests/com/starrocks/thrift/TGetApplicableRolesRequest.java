/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TGetApplicableRolesRequest implements org.apache.thrift.TBase<TGetApplicableRolesRequest, TGetApplicableRolesRequest._Fields>, java.io.Serializable, Cloneable, Comparable<TGetApplicableRolesRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TGetApplicableRolesRequest");

  private static final org.apache.thrift.protocol.TField AUTH_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("auth_info", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField START_TABLE_ID_OFFSET_FIELD_DESC = new org.apache.thrift.protocol.TField("start_table_id_offset", org.apache.thrift.protocol.TType.I64, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TGetApplicableRolesRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TGetApplicableRolesRequestTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable TAuthInfo auth_info; // optional
  public long start_table_id_offset; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    AUTH_INFO((short)1, "auth_info"),
    START_TABLE_ID_OFFSET((short)2, "start_table_id_offset");

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
        case 1: // AUTH_INFO
          return AUTH_INFO;
        case 2: // START_TABLE_ID_OFFSET
          return START_TABLE_ID_OFFSET;
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
  private static final int __START_TABLE_ID_OFFSET_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.AUTH_INFO,_Fields.START_TABLE_ID_OFFSET};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.AUTH_INFO, new org.apache.thrift.meta_data.FieldMetaData("auth_info", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TAuthInfo.class)));
    tmpMap.put(_Fields.START_TABLE_ID_OFFSET, new org.apache.thrift.meta_data.FieldMetaData("start_table_id_offset", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TGetApplicableRolesRequest.class, metaDataMap);
  }

  public TGetApplicableRolesRequest() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TGetApplicableRolesRequest(TGetApplicableRolesRequest other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetAuth_info()) {
      this.auth_info = new TAuthInfo(other.auth_info);
    }
    this.start_table_id_offset = other.start_table_id_offset;
  }

  @Override
  public TGetApplicableRolesRequest deepCopy() {
    return new TGetApplicableRolesRequest(this);
  }

  @Override
  public void clear() {
    this.auth_info = null;
    setStart_table_id_offsetIsSet(false);
    this.start_table_id_offset = 0;
  }

  @org.apache.thrift.annotation.Nullable
  public TAuthInfo getAuth_info() {
    return this.auth_info;
  }

  public TGetApplicableRolesRequest setAuth_info(@org.apache.thrift.annotation.Nullable TAuthInfo auth_info) {
    this.auth_info = auth_info;
    return this;
  }

  public void unsetAuth_info() {
    this.auth_info = null;
  }

  /** Returns true if field auth_info is set (has been assigned a value) and false otherwise */
  public boolean isSetAuth_info() {
    return this.auth_info != null;
  }

  public void setAuth_infoIsSet(boolean value) {
    if (!value) {
      this.auth_info = null;
    }
  }

  public long getStart_table_id_offset() {
    return this.start_table_id_offset;
  }

  public TGetApplicableRolesRequest setStart_table_id_offset(long start_table_id_offset) {
    this.start_table_id_offset = start_table_id_offset;
    setStart_table_id_offsetIsSet(true);
    return this;
  }

  public void unsetStart_table_id_offset() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __START_TABLE_ID_OFFSET_ISSET_ID);
  }

  /** Returns true if field start_table_id_offset is set (has been assigned a value) and false otherwise */
  public boolean isSetStart_table_id_offset() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __START_TABLE_ID_OFFSET_ISSET_ID);
  }

  public void setStart_table_id_offsetIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __START_TABLE_ID_OFFSET_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case AUTH_INFO:
      if (value == null) {
        unsetAuth_info();
      } else {
        setAuth_info((TAuthInfo)value);
      }
      break;

    case START_TABLE_ID_OFFSET:
      if (value == null) {
        unsetStart_table_id_offset();
      } else {
        setStart_table_id_offset((java.lang.Long)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case AUTH_INFO:
      return getAuth_info();

    case START_TABLE_ID_OFFSET:
      return getStart_table_id_offset();

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
    case AUTH_INFO:
      return isSetAuth_info();
    case START_TABLE_ID_OFFSET:
      return isSetStart_table_id_offset();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TGetApplicableRolesRequest)
      return this.equals((TGetApplicableRolesRequest)that);
    return false;
  }

  public boolean equals(TGetApplicableRolesRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_auth_info = true && this.isSetAuth_info();
    boolean that_present_auth_info = true && that.isSetAuth_info();
    if (this_present_auth_info || that_present_auth_info) {
      if (!(this_present_auth_info && that_present_auth_info))
        return false;
      if (!this.auth_info.equals(that.auth_info))
        return false;
    }

    boolean this_present_start_table_id_offset = true && this.isSetStart_table_id_offset();
    boolean that_present_start_table_id_offset = true && that.isSetStart_table_id_offset();
    if (this_present_start_table_id_offset || that_present_start_table_id_offset) {
      if (!(this_present_start_table_id_offset && that_present_start_table_id_offset))
        return false;
      if (this.start_table_id_offset != that.start_table_id_offset)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetAuth_info()) ? 131071 : 524287);
    if (isSetAuth_info())
      hashCode = hashCode * 8191 + auth_info.hashCode();

    hashCode = hashCode * 8191 + ((isSetStart_table_id_offset()) ? 131071 : 524287);
    if (isSetStart_table_id_offset())
      hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(start_table_id_offset);

    return hashCode;
  }

  @Override
  public int compareTo(TGetApplicableRolesRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetAuth_info(), other.isSetAuth_info());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAuth_info()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.auth_info, other.auth_info);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetStart_table_id_offset(), other.isSetStart_table_id_offset());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStart_table_id_offset()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.start_table_id_offset, other.start_table_id_offset);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TGetApplicableRolesRequest(");
    boolean first = true;

    if (isSetAuth_info()) {
      sb.append("auth_info:");
      if (this.auth_info == null) {
        sb.append("null");
      } else {
        sb.append(this.auth_info);
      }
      first = false;
    }
    if (isSetStart_table_id_offset()) {
      if (!first) sb.append(", ");
      sb.append("start_table_id_offset:");
      sb.append(this.start_table_id_offset);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (auth_info != null) {
      auth_info.validate();
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

  private static class TGetApplicableRolesRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TGetApplicableRolesRequestStandardScheme getScheme() {
      return new TGetApplicableRolesRequestStandardScheme();
    }
  }

  private static class TGetApplicableRolesRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<TGetApplicableRolesRequest> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TGetApplicableRolesRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // AUTH_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.auth_info = new TAuthInfo();
              struct.auth_info.read(iprot);
              struct.setAuth_infoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // START_TABLE_ID_OFFSET
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.start_table_id_offset = iprot.readI64();
              struct.setStart_table_id_offsetIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TGetApplicableRolesRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.auth_info != null) {
        if (struct.isSetAuth_info()) {
          oprot.writeFieldBegin(AUTH_INFO_FIELD_DESC);
          struct.auth_info.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetStart_table_id_offset()) {
        oprot.writeFieldBegin(START_TABLE_ID_OFFSET_FIELD_DESC);
        oprot.writeI64(struct.start_table_id_offset);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TGetApplicableRolesRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TGetApplicableRolesRequestTupleScheme getScheme() {
      return new TGetApplicableRolesRequestTupleScheme();
    }
  }

  private static class TGetApplicableRolesRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<TGetApplicableRolesRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TGetApplicableRolesRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetAuth_info()) {
        optionals.set(0);
      }
      if (struct.isSetStart_table_id_offset()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetAuth_info()) {
        struct.auth_info.write(oprot);
      }
      if (struct.isSetStart_table_id_offset()) {
        oprot.writeI64(struct.start_table_id_offset);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TGetApplicableRolesRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.auth_info = new TAuthInfo();
        struct.auth_info.read(iprot);
        struct.setAuth_infoIsSet(true);
      }
      if (incoming.get(1)) {
        struct.start_table_id_offset = iprot.readI64();
        struct.setStart_table_id_offsetIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

