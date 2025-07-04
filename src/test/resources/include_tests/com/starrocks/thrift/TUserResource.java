/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TUserResource implements org.apache.thrift.TBase<TUserResource, TUserResource._Fields>, java.io.Serializable, Cloneable, Comparable<TUserResource> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TUserResource");

  private static final org.apache.thrift.protocol.TField RESOURCE_FIELD_DESC = new org.apache.thrift.protocol.TField("resource", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField SHARE_BY_GROUP_FIELD_DESC = new org.apache.thrift.protocol.TField("shareByGroup", org.apache.thrift.protocol.TType.MAP, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TUserResourceStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TUserResourceTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable TResourceGroup resource; // required
  public @org.apache.thrift.annotation.Nullable java.util.Map<java.lang.String,java.lang.Integer> shareByGroup; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RESOURCE((short)1, "resource"),
    SHARE_BY_GROUP((short)2, "shareByGroup");

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
        case 1: // RESOURCE
          return RESOURCE;
        case 2: // SHARE_BY_GROUP
          return SHARE_BY_GROUP;
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
    tmpMap.put(_Fields.RESOURCE, new org.apache.thrift.meta_data.FieldMetaData("resource", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TResourceGroup.class)));
    tmpMap.put(_Fields.SHARE_BY_GROUP, new org.apache.thrift.meta_data.FieldMetaData("shareByGroup", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TUserResource.class, metaDataMap);
  }

  public TUserResource() {
  }

  public TUserResource(
    TResourceGroup resource,
    java.util.Map<java.lang.String,java.lang.Integer> shareByGroup)
  {
    this();
    this.resource = resource;
    this.shareByGroup = shareByGroup;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TUserResource(TUserResource other) {
    if (other.isSetResource()) {
      this.resource = new TResourceGroup(other.resource);
    }
    if (other.isSetShareByGroup()) {
      java.util.Map<java.lang.String,java.lang.Integer> __this__shareByGroup = new java.util.HashMap<java.lang.String,java.lang.Integer>(other.shareByGroup);
      this.shareByGroup = __this__shareByGroup;
    }
  }

  @Override
  public TUserResource deepCopy() {
    return new TUserResource(this);
  }

  @Override
  public void clear() {
    this.resource = null;
    this.shareByGroup = null;
  }

  @org.apache.thrift.annotation.Nullable
  public TResourceGroup getResource() {
    return this.resource;
  }

  public TUserResource setResource(@org.apache.thrift.annotation.Nullable TResourceGroup resource) {
    this.resource = resource;
    return this;
  }

  public void unsetResource() {
    this.resource = null;
  }

  /** Returns true if field resource is set (has been assigned a value) and false otherwise */
  public boolean isSetResource() {
    return this.resource != null;
  }

  public void setResourceIsSet(boolean value) {
    if (!value) {
      this.resource = null;
    }
  }

  public int getShareByGroupSize() {
    return (this.shareByGroup == null) ? 0 : this.shareByGroup.size();
  }

  public void putToShareByGroup(java.lang.String key, int val) {
    if (this.shareByGroup == null) {
      this.shareByGroup = new java.util.HashMap<java.lang.String,java.lang.Integer>();
    }
    this.shareByGroup.put(key, val);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Map<java.lang.String,java.lang.Integer> getShareByGroup() {
    return this.shareByGroup;
  }

  public TUserResource setShareByGroup(@org.apache.thrift.annotation.Nullable java.util.Map<java.lang.String,java.lang.Integer> shareByGroup) {
    this.shareByGroup = shareByGroup;
    return this;
  }

  public void unsetShareByGroup() {
    this.shareByGroup = null;
  }

  /** Returns true if field shareByGroup is set (has been assigned a value) and false otherwise */
  public boolean isSetShareByGroup() {
    return this.shareByGroup != null;
  }

  public void setShareByGroupIsSet(boolean value) {
    if (!value) {
      this.shareByGroup = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case RESOURCE:
      if (value == null) {
        unsetResource();
      } else {
        setResource((TResourceGroup)value);
      }
      break;

    case SHARE_BY_GROUP:
      if (value == null) {
        unsetShareByGroup();
      } else {
        setShareByGroup((java.util.Map<java.lang.String,java.lang.Integer>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case RESOURCE:
      return getResource();

    case SHARE_BY_GROUP:
      return getShareByGroup();

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
    case RESOURCE:
      return isSetResource();
    case SHARE_BY_GROUP:
      return isSetShareByGroup();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TUserResource)
      return this.equals((TUserResource)that);
    return false;
  }

  public boolean equals(TUserResource that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_resource = true && this.isSetResource();
    boolean that_present_resource = true && that.isSetResource();
    if (this_present_resource || that_present_resource) {
      if (!(this_present_resource && that_present_resource))
        return false;
      if (!this.resource.equals(that.resource))
        return false;
    }

    boolean this_present_shareByGroup = true && this.isSetShareByGroup();
    boolean that_present_shareByGroup = true && that.isSetShareByGroup();
    if (this_present_shareByGroup || that_present_shareByGroup) {
      if (!(this_present_shareByGroup && that_present_shareByGroup))
        return false;
      if (!this.shareByGroup.equals(that.shareByGroup))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetResource()) ? 131071 : 524287);
    if (isSetResource())
      hashCode = hashCode * 8191 + resource.hashCode();

    hashCode = hashCode * 8191 + ((isSetShareByGroup()) ? 131071 : 524287);
    if (isSetShareByGroup())
      hashCode = hashCode * 8191 + shareByGroup.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TUserResource other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetResource(), other.isSetResource());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResource()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resource, other.resource);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetShareByGroup(), other.isSetShareByGroup());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetShareByGroup()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.shareByGroup, other.shareByGroup);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TUserResource(");
    boolean first = true;

    sb.append("resource:");
    if (this.resource == null) {
      sb.append("null");
    } else {
      sb.append(this.resource);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("shareByGroup:");
    if (this.shareByGroup == null) {
      sb.append("null");
    } else {
      sb.append(this.shareByGroup);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (resource == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'resource' was not present! Struct: " + toString());
    }
    if (shareByGroup == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'shareByGroup' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (resource != null) {
      resource.validate();
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

  private static class TUserResourceStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TUserResourceStandardScheme getScheme() {
      return new TUserResourceStandardScheme();
    }
  }

  private static class TUserResourceStandardScheme extends org.apache.thrift.scheme.StandardScheme<TUserResource> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TUserResource struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RESOURCE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.resource = new TResourceGroup();
              struct.resource.read(iprot);
              struct.setResourceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SHARE_BY_GROUP
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map154 = iprot.readMapBegin();
                struct.shareByGroup = new java.util.HashMap<java.lang.String,java.lang.Integer>(2*_map154.size);
                @org.apache.thrift.annotation.Nullable java.lang.String _key155;
                int _val156;
                for (int _i157 = 0; _i157 < _map154.size; ++_i157)
                {
                  _key155 = iprot.readString();
                  _val156 = iprot.readI32();
                  struct.shareByGroup.put(_key155, _val156);
                }
                iprot.readMapEnd();
              }
              struct.setShareByGroupIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TUserResource struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.resource != null) {
        oprot.writeFieldBegin(RESOURCE_FIELD_DESC);
        struct.resource.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.shareByGroup != null) {
        oprot.writeFieldBegin(SHARE_BY_GROUP_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.I32, struct.shareByGroup.size()));
          for (java.util.Map.Entry<java.lang.String, java.lang.Integer> _iter158 : struct.shareByGroup.entrySet())
          {
            oprot.writeString(_iter158.getKey());
            oprot.writeI32(_iter158.getValue());
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TUserResourceTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TUserResourceTupleScheme getScheme() {
      return new TUserResourceTupleScheme();
    }
  }

  private static class TUserResourceTupleScheme extends org.apache.thrift.scheme.TupleScheme<TUserResource> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TUserResource struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.resource.write(oprot);
      {
        oprot.writeI32(struct.shareByGroup.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> _iter159 : struct.shareByGroup.entrySet())
        {
          oprot.writeString(_iter159.getKey());
          oprot.writeI32(_iter159.getValue());
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TUserResource struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.resource = new TResourceGroup();
      struct.resource.read(iprot);
      struct.setResourceIsSet(true);
      {
        org.apache.thrift.protocol.TMap _map160 = iprot.readMapBegin(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.I32); 
        struct.shareByGroup = new java.util.HashMap<java.lang.String,java.lang.Integer>(2*_map160.size);
        @org.apache.thrift.annotation.Nullable java.lang.String _key161;
        int _val162;
        for (int _i163 = 0; _i163 < _map160.size; ++_i163)
        {
          _key161 = iprot.readString();
          _val162 = iprot.readI32();
          struct.shareByGroup.put(_key161, _val162);
        }
      }
      struct.setShareByGroupIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

