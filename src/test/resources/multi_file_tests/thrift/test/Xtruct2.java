/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.test;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-06")
public class Xtruct2 implements org.apache.thrift.TBase<Xtruct2, Xtruct2._Fields>, java.io.Serializable, Cloneable, Comparable<Xtruct2> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Xtruct2");

  private static final org.apache.thrift.protocol.TField BYTE_THING_FIELD_DESC = new org.apache.thrift.protocol.TField("byte_thing", org.apache.thrift.protocol.TType.BYTE, (short)1);
  private static final org.apache.thrift.protocol.TField STRUCT_THING_FIELD_DESC = new org.apache.thrift.protocol.TField("struct_thing", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField I32_THING_FIELD_DESC = new org.apache.thrift.protocol.TField("i32_thing", org.apache.thrift.protocol.TType.I32, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new Xtruct2StandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new Xtruct2TupleSchemeFactory();

  public byte byte_thing; // required
  public @org.apache.thrift.annotation.Nullable Xtruct struct_thing; // required
  public int i32_thing; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    BYTE_THING((short)1, "byte_thing"),
    STRUCT_THING((short)2, "struct_thing"),
    I32_THING((short)3, "i32_thing");

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
        case 1: // BYTE_THING
          return BYTE_THING;
        case 2: // STRUCT_THING
          return STRUCT_THING;
        case 3: // I32_THING
          return I32_THING;
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
  private static final int __BYTE_THING_ISSET_ID = 0;
  private static final int __I32_THING_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.BYTE_THING, new org.apache.thrift.meta_data.FieldMetaData("byte_thing", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    tmpMap.put(_Fields.STRUCT_THING, new org.apache.thrift.meta_data.FieldMetaData("struct_thing", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Xtruct.class)));
    tmpMap.put(_Fields.I32_THING, new org.apache.thrift.meta_data.FieldMetaData("i32_thing", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Xtruct2.class, metaDataMap);
  }

  public Xtruct2() {
  }

  public Xtruct2(
    byte byte_thing,
    Xtruct struct_thing,
    int i32_thing)
  {
    this();
    this.byte_thing = byte_thing;
    setByte_thingIsSet(true);
    this.struct_thing = struct_thing;
    this.i32_thing = i32_thing;
    setI32_thingIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Xtruct2(Xtruct2 other) {
    __isset_bitfield = other.__isset_bitfield;
    this.byte_thing = other.byte_thing;
    if (other.isSetStruct_thing()) {
      this.struct_thing = new Xtruct(other.struct_thing);
    }
    this.i32_thing = other.i32_thing;
  }

  @Override
  public Xtruct2 deepCopy() {
    return new Xtruct2(this);
  }

  @Override
  public void clear() {
    setByte_thingIsSet(false);
    this.byte_thing = 0;
    this.struct_thing = null;
    setI32_thingIsSet(false);
    this.i32_thing = 0;
  }

  public byte getByte_thing() {
    return this.byte_thing;
  }

  public Xtruct2 setByte_thing(byte byte_thing) {
    this.byte_thing = byte_thing;
    setByte_thingIsSet(true);
    return this;
  }

  public void unsetByte_thing() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __BYTE_THING_ISSET_ID);
  }

  /** Returns true if field byte_thing is set (has been assigned a value) and false otherwise */
  public boolean isSetByte_thing() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __BYTE_THING_ISSET_ID);
  }

  public void setByte_thingIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __BYTE_THING_ISSET_ID, value);
  }

  @org.apache.thrift.annotation.Nullable
  public Xtruct getStruct_thing() {
    return this.struct_thing;
  }

  public Xtruct2 setStruct_thing(@org.apache.thrift.annotation.Nullable Xtruct struct_thing) {
    this.struct_thing = struct_thing;
    return this;
  }

  public void unsetStruct_thing() {
    this.struct_thing = null;
  }

  /** Returns true if field struct_thing is set (has been assigned a value) and false otherwise */
  public boolean isSetStruct_thing() {
    return this.struct_thing != null;
  }

  public void setStruct_thingIsSet(boolean value) {
    if (!value) {
      this.struct_thing = null;
    }
  }

  public int getI32_thing() {
    return this.i32_thing;
  }

  public Xtruct2 setI32_thing(int i32_thing) {
    this.i32_thing = i32_thing;
    setI32_thingIsSet(true);
    return this;
  }

  public void unsetI32_thing() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __I32_THING_ISSET_ID);
  }

  /** Returns true if field i32_thing is set (has been assigned a value) and false otherwise */
  public boolean isSetI32_thing() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __I32_THING_ISSET_ID);
  }

  public void setI32_thingIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __I32_THING_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case BYTE_THING:
      if (value == null) {
        unsetByte_thing();
      } else {
        setByte_thing((java.lang.Byte)value);
      }
      break;

    case STRUCT_THING:
      if (value == null) {
        unsetStruct_thing();
      } else {
        setStruct_thing((Xtruct)value);
      }
      break;

    case I32_THING:
      if (value == null) {
        unsetI32_thing();
      } else {
        setI32_thing((java.lang.Integer)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case BYTE_THING:
      return getByte_thing();

    case STRUCT_THING:
      return getStruct_thing();

    case I32_THING:
      return getI32_thing();

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
    case BYTE_THING:
      return isSetByte_thing();
    case STRUCT_THING:
      return isSetStruct_thing();
    case I32_THING:
      return isSetI32_thing();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof Xtruct2)
      return this.equals((Xtruct2)that);
    return false;
  }

  public boolean equals(Xtruct2 that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_byte_thing = true;
    boolean that_present_byte_thing = true;
    if (this_present_byte_thing || that_present_byte_thing) {
      if (!(this_present_byte_thing && that_present_byte_thing))
        return false;
      if (this.byte_thing != that.byte_thing)
        return false;
    }

    boolean this_present_struct_thing = true && this.isSetStruct_thing();
    boolean that_present_struct_thing = true && that.isSetStruct_thing();
    if (this_present_struct_thing || that_present_struct_thing) {
      if (!(this_present_struct_thing && that_present_struct_thing))
        return false;
      if (!this.struct_thing.equals(that.struct_thing))
        return false;
    }

    boolean this_present_i32_thing = true;
    boolean that_present_i32_thing = true;
    if (this_present_i32_thing || that_present_i32_thing) {
      if (!(this_present_i32_thing && that_present_i32_thing))
        return false;
      if (this.i32_thing != that.i32_thing)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + (int) (byte_thing);

    hashCode = hashCode * 8191 + ((isSetStruct_thing()) ? 131071 : 524287);
    if (isSetStruct_thing())
      hashCode = hashCode * 8191 + struct_thing.hashCode();

    hashCode = hashCode * 8191 + i32_thing;

    return hashCode;
  }

  @Override
  public int compareTo(Xtruct2 other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetByte_thing(), other.isSetByte_thing());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetByte_thing()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.byte_thing, other.byte_thing);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetStruct_thing(), other.isSetStruct_thing());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStruct_thing()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.struct_thing, other.struct_thing);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetI32_thing(), other.isSetI32_thing());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetI32_thing()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.i32_thing, other.i32_thing);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Xtruct2(");
    boolean first = true;

    sb.append("byte_thing:");
    sb.append(this.byte_thing);
    first = false;
    if (!first) sb.append(", ");
    sb.append("struct_thing:");
    if (this.struct_thing == null) {
      sb.append("null");
    } else {
      sb.append(this.struct_thing);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("i32_thing:");
    sb.append(this.i32_thing);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (struct_thing != null) {
      struct_thing.validate();
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class Xtruct2StandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public Xtruct2StandardScheme getScheme() {
      return new Xtruct2StandardScheme();
    }
  }

  private static class Xtruct2StandardScheme extends org.apache.thrift.scheme.StandardScheme<Xtruct2> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, Xtruct2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // BYTE_THING
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.byte_thing = iprot.readByte();
              struct.setByte_thingIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // STRUCT_THING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.struct_thing = new Xtruct();
              struct.struct_thing.read(iprot);
              struct.setStruct_thingIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // I32_THING
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.i32_thing = iprot.readI32();
              struct.setI32_thingIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, Xtruct2 struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(BYTE_THING_FIELD_DESC);
      oprot.writeByte(struct.byte_thing);
      oprot.writeFieldEnd();
      if (struct.struct_thing != null) {
        oprot.writeFieldBegin(STRUCT_THING_FIELD_DESC);
        struct.struct_thing.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(I32_THING_FIELD_DESC);
      oprot.writeI32(struct.i32_thing);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class Xtruct2TupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public Xtruct2TupleScheme getScheme() {
      return new Xtruct2TupleScheme();
    }
  }

  private static class Xtruct2TupleScheme extends org.apache.thrift.scheme.TupleScheme<Xtruct2> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Xtruct2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetByte_thing()) {
        optionals.set(0);
      }
      if (struct.isSetStruct_thing()) {
        optionals.set(1);
      }
      if (struct.isSetI32_thing()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetByte_thing()) {
        oprot.writeByte(struct.byte_thing);
      }
      if (struct.isSetStruct_thing()) {
        struct.struct_thing.write(oprot);
      }
      if (struct.isSetI32_thing()) {
        oprot.writeI32(struct.i32_thing);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Xtruct2 struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.byte_thing = iprot.readByte();
        struct.setByte_thingIsSet(true);
      }
      if (incoming.get(1)) {
        struct.struct_thing = new Xtruct();
        struct.struct_thing.read(iprot);
        struct.setStruct_thingIsSet(true);
      }
      if (incoming.get(2)) {
        struct.i32_thing = iprot.readI32();
        struct.setI32_thingIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

