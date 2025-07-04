/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TCancelDeleteDataReq implements org.apache.thrift.TBase<TCancelDeleteDataReq, TCancelDeleteDataReq._Fields>, java.io.Serializable, Cloneable, Comparable<TCancelDeleteDataReq> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TCancelDeleteDataReq");

  private static final org.apache.thrift.protocol.TField TABLET_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("tablet_id", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField SCHEMA_HASH_FIELD_DESC = new org.apache.thrift.protocol.TField("schema_hash", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField VERSION_FIELD_DESC = new org.apache.thrift.protocol.TField("version", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField VERSION_HASH_FIELD_DESC = new org.apache.thrift.protocol.TField("version_hash", org.apache.thrift.protocol.TType.I64, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TCancelDeleteDataReqStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TCancelDeleteDataReqTupleSchemeFactory();

  public long tablet_id; // required
  public int schema_hash; // required
  public long version; // required
  public long version_hash; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TABLET_ID((short)1, "tablet_id"),
    SCHEMA_HASH((short)2, "schema_hash"),
    VERSION((short)3, "version"),
    VERSION_HASH((short)4, "version_hash");

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
        case 1: // TABLET_ID
          return TABLET_ID;
        case 2: // SCHEMA_HASH
          return SCHEMA_HASH;
        case 3: // VERSION
          return VERSION;
        case 4: // VERSION_HASH
          return VERSION_HASH;
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
  private static final int __TABLET_ID_ISSET_ID = 0;
  private static final int __SCHEMA_HASH_ISSET_ID = 1;
  private static final int __VERSION_ISSET_ID = 2;
  private static final int __VERSION_HASH_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TABLET_ID, new org.apache.thrift.meta_data.FieldMetaData("tablet_id", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64        , "TTabletId")));
    tmpMap.put(_Fields.SCHEMA_HASH, new org.apache.thrift.meta_data.FieldMetaData("schema_hash", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "TSchemaHash")));
    tmpMap.put(_Fields.VERSION, new org.apache.thrift.meta_data.FieldMetaData("version", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64        , "TVersion")));
    tmpMap.put(_Fields.VERSION_HASH, new org.apache.thrift.meta_data.FieldMetaData("version_hash", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64        , "TVersionHash")));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TCancelDeleteDataReq.class, metaDataMap);
  }

  public TCancelDeleteDataReq() {
  }

  public TCancelDeleteDataReq(
    long tablet_id,
    int schema_hash,
    long version,
    long version_hash)
  {
    this();
    this.tablet_id = tablet_id;
    setTablet_idIsSet(true);
    this.schema_hash = schema_hash;
    setSchema_hashIsSet(true);
    this.version = version;
    setVersionIsSet(true);
    this.version_hash = version_hash;
    setVersion_hashIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TCancelDeleteDataReq(TCancelDeleteDataReq other) {
    __isset_bitfield = other.__isset_bitfield;
    this.tablet_id = other.tablet_id;
    this.schema_hash = other.schema_hash;
    this.version = other.version;
    this.version_hash = other.version_hash;
  }

  @Override
  public TCancelDeleteDataReq deepCopy() {
    return new TCancelDeleteDataReq(this);
  }

  @Override
  public void clear() {
    setTablet_idIsSet(false);
    this.tablet_id = 0;
    setSchema_hashIsSet(false);
    this.schema_hash = 0;
    setVersionIsSet(false);
    this.version = 0;
    setVersion_hashIsSet(false);
    this.version_hash = 0;
  }

  public long getTablet_id() {
    return this.tablet_id;
  }

  public TCancelDeleteDataReq setTablet_id(long tablet_id) {
    this.tablet_id = tablet_id;
    setTablet_idIsSet(true);
    return this;
  }

  public void unsetTablet_id() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __TABLET_ID_ISSET_ID);
  }

  /** Returns true if field tablet_id is set (has been assigned a value) and false otherwise */
  public boolean isSetTablet_id() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __TABLET_ID_ISSET_ID);
  }

  public void setTablet_idIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __TABLET_ID_ISSET_ID, value);
  }

  public int getSchema_hash() {
    return this.schema_hash;
  }

  public TCancelDeleteDataReq setSchema_hash(int schema_hash) {
    this.schema_hash = schema_hash;
    setSchema_hashIsSet(true);
    return this;
  }

  public void unsetSchema_hash() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __SCHEMA_HASH_ISSET_ID);
  }

  /** Returns true if field schema_hash is set (has been assigned a value) and false otherwise */
  public boolean isSetSchema_hash() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __SCHEMA_HASH_ISSET_ID);
  }

  public void setSchema_hashIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __SCHEMA_HASH_ISSET_ID, value);
  }

  public long getVersion() {
    return this.version;
  }

  public TCancelDeleteDataReq setVersion(long version) {
    this.version = version;
    setVersionIsSet(true);
    return this;
  }

  public void unsetVersion() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __VERSION_ISSET_ID);
  }

  /** Returns true if field version is set (has been assigned a value) and false otherwise */
  public boolean isSetVersion() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __VERSION_ISSET_ID);
  }

  public void setVersionIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __VERSION_ISSET_ID, value);
  }

  public long getVersion_hash() {
    return this.version_hash;
  }

  public TCancelDeleteDataReq setVersion_hash(long version_hash) {
    this.version_hash = version_hash;
    setVersion_hashIsSet(true);
    return this;
  }

  public void unsetVersion_hash() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __VERSION_HASH_ISSET_ID);
  }

  /** Returns true if field version_hash is set (has been assigned a value) and false otherwise */
  public boolean isSetVersion_hash() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __VERSION_HASH_ISSET_ID);
  }

  public void setVersion_hashIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __VERSION_HASH_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case TABLET_ID:
      if (value == null) {
        unsetTablet_id();
      } else {
        setTablet_id((java.lang.Long)value);
      }
      break;

    case SCHEMA_HASH:
      if (value == null) {
        unsetSchema_hash();
      } else {
        setSchema_hash((java.lang.Integer)value);
      }
      break;

    case VERSION:
      if (value == null) {
        unsetVersion();
      } else {
        setVersion((java.lang.Long)value);
      }
      break;

    case VERSION_HASH:
      if (value == null) {
        unsetVersion_hash();
      } else {
        setVersion_hash((java.lang.Long)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TABLET_ID:
      return getTablet_id();

    case SCHEMA_HASH:
      return getSchema_hash();

    case VERSION:
      return getVersion();

    case VERSION_HASH:
      return getVersion_hash();

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
    case TABLET_ID:
      return isSetTablet_id();
    case SCHEMA_HASH:
      return isSetSchema_hash();
    case VERSION:
      return isSetVersion();
    case VERSION_HASH:
      return isSetVersion_hash();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TCancelDeleteDataReq)
      return this.equals((TCancelDeleteDataReq)that);
    return false;
  }

  public boolean equals(TCancelDeleteDataReq that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_tablet_id = true;
    boolean that_present_tablet_id = true;
    if (this_present_tablet_id || that_present_tablet_id) {
      if (!(this_present_tablet_id && that_present_tablet_id))
        return false;
      if (this.tablet_id != that.tablet_id)
        return false;
    }

    boolean this_present_schema_hash = true;
    boolean that_present_schema_hash = true;
    if (this_present_schema_hash || that_present_schema_hash) {
      if (!(this_present_schema_hash && that_present_schema_hash))
        return false;
      if (this.schema_hash != that.schema_hash)
        return false;
    }

    boolean this_present_version = true;
    boolean that_present_version = true;
    if (this_present_version || that_present_version) {
      if (!(this_present_version && that_present_version))
        return false;
      if (this.version != that.version)
        return false;
    }

    boolean this_present_version_hash = true;
    boolean that_present_version_hash = true;
    if (this_present_version_hash || that_present_version_hash) {
      if (!(this_present_version_hash && that_present_version_hash))
        return false;
      if (this.version_hash != that.version_hash)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(tablet_id);

    hashCode = hashCode * 8191 + schema_hash;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(version);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(version_hash);

    return hashCode;
  }

  @Override
  public int compareTo(TCancelDeleteDataReq other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetTablet_id(), other.isSetTablet_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTablet_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tablet_id, other.tablet_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetSchema_hash(), other.isSetSchema_hash());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSchema_hash()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.schema_hash, other.schema_hash);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetVersion(), other.isSetVersion());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVersion()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.version, other.version);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetVersion_hash(), other.isSetVersion_hash());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVersion_hash()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.version_hash, other.version_hash);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TCancelDeleteDataReq(");
    boolean first = true;

    sb.append("tablet_id:");
    sb.append(this.tablet_id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("schema_hash:");
    sb.append(this.schema_hash);
    first = false;
    if (!first) sb.append(", ");
    sb.append("version:");
    sb.append(this.version);
    first = false;
    if (!first) sb.append(", ");
    sb.append("version_hash:");
    sb.append(this.version_hash);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'tablet_id' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'schema_hash' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'version' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'version_hash' because it's a primitive and you chose the non-beans generator.
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

  private static class TCancelDeleteDataReqStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TCancelDeleteDataReqStandardScheme getScheme() {
      return new TCancelDeleteDataReqStandardScheme();
    }
  }

  private static class TCancelDeleteDataReqStandardScheme extends org.apache.thrift.scheme.StandardScheme<TCancelDeleteDataReq> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TCancelDeleteDataReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TABLET_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.tablet_id = iprot.readI64();
              struct.setTablet_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SCHEMA_HASH
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.schema_hash = iprot.readI32();
              struct.setSchema_hashIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // VERSION
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.version = iprot.readI64();
              struct.setVersionIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // VERSION_HASH
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.version_hash = iprot.readI64();
              struct.setVersion_hashIsSet(true);
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
      if (!struct.isSetTablet_id()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'tablet_id' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetSchema_hash()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'schema_hash' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetVersion()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'version' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetVersion_hash()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'version_hash' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, TCancelDeleteDataReq struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(TABLET_ID_FIELD_DESC);
      oprot.writeI64(struct.tablet_id);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(SCHEMA_HASH_FIELD_DESC);
      oprot.writeI32(struct.schema_hash);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(VERSION_FIELD_DESC);
      oprot.writeI64(struct.version);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(VERSION_HASH_FIELD_DESC);
      oprot.writeI64(struct.version_hash);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TCancelDeleteDataReqTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TCancelDeleteDataReqTupleScheme getScheme() {
      return new TCancelDeleteDataReqTupleScheme();
    }
  }

  private static class TCancelDeleteDataReqTupleScheme extends org.apache.thrift.scheme.TupleScheme<TCancelDeleteDataReq> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TCancelDeleteDataReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI64(struct.tablet_id);
      oprot.writeI32(struct.schema_hash);
      oprot.writeI64(struct.version);
      oprot.writeI64(struct.version_hash);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TCancelDeleteDataReq struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.tablet_id = iprot.readI64();
      struct.setTablet_idIsSet(true);
      struct.schema_hash = iprot.readI32();
      struct.setSchema_hashIsSet(true);
      struct.version = iprot.readI64();
      struct.setVersionIsSet(true);
      struct.version_hash = iprot.readI64();
      struct.setVersion_hashIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

