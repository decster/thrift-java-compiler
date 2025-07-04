/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TTableFunctionTableSink implements org.apache.thrift.TBase<TTableFunctionTableSink, TTableFunctionTableSink._Fields>, java.io.Serializable, Cloneable, Comparable<TTableFunctionTableSink> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TTableFunctionTableSink");

  private static final org.apache.thrift.protocol.TField TARGET_TABLE_FIELD_DESC = new org.apache.thrift.protocol.TField("target_table", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField CLOUD_CONFIGURATION_FIELD_DESC = new org.apache.thrift.protocol.TField("cloud_configuration", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TTableFunctionTableSinkStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TTableFunctionTableSinkTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TTableFunctionTable target_table; // optional
  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TCloudConfiguration cloud_configuration; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TARGET_TABLE((short)1, "target_table"),
    CLOUD_CONFIGURATION((short)2, "cloud_configuration");

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
        case 1: // TARGET_TABLE
          return TARGET_TABLE;
        case 2: // CLOUD_CONFIGURATION
          return CLOUD_CONFIGURATION;
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
  private static final _Fields optionals[] = {_Fields.TARGET_TABLE,_Fields.CLOUD_CONFIGURATION};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TARGET_TABLE, new org.apache.thrift.meta_data.FieldMetaData("target_table", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TTableFunctionTable.class)));
    tmpMap.put(_Fields.CLOUD_CONFIGURATION, new org.apache.thrift.meta_data.FieldMetaData("cloud_configuration", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TCloudConfiguration.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TTableFunctionTableSink.class, metaDataMap);
  }

  public TTableFunctionTableSink() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TTableFunctionTableSink(TTableFunctionTableSink other) {
    if (other.isSetTarget_table()) {
      this.target_table = new com.starrocks.thrift.TTableFunctionTable(other.target_table);
    }
    if (other.isSetCloud_configuration()) {
      this.cloud_configuration = new com.starrocks.thrift.TCloudConfiguration(other.cloud_configuration);
    }
  }

  @Override
  public TTableFunctionTableSink deepCopy() {
    return new TTableFunctionTableSink(this);
  }

  @Override
  public void clear() {
    this.target_table = null;
    this.cloud_configuration = null;
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TTableFunctionTable getTarget_table() {
    return this.target_table;
  }

  public TTableFunctionTableSink setTarget_table(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TTableFunctionTable target_table) {
    this.target_table = target_table;
    return this;
  }

  public void unsetTarget_table() {
    this.target_table = null;
  }

  /** Returns true if field target_table is set (has been assigned a value) and false otherwise */
  public boolean isSetTarget_table() {
    return this.target_table != null;
  }

  public void setTarget_tableIsSet(boolean value) {
    if (!value) {
      this.target_table = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TCloudConfiguration getCloud_configuration() {
    return this.cloud_configuration;
  }

  public TTableFunctionTableSink setCloud_configuration(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TCloudConfiguration cloud_configuration) {
    this.cloud_configuration = cloud_configuration;
    return this;
  }

  public void unsetCloud_configuration() {
    this.cloud_configuration = null;
  }

  /** Returns true if field cloud_configuration is set (has been assigned a value) and false otherwise */
  public boolean isSetCloud_configuration() {
    return this.cloud_configuration != null;
  }

  public void setCloud_configurationIsSet(boolean value) {
    if (!value) {
      this.cloud_configuration = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case TARGET_TABLE:
      if (value == null) {
        unsetTarget_table();
      } else {
        setTarget_table((com.starrocks.thrift.TTableFunctionTable)value);
      }
      break;

    case CLOUD_CONFIGURATION:
      if (value == null) {
        unsetCloud_configuration();
      } else {
        setCloud_configuration((com.starrocks.thrift.TCloudConfiguration)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TARGET_TABLE:
      return getTarget_table();

    case CLOUD_CONFIGURATION:
      return getCloud_configuration();

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
    case TARGET_TABLE:
      return isSetTarget_table();
    case CLOUD_CONFIGURATION:
      return isSetCloud_configuration();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TTableFunctionTableSink)
      return this.equals((TTableFunctionTableSink)that);
    return false;
  }

  public boolean equals(TTableFunctionTableSink that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_target_table = true && this.isSetTarget_table();
    boolean that_present_target_table = true && that.isSetTarget_table();
    if (this_present_target_table || that_present_target_table) {
      if (!(this_present_target_table && that_present_target_table))
        return false;
      if (!this.target_table.equals(that.target_table))
        return false;
    }

    boolean this_present_cloud_configuration = true && this.isSetCloud_configuration();
    boolean that_present_cloud_configuration = true && that.isSetCloud_configuration();
    if (this_present_cloud_configuration || that_present_cloud_configuration) {
      if (!(this_present_cloud_configuration && that_present_cloud_configuration))
        return false;
      if (!this.cloud_configuration.equals(that.cloud_configuration))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetTarget_table()) ? 131071 : 524287);
    if (isSetTarget_table())
      hashCode = hashCode * 8191 + target_table.hashCode();

    hashCode = hashCode * 8191 + ((isSetCloud_configuration()) ? 131071 : 524287);
    if (isSetCloud_configuration())
      hashCode = hashCode * 8191 + cloud_configuration.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TTableFunctionTableSink other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetTarget_table(), other.isSetTarget_table());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTarget_table()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.target_table, other.target_table);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetCloud_configuration(), other.isSetCloud_configuration());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCloud_configuration()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cloud_configuration, other.cloud_configuration);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TTableFunctionTableSink(");
    boolean first = true;

    if (isSetTarget_table()) {
      sb.append("target_table:");
      if (this.target_table == null) {
        sb.append("null");
      } else {
        sb.append(this.target_table);
      }
      first = false;
    }
    if (isSetCloud_configuration()) {
      if (!first) sb.append(", ");
      sb.append("cloud_configuration:");
      if (this.cloud_configuration == null) {
        sb.append("null");
      } else {
        sb.append(this.cloud_configuration);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (target_table != null) {
      target_table.validate();
    }
    if (cloud_configuration != null) {
      cloud_configuration.validate();
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

  private static class TTableFunctionTableSinkStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TTableFunctionTableSinkStandardScheme getScheme() {
      return new TTableFunctionTableSinkStandardScheme();
    }
  }

  private static class TTableFunctionTableSinkStandardScheme extends org.apache.thrift.scheme.StandardScheme<TTableFunctionTableSink> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TTableFunctionTableSink struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TARGET_TABLE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.target_table = new com.starrocks.thrift.TTableFunctionTable();
              struct.target_table.read(iprot);
              struct.setTarget_tableIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CLOUD_CONFIGURATION
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.cloud_configuration = new com.starrocks.thrift.TCloudConfiguration();
              struct.cloud_configuration.read(iprot);
              struct.setCloud_configurationIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TTableFunctionTableSink struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.target_table != null) {
        if (struct.isSetTarget_table()) {
          oprot.writeFieldBegin(TARGET_TABLE_FIELD_DESC);
          struct.target_table.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.cloud_configuration != null) {
        if (struct.isSetCloud_configuration()) {
          oprot.writeFieldBegin(CLOUD_CONFIGURATION_FIELD_DESC);
          struct.cloud_configuration.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TTableFunctionTableSinkTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TTableFunctionTableSinkTupleScheme getScheme() {
      return new TTableFunctionTableSinkTupleScheme();
    }
  }

  private static class TTableFunctionTableSinkTupleScheme extends org.apache.thrift.scheme.TupleScheme<TTableFunctionTableSink> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TTableFunctionTableSink struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetTarget_table()) {
        optionals.set(0);
      }
      if (struct.isSetCloud_configuration()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetTarget_table()) {
        struct.target_table.write(oprot);
      }
      if (struct.isSetCloud_configuration()) {
        struct.cloud_configuration.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TTableFunctionTableSink struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.target_table = new com.starrocks.thrift.TTableFunctionTable();
        struct.target_table.read(iprot);
        struct.setTarget_tableIsSet(true);
      }
      if (incoming.get(1)) {
        struct.cloud_configuration = new com.starrocks.thrift.TCloudConfiguration();
        struct.cloud_configuration.read(iprot);
        struct.setCloud_configurationIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

