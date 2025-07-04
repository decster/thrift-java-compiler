/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TTupleIsNullPredicate implements org.apache.thrift.TBase<TTupleIsNullPredicate, TTupleIsNullPredicate._Fields>, java.io.Serializable, Cloneable, Comparable<TTupleIsNullPredicate> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TTupleIsNullPredicate");

  private static final org.apache.thrift.protocol.TField TUPLE_IDS_FIELD_DESC = new org.apache.thrift.protocol.TField("tuple_ids", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TTupleIsNullPredicateStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TTupleIsNullPredicateTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<java.lang.Integer> tuple_ids; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TUPLE_IDS((short)1, "tuple_ids");

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
        case 1: // TUPLE_IDS
          return TUPLE_IDS;
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
    tmpMap.put(_Fields.TUPLE_IDS, new org.apache.thrift.meta_data.FieldMetaData("tuple_ids", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32            , "TTupleId"))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TTupleIsNullPredicate.class, metaDataMap);
  }

  public TTupleIsNullPredicate() {
  }

  public TTupleIsNullPredicate(
    java.util.List<java.lang.Integer> tuple_ids)
  {
    this();
    this.tuple_ids = tuple_ids;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TTupleIsNullPredicate(TTupleIsNullPredicate other) {
    if (other.isSetTuple_ids()) {
      java.util.List<java.lang.Integer> __this__tuple_ids = new java.util.ArrayList<java.lang.Integer>(other.tuple_ids.size());
      for (java.lang.Integer other_element : other.tuple_ids) {
        __this__tuple_ids.add(other_element);
      }
      this.tuple_ids = __this__tuple_ids;
    }
  }

  @Override
  public TTupleIsNullPredicate deepCopy() {
    return new TTupleIsNullPredicate(this);
  }

  @Override
  public void clear() {
    this.tuple_ids = null;
  }

  public int getTuple_idsSize() {
    return (this.tuple_ids == null) ? 0 : this.tuple_ids.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<java.lang.Integer> getTuple_idsIterator() {
    return (this.tuple_ids == null) ? null : this.tuple_ids.iterator();
  }

  public void addToTuple_ids(int elem) {
    if (this.tuple_ids == null) {
      this.tuple_ids = new java.util.ArrayList<java.lang.Integer>();
    }
    this.tuple_ids.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<java.lang.Integer> getTuple_ids() {
    return this.tuple_ids;
  }

  public TTupleIsNullPredicate setTuple_ids(@org.apache.thrift.annotation.Nullable java.util.List<java.lang.Integer> tuple_ids) {
    this.tuple_ids = tuple_ids;
    return this;
  }

  public void unsetTuple_ids() {
    this.tuple_ids = null;
  }

  /** Returns true if field tuple_ids is set (has been assigned a value) and false otherwise */
  public boolean isSetTuple_ids() {
    return this.tuple_ids != null;
  }

  public void setTuple_idsIsSet(boolean value) {
    if (!value) {
      this.tuple_ids = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case TUPLE_IDS:
      if (value == null) {
        unsetTuple_ids();
      } else {
        setTuple_ids((java.util.List<java.lang.Integer>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TUPLE_IDS:
      return getTuple_ids();

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
    case TUPLE_IDS:
      return isSetTuple_ids();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TTupleIsNullPredicate)
      return this.equals((TTupleIsNullPredicate)that);
    return false;
  }

  public boolean equals(TTupleIsNullPredicate that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_tuple_ids = true && this.isSetTuple_ids();
    boolean that_present_tuple_ids = true && that.isSetTuple_ids();
    if (this_present_tuple_ids || that_present_tuple_ids) {
      if (!(this_present_tuple_ids && that_present_tuple_ids))
        return false;
      if (!this.tuple_ids.equals(that.tuple_ids))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetTuple_ids()) ? 131071 : 524287);
    if (isSetTuple_ids())
      hashCode = hashCode * 8191 + tuple_ids.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TTupleIsNullPredicate other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetTuple_ids(), other.isSetTuple_ids());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTuple_ids()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tuple_ids, other.tuple_ids);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TTupleIsNullPredicate(");
    boolean first = true;

    sb.append("tuple_ids:");
    if (this.tuple_ids == null) {
      sb.append("null");
    } else {
      sb.append(this.tuple_ids);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (tuple_ids == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'tuple_ids' was not present! Struct: " + toString());
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

  private static class TTupleIsNullPredicateStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TTupleIsNullPredicateStandardScheme getScheme() {
      return new TTupleIsNullPredicateStandardScheme();
    }
  }

  private static class TTupleIsNullPredicateStandardScheme extends org.apache.thrift.scheme.StandardScheme<TTupleIsNullPredicate> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TTupleIsNullPredicate struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TUPLE_IDS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.tuple_ids = new java.util.ArrayList<java.lang.Integer>(_list0.size);
                int _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = iprot.readI32();
                  struct.tuple_ids.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setTuple_idsIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TTupleIsNullPredicate struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.tuple_ids != null) {
        oprot.writeFieldBegin(TUPLE_IDS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, struct.tuple_ids.size()));
          for (int _iter3 : struct.tuple_ids)
          {
            oprot.writeI32(_iter3);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TTupleIsNullPredicateTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TTupleIsNullPredicateTupleScheme getScheme() {
      return new TTupleIsNullPredicateTupleScheme();
    }
  }

  private static class TTupleIsNullPredicateTupleScheme extends org.apache.thrift.scheme.TupleScheme<TTupleIsNullPredicate> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TTupleIsNullPredicate struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.tuple_ids.size());
        for (int _iter4 : struct.tuple_ids)
        {
          oprot.writeI32(_iter4);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TTupleIsNullPredicate struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list5 = iprot.readListBegin(org.apache.thrift.protocol.TType.I32);
        struct.tuple_ids = new java.util.ArrayList<java.lang.Integer>(_list5.size);
        int _elem6;
        for (int _i7 = 0; _i7 < _list5.size; ++_i7)
        {
          _elem6 = iprot.readI32();
          struct.tuple_ids.add(_elem6);
        }
      }
      struct.setTuple_idsIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

