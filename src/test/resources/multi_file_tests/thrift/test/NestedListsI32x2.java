/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.test;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-17")
public class NestedListsI32x2 implements org.apache.thrift.TBase<NestedListsI32x2, NestedListsI32x2._Fields>, java.io.Serializable, Cloneable, Comparable<NestedListsI32x2> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("NestedListsI32x2");

  private static final org.apache.thrift.protocol.TField INTEGERLIST_FIELD_DESC = new org.apache.thrift.protocol.TField("integerlist", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new NestedListsI32x2StandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new NestedListsI32x2TupleSchemeFactory();

  private @org.apache.thrift.annotation.Nullable java.util.List<java.util.List<java.lang.Integer>> integerlist; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    INTEGERLIST((short)1, "integerlist");

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
        case 1: // INTEGERLIST
          return INTEGERLIST;
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
    tmpMap.put(_Fields.INTEGERLIST, new org.apache.thrift.meta_data.FieldMetaData("integerlist", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(NestedListsI32x2.class, metaDataMap);
  }

  public NestedListsI32x2() {
  }

  public NestedListsI32x2(
    java.util.List<java.util.List<java.lang.Integer>> integerlist)
  {
    this();
    this.integerlist = integerlist;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public NestedListsI32x2(NestedListsI32x2 other) {
    if (other.isSetIntegerlist()) {
      java.util.List<java.util.List<java.lang.Integer>> __this__integerlist = new java.util.ArrayList<java.util.List<java.lang.Integer>>(other.integerlist.size());
      for (java.util.List<java.lang.Integer> other_element : other.integerlist) {
        java.util.List<java.lang.Integer> __this__integerlist_copy = new java.util.ArrayList<java.lang.Integer>(other_element);
        __this__integerlist.add(__this__integerlist_copy);
      }
      this.integerlist = __this__integerlist;
    }
  }

  @Override
  public NestedListsI32x2 deepCopy() {
    return new NestedListsI32x2(this);
  }

  @Override
  public void clear() {
    this.integerlist = null;
  }

  public int getIntegerlistSize() {
    return (this.integerlist == null) ? 0 : this.integerlist.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<java.util.List<java.lang.Integer>> getIntegerlistIterator() {
    return (this.integerlist == null) ? null : this.integerlist.iterator();
  }

  public void addToIntegerlist(java.util.List<java.lang.Integer> elem) {
    if (this.integerlist == null) {
      this.integerlist = new java.util.ArrayList<java.util.List<java.lang.Integer>>();
    }
    this.integerlist.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<java.util.List<java.lang.Integer>> getIntegerlist() {
    return this.integerlist;
  }

  public void setIntegerlist(@org.apache.thrift.annotation.Nullable java.util.List<java.util.List<java.lang.Integer>> integerlist) {
    this.integerlist = integerlist;
  }

  public void unsetIntegerlist() {
    this.integerlist = null;
  }

  /** Returns true if field integerlist is set (has been assigned a value) and false otherwise */
  public boolean isSetIntegerlist() {
    return this.integerlist != null;
  }

  public void setIntegerlistIsSet(boolean value) {
    if (!value) {
      this.integerlist = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case INTEGERLIST:
      if (value == null) {
        unsetIntegerlist();
      } else {
        setIntegerlist((java.util.List<java.util.List<java.lang.Integer>>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case INTEGERLIST:
      return getIntegerlist();

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
    case INTEGERLIST:
      return isSetIntegerlist();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof NestedListsI32x2)
      return this.equals((NestedListsI32x2)that);
    return false;
  }

  public boolean equals(NestedListsI32x2 that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_integerlist = true && this.isSetIntegerlist();
    boolean that_present_integerlist = true && that.isSetIntegerlist();
    if (this_present_integerlist || that_present_integerlist) {
      if (!(this_present_integerlist && that_present_integerlist))
        return false;
      if (!this.integerlist.equals(that.integerlist))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetIntegerlist()) ? 131071 : 524287);
    if (isSetIntegerlist())
      hashCode = hashCode * 8191 + integerlist.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(NestedListsI32x2 other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetIntegerlist(), other.isSetIntegerlist());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIntegerlist()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.integerlist, other.integerlist);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("NestedListsI32x2(");
    boolean first = true;

    sb.append("integerlist:");
    if (this.integerlist == null) {
      sb.append("null");
    } else {
      sb.append(this.integerlist);
    }
    first = false;
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

  private static class NestedListsI32x2StandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public NestedListsI32x2StandardScheme getScheme() {
      return new NestedListsI32x2StandardScheme();
    }
  }

  private static class NestedListsI32x2StandardScheme extends org.apache.thrift.scheme.StandardScheme<NestedListsI32x2> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, NestedListsI32x2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // INTEGERLIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list166 = iprot.readListBegin();
                struct.integerlist = new java.util.ArrayList<java.util.List<java.lang.Integer>>(_list166.size);
                @org.apache.thrift.annotation.Nullable java.util.List<java.lang.Integer> _elem167;
                for (int _i168 = 0; _i168 < _list166.size; ++_i168)
                {
                  {
                    org.apache.thrift.protocol.TList _list169 = iprot.readListBegin();
                    _elem167 = new java.util.ArrayList<java.lang.Integer>(_list169.size);
                    int _elem170;
                    for (int _i171 = 0; _i171 < _list169.size; ++_i171)
                    {
                      _elem170 = iprot.readI32();
                      _elem167.add(_elem170);
                    }
                    iprot.readListEnd();
                  }
                  struct.integerlist.add(_elem167);
                }
                iprot.readListEnd();
              }
              struct.setIntegerlistIsSet(true);
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
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, NestedListsI32x2 struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.integerlist != null) {
        oprot.writeFieldBegin(INTEGERLIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.LIST, struct.integerlist.size()));
          for (java.util.List<java.lang.Integer> _iter172 : struct.integerlist)
          {
            {
              oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, _iter172.size()));
              for (int _iter173 : _iter172)
              {
                oprot.writeI32(_iter173);
              }
              oprot.writeListEnd();
            }
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class NestedListsI32x2TupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public NestedListsI32x2TupleScheme getScheme() {
      return new NestedListsI32x2TupleScheme();
    }
  }

  private static class NestedListsI32x2TupleScheme extends org.apache.thrift.scheme.TupleScheme<NestedListsI32x2> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, NestedListsI32x2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetIntegerlist()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetIntegerlist()) {
        {
          oprot.writeI32(struct.integerlist.size());
          for (java.util.List<java.lang.Integer> _iter174 : struct.integerlist)
          {
            {
              oprot.writeI32(_iter174.size());
              for (int _iter175 : _iter174)
              {
                oprot.writeI32(_iter175);
              }
            }
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, NestedListsI32x2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list176 = iprot.readListBegin(org.apache.thrift.protocol.TType.LIST);
          struct.integerlist = new java.util.ArrayList<java.util.List<java.lang.Integer>>(_list176.size);
          @org.apache.thrift.annotation.Nullable java.util.List<java.lang.Integer> _elem177;
          for (int _i178 = 0; _i178 < _list176.size; ++_i178)
          {
            {
              org.apache.thrift.protocol.TList _list179 = iprot.readListBegin(org.apache.thrift.protocol.TType.I32);
              _elem177 = new java.util.ArrayList<java.lang.Integer>(_list179.size);
              int _elem180;
              for (int _i181 = 0; _i181 < _list179.size; ++_i181)
              {
                _elem180 = iprot.readI32();
                _elem177.add(_elem180);
              }
            }
            struct.integerlist.add(_elem177);
          }
        }
        struct.setIntegerlistIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

