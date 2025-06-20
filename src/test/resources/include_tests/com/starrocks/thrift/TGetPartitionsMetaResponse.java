/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TGetPartitionsMetaResponse implements org.apache.thrift.TBase<TGetPartitionsMetaResponse, TGetPartitionsMetaResponse._Fields>, java.io.Serializable, Cloneable, Comparable<TGetPartitionsMetaResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TGetPartitionsMetaResponse");

  private static final org.apache.thrift.protocol.TField PARTITIONS_META_INFOS_FIELD_DESC = new org.apache.thrift.protocol.TField("partitions_meta_infos", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField NEXT_TABLE_ID_OFFSET_FIELD_DESC = new org.apache.thrift.protocol.TField("next_table_id_offset", org.apache.thrift.protocol.TType.I64, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TGetPartitionsMetaResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TGetPartitionsMetaResponseTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<TPartitionMetaInfo> partitions_meta_infos; // optional
  public long next_table_id_offset; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PARTITIONS_META_INFOS((short)1, "partitions_meta_infos"),
    NEXT_TABLE_ID_OFFSET((short)2, "next_table_id_offset");

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
        case 1: // PARTITIONS_META_INFOS
          return PARTITIONS_META_INFOS;
        case 2: // NEXT_TABLE_ID_OFFSET
          return NEXT_TABLE_ID_OFFSET;
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
  private static final int __NEXT_TABLE_ID_OFFSET_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.PARTITIONS_META_INFOS,_Fields.NEXT_TABLE_ID_OFFSET};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PARTITIONS_META_INFOS, new org.apache.thrift.meta_data.FieldMetaData("partitions_meta_infos", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TPartitionMetaInfo.class))));
    tmpMap.put(_Fields.NEXT_TABLE_ID_OFFSET, new org.apache.thrift.meta_data.FieldMetaData("next_table_id_offset", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TGetPartitionsMetaResponse.class, metaDataMap);
  }

  public TGetPartitionsMetaResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TGetPartitionsMetaResponse(TGetPartitionsMetaResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetPartitions_meta_infos()) {
      java.util.List<TPartitionMetaInfo> __this__partitions_meta_infos = new java.util.ArrayList<TPartitionMetaInfo>(other.partitions_meta_infos.size());
      for (TPartitionMetaInfo other_element : other.partitions_meta_infos) {
        __this__partitions_meta_infos.add(new TPartitionMetaInfo(other_element));
      }
      this.partitions_meta_infos = __this__partitions_meta_infos;
    }
    this.next_table_id_offset = other.next_table_id_offset;
  }

  @Override
  public TGetPartitionsMetaResponse deepCopy() {
    return new TGetPartitionsMetaResponse(this);
  }

  @Override
  public void clear() {
    this.partitions_meta_infos = null;
    setNext_table_id_offsetIsSet(false);
    this.next_table_id_offset = 0;
  }

  public int getPartitions_meta_infosSize() {
    return (this.partitions_meta_infos == null) ? 0 : this.partitions_meta_infos.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<TPartitionMetaInfo> getPartitions_meta_infosIterator() {
    return (this.partitions_meta_infos == null) ? null : this.partitions_meta_infos.iterator();
  }

  public void addToPartitions_meta_infos(TPartitionMetaInfo elem) {
    if (this.partitions_meta_infos == null) {
      this.partitions_meta_infos = new java.util.ArrayList<TPartitionMetaInfo>();
    }
    this.partitions_meta_infos.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<TPartitionMetaInfo> getPartitions_meta_infos() {
    return this.partitions_meta_infos;
  }

  public TGetPartitionsMetaResponse setPartitions_meta_infos(@org.apache.thrift.annotation.Nullable java.util.List<TPartitionMetaInfo> partitions_meta_infos) {
    this.partitions_meta_infos = partitions_meta_infos;
    return this;
  }

  public void unsetPartitions_meta_infos() {
    this.partitions_meta_infos = null;
  }

  /** Returns true if field partitions_meta_infos is set (has been assigned a value) and false otherwise */
  public boolean isSetPartitions_meta_infos() {
    return this.partitions_meta_infos != null;
  }

  public void setPartitions_meta_infosIsSet(boolean value) {
    if (!value) {
      this.partitions_meta_infos = null;
    }
  }

  public long getNext_table_id_offset() {
    return this.next_table_id_offset;
  }

  public TGetPartitionsMetaResponse setNext_table_id_offset(long next_table_id_offset) {
    this.next_table_id_offset = next_table_id_offset;
    setNext_table_id_offsetIsSet(true);
    return this;
  }

  public void unsetNext_table_id_offset() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __NEXT_TABLE_ID_OFFSET_ISSET_ID);
  }

  /** Returns true if field next_table_id_offset is set (has been assigned a value) and false otherwise */
  public boolean isSetNext_table_id_offset() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __NEXT_TABLE_ID_OFFSET_ISSET_ID);
  }

  public void setNext_table_id_offsetIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __NEXT_TABLE_ID_OFFSET_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case PARTITIONS_META_INFOS:
      if (value == null) {
        unsetPartitions_meta_infos();
      } else {
        setPartitions_meta_infos((java.util.List<TPartitionMetaInfo>)value);
      }
      break;

    case NEXT_TABLE_ID_OFFSET:
      if (value == null) {
        unsetNext_table_id_offset();
      } else {
        setNext_table_id_offset((java.lang.Long)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case PARTITIONS_META_INFOS:
      return getPartitions_meta_infos();

    case NEXT_TABLE_ID_OFFSET:
      return getNext_table_id_offset();

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
    case PARTITIONS_META_INFOS:
      return isSetPartitions_meta_infos();
    case NEXT_TABLE_ID_OFFSET:
      return isSetNext_table_id_offset();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TGetPartitionsMetaResponse)
      return this.equals((TGetPartitionsMetaResponse)that);
    return false;
  }

  public boolean equals(TGetPartitionsMetaResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_partitions_meta_infos = true && this.isSetPartitions_meta_infos();
    boolean that_present_partitions_meta_infos = true && that.isSetPartitions_meta_infos();
    if (this_present_partitions_meta_infos || that_present_partitions_meta_infos) {
      if (!(this_present_partitions_meta_infos && that_present_partitions_meta_infos))
        return false;
      if (!this.partitions_meta_infos.equals(that.partitions_meta_infos))
        return false;
    }

    boolean this_present_next_table_id_offset = true && this.isSetNext_table_id_offset();
    boolean that_present_next_table_id_offset = true && that.isSetNext_table_id_offset();
    if (this_present_next_table_id_offset || that_present_next_table_id_offset) {
      if (!(this_present_next_table_id_offset && that_present_next_table_id_offset))
        return false;
      if (this.next_table_id_offset != that.next_table_id_offset)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetPartitions_meta_infos()) ? 131071 : 524287);
    if (isSetPartitions_meta_infos())
      hashCode = hashCode * 8191 + partitions_meta_infos.hashCode();

    hashCode = hashCode * 8191 + ((isSetNext_table_id_offset()) ? 131071 : 524287);
    if (isSetNext_table_id_offset())
      hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(next_table_id_offset);

    return hashCode;
  }

  @Override
  public int compareTo(TGetPartitionsMetaResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetPartitions_meta_infos(), other.isSetPartitions_meta_infos());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartitions_meta_infos()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partitions_meta_infos, other.partitions_meta_infos);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetNext_table_id_offset(), other.isSetNext_table_id_offset());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNext_table_id_offset()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.next_table_id_offset, other.next_table_id_offset);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TGetPartitionsMetaResponse(");
    boolean first = true;

    if (isSetPartitions_meta_infos()) {
      sb.append("partitions_meta_infos:");
      if (this.partitions_meta_infos == null) {
        sb.append("null");
      } else {
        sb.append(this.partitions_meta_infos);
      }
      first = false;
    }
    if (isSetNext_table_id_offset()) {
      if (!first) sb.append(", ");
      sb.append("next_table_id_offset:");
      sb.append(this.next_table_id_offset);
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TGetPartitionsMetaResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TGetPartitionsMetaResponseStandardScheme getScheme() {
      return new TGetPartitionsMetaResponseStandardScheme();
    }
  }

  private static class TGetPartitionsMetaResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<TGetPartitionsMetaResponse> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TGetPartitionsMetaResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PARTITIONS_META_INFOS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list688 = iprot.readListBegin();
                struct.partitions_meta_infos = new java.util.ArrayList<TPartitionMetaInfo>(_list688.size);
                @org.apache.thrift.annotation.Nullable TPartitionMetaInfo _elem689;
                for (int _i690 = 0; _i690 < _list688.size; ++_i690)
                {
                  _elem689 = new TPartitionMetaInfo();
                  _elem689.read(iprot);
                  struct.partitions_meta_infos.add(_elem689);
                }
                iprot.readListEnd();
              }
              struct.setPartitions_meta_infosIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // NEXT_TABLE_ID_OFFSET
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.next_table_id_offset = iprot.readI64();
              struct.setNext_table_id_offsetIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TGetPartitionsMetaResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.partitions_meta_infos != null) {
        if (struct.isSetPartitions_meta_infos()) {
          oprot.writeFieldBegin(PARTITIONS_META_INFOS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.partitions_meta_infos.size()));
            for (TPartitionMetaInfo _iter691 : struct.partitions_meta_infos)
            {
              _iter691.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetNext_table_id_offset()) {
        oprot.writeFieldBegin(NEXT_TABLE_ID_OFFSET_FIELD_DESC);
        oprot.writeI64(struct.next_table_id_offset);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TGetPartitionsMetaResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TGetPartitionsMetaResponseTupleScheme getScheme() {
      return new TGetPartitionsMetaResponseTupleScheme();
    }
  }

  private static class TGetPartitionsMetaResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<TGetPartitionsMetaResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TGetPartitionsMetaResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetPartitions_meta_infos()) {
        optionals.set(0);
      }
      if (struct.isSetNext_table_id_offset()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetPartitions_meta_infos()) {
        {
          oprot.writeI32(struct.partitions_meta_infos.size());
          for (TPartitionMetaInfo _iter692 : struct.partitions_meta_infos)
          {
            _iter692.write(oprot);
          }
        }
      }
      if (struct.isSetNext_table_id_offset()) {
        oprot.writeI64(struct.next_table_id_offset);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TGetPartitionsMetaResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list693 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
          struct.partitions_meta_infos = new java.util.ArrayList<TPartitionMetaInfo>(_list693.size);
          @org.apache.thrift.annotation.Nullable TPartitionMetaInfo _elem694;
          for (int _i695 = 0; _i695 < _list693.size; ++_i695)
          {
            _elem694 = new TPartitionMetaInfo();
            _elem694.read(iprot);
            struct.partitions_meta_infos.add(_elem694);
          }
        }
        struct.setPartitions_meta_infosIsSet(true);
      }
      if (incoming.get(1)) {
        struct.next_table_id_offset = iprot.readI64();
        struct.setNext_table_id_offsetIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

