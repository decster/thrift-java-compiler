/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.test;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-17")
public class ListTypeVersioningV2 implements org.apache.thrift.TBase<ListTypeVersioningV2, ListTypeVersioningV2._Fields>, java.io.Serializable, Cloneable, Comparable<ListTypeVersioningV2> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ListTypeVersioningV2");

  private static final org.apache.thrift.protocol.TField STRINGS_FIELD_DESC = new org.apache.thrift.protocol.TField("strings", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField HELLO_FIELD_DESC = new org.apache.thrift.protocol.TField("hello", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new ListTypeVersioningV2StandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new ListTypeVersioningV2TupleSchemeFactory();

  private @org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> strings; // required
  private @org.apache.thrift.annotation.Nullable java.lang.String hello; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STRINGS((short)1, "strings"),
    HELLO((short)2, "hello");

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
        case 1: // STRINGS
          return STRINGS;
        case 2: // HELLO
          return HELLO;
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
    tmpMap.put(_Fields.STRINGS, new org.apache.thrift.meta_data.FieldMetaData("strings", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.HELLO, new org.apache.thrift.meta_data.FieldMetaData("hello", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ListTypeVersioningV2.class, metaDataMap);
  }

  public ListTypeVersioningV2() {
  }

  public ListTypeVersioningV2(
    java.util.List<java.lang.String> strings,
    java.lang.String hello)
  {
    this();
    this.strings = strings;
    this.hello = hello;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ListTypeVersioningV2(ListTypeVersioningV2 other) {
    if (other.isSetStrings()) {
      java.util.List<java.lang.String> __this__strings = new java.util.ArrayList<java.lang.String>(other.strings);
      this.strings = __this__strings;
    }
    if (other.isSetHello()) {
      this.hello = other.hello;
    }
  }

  @Override
  public ListTypeVersioningV2 deepCopy() {
    return new ListTypeVersioningV2(this);
  }

  @Override
  public void clear() {
    this.strings = null;
    this.hello = null;
  }

  public int getStringsSize() {
    return (this.strings == null) ? 0 : this.strings.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<java.lang.String> getStringsIterator() {
    return (this.strings == null) ? null : this.strings.iterator();
  }

  public void addToStrings(java.lang.String elem) {
    if (this.strings == null) {
      this.strings = new java.util.ArrayList<java.lang.String>();
    }
    this.strings.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<java.lang.String> getStrings() {
    return this.strings;
  }

  public void setStrings(@org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> strings) {
    this.strings = strings;
  }

  public void unsetStrings() {
    this.strings = null;
  }

  /** Returns true if field strings is set (has been assigned a value) and false otherwise */
  public boolean isSetStrings() {
    return this.strings != null;
  }

  public void setStringsIsSet(boolean value) {
    if (!value) {
      this.strings = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getHello() {
    return this.hello;
  }

  public void setHello(@org.apache.thrift.annotation.Nullable java.lang.String hello) {
    this.hello = hello;
  }

  public void unsetHello() {
    this.hello = null;
  }

  /** Returns true if field hello is set (has been assigned a value) and false otherwise */
  public boolean isSetHello() {
    return this.hello != null;
  }

  public void setHelloIsSet(boolean value) {
    if (!value) {
      this.hello = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case STRINGS:
      if (value == null) {
        unsetStrings();
      } else {
        setStrings((java.util.List<java.lang.String>)value);
      }
      break;

    case HELLO:
      if (value == null) {
        unsetHello();
      } else {
        setHello((java.lang.String)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STRINGS:
      return getStrings();

    case HELLO:
      return getHello();

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
    case STRINGS:
      return isSetStrings();
    case HELLO:
      return isSetHello();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof ListTypeVersioningV2)
      return this.equals((ListTypeVersioningV2)that);
    return false;
  }

  public boolean equals(ListTypeVersioningV2 that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_strings = true && this.isSetStrings();
    boolean that_present_strings = true && that.isSetStrings();
    if (this_present_strings || that_present_strings) {
      if (!(this_present_strings && that_present_strings))
        return false;
      if (!this.strings.equals(that.strings))
        return false;
    }

    boolean this_present_hello = true && this.isSetHello();
    boolean that_present_hello = true && that.isSetHello();
    if (this_present_hello || that_present_hello) {
      if (!(this_present_hello && that_present_hello))
        return false;
      if (!this.hello.equals(that.hello))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStrings()) ? 131071 : 524287);
    if (isSetStrings())
      hashCode = hashCode * 8191 + strings.hashCode();

    hashCode = hashCode * 8191 + ((isSetHello()) ? 131071 : 524287);
    if (isSetHello())
      hashCode = hashCode * 8191 + hello.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(ListTypeVersioningV2 other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetStrings(), other.isSetStrings());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStrings()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.strings, other.strings);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetHello(), other.isSetHello());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHello()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.hello, other.hello);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("ListTypeVersioningV2(");
    boolean first = true;

    sb.append("strings:");
    if (this.strings == null) {
      sb.append("null");
    } else {
      sb.append(this.strings);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("hello:");
    if (this.hello == null) {
      sb.append("null");
    } else {
      sb.append(this.hello);
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

  private static class ListTypeVersioningV2StandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public ListTypeVersioningV2StandardScheme getScheme() {
      return new ListTypeVersioningV2StandardScheme();
    }
  }

  private static class ListTypeVersioningV2StandardScheme extends org.apache.thrift.scheme.StandardScheme<ListTypeVersioningV2> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, ListTypeVersioningV2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STRINGS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list132 = iprot.readListBegin();
                struct.strings = new java.util.ArrayList<java.lang.String>(_list132.size);
                @org.apache.thrift.annotation.Nullable java.lang.String _elem133;
                for (int _i134 = 0; _i134 < _list132.size; ++_i134)
                {
                  _elem133 = iprot.readString();
                  struct.strings.add(_elem133);
                }
                iprot.readListEnd();
              }
              struct.setStringsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // HELLO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.hello = iprot.readString();
              struct.setHelloIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, ListTypeVersioningV2 struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.strings != null) {
        oprot.writeFieldBegin(STRINGS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.strings.size()));
          for (java.lang.String _iter135 : struct.strings)
          {
            oprot.writeString(_iter135);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.hello != null) {
        oprot.writeFieldBegin(HELLO_FIELD_DESC);
        oprot.writeString(struct.hello);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ListTypeVersioningV2TupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public ListTypeVersioningV2TupleScheme getScheme() {
      return new ListTypeVersioningV2TupleScheme();
    }
  }

  private static class ListTypeVersioningV2TupleScheme extends org.apache.thrift.scheme.TupleScheme<ListTypeVersioningV2> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ListTypeVersioningV2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetStrings()) {
        optionals.set(0);
      }
      if (struct.isSetHello()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetStrings()) {
        {
          oprot.writeI32(struct.strings.size());
          for (java.lang.String _iter136 : struct.strings)
          {
            oprot.writeString(_iter136);
          }
        }
      }
      if (struct.isSetHello()) {
        oprot.writeString(struct.hello);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ListTypeVersioningV2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list137 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRING);
          struct.strings = new java.util.ArrayList<java.lang.String>(_list137.size);
          @org.apache.thrift.annotation.Nullable java.lang.String _elem138;
          for (int _i139 = 0; _i139 < _list137.size; ++_i139)
          {
            _elem138 = iprot.readString();
            struct.strings.add(_elem138);
          }
        }
        struct.setStringsIsSet(true);
      }
      if (incoming.get(1)) {
        struct.hello = iprot.readString();
        struct.setHelloIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

