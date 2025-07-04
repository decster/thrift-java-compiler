/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TGetTablesConfigResponse implements org.apache.thrift.TBase<TGetTablesConfigResponse, TGetTablesConfigResponse._Fields>, java.io.Serializable, Cloneable, Comparable<TGetTablesConfigResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TGetTablesConfigResponse");

  private static final org.apache.thrift.protocol.TField TABLES_CONFIG_INFOS_FIELD_DESC = new org.apache.thrift.protocol.TField("tables_config_infos", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TGetTablesConfigResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TGetTablesConfigResponseTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<TTableConfigInfo> tables_config_infos; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TABLES_CONFIG_INFOS((short)1, "tables_config_infos");

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
        case 1: // TABLES_CONFIG_INFOS
          return TABLES_CONFIG_INFOS;
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
  private static final _Fields optionals[] = {_Fields.TABLES_CONFIG_INFOS};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TABLES_CONFIG_INFOS, new org.apache.thrift.meta_data.FieldMetaData("tables_config_infos", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TTableConfigInfo.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TGetTablesConfigResponse.class, metaDataMap);
  }

  public TGetTablesConfigResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TGetTablesConfigResponse(TGetTablesConfigResponse other) {
    if (other.isSetTables_config_infos()) {
      java.util.List<TTableConfigInfo> __this__tables_config_infos = new java.util.ArrayList<TTableConfigInfo>(other.tables_config_infos.size());
      for (TTableConfigInfo other_element : other.tables_config_infos) {
        __this__tables_config_infos.add(new TTableConfigInfo(other_element));
      }
      this.tables_config_infos = __this__tables_config_infos;
    }
  }

  @Override
  public TGetTablesConfigResponse deepCopy() {
    return new TGetTablesConfigResponse(this);
  }

  @Override
  public void clear() {
    this.tables_config_infos = null;
  }

  public int getTables_config_infosSize() {
    return (this.tables_config_infos == null) ? 0 : this.tables_config_infos.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<TTableConfigInfo> getTables_config_infosIterator() {
    return (this.tables_config_infos == null) ? null : this.tables_config_infos.iterator();
  }

  public void addToTables_config_infos(TTableConfigInfo elem) {
    if (this.tables_config_infos == null) {
      this.tables_config_infos = new java.util.ArrayList<TTableConfigInfo>();
    }
    this.tables_config_infos.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<TTableConfigInfo> getTables_config_infos() {
    return this.tables_config_infos;
  }

  public TGetTablesConfigResponse setTables_config_infos(@org.apache.thrift.annotation.Nullable java.util.List<TTableConfigInfo> tables_config_infos) {
    this.tables_config_infos = tables_config_infos;
    return this;
  }

  public void unsetTables_config_infos() {
    this.tables_config_infos = null;
  }

  /** Returns true if field tables_config_infos is set (has been assigned a value) and false otherwise */
  public boolean isSetTables_config_infos() {
    return this.tables_config_infos != null;
  }

  public void setTables_config_infosIsSet(boolean value) {
    if (!value) {
      this.tables_config_infos = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case TABLES_CONFIG_INFOS:
      if (value == null) {
        unsetTables_config_infos();
      } else {
        setTables_config_infos((java.util.List<TTableConfigInfo>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TABLES_CONFIG_INFOS:
      return getTables_config_infos();

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
    case TABLES_CONFIG_INFOS:
      return isSetTables_config_infos();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TGetTablesConfigResponse)
      return this.equals((TGetTablesConfigResponse)that);
    return false;
  }

  public boolean equals(TGetTablesConfigResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_tables_config_infos = true && this.isSetTables_config_infos();
    boolean that_present_tables_config_infos = true && that.isSetTables_config_infos();
    if (this_present_tables_config_infos || that_present_tables_config_infos) {
      if (!(this_present_tables_config_infos && that_present_tables_config_infos))
        return false;
      if (!this.tables_config_infos.equals(that.tables_config_infos))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetTables_config_infos()) ? 131071 : 524287);
    if (isSetTables_config_infos())
      hashCode = hashCode * 8191 + tables_config_infos.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TGetTablesConfigResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetTables_config_infos(), other.isSetTables_config_infos());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTables_config_infos()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tables_config_infos, other.tables_config_infos);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TGetTablesConfigResponse(");
    boolean first = true;

    if (isSetTables_config_infos()) {
      sb.append("tables_config_infos:");
      if (this.tables_config_infos == null) {
        sb.append("null");
      } else {
        sb.append(this.tables_config_infos);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TGetTablesConfigResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TGetTablesConfigResponseStandardScheme getScheme() {
      return new TGetTablesConfigResponseStandardScheme();
    }
  }

  private static class TGetTablesConfigResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<TGetTablesConfigResponse> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TGetTablesConfigResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TABLES_CONFIG_INFOS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list680 = iprot.readListBegin();
                struct.tables_config_infos = new java.util.ArrayList<TTableConfigInfo>(_list680.size);
                @org.apache.thrift.annotation.Nullable TTableConfigInfo _elem681;
                for (int _i682 = 0; _i682 < _list680.size; ++_i682)
                {
                  _elem681 = new TTableConfigInfo();
                  _elem681.read(iprot);
                  struct.tables_config_infos.add(_elem681);
                }
                iprot.readListEnd();
              }
              struct.setTables_config_infosIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TGetTablesConfigResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.tables_config_infos != null) {
        if (struct.isSetTables_config_infos()) {
          oprot.writeFieldBegin(TABLES_CONFIG_INFOS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.tables_config_infos.size()));
            for (TTableConfigInfo _iter683 : struct.tables_config_infos)
            {
              _iter683.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TGetTablesConfigResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TGetTablesConfigResponseTupleScheme getScheme() {
      return new TGetTablesConfigResponseTupleScheme();
    }
  }

  private static class TGetTablesConfigResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<TGetTablesConfigResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TGetTablesConfigResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetTables_config_infos()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetTables_config_infos()) {
        {
          oprot.writeI32(struct.tables_config_infos.size());
          for (TTableConfigInfo _iter684 : struct.tables_config_infos)
          {
            _iter684.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TGetTablesConfigResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list685 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
          struct.tables_config_infos = new java.util.ArrayList<TTableConfigInfo>(_list685.size);
          @org.apache.thrift.annotation.Nullable TTableConfigInfo _elem686;
          for (int _i687 = 0; _i687 < _list685.size; ++_i687)
          {
            _elem686 = new TTableConfigInfo();
            _elem686.read(iprot);
            struct.tables_config_infos.add(_elem686);
          }
        }
        struct.setTables_config_infosIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

