/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TAlterTabletReq implements org.apache.thrift.TBase<TAlterTabletReq, TAlterTabletReq._Fields>, java.io.Serializable, Cloneable, Comparable<TAlterTabletReq> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TAlterTabletReq");

  private static final org.apache.thrift.protocol.TField BASE_TABLET_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("base_tablet_id", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_SCHEMA_HASH_FIELD_DESC = new org.apache.thrift.protocol.TField("base_schema_hash", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField NEW_TABLET_REQ_FIELD_DESC = new org.apache.thrift.protocol.TField("new_tablet_req", org.apache.thrift.protocol.TType.STRUCT, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TAlterTabletReqStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TAlterTabletReqTupleSchemeFactory();

  public long base_tablet_id; // required
  public int base_schema_hash; // required
  public @org.apache.thrift.annotation.Nullable TCreateTabletReq new_tablet_req; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    BASE_TABLET_ID((short)1, "base_tablet_id"),
    BASE_SCHEMA_HASH((short)2, "base_schema_hash"),
    NEW_TABLET_REQ((short)3, "new_tablet_req");

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
        case 1: // BASE_TABLET_ID
          return BASE_TABLET_ID;
        case 2: // BASE_SCHEMA_HASH
          return BASE_SCHEMA_HASH;
        case 3: // NEW_TABLET_REQ
          return NEW_TABLET_REQ;
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
  private static final int __BASE_TABLET_ID_ISSET_ID = 0;
  private static final int __BASE_SCHEMA_HASH_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.BASE_TABLET_ID, new org.apache.thrift.meta_data.FieldMetaData("base_tablet_id", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64        , "TTabletId")));
    tmpMap.put(_Fields.BASE_SCHEMA_HASH, new org.apache.thrift.meta_data.FieldMetaData("base_schema_hash", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "TSchemaHash")));
    tmpMap.put(_Fields.NEW_TABLET_REQ, new org.apache.thrift.meta_data.FieldMetaData("new_tablet_req", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TCreateTabletReq.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TAlterTabletReq.class, metaDataMap);
  }

  public TAlterTabletReq() {
  }

  public TAlterTabletReq(
    long base_tablet_id,
    int base_schema_hash,
    TCreateTabletReq new_tablet_req)
  {
    this();
    this.base_tablet_id = base_tablet_id;
    setBase_tablet_idIsSet(true);
    this.base_schema_hash = base_schema_hash;
    setBase_schema_hashIsSet(true);
    this.new_tablet_req = new_tablet_req;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TAlterTabletReq(TAlterTabletReq other) {
    __isset_bitfield = other.__isset_bitfield;
    this.base_tablet_id = other.base_tablet_id;
    this.base_schema_hash = other.base_schema_hash;
    if (other.isSetNew_tablet_req()) {
      this.new_tablet_req = new TCreateTabletReq(other.new_tablet_req);
    }
  }

  @Override
  public TAlterTabletReq deepCopy() {
    return new TAlterTabletReq(this);
  }

  @Override
  public void clear() {
    setBase_tablet_idIsSet(false);
    this.base_tablet_id = 0;
    setBase_schema_hashIsSet(false);
    this.base_schema_hash = 0;
    this.new_tablet_req = null;
  }

  public long getBase_tablet_id() {
    return this.base_tablet_id;
  }

  public TAlterTabletReq setBase_tablet_id(long base_tablet_id) {
    this.base_tablet_id = base_tablet_id;
    setBase_tablet_idIsSet(true);
    return this;
  }

  public void unsetBase_tablet_id() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __BASE_TABLET_ID_ISSET_ID);
  }

  /** Returns true if field base_tablet_id is set (has been assigned a value) and false otherwise */
  public boolean isSetBase_tablet_id() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __BASE_TABLET_ID_ISSET_ID);
  }

  public void setBase_tablet_idIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __BASE_TABLET_ID_ISSET_ID, value);
  }

  public int getBase_schema_hash() {
    return this.base_schema_hash;
  }

  public TAlterTabletReq setBase_schema_hash(int base_schema_hash) {
    this.base_schema_hash = base_schema_hash;
    setBase_schema_hashIsSet(true);
    return this;
  }

  public void unsetBase_schema_hash() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __BASE_SCHEMA_HASH_ISSET_ID);
  }

  /** Returns true if field base_schema_hash is set (has been assigned a value) and false otherwise */
  public boolean isSetBase_schema_hash() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __BASE_SCHEMA_HASH_ISSET_ID);
  }

  public void setBase_schema_hashIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __BASE_SCHEMA_HASH_ISSET_ID, value);
  }

  @org.apache.thrift.annotation.Nullable
  public TCreateTabletReq getNew_tablet_req() {
    return this.new_tablet_req;
  }

  public TAlterTabletReq setNew_tablet_req(@org.apache.thrift.annotation.Nullable TCreateTabletReq new_tablet_req) {
    this.new_tablet_req = new_tablet_req;
    return this;
  }

  public void unsetNew_tablet_req() {
    this.new_tablet_req = null;
  }

  /** Returns true if field new_tablet_req is set (has been assigned a value) and false otherwise */
  public boolean isSetNew_tablet_req() {
    return this.new_tablet_req != null;
  }

  public void setNew_tablet_reqIsSet(boolean value) {
    if (!value) {
      this.new_tablet_req = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case BASE_TABLET_ID:
      if (value == null) {
        unsetBase_tablet_id();
      } else {
        setBase_tablet_id((java.lang.Long)value);
      }
      break;

    case BASE_SCHEMA_HASH:
      if (value == null) {
        unsetBase_schema_hash();
      } else {
        setBase_schema_hash((java.lang.Integer)value);
      }
      break;

    case NEW_TABLET_REQ:
      if (value == null) {
        unsetNew_tablet_req();
      } else {
        setNew_tablet_req((TCreateTabletReq)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case BASE_TABLET_ID:
      return getBase_tablet_id();

    case BASE_SCHEMA_HASH:
      return getBase_schema_hash();

    case NEW_TABLET_REQ:
      return getNew_tablet_req();

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
    case BASE_TABLET_ID:
      return isSetBase_tablet_id();
    case BASE_SCHEMA_HASH:
      return isSetBase_schema_hash();
    case NEW_TABLET_REQ:
      return isSetNew_tablet_req();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TAlterTabletReq)
      return this.equals((TAlterTabletReq)that);
    return false;
  }

  public boolean equals(TAlterTabletReq that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_base_tablet_id = true;
    boolean that_present_base_tablet_id = true;
    if (this_present_base_tablet_id || that_present_base_tablet_id) {
      if (!(this_present_base_tablet_id && that_present_base_tablet_id))
        return false;
      if (this.base_tablet_id != that.base_tablet_id)
        return false;
    }

    boolean this_present_base_schema_hash = true;
    boolean that_present_base_schema_hash = true;
    if (this_present_base_schema_hash || that_present_base_schema_hash) {
      if (!(this_present_base_schema_hash && that_present_base_schema_hash))
        return false;
      if (this.base_schema_hash != that.base_schema_hash)
        return false;
    }

    boolean this_present_new_tablet_req = true && this.isSetNew_tablet_req();
    boolean that_present_new_tablet_req = true && that.isSetNew_tablet_req();
    if (this_present_new_tablet_req || that_present_new_tablet_req) {
      if (!(this_present_new_tablet_req && that_present_new_tablet_req))
        return false;
      if (!this.new_tablet_req.equals(that.new_tablet_req))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(base_tablet_id);

    hashCode = hashCode * 8191 + base_schema_hash;

    hashCode = hashCode * 8191 + ((isSetNew_tablet_req()) ? 131071 : 524287);
    if (isSetNew_tablet_req())
      hashCode = hashCode * 8191 + new_tablet_req.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TAlterTabletReq other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetBase_tablet_id(), other.isSetBase_tablet_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBase_tablet_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.base_tablet_id, other.base_tablet_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetBase_schema_hash(), other.isSetBase_schema_hash());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBase_schema_hash()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.base_schema_hash, other.base_schema_hash);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetNew_tablet_req(), other.isSetNew_tablet_req());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNew_tablet_req()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.new_tablet_req, other.new_tablet_req);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TAlterTabletReq(");
    boolean first = true;

    sb.append("base_tablet_id:");
    sb.append(this.base_tablet_id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("base_schema_hash:");
    sb.append(this.base_schema_hash);
    first = false;
    if (!first) sb.append(", ");
    sb.append("new_tablet_req:");
    if (this.new_tablet_req == null) {
      sb.append("null");
    } else {
      sb.append(this.new_tablet_req);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'base_tablet_id' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'base_schema_hash' because it's a primitive and you chose the non-beans generator.
    if (new_tablet_req == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'new_tablet_req' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (new_tablet_req != null) {
      new_tablet_req.validate();
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

  private static class TAlterTabletReqStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TAlterTabletReqStandardScheme getScheme() {
      return new TAlterTabletReqStandardScheme();
    }
  }

  private static class TAlterTabletReqStandardScheme extends org.apache.thrift.scheme.StandardScheme<TAlterTabletReq> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TAlterTabletReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // BASE_TABLET_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.base_tablet_id = iprot.readI64();
              struct.setBase_tablet_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // BASE_SCHEMA_HASH
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.base_schema_hash = iprot.readI32();
              struct.setBase_schema_hashIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // NEW_TABLET_REQ
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.new_tablet_req = new TCreateTabletReq();
              struct.new_tablet_req.read(iprot);
              struct.setNew_tablet_reqIsSet(true);
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
      if (!struct.isSetBase_tablet_id()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'base_tablet_id' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetBase_schema_hash()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'base_schema_hash' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, TAlterTabletReq struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(BASE_TABLET_ID_FIELD_DESC);
      oprot.writeI64(struct.base_tablet_id);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(BASE_SCHEMA_HASH_FIELD_DESC);
      oprot.writeI32(struct.base_schema_hash);
      oprot.writeFieldEnd();
      if (struct.new_tablet_req != null) {
        oprot.writeFieldBegin(NEW_TABLET_REQ_FIELD_DESC);
        struct.new_tablet_req.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TAlterTabletReqTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TAlterTabletReqTupleScheme getScheme() {
      return new TAlterTabletReqTupleScheme();
    }
  }

  private static class TAlterTabletReqTupleScheme extends org.apache.thrift.scheme.TupleScheme<TAlterTabletReq> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TAlterTabletReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI64(struct.base_tablet_id);
      oprot.writeI32(struct.base_schema_hash);
      struct.new_tablet_req.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TAlterTabletReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.base_tablet_id = iprot.readI64();
      struct.setBase_tablet_idIsSet(true);
      struct.base_schema_hash = iprot.readI32();
      struct.setBase_schema_hashIsSet(true);
      struct.new_tablet_req = new TCreateTabletReq();
      struct.new_tablet_req.read(iprot);
      struct.setNew_tablet_reqIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

