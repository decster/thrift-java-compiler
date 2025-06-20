/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TExplainResult implements org.apache.thrift.TBase<TExplainResult, TExplainResult._Fields>, java.io.Serializable, Cloneable, Comparable<TExplainResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TExplainResult");

  private static final org.apache.thrift.protocol.TField RESULTS_FIELD_DESC = new org.apache.thrift.protocol.TField("results", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TExplainResultStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TExplainResultTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<com.starrocks.thrift.TResultRow> results; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RESULTS((short)1, "results");

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
        case 1: // RESULTS
          return RESULTS;
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
    tmpMap.put(_Fields.RESULTS, new org.apache.thrift.meta_data.FieldMetaData("results", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TResultRow.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TExplainResult.class, metaDataMap);
  }

  public TExplainResult() {
  }

  public TExplainResult(
    java.util.List<com.starrocks.thrift.TResultRow> results)
  {
    this();
    this.results = results;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TExplainResult(TExplainResult other) {
    if (other.isSetResults()) {
      java.util.List<com.starrocks.thrift.TResultRow> __this__results = new java.util.ArrayList<com.starrocks.thrift.TResultRow>(other.results.size());
      for (com.starrocks.thrift.TResultRow other_element : other.results) {
        __this__results.add(new com.starrocks.thrift.TResultRow(other_element));
      }
      this.results = __this__results;
    }
  }

  @Override
  public TExplainResult deepCopy() {
    return new TExplainResult(this);
  }

  @Override
  public void clear() {
    this.results = null;
  }

  public int getResultsSize() {
    return (this.results == null) ? 0 : this.results.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<com.starrocks.thrift.TResultRow> getResultsIterator() {
    return (this.results == null) ? null : this.results.iterator();
  }

  public void addToResults(com.starrocks.thrift.TResultRow elem) {
    if (this.results == null) {
      this.results = new java.util.ArrayList<com.starrocks.thrift.TResultRow>();
    }
    this.results.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<com.starrocks.thrift.TResultRow> getResults() {
    return this.results;
  }

  public TExplainResult setResults(@org.apache.thrift.annotation.Nullable java.util.List<com.starrocks.thrift.TResultRow> results) {
    this.results = results;
    return this;
  }

  public void unsetResults() {
    this.results = null;
  }

  /** Returns true if field results is set (has been assigned a value) and false otherwise */
  public boolean isSetResults() {
    return this.results != null;
  }

  public void setResultsIsSet(boolean value) {
    if (!value) {
      this.results = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case RESULTS:
      if (value == null) {
        unsetResults();
      } else {
        setResults((java.util.List<com.starrocks.thrift.TResultRow>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case RESULTS:
      return getResults();

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
    case RESULTS:
      return isSetResults();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TExplainResult)
      return this.equals((TExplainResult)that);
    return false;
  }

  public boolean equals(TExplainResult that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_results = true && this.isSetResults();
    boolean that_present_results = true && that.isSetResults();
    if (this_present_results || that_present_results) {
      if (!(this_present_results && that_present_results))
        return false;
      if (!this.results.equals(that.results))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetResults()) ? 131071 : 524287);
    if (isSetResults())
      hashCode = hashCode * 8191 + results.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TExplainResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetResults(), other.isSetResults());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResults()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.results, other.results);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TExplainResult(");
    boolean first = true;

    sb.append("results:");
    if (this.results == null) {
      sb.append("null");
    } else {
      sb.append(this.results);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (results == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'results' was not present! Struct: " + toString());
    }
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

  private static class TExplainResultStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TExplainResultStandardScheme getScheme() {
      return new TExplainResultStandardScheme();
    }
  }

  private static class TExplainResultStandardScheme extends org.apache.thrift.scheme.StandardScheme<TExplainResult> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TExplainResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RESULTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list84 = iprot.readListBegin();
                struct.results = new java.util.ArrayList<com.starrocks.thrift.TResultRow>(_list84.size);
                @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TResultRow _elem85;
                for (int _i86 = 0; _i86 < _list84.size; ++_i86)
                {
                  _elem85 = new com.starrocks.thrift.TResultRow();
                  _elem85.read(iprot);
                  struct.results.add(_elem85);
                }
                iprot.readListEnd();
              }
              struct.setResultsIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TExplainResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.results != null) {
        oprot.writeFieldBegin(RESULTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.results.size()));
          for (com.starrocks.thrift.TResultRow _iter87 : struct.results)
          {
            _iter87.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TExplainResultTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TExplainResultTupleScheme getScheme() {
      return new TExplainResultTupleScheme();
    }
  }

  private static class TExplainResultTupleScheme extends org.apache.thrift.scheme.TupleScheme<TExplainResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TExplainResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.results.size());
        for (com.starrocks.thrift.TResultRow _iter88 : struct.results)
        {
          _iter88.write(oprot);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TExplainResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list89 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
        struct.results = new java.util.ArrayList<com.starrocks.thrift.TResultRow>(_list89.size);
        @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TResultRow _elem90;
        for (int _i91 = 0; _i91 < _list89.size; ++_i91)
        {
          _elem90 = new com.starrocks.thrift.TResultRow();
          _elem90.read(iprot);
          struct.results.add(_elem90);
        }
      }
      struct.setResultsIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

