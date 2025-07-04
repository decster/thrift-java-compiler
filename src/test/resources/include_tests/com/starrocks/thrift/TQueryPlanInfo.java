/**
 * Autogenerated by Thrift Compiler (0.20.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.starrocks.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.20.0)", date = "2025-06-16")
public class TQueryPlanInfo implements org.apache.thrift.TBase<TQueryPlanInfo, TQueryPlanInfo._Fields>, java.io.Serializable, Cloneable, Comparable<TQueryPlanInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TQueryPlanInfo");

  private static final org.apache.thrift.protocol.TField PLAN_FRAGMENT_FIELD_DESC = new org.apache.thrift.protocol.TField("plan_fragment", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField TABLET_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("tablet_info", org.apache.thrift.protocol.TType.MAP, (short)2);
  private static final org.apache.thrift.protocol.TField DESC_TBL_FIELD_DESC = new org.apache.thrift.protocol.TField("desc_tbl", org.apache.thrift.protocol.TType.STRUCT, (short)3);
  private static final org.apache.thrift.protocol.TField QUERY_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("query_id", org.apache.thrift.protocol.TType.STRUCT, (short)4);
  private static final org.apache.thrift.protocol.TField OUTPUT_NAMES_FIELD_DESC = new org.apache.thrift.protocol.TField("output_names", org.apache.thrift.protocol.TType.LIST, (short)5);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TQueryPlanInfoStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TQueryPlanInfoTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TPlanFragment plan_fragment; // required
  public @org.apache.thrift.annotation.Nullable java.util.Map<java.lang.Long,TTabletVersionInfo> tablet_info; // required
  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TDescriptorTable desc_tbl; // required
  public @org.apache.thrift.annotation.Nullable com.starrocks.thrift.TUniqueId query_id; // required
  public @org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> output_names; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PLAN_FRAGMENT((short)1, "plan_fragment"),
    TABLET_INFO((short)2, "tablet_info"),
    DESC_TBL((short)3, "desc_tbl"),
    QUERY_ID((short)4, "query_id"),
    OUTPUT_NAMES((short)5, "output_names");

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
        case 1: // PLAN_FRAGMENT
          return PLAN_FRAGMENT;
        case 2: // TABLET_INFO
          return TABLET_INFO;
        case 3: // DESC_TBL
          return DESC_TBL;
        case 4: // QUERY_ID
          return QUERY_ID;
        case 5: // OUTPUT_NAMES
          return OUTPUT_NAMES;
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
  private static final _Fields optionals[] = {_Fields.OUTPUT_NAMES};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PLAN_FRAGMENT, new org.apache.thrift.meta_data.FieldMetaData("plan_fragment", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TPlanFragment.class)));
    tmpMap.put(_Fields.TABLET_INFO, new org.apache.thrift.meta_data.FieldMetaData("tablet_info", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64), 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TTabletVersionInfo.class))));
    tmpMap.put(_Fields.DESC_TBL, new org.apache.thrift.meta_data.FieldMetaData("desc_tbl", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TDescriptorTable.class)));
    tmpMap.put(_Fields.QUERY_ID, new org.apache.thrift.meta_data.FieldMetaData("query_id", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.starrocks.thrift.TUniqueId.class)));
    tmpMap.put(_Fields.OUTPUT_NAMES, new org.apache.thrift.meta_data.FieldMetaData("output_names", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TQueryPlanInfo.class, metaDataMap);
  }

  public TQueryPlanInfo() {
  }

  public TQueryPlanInfo(
    com.starrocks.thrift.TPlanFragment plan_fragment,
    java.util.Map<java.lang.Long,TTabletVersionInfo> tablet_info,
    com.starrocks.thrift.TDescriptorTable desc_tbl,
    com.starrocks.thrift.TUniqueId query_id)
  {
    this();
    this.plan_fragment = plan_fragment;
    this.tablet_info = tablet_info;
    this.desc_tbl = desc_tbl;
    this.query_id = query_id;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TQueryPlanInfo(TQueryPlanInfo other) {
    if (other.isSetPlan_fragment()) {
      this.plan_fragment = new com.starrocks.thrift.TPlanFragment(other.plan_fragment);
    }
    if (other.isSetTablet_info()) {
      java.util.Map<java.lang.Long,TTabletVersionInfo> __this__tablet_info = new java.util.HashMap<java.lang.Long,TTabletVersionInfo>(other.tablet_info.size());
      for (java.util.Map.Entry<java.lang.Long, TTabletVersionInfo> other_element : other.tablet_info.entrySet()) {

        java.lang.Long other_element_key = other_element.getKey();
        TTabletVersionInfo other_element_value = other_element.getValue();

        java.lang.Long __this__tablet_info_copy_key = other_element_key;

        TTabletVersionInfo __this__tablet_info_copy_value = new TTabletVersionInfo(other_element_value);

        __this__tablet_info.put(__this__tablet_info_copy_key, __this__tablet_info_copy_value);
      }
      this.tablet_info = __this__tablet_info;
    }
    if (other.isSetDesc_tbl()) {
      this.desc_tbl = new com.starrocks.thrift.TDescriptorTable(other.desc_tbl);
    }
    if (other.isSetQuery_id()) {
      this.query_id = new com.starrocks.thrift.TUniqueId(other.query_id);
    }
    if (other.isSetOutput_names()) {
      java.util.List<java.lang.String> __this__output_names = new java.util.ArrayList<java.lang.String>(other.output_names);
      this.output_names = __this__output_names;
    }
  }

  @Override
  public TQueryPlanInfo deepCopy() {
    return new TQueryPlanInfo(this);
  }

  @Override
  public void clear() {
    this.plan_fragment = null;
    this.tablet_info = null;
    this.desc_tbl = null;
    this.query_id = null;
    this.output_names = null;
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TPlanFragment getPlan_fragment() {
    return this.plan_fragment;
  }

  public TQueryPlanInfo setPlan_fragment(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TPlanFragment plan_fragment) {
    this.plan_fragment = plan_fragment;
    return this;
  }

  public void unsetPlan_fragment() {
    this.plan_fragment = null;
  }

  /** Returns true if field plan_fragment is set (has been assigned a value) and false otherwise */
  public boolean isSetPlan_fragment() {
    return this.plan_fragment != null;
  }

  public void setPlan_fragmentIsSet(boolean value) {
    if (!value) {
      this.plan_fragment = null;
    }
  }

  public int getTablet_infoSize() {
    return (this.tablet_info == null) ? 0 : this.tablet_info.size();
  }

  public void putToTablet_info(long key, TTabletVersionInfo val) {
    if (this.tablet_info == null) {
      this.tablet_info = new java.util.HashMap<java.lang.Long,TTabletVersionInfo>();
    }
    this.tablet_info.put(key, val);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Map<java.lang.Long,TTabletVersionInfo> getTablet_info() {
    return this.tablet_info;
  }

  public TQueryPlanInfo setTablet_info(@org.apache.thrift.annotation.Nullable java.util.Map<java.lang.Long,TTabletVersionInfo> tablet_info) {
    this.tablet_info = tablet_info;
    return this;
  }

  public void unsetTablet_info() {
    this.tablet_info = null;
  }

  /** Returns true if field tablet_info is set (has been assigned a value) and false otherwise */
  public boolean isSetTablet_info() {
    return this.tablet_info != null;
  }

  public void setTablet_infoIsSet(boolean value) {
    if (!value) {
      this.tablet_info = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TDescriptorTable getDesc_tbl() {
    return this.desc_tbl;
  }

  public TQueryPlanInfo setDesc_tbl(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TDescriptorTable desc_tbl) {
    this.desc_tbl = desc_tbl;
    return this;
  }

  public void unsetDesc_tbl() {
    this.desc_tbl = null;
  }

  /** Returns true if field desc_tbl is set (has been assigned a value) and false otherwise */
  public boolean isSetDesc_tbl() {
    return this.desc_tbl != null;
  }

  public void setDesc_tblIsSet(boolean value) {
    if (!value) {
      this.desc_tbl = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public com.starrocks.thrift.TUniqueId getQuery_id() {
    return this.query_id;
  }

  public TQueryPlanInfo setQuery_id(@org.apache.thrift.annotation.Nullable com.starrocks.thrift.TUniqueId query_id) {
    this.query_id = query_id;
    return this;
  }

  public void unsetQuery_id() {
    this.query_id = null;
  }

  /** Returns true if field query_id is set (has been assigned a value) and false otherwise */
  public boolean isSetQuery_id() {
    return this.query_id != null;
  }

  public void setQuery_idIsSet(boolean value) {
    if (!value) {
      this.query_id = null;
    }
  }

  public int getOutput_namesSize() {
    return (this.output_names == null) ? 0 : this.output_names.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<java.lang.String> getOutput_namesIterator() {
    return (this.output_names == null) ? null : this.output_names.iterator();
  }

  public void addToOutput_names(java.lang.String elem) {
    if (this.output_names == null) {
      this.output_names = new java.util.ArrayList<java.lang.String>();
    }
    this.output_names.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<java.lang.String> getOutput_names() {
    return this.output_names;
  }

  public TQueryPlanInfo setOutput_names(@org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> output_names) {
    this.output_names = output_names;
    return this;
  }

  public void unsetOutput_names() {
    this.output_names = null;
  }

  /** Returns true if field output_names is set (has been assigned a value) and false otherwise */
  public boolean isSetOutput_names() {
    return this.output_names != null;
  }

  public void setOutput_namesIsSet(boolean value) {
    if (!value) {
      this.output_names = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case PLAN_FRAGMENT:
      if (value == null) {
        unsetPlan_fragment();
      } else {
        setPlan_fragment((com.starrocks.thrift.TPlanFragment)value);
      }
      break;

    case TABLET_INFO:
      if (value == null) {
        unsetTablet_info();
      } else {
        setTablet_info((java.util.Map<java.lang.Long,TTabletVersionInfo>)value);
      }
      break;

    case DESC_TBL:
      if (value == null) {
        unsetDesc_tbl();
      } else {
        setDesc_tbl((com.starrocks.thrift.TDescriptorTable)value);
      }
      break;

    case QUERY_ID:
      if (value == null) {
        unsetQuery_id();
      } else {
        setQuery_id((com.starrocks.thrift.TUniqueId)value);
      }
      break;

    case OUTPUT_NAMES:
      if (value == null) {
        unsetOutput_names();
      } else {
        setOutput_names((java.util.List<java.lang.String>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case PLAN_FRAGMENT:
      return getPlan_fragment();

    case TABLET_INFO:
      return getTablet_info();

    case DESC_TBL:
      return getDesc_tbl();

    case QUERY_ID:
      return getQuery_id();

    case OUTPUT_NAMES:
      return getOutput_names();

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
    case PLAN_FRAGMENT:
      return isSetPlan_fragment();
    case TABLET_INFO:
      return isSetTablet_info();
    case DESC_TBL:
      return isSetDesc_tbl();
    case QUERY_ID:
      return isSetQuery_id();
    case OUTPUT_NAMES:
      return isSetOutput_names();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TQueryPlanInfo)
      return this.equals((TQueryPlanInfo)that);
    return false;
  }

  public boolean equals(TQueryPlanInfo that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_plan_fragment = true && this.isSetPlan_fragment();
    boolean that_present_plan_fragment = true && that.isSetPlan_fragment();
    if (this_present_plan_fragment || that_present_plan_fragment) {
      if (!(this_present_plan_fragment && that_present_plan_fragment))
        return false;
      if (!this.plan_fragment.equals(that.plan_fragment))
        return false;
    }

    boolean this_present_tablet_info = true && this.isSetTablet_info();
    boolean that_present_tablet_info = true && that.isSetTablet_info();
    if (this_present_tablet_info || that_present_tablet_info) {
      if (!(this_present_tablet_info && that_present_tablet_info))
        return false;
      if (!this.tablet_info.equals(that.tablet_info))
        return false;
    }

    boolean this_present_desc_tbl = true && this.isSetDesc_tbl();
    boolean that_present_desc_tbl = true && that.isSetDesc_tbl();
    if (this_present_desc_tbl || that_present_desc_tbl) {
      if (!(this_present_desc_tbl && that_present_desc_tbl))
        return false;
      if (!this.desc_tbl.equals(that.desc_tbl))
        return false;
    }

    boolean this_present_query_id = true && this.isSetQuery_id();
    boolean that_present_query_id = true && that.isSetQuery_id();
    if (this_present_query_id || that_present_query_id) {
      if (!(this_present_query_id && that_present_query_id))
        return false;
      if (!this.query_id.equals(that.query_id))
        return false;
    }

    boolean this_present_output_names = true && this.isSetOutput_names();
    boolean that_present_output_names = true && that.isSetOutput_names();
    if (this_present_output_names || that_present_output_names) {
      if (!(this_present_output_names && that_present_output_names))
        return false;
      if (!this.output_names.equals(that.output_names))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetPlan_fragment()) ? 131071 : 524287);
    if (isSetPlan_fragment())
      hashCode = hashCode * 8191 + plan_fragment.hashCode();

    hashCode = hashCode * 8191 + ((isSetTablet_info()) ? 131071 : 524287);
    if (isSetTablet_info())
      hashCode = hashCode * 8191 + tablet_info.hashCode();

    hashCode = hashCode * 8191 + ((isSetDesc_tbl()) ? 131071 : 524287);
    if (isSetDesc_tbl())
      hashCode = hashCode * 8191 + desc_tbl.hashCode();

    hashCode = hashCode * 8191 + ((isSetQuery_id()) ? 131071 : 524287);
    if (isSetQuery_id())
      hashCode = hashCode * 8191 + query_id.hashCode();

    hashCode = hashCode * 8191 + ((isSetOutput_names()) ? 131071 : 524287);
    if (isSetOutput_names())
      hashCode = hashCode * 8191 + output_names.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TQueryPlanInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetPlan_fragment(), other.isSetPlan_fragment());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPlan_fragment()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.plan_fragment, other.plan_fragment);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetTablet_info(), other.isSetTablet_info());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTablet_info()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tablet_info, other.tablet_info);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetDesc_tbl(), other.isSetDesc_tbl());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDesc_tbl()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.desc_tbl, other.desc_tbl);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetQuery_id(), other.isSetQuery_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetQuery_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.query_id, other.query_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetOutput_names(), other.isSetOutput_names());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOutput_names()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.output_names, other.output_names);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TQueryPlanInfo(");
    boolean first = true;

    sb.append("plan_fragment:");
    if (this.plan_fragment == null) {
      sb.append("null");
    } else {
      sb.append(this.plan_fragment);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("tablet_info:");
    if (this.tablet_info == null) {
      sb.append("null");
    } else {
      sb.append(this.tablet_info);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("desc_tbl:");
    if (this.desc_tbl == null) {
      sb.append("null");
    } else {
      sb.append(this.desc_tbl);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("query_id:");
    if (this.query_id == null) {
      sb.append("null");
    } else {
      sb.append(this.query_id);
    }
    first = false;
    if (isSetOutput_names()) {
      if (!first) sb.append(", ");
      sb.append("output_names:");
      if (this.output_names == null) {
        sb.append("null");
      } else {
        sb.append(this.output_names);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (plan_fragment == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'plan_fragment' was not present! Struct: " + toString());
    }
    if (tablet_info == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'tablet_info' was not present! Struct: " + toString());
    }
    if (desc_tbl == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'desc_tbl' was not present! Struct: " + toString());
    }
    if (query_id == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'query_id' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (plan_fragment != null) {
      plan_fragment.validate();
    }
    if (desc_tbl != null) {
      desc_tbl.validate();
    }
    if (query_id != null) {
      query_id.validate();
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

  private static class TQueryPlanInfoStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TQueryPlanInfoStandardScheme getScheme() {
      return new TQueryPlanInfoStandardScheme();
    }
  }

  private static class TQueryPlanInfoStandardScheme extends org.apache.thrift.scheme.StandardScheme<TQueryPlanInfo> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, TQueryPlanInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PLAN_FRAGMENT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.plan_fragment = new com.starrocks.thrift.TPlanFragment();
              struct.plan_fragment.read(iprot);
              struct.setPlan_fragmentIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TABLET_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
                struct.tablet_info = new java.util.HashMap<java.lang.Long,TTabletVersionInfo>(2*_map0.size);
                long _key1;
                @org.apache.thrift.annotation.Nullable TTabletVersionInfo _val2;
                for (int _i3 = 0; _i3 < _map0.size; ++_i3)
                {
                  _key1 = iprot.readI64();
                  _val2 = new TTabletVersionInfo();
                  _val2.read(iprot);
                  struct.tablet_info.put(_key1, _val2);
                }
                iprot.readMapEnd();
              }
              struct.setTablet_infoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // DESC_TBL
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.desc_tbl = new com.starrocks.thrift.TDescriptorTable();
              struct.desc_tbl.read(iprot);
              struct.setDesc_tblIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // QUERY_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.query_id = new com.starrocks.thrift.TUniqueId();
              struct.query_id.read(iprot);
              struct.setQuery_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // OUTPUT_NAMES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list4 = iprot.readListBegin();
                struct.output_names = new java.util.ArrayList<java.lang.String>(_list4.size);
                @org.apache.thrift.annotation.Nullable java.lang.String _elem5;
                for (int _i6 = 0; _i6 < _list4.size; ++_i6)
                {
                  _elem5 = iprot.readString();
                  struct.output_names.add(_elem5);
                }
                iprot.readListEnd();
              }
              struct.setOutput_namesIsSet(true);
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
    public void write(org.apache.thrift.protocol.TProtocol oprot, TQueryPlanInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.plan_fragment != null) {
        oprot.writeFieldBegin(PLAN_FRAGMENT_FIELD_DESC);
        struct.plan_fragment.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.tablet_info != null) {
        oprot.writeFieldBegin(TABLET_INFO_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.STRUCT, struct.tablet_info.size()));
          for (java.util.Map.Entry<java.lang.Long, TTabletVersionInfo> _iter7 : struct.tablet_info.entrySet())
          {
            oprot.writeI64(_iter7.getKey());
            _iter7.getValue().write(oprot);
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.desc_tbl != null) {
        oprot.writeFieldBegin(DESC_TBL_FIELD_DESC);
        struct.desc_tbl.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.query_id != null) {
        oprot.writeFieldBegin(QUERY_ID_FIELD_DESC);
        struct.query_id.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.output_names != null) {
        if (struct.isSetOutput_names()) {
          oprot.writeFieldBegin(OUTPUT_NAMES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.output_names.size()));
            for (java.lang.String _iter8 : struct.output_names)
            {
              oprot.writeString(_iter8);
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

  private static class TQueryPlanInfoTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public TQueryPlanInfoTupleScheme getScheme() {
      return new TQueryPlanInfoTupleScheme();
    }
  }

  private static class TQueryPlanInfoTupleScheme extends org.apache.thrift.scheme.TupleScheme<TQueryPlanInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TQueryPlanInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.plan_fragment.write(oprot);
      {
        oprot.writeI32(struct.tablet_info.size());
        for (java.util.Map.Entry<java.lang.Long, TTabletVersionInfo> _iter9 : struct.tablet_info.entrySet())
        {
          oprot.writeI64(_iter9.getKey());
          _iter9.getValue().write(oprot);
        }
      }
      struct.desc_tbl.write(oprot);
      struct.query_id.write(oprot);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetOutput_names()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetOutput_names()) {
        {
          oprot.writeI32(struct.output_names.size());
          for (java.lang.String _iter10 : struct.output_names)
          {
            oprot.writeString(_iter10);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TQueryPlanInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.plan_fragment = new com.starrocks.thrift.TPlanFragment();
      struct.plan_fragment.read(iprot);
      struct.setPlan_fragmentIsSet(true);
      {
        org.apache.thrift.protocol.TMap _map11 = iprot.readMapBegin(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.STRUCT); 
        struct.tablet_info = new java.util.HashMap<java.lang.Long,TTabletVersionInfo>(2*_map11.size);
        long _key12;
        @org.apache.thrift.annotation.Nullable TTabletVersionInfo _val13;
        for (int _i14 = 0; _i14 < _map11.size; ++_i14)
        {
          _key12 = iprot.readI64();
          _val13 = new TTabletVersionInfo();
          _val13.read(iprot);
          struct.tablet_info.put(_key12, _val13);
        }
      }
      struct.setTablet_infoIsSet(true);
      struct.desc_tbl = new com.starrocks.thrift.TDescriptorTable();
      struct.desc_tbl.read(iprot);
      struct.setDesc_tblIsSet(true);
      struct.query_id = new com.starrocks.thrift.TUniqueId();
      struct.query_id.read(iprot);
      struct.setQuery_idIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list15 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRING);
          struct.output_names = new java.util.ArrayList<java.lang.String>(_list15.size);
          @org.apache.thrift.annotation.Nullable java.lang.String _elem16;
          for (int _i17 = 0; _i17 < _list15.size; ++_i17)
          {
            _elem16 = iprot.readString();
            struct.output_names.add(_elem16);
          }
        }
        struct.setOutput_namesIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

