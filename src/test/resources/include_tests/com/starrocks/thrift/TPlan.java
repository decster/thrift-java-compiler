/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TPlan implements org.apache.thrift.TBase<TPlan, TPlan._Fields>, java.io.Serializable, Cloneable, Comparable<TPlan> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TPlan");

  private static final org.apache.thrift.protocol.TField NODES_FIELD_DESC = new org.apache.thrift.protocol.TField("nodes", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TPlanStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TPlanTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<TPlanNode> nodes; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NODES((short)1, "nodes");

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
        case 1: // NODES
          return NODES;
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
    tmpMap.put(_Fields.NODES, new org.apache.thrift.meta_data.FieldMetaData("nodes", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TPlanNode.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TPlan.class, metaDataMap);
  }

  public TPlan() {
  }

  public TPlan(
    java.util.List<TPlanNode> nodes)
  {
    this();
    this.nodes = nodes;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TPlan(TPlan other) {
    if (other.isSetNodes()) {
      java.util.List<TPlanNode> __this__nodes = new java.util.ArrayList<TPlanNode>(other.nodes.size());
      for (TPlanNode other_element : other.nodes) {
        __this__nodes.add(new TPlanNode(other_element));
      }
      this.nodes = __this__nodes;
    }
  }

  @Override
  public TPlan deepCopy() {
    return new TPlan(this);
  }

  @Override
  public void clear() {
    this.nodes = null;
  }

  public int getNodesSize() {
    return (this.nodes == null) ? 0 : this.nodes.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<TPlanNode> getNodesIterator() {
    return (this.nodes == null) ? null : this.nodes.iterator();
  }

  public void addToNodes(TPlanNode elem) {
    if (this.nodes == null) {
      this.nodes = new java.util.ArrayList<TPlanNode>();
    }
    this.nodes.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<TPlanNode> getNodes() {
    return this.nodes;
  }

  public TPlan setNodes(@org.apache.thrift.annotation.Nullable java.util.List<TPlanNode> nodes) {
    this.nodes = nodes;
    return this;
  }

  public void unsetNodes() {
    this.nodes = null;
  }

  /** Returns true if field nodes is set (has been assigned a value) and false otherwise */
  public boolean isSetNodes() {
    return this.nodes != null;
  }

  public void setNodesIsSet(boolean value) {
    if (!value) {
      this.nodes = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case NODES:
      if (value == null) {
        unsetNodes();
      } else {
        setNodes((java.util.List<TPlanNode>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NODES:
      return getNodes();

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
    case NODES:
      return isSetNodes();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TPlan)
      return this.equals((TPlan)that);
    return false;
  }

  public boolean equals(TPlan that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_nodes = true && this.isSetNodes();
    boolean that_present_nodes = true && that.isSetNodes();
    if (this_present_nodes || that_present_nodes) {
      if (!(this_present_nodes && that_present_nodes))
        return false;
      if (!this.nodes.equals(that.nodes))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetNodes()) ? 131071 : 524287);
    if (isSetNodes())
      hashCode = hashCode * 8191 + nodes.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TPlan other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetNodes(), other.isSetNodes());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodes()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nodes, other.nodes);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TPlan(");
    boolean first = true;

    sb.append("nodes:");
    if (this.nodes == null) {
      sb.append("null");
    } else {
      sb.append(this.nodes);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (nodes == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'nodes' was not present! Struct: " + toString());
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

  private static class TPlanStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TPlanStandardScheme getScheme() {
      return new TPlanStandardScheme();
    }
  }

  private static class TPlanStandardScheme extends org.apache.thrift.scheme.StandardScheme<TPlan> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TPlan struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NODES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list1114 = iprot.readListBegin();
                struct.nodes = new java.util.ArrayList<TPlanNode>(_list1114.size);
                @org.apache.thrift.annotation.Nullable TPlanNode _elem1115;
                for (int _i1116 = 0; _i1116 < _list1114.size; ++_i1116)
                {
                  _elem1115 = new TPlanNode();
                  _elem1115.read(iprot);
                  struct.nodes.add(_elem1115);
                }
                iprot.readListEnd();
              }
              struct.setNodesIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TPlan struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.nodes != null) {
        oprot.writeFieldBegin(NODES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.nodes.size()));
          for (TPlanNode _iter1117 : struct.nodes)
          {
            _iter1117.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TPlanTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TPlanTupleScheme getScheme() {
      return new TPlanTupleScheme();
    }
  }

  private static class TPlanTupleScheme extends org.apache.thrift.scheme.TupleScheme<TPlan> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TPlan struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.nodes.size());
        for (TPlanNode _iter1118 : struct.nodes)
        {
          _iter1118.write(oprot);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TPlan struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list1119 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
        struct.nodes = new java.util.ArrayList<TPlanNode>(_list1119.size);
        @org.apache.thrift.annotation.Nullable TPlanNode _elem1120;
        for (int _i1121 = 0; _i1121 < _list1119.size; ++_i1121)
        {
          _elem1120 = new TPlanNode();
          _elem1120.read(iprot);
          struct.nodes.add(_elem1120);
        }
      }
      struct.setNodesIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

