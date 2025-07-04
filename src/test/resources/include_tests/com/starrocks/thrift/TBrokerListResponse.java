/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TBrokerListResponse implements org.apache.thrift.TBase<TBrokerListResponse, TBrokerListResponse._Fields>, java.io.Serializable, Cloneable, Comparable<TBrokerListResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TBrokerListResponse");

  private static final org.apache.thrift.protocol.TField OP_STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("opStatus", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField FILES_FIELD_DESC = new org.apache.thrift.protocol.TField("files", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TBrokerListResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TBrokerListResponseTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable TBrokerOperationStatus opStatus; // required
  public @org.apache.thrift.annotation.Nullable java.util.List<TBrokerFileStatus> files; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    OP_STATUS((short)1, "opStatus"),
    FILES((short)2, "files");

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
        case 2: // FILES
          return FILES;
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
  private static final _Fields optionals[] = {_Fields.FILES};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.OP_STATUS, new org.apache.thrift.meta_data.FieldMetaData("opStatus", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TBrokerOperationStatus.class)));
    tmpMap.put(_Fields.FILES, new org.apache.thrift.meta_data.FieldMetaData("files", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TBrokerFileStatus.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TBrokerListResponse.class, metaDataMap);
  }

  public TBrokerListResponse() {
  }

  public TBrokerListResponse(
    TBrokerOperationStatus opStatus)
  {
    this();
    this.opStatus = opStatus;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TBrokerListResponse(TBrokerListResponse other) {
    if (other.isSetOpStatus()) {
      this.opStatus = new TBrokerOperationStatus(other.opStatus);
    }
    if (other.isSetFiles()) {
      java.util.List<TBrokerFileStatus> __this__files = new java.util.ArrayList<TBrokerFileStatus>(other.files.size());
      for (TBrokerFileStatus other_element : other.files) {
        __this__files.add(new TBrokerFileStatus(other_element));
      }
      this.files = __this__files;
    }
  }

  @Override
  public TBrokerListResponse deepCopy() {
    return new TBrokerListResponse(this);
  }

  @Override
  public void clear() {
    this.opStatus = null;
    this.files = null;
  }

  @org.apache.thrift.annotation.Nullable
  public TBrokerOperationStatus getOpStatus() {
    return this.opStatus;
  }

  public TBrokerListResponse setOpStatus(@org.apache.thrift.annotation.Nullable TBrokerOperationStatus opStatus) {
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

  public int getFilesSize() {
    return (this.files == null) ? 0 : this.files.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<TBrokerFileStatus> getFilesIterator() {
    return (this.files == null) ? null : this.files.iterator();
  }

  public void addToFiles(TBrokerFileStatus elem) {
    if (this.files == null) {
      this.files = new java.util.ArrayList<TBrokerFileStatus>();
    }
    this.files.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<TBrokerFileStatus> getFiles() {
    return this.files;
  }

  public TBrokerListResponse setFiles(@org.apache.thrift.annotation.Nullable java.util.List<TBrokerFileStatus> files) {
    this.files = files;
    return this;
  }

  public void unsetFiles() {
    this.files = null;
  }

  /** Returns true if field files is set (has been assigned a value) and false otherwise */
  public boolean isSetFiles() {
    return this.files != null;
  }

  public void setFilesIsSet(boolean value) {
    if (!value) {
      this.files = null;
    }
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

    case FILES:
      if (value == null) {
        unsetFiles();
      } else {
        setFiles((java.util.List<TBrokerFileStatus>)value);
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

    case FILES:
      return getFiles();

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
    case FILES:
      return isSetFiles();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TBrokerListResponse)
      return this.equals((TBrokerListResponse)that);
    return false;
  }

  public boolean equals(TBrokerListResponse that) {
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

    boolean this_present_files = true && this.isSetFiles();
    boolean that_present_files = true && that.isSetFiles();
    if (this_present_files || that_present_files) {
      if (!(this_present_files && that_present_files))
        return false;
      if (!this.files.equals(that.files))
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

    hashCode = hashCode * 8191 + ((isSetFiles()) ? 131071 : 524287);
    if (isSetFiles())
      hashCode = hashCode * 8191 + files.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TBrokerListResponse other) {
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
    lastComparison = java.lang.Boolean.compare(isSetFiles(), other.isSetFiles());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFiles()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.files, other.files);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TBrokerListResponse(");
    boolean first = true;

    sb.append("opStatus:");
    if (this.opStatus == null) {
      sb.append("null");
    } else {
      sb.append(this.opStatus);
    }
    first = false;
    if (isSetFiles()) {
      if (!first) sb.append(", ");
      sb.append("files:");
      if (this.files == null) {
        sb.append("null");
      } else {
        sb.append(this.files);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (opStatus == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'opStatus' was not present! Struct: " + toString());
    }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TBrokerListResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TBrokerListResponseStandardScheme getScheme() {
      return new TBrokerListResponseStandardScheme();
    }
  }

  private static class TBrokerListResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<TBrokerListResponse> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TBrokerListResponse struct) throws org.apache.thrift.TException {
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
          case 2: // FILES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.files = new java.util.ArrayList<TBrokerFileStatus>(_list0.size);
                @org.apache.thrift.annotation.Nullable TBrokerFileStatus _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = new TBrokerFileStatus();
                  _elem1.read(iprot);
                  struct.files.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setFilesIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TBrokerListResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.opStatus != null) {
        oprot.writeFieldBegin(OP_STATUS_FIELD_DESC);
        struct.opStatus.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.files != null) {
        if (struct.isSetFiles()) {
          oprot.writeFieldBegin(FILES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.files.size()));
            for (TBrokerFileStatus _iter3 : struct.files)
            {
              _iter3.write(oprot);
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

  private static class TBrokerListResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TBrokerListResponseTupleScheme getScheme() {
      return new TBrokerListResponseTupleScheme();
    }
  }

  private static class TBrokerListResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<TBrokerListResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TBrokerListResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.opStatus.write(oprot);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetFiles()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetFiles()) {
        {
          oprot.writeI32(struct.files.size());
          for (TBrokerFileStatus _iter4 : struct.files)
          {
            _iter4.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TBrokerListResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.opStatus = new TBrokerOperationStatus();
      struct.opStatus.read(iprot);
      struct.setOpStatusIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list5 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
          struct.files = new java.util.ArrayList<TBrokerFileStatus>(_list5.size);
          @org.apache.thrift.annotation.Nullable TBrokerFileStatus _elem6;
          for (int _i7 = 0; _i7 < _list5.size; ++_i7)
          {
            _elem6 = new TBrokerFileStatus();
            _elem6.read(iprot);
            struct.files.add(_elem6);
          }
        }
        struct.setFilesIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

