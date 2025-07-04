/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TUpdateMiniEtlTaskStatusRequest implements org.apache.thrift.TBase<TUpdateMiniEtlTaskStatusRequest, TUpdateMiniEtlTaskStatusRequest._Fields>, java.io.Serializable, Cloneable, Comparable<TUpdateMiniEtlTaskStatusRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TUpdateMiniEtlTaskStatusRequest");

  private static final org.apache.thrift.protocol.TField PROTOCOL_VERSION_FIELD_DESC = new org.apache.thrift.protocol.TField("protocolVersion", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField ETL_TASK_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("etlTaskId", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField ETL_TASK_STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("etlTaskStatus", org.apache.thrift.protocol.TType.STRUCT, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TUpdateMiniEtlTaskStatusRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TUpdateMiniEtlTaskStatusRequestTupleSchemeFactory();

  /**
   * 
   * @see FrontendServiceVersion
   */
  public @org.apache.thrift.annotation.Nullable FrontendServiceVersion protocolVersion; // required
  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TUniqueId etlTaskId; // required
  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TMiniLoadEtlStatusResult etlTaskStatus; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see FrontendServiceVersion
     */
    PROTOCOL_VERSION((short)1, "protocolVersion"),
    ETL_TASK_ID((short)2, "etlTaskId"),
    ETL_TASK_STATUS((short)3, "etlTaskStatus");

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
        case 1: // PROTOCOL_VERSION
          return PROTOCOL_VERSION;
        case 2: // ETL_TASK_ID
          return ETL_TASK_ID;
        case 3: // ETL_TASK_STATUS
          return ETL_TASK_STATUS;
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
    tmpMap.put(_Fields.PROTOCOL_VERSION, new org.apache.thrift.meta_data.FieldMetaData("protocolVersion", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, FrontendServiceVersion.class)));
    tmpMap.put(_Fields.ETL_TASK_ID, new org.apache.thrift.meta_data.FieldMetaData("etlTaskId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TUniqueId.class)));
    tmpMap.put(_Fields.ETL_TASK_STATUS, new org.apache.thrift.meta_data.FieldMetaData("etlTaskStatus", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TMiniLoadEtlStatusResult.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TUpdateMiniEtlTaskStatusRequest.class, metaDataMap);
  }

  public TUpdateMiniEtlTaskStatusRequest() {
  }

  public TUpdateMiniEtlTaskStatusRequest(
    FrontendServiceVersion protocolVersion,
    com.starrocks.thrift.TUniqueId etlTaskId,
    com.starrocks.thrift.TMiniLoadEtlStatusResult etlTaskStatus)
  {
    this();
    this.protocolVersion = protocolVersion;
    this.etlTaskId = etlTaskId;
    this.etlTaskStatus = etlTaskStatus;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TUpdateMiniEtlTaskStatusRequest(TUpdateMiniEtlTaskStatusRequest other) {
    if (other.isSetProtocolVersion()) {
      this.protocolVersion = other.protocolVersion;
    }
    if (other.isSetEtlTaskId()) {
      this.etlTaskId = new com.starrocks.thrift.TUniqueId(other.etlTaskId);
    }
    if (other.isSetEtlTaskStatus()) {
      this.etlTaskStatus = new com.starrocks.thrift.TMiniLoadEtlStatusResult(other.etlTaskStatus);
    }
  }

  @Override
  public TUpdateMiniEtlTaskStatusRequest deepCopy() {
    return new TUpdateMiniEtlTaskStatusRequest(this);
  }

  @Override
  public void clear() {
    this.protocolVersion = null;
    this.etlTaskId = null;
    this.etlTaskStatus = null;
  }

  /**
   * 
   * @see FrontendServiceVersion
   */
  @org.apache.thrift.annotation.Nullable
  public FrontendServiceVersion getProtocolVersion() {
    return this.protocolVersion;
  }

  /**
   * 
   * @see FrontendServiceVersion
   */
  public TUpdateMiniEtlTaskStatusRequest setProtocolVersion(@org.apache.thrift.annotation.Nullable FrontendServiceVersion protocolVersion) {
    this.protocolVersion = protocolVersion;
    return this;
  }

  public void unsetProtocolVersion() {
    this.protocolVersion = null;
  }

  /** Returns true if field protocolVersion is set (has been assigned a value) and false otherwise */
  public boolean isSetProtocolVersion() {
    return this.protocolVersion != null;
  }

  public void setProtocolVersionIsSet(boolean value) {
    if (!value) {
      this.protocolVersion = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TUniqueId getEtlTaskId() {
    return this.etlTaskId;
  }

  public TUpdateMiniEtlTaskStatusRequest setEtlTaskId(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TUniqueId etlTaskId) {
    this.etlTaskId = etlTaskId;
    return this;
  }

  public void unsetEtlTaskId() {
    this.etlTaskId = null;
  }

  /** Returns true if field etlTaskId is set (has been assigned a value) and false otherwise */
  public boolean isSetEtlTaskId() {
    return this.etlTaskId != null;
  }

  public void setEtlTaskIdIsSet(boolean value) {
    if (!value) {
      this.etlTaskId = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TMiniLoadEtlStatusResult getEtlTaskStatus() {
    return this.etlTaskStatus;
  }

  public TUpdateMiniEtlTaskStatusRequest setEtlTaskStatus(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TMiniLoadEtlStatusResult etlTaskStatus) {
    this.etlTaskStatus = etlTaskStatus;
    return this;
  }

  public void unsetEtlTaskStatus() {
    this.etlTaskStatus = null;
  }

  /** Returns true if field etlTaskStatus is set (has been assigned a value) and false otherwise */
  public boolean isSetEtlTaskStatus() {
    return this.etlTaskStatus != null;
  }

  public void setEtlTaskStatusIsSet(boolean value) {
    if (!value) {
      this.etlTaskStatus = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case PROTOCOL_VERSION:
      if (value == null) {
        unsetProtocolVersion();
      } else {
        setProtocolVersion((FrontendServiceVersion)value);
      }
      break;

    case ETL_TASK_ID:
      if (value == null) {
        unsetEtlTaskId();
      } else {
        setEtlTaskId((com.starrocks.thrift.TUniqueId)value);
      }
      break;

    case ETL_TASK_STATUS:
      if (value == null) {
        unsetEtlTaskStatus();
      } else {
        setEtlTaskStatus((com.starrocks.thrift.TMiniLoadEtlStatusResult)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case PROTOCOL_VERSION:
      return getProtocolVersion();

    case ETL_TASK_ID:
      return getEtlTaskId();

    case ETL_TASK_STATUS:
      return getEtlTaskStatus();

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
    case PROTOCOL_VERSION:
      return isSetProtocolVersion();
    case ETL_TASK_ID:
      return isSetEtlTaskId();
    case ETL_TASK_STATUS:
      return isSetEtlTaskStatus();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TUpdateMiniEtlTaskStatusRequest)
      return this.equals((TUpdateMiniEtlTaskStatusRequest)that);
    return false;
  }

  public boolean equals(TUpdateMiniEtlTaskStatusRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_protocolVersion = true && this.isSetProtocolVersion();
    boolean that_present_protocolVersion = true && that.isSetProtocolVersion();
    if (this_present_protocolVersion || that_present_protocolVersion) {
      if (!(this_present_protocolVersion && that_present_protocolVersion))
        return false;
      if (!this.protocolVersion.equals(that.protocolVersion))
        return false;
    }

    boolean this_present_etlTaskId = true && this.isSetEtlTaskId();
    boolean that_present_etlTaskId = true && that.isSetEtlTaskId();
    if (this_present_etlTaskId || that_present_etlTaskId) {
      if (!(this_present_etlTaskId && that_present_etlTaskId))
        return false;
      if (!this.etlTaskId.equals(that.etlTaskId))
        return false;
    }

    boolean this_present_etlTaskStatus = true && this.isSetEtlTaskStatus();
    boolean that_present_etlTaskStatus = true && that.isSetEtlTaskStatus();
    if (this_present_etlTaskStatus || that_present_etlTaskStatus) {
      if (!(this_present_etlTaskStatus && that_present_etlTaskStatus))
        return false;
      if (!this.etlTaskStatus.equals(that.etlTaskStatus))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetProtocolVersion()) ? 131071 : 524287);
    if (isSetProtocolVersion())
      hashCode = hashCode * 8191 + protocolVersion.getValue();

    hashCode = hashCode * 8191 + ((isSetEtlTaskId()) ? 131071 : 524287);
    if (isSetEtlTaskId())
      hashCode = hashCode * 8191 + etlTaskId.hashCode();

    hashCode = hashCode * 8191 + ((isSetEtlTaskStatus()) ? 131071 : 524287);
    if (isSetEtlTaskStatus())
      hashCode = hashCode * 8191 + etlTaskStatus.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TUpdateMiniEtlTaskStatusRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetProtocolVersion(), other.isSetProtocolVersion());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProtocolVersion()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.protocolVersion, other.protocolVersion);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetEtlTaskId(), other.isSetEtlTaskId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEtlTaskId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.etlTaskId, other.etlTaskId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetEtlTaskStatus(), other.isSetEtlTaskStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEtlTaskStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.etlTaskStatus, other.etlTaskStatus);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TUpdateMiniEtlTaskStatusRequest(");
    boolean first = true;

    sb.append("protocolVersion:");
    if (this.protocolVersion == null) {
      sb.append("null");
    } else {
      sb.append(this.protocolVersion);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("etlTaskId:");
    if (this.etlTaskId == null) {
      sb.append("null");
    } else {
      sb.append(this.etlTaskId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("etlTaskStatus:");
    if (this.etlTaskStatus == null) {
      sb.append("null");
    } else {
      sb.append(this.etlTaskStatus);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (protocolVersion == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'protocolVersion' was not present! Struct: " + toString());
    }
    if (etlTaskId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'etlTaskId' was not present! Struct: " + toString());
    }
    if (etlTaskStatus == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'etlTaskStatus' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (etlTaskId != null) {
      etlTaskId.validate();
    }
    if (etlTaskStatus != null) {
      etlTaskStatus.validate();
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

  private static class TUpdateMiniEtlTaskStatusRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TUpdateMiniEtlTaskStatusRequestStandardScheme getScheme() {
      return new TUpdateMiniEtlTaskStatusRequestStandardScheme();
    }
  }

  private static class TUpdateMiniEtlTaskStatusRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<TUpdateMiniEtlTaskStatusRequest> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TUpdateMiniEtlTaskStatusRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PROTOCOL_VERSION
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.protocolVersion = com.starrocks.thrift.FrontendServiceVersion.findByValue(iprot.readI32());
              struct.setProtocolVersionIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ETL_TASK_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.etlTaskId = new com.starrocks.thrift.TUniqueId();
              struct.etlTaskId.read(iprot);
              struct.setEtlTaskIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ETL_TASK_STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.etlTaskStatus = new com.starrocks.thrift.TMiniLoadEtlStatusResult();
              struct.etlTaskStatus.read(iprot);
              struct.setEtlTaskStatusIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TUpdateMiniEtlTaskStatusRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.protocolVersion != null) {
        oprot.writeFieldBegin(PROTOCOL_VERSION_FIELD_DESC);
        oprot.writeI32(struct.protocolVersion.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.etlTaskId != null) {
        oprot.writeFieldBegin(ETL_TASK_ID_FIELD_DESC);
        struct.etlTaskId.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.etlTaskStatus != null) {
        oprot.writeFieldBegin(ETL_TASK_STATUS_FIELD_DESC);
        struct.etlTaskStatus.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TUpdateMiniEtlTaskStatusRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TUpdateMiniEtlTaskStatusRequestTupleScheme getScheme() {
      return new TUpdateMiniEtlTaskStatusRequestTupleScheme();
    }
  }

  private static class TUpdateMiniEtlTaskStatusRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<TUpdateMiniEtlTaskStatusRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TUpdateMiniEtlTaskStatusRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI32(struct.protocolVersion.getValue());
      struct.etlTaskId.write(oprot);
      struct.etlTaskStatus.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TUpdateMiniEtlTaskStatusRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.protocolVersion = com.starrocks.thrift.FrontendServiceVersion.findByValue(iprot.readI32());
      struct.setProtocolVersionIsSet(true);
      struct.etlTaskId = new com.starrocks.thrift.TUniqueId();
      struct.etlTaskId.read(iprot);
      struct.setEtlTaskIdIsSet(true);
      struct.etlTaskStatus = new com.starrocks.thrift.TMiniLoadEtlStatusResult();
      struct.etlTaskStatus.read(iprot);
      struct.setEtlTaskStatusIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

