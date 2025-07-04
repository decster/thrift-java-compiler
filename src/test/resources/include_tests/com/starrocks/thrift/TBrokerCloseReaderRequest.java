/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TBrokerCloseReaderRequest implements org.apache.thrift.TBase<TBrokerCloseReaderRequest, TBrokerCloseReaderRequest._Fields>, java.io.Serializable, Cloneable, Comparable<TBrokerCloseReaderRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TBrokerCloseReaderRequest");

  private static final org.apache.thrift.protocol.TField VERSION_FIELD_DESC = new org.apache.thrift.protocol.TField("version", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField FD_FIELD_DESC = new org.apache.thrift.protocol.TField("fd", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TBrokerCloseReaderRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TBrokerCloseReaderRequestTupleSchemeFactory();

  /**
   * 
   * @see TBrokerVersion
   */
  public @org.apache.thrift.annotation.Nullable TBrokerVersion version; // required
  public @org.apache.thrift.annotation.Nullable TBrokerFD fd; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see TBrokerVersion
     */
    VERSION((short)1, "version"),
    FD((short)2, "fd");

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
        case 1: // VERSION
          return VERSION;
        case 2: // FD
          return FD;
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
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VERSION, new org.apache.thrift.meta_data.FieldMetaData("version", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, TBrokerVersion.class)));
    tmpMap.put(_Fields.FD, new org.apache.thrift.meta_data.FieldMetaData("fd", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TBrokerFD.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TBrokerCloseReaderRequest.class, metaDataMap);
  }

  public TBrokerCloseReaderRequest() {
  }

  public TBrokerCloseReaderRequest(
    TBrokerVersion version,
    TBrokerFD fd)
  {
    this();
    this.version = version;
    this.fd = fd;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TBrokerCloseReaderRequest(TBrokerCloseReaderRequest other) {
    if (other.isSetVersion()) {
      this.version = other.version;
    }
    if (other.isSetFd()) {
      this.fd = new TBrokerFD(other.fd);
    }
  }

  @Override
  public TBrokerCloseReaderRequest deepCopy() {
    return new TBrokerCloseReaderRequest(this);
  }

  @Override
  public void clear() {
    this.version = null;
    this.fd = null;
  }

  /**
   * 
   * @see TBrokerVersion
   */
  @org.apache.thrift.annotation.Nullable
  public TBrokerVersion getVersion() {
    return this.version;
  }

  /**
   * 
   * @see TBrokerVersion
   */
  public TBrokerCloseReaderRequest setVersion(@org.apache.thrift.annotation.Nullable TBrokerVersion version) {
    this.version = version;
    return this;
  }

  public void unsetVersion() {
    this.version = null;
  }

  /** Returns true if field version is set (has been assigned a value) and false otherwise */
  public boolean isSetVersion() {
    return this.version != null;
  }

  public void setVersionIsSet(boolean value) {
    if (!value) {
      this.version = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public TBrokerFD getFd() {
    return this.fd;
  }

  public TBrokerCloseReaderRequest setFd(@org.apache.thrift.annotation.Nullable TBrokerFD fd) {
    this.fd = fd;
    return this;
  }

  public void unsetFd() {
    this.fd = null;
  }

  /** Returns true if field fd is set (has been assigned a value) and false otherwise */
  public boolean isSetFd() {
    return this.fd != null;
  }

  public void setFdIsSet(boolean value) {
    if (!value) {
      this.fd = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case VERSION:
      if (value == null) {
        unsetVersion();
      } else {
        setVersion((TBrokerVersion)value);
      }
      break;

    case FD:
      if (value == null) {
        unsetFd();
      } else {
        setFd((TBrokerFD)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VERSION:
      return getVersion();

    case FD:
      return getFd();

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
    case VERSION:
      return isSetVersion();
    case FD:
      return isSetFd();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TBrokerCloseReaderRequest)
      return this.equals((TBrokerCloseReaderRequest)that);
    return false;
  }

  public boolean equals(TBrokerCloseReaderRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_version = true && this.isSetVersion();
    boolean that_present_version = true && that.isSetVersion();
    if (this_present_version || that_present_version) {
      if (!(this_present_version && that_present_version))
        return false;
      if (!this.version.equals(that.version))
        return false;
    }

    boolean this_present_fd = true && this.isSetFd();
    boolean that_present_fd = true && that.isSetFd();
    if (this_present_fd || that_present_fd) {
      if (!(this_present_fd && that_present_fd))
        return false;
      if (!this.fd.equals(that.fd))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetVersion()) ? 131071 : 524287);
    if (isSetVersion())
      hashCode = hashCode * 8191 + version.getValue();

    hashCode = hashCode * 8191 + ((isSetFd()) ? 131071 : 524287);
    if (isSetFd())
      hashCode = hashCode * 8191 + fd.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TBrokerCloseReaderRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

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
    lastComparison = java.lang.Boolean.compare(isSetFd(), other.isSetFd());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFd()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fd, other.fd);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TBrokerCloseReaderRequest(");
    boolean first = true;

    sb.append("version:");
    if (this.version == null) {
      sb.append("null");
    } else {
      sb.append(this.version);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("fd:");
    if (this.fd == null) {
      sb.append("null");
    } else {
      sb.append(this.fd);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (version == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'version' was not present! Struct: " + toString());
    }
    if (fd == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'fd' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (fd != null) {
      fd.validate();
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

  private static class TBrokerCloseReaderRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TBrokerCloseReaderRequestStandardScheme getScheme() {
      return new TBrokerCloseReaderRequestStandardScheme();
    }
  }

  private static class TBrokerCloseReaderRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<TBrokerCloseReaderRequest> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TBrokerCloseReaderRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VERSION
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.version = com.starrocks.thrift.TBrokerVersion.findByValue(iprot.readI32());
              struct.setVersionIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // FD
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.fd = new TBrokerFD();
              struct.fd.read(iprot);
              struct.setFdIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TBrokerCloseReaderRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.version != null) {
        oprot.writeFieldBegin(VERSION_FIELD_DESC);
        oprot.writeI32(struct.version.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.fd != null) {
        oprot.writeFieldBegin(FD_FIELD_DESC);
        struct.fd.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TBrokerCloseReaderRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TBrokerCloseReaderRequestTupleScheme getScheme() {
      return new TBrokerCloseReaderRequestTupleScheme();
    }
  }

  private static class TBrokerCloseReaderRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<TBrokerCloseReaderRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TBrokerCloseReaderRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI32(struct.version.getValue());
      struct.fd.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TBrokerCloseReaderRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.version = com.starrocks.thrift.TBrokerVersion.findByValue(iprot.readI32());
      struct.setVersionIsSet(true);
      struct.fd = new TBrokerFD();
      struct.fd.read(iprot);
      struct.setFdIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

