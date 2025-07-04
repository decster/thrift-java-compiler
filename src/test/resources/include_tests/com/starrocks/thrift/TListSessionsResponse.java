/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TListSessionsResponse implements org.apache.thrift.TBase<TListSessionsResponse, TListSessionsResponse._Fields>, java.io.Serializable, Cloneable, Comparable<TListSessionsResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TListSessionsResponse");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField SESSIONS_FIELD_DESC = new org.apache.thrift.protocol.TField("sessions", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TListSessionsResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TListSessionsResponseTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TStatus status; // optional
  public @org.apache.thrift.annotation.Nullable java.util.List<TSessionInfo> sessions; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATUS((short)1, "status"),
    SESSIONS((short)2, "sessions");

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
        case 1: // STATUS
          return STATUS;
        case 2: // SESSIONS
          return SESSIONS;
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
  private static final _Fields optionals[] = {_Fields.STATUS,_Fields.SESSIONS};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TStatus.class)));
    tmpMap.put(_Fields.SESSIONS, new org.apache.thrift.meta_data.FieldMetaData("sessions", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TSessionInfo.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TListSessionsResponse.class, metaDataMap);
  }

  public TListSessionsResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TListSessionsResponse(TListSessionsResponse other) {
    if (other.isSetStatus()) {
      this.status = new com.starrocks.thrift.TStatus(other.status);
    }
    if (other.isSetSessions()) {
      java.util.List<TSessionInfo> __this__sessions = new java.util.ArrayList<TSessionInfo>(other.sessions.size());
      for (TSessionInfo other_element : other.sessions) {
        __this__sessions.add(new TSessionInfo(other_element));
      }
      this.sessions = __this__sessions;
    }
  }

  @Override
  public TListSessionsResponse deepCopy() {
    return new TListSessionsResponse(this);
  }

  @Override
  public void clear() {
    this.status = null;
    this.sessions = null;
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TStatus getStatus() {
    return this.status;
  }

  public TListSessionsResponse setStatus(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TStatus status) {
    this.status = status;
    return this;
  }

  public void unsetStatus() {
    this.status = null;
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return this.status != null;
  }

  public void setStatusIsSet(boolean value) {
    if (!value) {
      this.status = null;
    }
  }

  public int getSessionsSize() {
    return (this.sessions == null) ? 0 : this.sessions.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<TSessionInfo> getSessionsIterator() {
    return (this.sessions == null) ? null : this.sessions.iterator();
  }

  public void addToSessions(TSessionInfo elem) {
    if (this.sessions == null) {
      this.sessions = new java.util.ArrayList<TSessionInfo>();
    }
    this.sessions.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<TSessionInfo> getSessions() {
    return this.sessions;
  }

  public TListSessionsResponse setSessions(@org.apache.thrift.annotation.Nullable java.util.List<TSessionInfo> sessions) {
    this.sessions = sessions;
    return this;
  }

  public void unsetSessions() {
    this.sessions = null;
  }

  /** Returns true if field sessions is set (has been assigned a value) and false otherwise */
  public boolean isSetSessions() {
    return this.sessions != null;
  }

  public void setSessionsIsSet(boolean value) {
    if (!value) {
      this.sessions = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((com.starrocks.thrift.TStatus)value);
      }
      break;

    case SESSIONS:
      if (value == null) {
        unsetSessions();
      } else {
        setSessions((java.util.List<TSessionInfo>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return getStatus();

    case SESSIONS:
      return getSessions();

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
    case STATUS:
      return isSetStatus();
    case SESSIONS:
      return isSetSessions();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TListSessionsResponse)
      return this.equals((TListSessionsResponse)that);
    return false;
  }

  public boolean equals(TListSessionsResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_status = true && this.isSetStatus();
    boolean that_present_status = true && that.isSetStatus();
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (!this.status.equals(that.status))
        return false;
    }

    boolean this_present_sessions = true && this.isSetSessions();
    boolean that_present_sessions = true && that.isSetSessions();
    if (this_present_sessions || that_present_sessions) {
      if (!(this_present_sessions && that_present_sessions))
        return false;
      if (!this.sessions.equals(that.sessions))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStatus()) ? 131071 : 524287);
    if (isSetStatus())
      hashCode = hashCode * 8191 + status.hashCode();

    hashCode = hashCode * 8191 + ((isSetSessions()) ? 131071 : 524287);
    if (isSetSessions())
      hashCode = hashCode * 8191 + sessions.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TListSessionsResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetStatus(), other.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, other.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetSessions(), other.isSetSessions());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSessions()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sessions, other.sessions);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TListSessionsResponse(");
    boolean first = true;

    if (isSetStatus()) {
      sb.append("status:");
      if (this.status == null) {
        sb.append("null");
      } else {
        sb.append(this.status);
      }
      first = false;
    }
    if (isSetSessions()) {
      if (!first) sb.append(", ");
      sb.append("sessions:");
      if (this.sessions == null) {
        sb.append("null");
      } else {
        sb.append(this.sessions);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (status != null) {
      status.validate();
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

  private static class TListSessionsResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TListSessionsResponseStandardScheme getScheme() {
      return new TListSessionsResponseStandardScheme();
    }
  }

  private static class TListSessionsResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<TListSessionsResponse> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TListSessionsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.status = new com.starrocks.thrift.TStatus();
              struct.status.read(iprot);
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SESSIONS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list854 = iprot.readListBegin();
                struct.sessions = new java.util.ArrayList<TSessionInfo>(_list854.size);
                @org.apache.thrift.annotation.Nullable TSessionInfo _elem855;
                for (int _i856 = 0; _i856 < _list854.size; ++_i856)
                {
                  _elem855 = new TSessionInfo();
                  _elem855.read(iprot);
                  struct.sessions.add(_elem855);
                }
                iprot.readListEnd();
              }
              struct.setSessionsIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TListSessionsResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.status != null) {
        if (struct.isSetStatus()) {
          oprot.writeFieldBegin(STATUS_FIELD_DESC);
          struct.status.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.sessions != null) {
        if (struct.isSetSessions()) {
          oprot.writeFieldBegin(SESSIONS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.sessions.size()));
            for (TSessionInfo _iter857 : struct.sessions)
            {
              _iter857.write(oprot);
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

  private static class TListSessionsResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TListSessionsResponseTupleScheme getScheme() {
      return new TListSessionsResponseTupleScheme();
    }
  }

  private static class TListSessionsResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<TListSessionsResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TListSessionsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetStatus()) {
        optionals.set(0);
      }
      if (struct.isSetSessions()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetStatus()) {
        struct.status.write(oprot);
      }
      if (struct.isSetSessions()) {
        {
          oprot.writeI32(struct.sessions.size());
          for (TSessionInfo _iter858 : struct.sessions)
          {
            _iter858.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TListSessionsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.status = new com.starrocks.thrift.TStatus();
        struct.status.read(iprot);
        struct.setStatusIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list859 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
          struct.sessions = new java.util.ArrayList<TSessionInfo>(_list859.size);
          @org.apache.thrift.annotation.Nullable TSessionInfo _elem860;
          for (int _i861 = 0; _i861 < _list859.size; ++_i861)
          {
            _elem860 = new TSessionInfo();
            _elem860.read(iprot);
            struct.sessions.add(_elem860);
          }
        }
        struct.setSessionsIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

