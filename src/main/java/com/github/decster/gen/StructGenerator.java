package com.github.decster.gen;

import com.github.decster.ast.*;
import org.apache.thrift.meta_data.FieldMetaData;
import org.apache.thrift.meta_data.FieldValueMetaData;
import org.apache.thrift.meta_data.StructMetaData;
import org.apache.thrift.meta_data.ListMetaData;
import org.apache.thrift.meta_data.SetMetaData;
import org.apache.thrift.meta_data.MapMetaData;
import org.apache.thrift.meta_data.EnumMetaData;
import org.apache.thrift.protocol.TType;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Locale;


public class StructGenerator {
    private final StructNode structNode;
    private final String packageName;
    private final String date;
    private final Set<String> imports = new HashSet<>();

    public StructGenerator(StructNode structNode, String packageName, String date) {
        this.structNode = structNode;
        this.packageName = packageName;
        this.date = date;
    }

    private static class JavaTypeResolution {
        String javaType;
        byte thriftType;
        String fieldValueMetaData;
        Set<String> imports = new HashSet<>();

        JavaTypeResolution(String javaType, byte thriftType, String fieldValueMetaData) {
            this.javaType = javaType;
            this.thriftType = thriftType;
            this.fieldValueMetaData = fieldValueMetaData;
        }

        void addImport(String imp) {
            imports.add(imp);
        }
    }

    private JavaTypeResolution resolveType(TypeNode typeNode) {
        if (typeNode instanceof BaseTypeNode) {
            BaseTypeNode baseType = (BaseTypeNode) typeNode;
            switch (baseType.getType()) {
                case BOOL:
                    return new JavaTypeResolution("boolean", TType.BOOL, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)");
                case BYTE:
                    return new JavaTypeResolution("byte", TType.BYTE, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)");
                case I16:
                    return new JavaTypeResolution("short", TType.I16, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)");
                case I32:
                    return new JavaTypeResolution("int", TType.I32, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)");
                case I64:
                    return new JavaTypeResolution("long", TType.I64, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)");
                case DOUBLE:
                    return new JavaTypeResolution("double", TType.DOUBLE, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)");
                case STRING:
                    JavaTypeResolution strRes = new JavaTypeResolution("java.lang.String", TType.STRING, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)");
                    strRes.addImport("java.lang.String");
                    return strRes;
                case BINARY:
                    JavaTypeResolution binRes = new JavaTypeResolution("java.nio.ByteBuffer", TType.STRING, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING, true)"); // True for binary
                    binRes.addImport("java.nio.ByteBuffer");
                    return binRes;
                default:
                    throw new IllegalArgumentException("Unsupported base type: " + baseType.getType());
            }
        } else if (typeNode instanceof IdentifierTypeNode) {
            IdentifierTypeNode idType = (IdentifierTypeNode) typeNode;
            // Assuming it's a struct or enum defined elsewhere.
            // For enums, TType is ENUM. For structs, TType is STRUCT.
            // This part needs more context to differentiate (e.g. looking up the definition of idType.getName())
            // For now, let's assume STRUCT for simplicity if not a known enum.
            // We'd need a way to know if idType.getName() refers to an Enum.
            // For now, we'll default to STRUCT for unknown identifiers.
            // A proper implementation would require access to all defined types.
            String typeName = idType.getName();
            JavaTypeResolution idRes = new JavaTypeResolution(typeName, TType.STRUCT, "new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, " + typeName + ".class)");
            // Potentially add import for typeName if not in same package - complex.
            return idRes;
        } else if (typeNode instanceof ListTypeNode) {
            ListTypeNode listType = (ListTypeNode) typeNode;
            JavaTypeResolution elementTypeRes = resolveType(listType.getElementType());
            imports.addAll(elementTypeRes.imports);
            imports.add("java.util.List");
            String javaType = "java.util.List<" + elementTypeRes.javaType + ">";
            String metaData = "new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, " + elementTypeRes.fieldValueMetaData + ")";
            JavaTypeResolution listRes = new JavaTypeResolution(javaType, TType.LIST, metaData);
            listRes.imports.addAll(elementTypeRes.imports);
            listRes.addImport("java.util.List");
            return listRes;
        } else if (typeNode instanceof SetTypeNode) {
            SetTypeNode setType = (SetTypeNode) typeNode;
            JavaTypeResolution elementTypeRes = resolveType(setType.getElementType());
            imports.addAll(elementTypeRes.imports);
            imports.add("java.util.Set");
            String javaType = "java.util.Set<" + elementTypeRes.javaType + ">";
            String metaData = "new org.apache.thrift.meta_data.SetMetaData(org.apache.thrift.protocol.TType.SET, " + elementTypeRes.fieldValueMetaData + ")";
            JavaTypeResolution setRes = new JavaTypeResolution(javaType, TType.SET, metaData);
            setRes.imports.addAll(elementTypeRes.imports);
            setRes.addImport("java.util.Set");
            return setRes;
        } else if (typeNode instanceof MapTypeNode) {
            MapTypeNode mapType = (MapTypeNode) typeNode;
            JavaTypeResolution keyTypeRes = resolveType(mapType.getKeyType());
            JavaTypeResolution valueTypeRes = resolveType(mapType.getValueType());
            imports.addAll(keyTypeRes.imports);
            imports.addAll(valueTypeRes.imports);
            imports.add("java.util.Map");
            String javaType = "java.util.Map<" + keyTypeRes.javaType + ", " + valueTypeRes.javaType + ">";
            String metaData = "new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, " + keyTypeRes.fieldValueMetaData + ", " + valueTypeRes.fieldValueMetaData + ")";
            JavaTypeResolution mapRes = new JavaTypeResolution(javaType, TType.MAP, metaData);
            mapRes.imports.addAll(keyTypeRes.imports);
            mapRes.imports.addAll(valueTypeRes.imports);
            mapRes.addImport("java.util.Map");
            return mapRes;
        }
        throw new IllegalArgumentException("Unsupported type node: " + typeNode.getClass().getName());
    }


    private String getRequirementType(FieldNode.Requirement req) {
        switch (req) {
            case REQUIRED:
                return "org.apache.thrift.TFieldRequirementType.REQUIRED";
            case OPTIONAL:
                return "org.apache.thrift.TFieldRequirementType.OPTIONAL";
            case DEFAULT:
            default:
                return "org.apache.thrift.TFieldRequirementType.DEFAULT";
        }
    }

    // Helper to convert camelCase or under_score to ALL_CAPS_UNDERSCORE
    private String toAllCapsUnderscore(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        // Convert camelCase to underscore_case first
        String underscoreCase = name.replaceAll("(?<=[a-z0-9])(?=[A-Z])", "_");
        return underscoreCase.toUpperCase(Locale.ROOT);
    }


    public String generate() {
        imports.clear();
        StringBuilder sb = new StringBuilder();
        String structName = structNode.getName();

        // Standard imports - will add more dynamically
        imports.add("java.util.Map");
        imports.add("java.util.HashMap");
        imports.add("java.util.EnumMap");
        imports.add("java.util.Set");
        imports.add("java.util.HashSet");
        imports.add("java.util.EnumSet");
        imports.add("java.util.Collections");
        imports.add("java.util.BitSet");
        imports.add("java.nio.ByteBuffer");
        imports.add("org.apache.thrift.scheme.IScheme");
        imports.add("org.apache.thrift.scheme.SchemeFactory");
        imports.add("org.apache.thrift.scheme.StandardScheme");
        imports.add("org.apache.thrift.scheme.TupleScheme");
        imports.add("org.apache.thrift.protocol.TTupleProtocol");
        imports.add("org.apache.thrift.protocol.TProtocolUtil");
        imports.add("org.apache.thrift.protocol.TProtocol");
        imports.add("org.apache.thrift.protocol.TField");
        imports.add("org.apache.thrift.protocol.TStruct");
        imports.add("org.apache.thrift.TBase");
        imports.add("org.apache.thrift.TException");
        imports.add("org.apache.thrift.TFieldIdEnum");
        imports.add("org.apache.thrift.meta_data.FieldMetaData");
        imports.add("org.apache.thrift.meta_data.FieldValueMetaData");
        imports.add("org.apache.thrift.server.AbstractNonblockingServer.*;"); // For Tuples
        imports.add("org.apache.thrift.EncodingUtils"); // For isset bit manipulation
        imports.add("org.apache.thrift.TBaseHelper");


        // Package
        sb.append("package ").append(packageName).append(";\n\n");

        // Placeholder for actual imports to be added at the end
        String importPlaceholder = "// IMPORTS_PLACEHOLDER\n";
        sb.append(importPlaceholder);

        // Class Javadoc (omitted for now)

        // Generated annotation
        sb.append("@javax.annotation.Generated(value = \"Autogenerated by Thrift Compiler (0.20.0)\", date = \"").append(date).append("\")\n");
        // SuppressWarnings
        sb.append("@SuppressWarnings({\"cast\", \"rawtypes\", \"serial\", \"unchecked\", \"unused\"})\n");
        // Class declaration
        sb.append("public class ").append(structName)
          .append(" implements org.apache.thrift.TBase<").append(structName).append(", ").append(structName).append("._Fields>, java.io.Serializable, Cloneable, Comparable<").append(structName).append("> {\n");

        // Static field descriptors
        sb.append("  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(\"").append(structName).append("\");\n\n");

        List<FieldNode> fields = structNode.getFields();
        Map<FieldNode, JavaTypeResolution> fieldResolutions = new HashMap<>();
        int bitFieldIndex = 0;

        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = resolveType(field.getType());
            fieldResolutions.put(field, typeRes);
            imports.addAll(typeRes.imports);
            sb.append("  private static final org.apache.thrift.protocol.TField ").append(toAllCapsUnderscore(field.getName())).append("_FIELD_DESC = new org.apache.thrift.protocol.TField(\"").append(field.getName()).append("\", ").append(typeRes.thriftType).append(", (short)").append(field.getId()).append(");\n");
        }
        sb.append("\n");

        // Scheme factories
        sb.append("  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();\n");
        sb.append("  static {\n");
        sb.append("    schemes.put(StandardScheme.class, new ").append(structName).append("StandardSchemeFactory());\n");
        sb.append("    schemes.put(TupleScheme.class, new ").append(structName).append("TupleSchemeFactory());\n");
        sb.append("  }\n\n");

        // Instance fields
        StringBuilder issetBitfieldDeclaration = new StringBuilder();
        List<String> issetConstants = new ArrayList<>();

        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String fieldJavaName = field.getName(); // Assuming direct mapping for now
            sb.append("  public ").append(typeRes.javaType).append(" ").append(fieldJavaName).append(";\n");
            if (isPrimitive(typeRes.javaType)) {
                if (issetBitfieldDeclaration.length() == 0) {
                    issetBitfieldDeclaration.append("  private byte __isset_bitfield = 0;\n");
                }
                issetConstants.add("  private static final int __" + fieldJavaName.toUpperCase() + "_ISSET_ID = " + bitFieldIndex++ + ";\n");
            }
        }
        sb.append("\n");
        if (issetBitfieldDeclaration.length() > 0) {
            sb.append(issetBitfieldDeclaration);
            for(String s : issetConstants) {
                sb.append(s);
            }
            sb.append("\n");
        }


        // _Fields enum
        sb.append("  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */\n");
        sb.append("  public enum _Fields implements org.apache.thrift.TFieldIdEnum {\n");
        for (int i = 0; i < fields.size(); i++) {
            FieldNode field = fields.get(i);
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            sb.append("    ").append(toAllCapsUnderscore(field.getName())).append("((short)").append(field.getId()).append(", \"").append(field.getName()).append("\")");
            if (i < fields.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append(";\n\n");
            }
        }
        sb.append("    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();\n\n");
        sb.append("    static {\n");
        sb.append("      for (_Fields field : EnumSet.allOf(_Fields.class)) {\n");
        sb.append("        byName.put(field.getFieldName(), field);\n");
        sb.append("      }\n");
        sb.append("    }\n\n");
        sb.append("    /**\n");
        sb.append("     * Find the _Fields constant that matches fieldId, or null if its not found.\n");
        sb.append("     */\n");
        sb.append("    @org.apache.thrift.annotation.Nullable\n");
        sb.append("    public static _Fields findByThriftId(int fieldId) {\n");
        sb.append("      switch(fieldId) {\n");
        for (FieldNode field : fields) {
            sb.append("        case ").append(field.getId()).append(": // ").append(toAllCapsUnderscore(field.getName())).append("\n");
            sb.append("          return ").append(toAllCapsUnderscore(field.getName())).append(";\n");
        }
        sb.append("        default:\n");
        sb.append("          return null;\n");
        sb.append("      }\n");
        sb.append("    }\n\n");
        sb.append("    /**\n");
        sb.append("     * Find the _Fields constant that matches fieldId, throwing an exception\n");
        sb.append("     * if it is not found.\n");
        sb.append("     */\n");
        sb.append("    public static _Fields findByThriftIdOrThrow(int fieldId) {\n");
        sb.append("      _Fields fields = findByThriftId(fieldId);\n");
        sb.append("      if (fields == null) throw new IllegalArgumentException(\"Field \" + fieldId + \" doesn't exist!\");\n");
        sb.append("      return fields;\n");
        sb.append("    }\n\n");
        sb.append("    /**\n");
        sb.append("     * Find the _Fields constant that matches name, or null if its not found.\n");
        sb.append("     */\n");
        sb.append("    @org.apache.thrift.annotation.Nullable\n");
        sb.append("    public static _Fields findByName(String name) {\n");
        sb.append("      return byName.get(name);\n");
        sb.append("    }\n\n");
        sb.append("    private final short _thriftId;\n");
        sb.append("    private final String _fieldName;\n\n");
        sb.append("    _Fields(short thriftId, String fieldName) {\n");
        sb.append("      _thriftId = thriftId;\n");
        sb.append("      _fieldName = fieldName;\n");
        sb.append("    }\n\n");
        sb.append("    @Override\n");
        sb.append("    public short getThriftFieldId() {\n");
        sb.append("      return _thriftId;\n");
        sb.append("    }\n\n");
        sb.append("    @Override\n");
        sb.append("    public String getFieldName() {\n");
        sb.append("      return _fieldName;\n");
        sb.append("    }\n");
        sb.append("  } // enum _Fields\n\n");

        // metaDataMap
        sb.append("  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;\n");
        sb.append("  static {\n");
        sb.append("    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String requirementType = getRequirementType(field.getRequirement());
            imports.add("org.apache.thrift.TFieldRequirementType"); // ensure this is imported
            sb.append("    tmpMap.put(_Fields.").append(toAllCapsUnderscore(field.getName())).append(", new org.apache.thrift.meta_data.FieldMetaData(\"").append(field.getName()).append("\", ").append(requirementType).append(", ").append(typeRes.fieldValueMetaData).append("));\n");
        }
        sb.append("    metaDataMap = Collections.unmodifiableMap(tmpMap);\n");
        sb.append("    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(").append(structName).append(".class, metaDataMap);\n");
        sb.append("  }\n\n");

        // Constructors
        sb.append("  public ").append(structName).append("() {\n");
        // TODO: Handle default values if specified in Thrift and field is not optional
        sb.append("  }\n\n");

        sb.append("  public ").append(structName).append("(\n");
        for (int i = 0; i < fields.size(); i++) {
            FieldNode field = fields.get(i);
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            sb.append("    ").append(typeRes.javaType).append(" ").append(field.getName());
            if (i < fields.size() - 1) {
                sb.append(",\n");
            }
        }
        sb.append(") {\n");
        sb.append("    this();\n");
        for (FieldNode field : fields) {
            sb.append("    this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n");
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            if (isPrimitive(typeRes.javaType)) {
                 sb.append("    set").append(capitalize(field.getName())).append("IsSet(true);\n");
            }
        }
        sb.append("  }\n\n");

        // Copy constructor
        sb.append("  /**\n");
        sb.append("   * Performs a deep copy on <i>other</i>.\n");
        sb.append("   */\n");
        sb.append("  public ").append(structName).append("(").append(structName).append(" other) {\n");
        if (issetBitfieldDeclaration.length() > 0) {
            sb.append("    __isset_bitfield = other.__isset_bitfield;\n");
        }
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String fieldName = field.getName();
            if (isPrimitive(typeRes.javaType) || "java.lang.String".equals(typeRes.javaType)) { // Strings are immutable
                 sb.append("    this.").append(fieldName).append(" = other.").append(fieldName).append(";\n");
            } else if ("java.nio.ByteBuffer".equals(typeRes.javaType)) {
                 sb.append("    if (other.isSet").append(capitalize(fieldName)).append("()) {\n");
                 sb.append("      this.").append(fieldName).append(" = org.apache.thrift.TBaseHelper.copyBinary(other.").append(fieldName).append(");\n");
                 sb.append("    }\n");
            } else if (typeRes.javaType.startsWith("java.util.List") || typeRes.javaType.startsWith("java.util.Set") || typeRes.javaType.startsWith("java.util.Map")) {
                 sb.append("    if (other.isSet").append(capitalize(fieldName)).append("()) {\n");
                 // This needs to be a deep copy of the container and its elements if they are mutable
                 // For now, a shallow copy of container, assuming elements are handled if they are structs
                 // Example for List: new ArrayList<>(other.listField)
                 // This part needs more detailed type inspection for full deep copy
                 String containerType = typeRes.javaType.substring(0, typeRes.javaType.indexOf('<'));
                 String genericPart = typeRes.javaType.substring(typeRes.javaType.indexOf('<'));
                 if (containerType.equals("java.util.List")) imports.add("java.util.ArrayList");
                 if (containerType.equals("java.util.Set")) imports.add("java.util.HashSet");
                 if (containerType.equals("java.util.Map")) imports.add("java.util.HashMap");

                 // Need to handle deep copy of complex elements within collections.
                 // For now, just creating new collection with same elements.
                 // If elements are structs, their copy constructors would handle deep copy.
                 sb.append("      this.").append(fieldName).append(" = new ").append(getConcreteContainerType(containerType)).append(genericPart).append("(other.").append(fieldName).append(");\n");
                 sb.append("    }\n");
            } else { // Assumed to be another TBase struct
                 sb.append("    if (other.isSet").append(capitalize(fieldName)).append("()) {\n");
                 sb.append("      this.").append(fieldName).append(" = new ").append(typeRes.javaType).append("(other.").append(fieldName).append(");\n");
                 sb.append("    }\n");
            }
        }
        sb.append("  }\n\n");

        sb.append("  @Override\n");
        sb.append("  public ").append(structName).append(" deepCopy() {\n");
        sb.append("    return new ").append(structName).append("(this);\n");
        sb.append("  }\n\n");

        // clear()
        sb.append("  @Override\n");
        sb.append("  public void clear() {\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String fieldName = field.getName();
            // Reset to default or null
            if (isPrimitive(typeRes.javaType)) {
                sb.append("    set").append(capitalize(fieldName)).append("IsSet(false);\n");
                sb.append("    this.").append(fieldName).append(" = ").append(getDefaultValueForPrimitive(typeRes.javaType)).append(";\n");
            } else {
                sb.append("    this.").append(fieldName).append(" = null;\n");
            }
        }
        sb.append("  }\n\n");


        // Getters, Setters, Unsetters, isSet
        int currentIsSetIndex = 0;
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String fieldName = field.getName();
            String capFieldName = capitalize(fieldName);
            String javaType = typeRes.javaType;

            // Getter
            sb.append("  @org.apache.thrift.annotation.Nullable\n"); // assume nullable for non-primitives
            sb.append("  public ").append(javaType).append(" get").append(capFieldName).append("() {\n");
            sb.append("    return this.").append(fieldName).append(";\n");
            sb.append("  }\n\n");

            // Setter
            sb.append("  public ").append(structName).append(" set").append(capFieldName).append("(@org.apache.thrift.annotation.Nullable ").append(javaType).append(" ").append(fieldName).append(") {\n");
            sb.append("    this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            if (isPrimitive(javaType)) {
                sb.append("    set").append(capFieldName).append("IsSet(true);\n");
            }
            sb.append("    return this;\n");
            sb.append("  }\n\n");

            // Unsetter
            sb.append("  public void unset").append(capFieldName).append("() {\n");
            if (isPrimitive(javaType)) {
                sb.append("    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __").append(fieldName.toUpperCase()).append("_ISSET_ID);\n");
            } else {
                sb.append("    this.").append(fieldName).append(" = null;\n");
            }
            sb.append("  }\n\n");

            // isSet
            sb.append("  /** Returns true if field ").append(fieldName).append(" is set (has been assigned a value) and false otherwise */\n");
            sb.append("  public boolean isSet").append(capFieldName).append("() {\n");
            if (isPrimitive(javaType)) {
                sb.append("    return EncodingUtils.testBit(__isset_bitfield, __").append(fieldName.toUpperCase()).append("_ISSET_ID);\n");
            } else {
                sb.append("    return this.").append(fieldName).append(" != null;\n");
            }
            sb.append("  }\n\n");

            // set<FieldName>IsSet (for primitives)
            if (isPrimitive(javaType)) {
                sb.append("  public void set").append(capFieldName).append("IsSet(boolean value) {\n");
                sb.append("    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __").append(fieldName.toUpperCase()).append("_ISSET_ID, value);\n");
                sb.append("  }\n\n");
            }
        }

        // setFieldValue, getFieldValue, isSet(_Fields)
        sb.append("  @Override\n");
        sb.append("  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable Object value) { /* TODO */ }\n\n");
        sb.append("  @Override\n");
        sb.append("  @org.apache.thrift.annotation.Nullable\n");
        sb.append("  public Object getFieldValue(_Fields field) { /* TODO */ return null; }\n\n");
        sb.append("  @Override\n");
        sb.append("  public boolean isSet(_Fields field) { /* TODO */ return false; }\n\n");

        // equals(Object) and equals(StructName)
        sb.append("  @Override\n");
        sb.append("  public boolean equals(Object that) {\n");
        sb.append("    if (that == null) return false;\n");
        sb.append("    if (that instanceof ").append(structName).append(")\n");
        sb.append("      return this.equals((").append(structName).append(")that);\n");
        sb.append("    return false;\n");
        sb.append("  }\n\n");

        sb.append("  public boolean equals(").append(structName).append(" that) {\n");
        sb.append("    if (that == null) return false;\n");
        sb.append("    if (this == that) return true;\n");
        // TODO: Implement actual field comparisons
        sb.append("    return true; // Placeholder\n");
        sb.append("  }\n\n");

        // hashCode()
        sb.append("  @Override\n");
        sb.append("  public int hashCode() {\n");
        sb.append("    int lastComparison = 0;\n"); // Not used in example, but good for TBaseHelper
        sb.append("    int hashCode = 1;\n\n");
        // TODO: Implement actual field hashing
        sb.append("    return hashCode;\n");
        sb.append("  }\n\n");

        // compareTo
        sb.append("  @Override\n");
        sb.append("  public int compareTo(").append(structName).append(" other) {\n");
        sb.append("    if (!getClass().equals(other.getClass())) {\n");
        sb.append("      return getClass().getName().compareTo(other.getClass().getName());\n");
        sb.append("    }\n\n");
        sb.append("    int lastComparison = 0;\n");
        // TODO: Implement actual field comparisons
        sb.append("    return 0; // Placeholder\n");
        sb.append("  }\n\n");

        // fieldForId
        sb.append("  @Override\n");
        sb.append("  @org.apache.thrift.annotation.Nullable\n");
        sb.append("  public _Fields fieldForId(int fieldId) {\n");
        sb.append("    return _Fields.findByThriftId(fieldId);\n");
        sb.append("  }\n\n");

        // read(TProtocol iprot)
        sb.append("  @Override\n");
        sb.append("  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {\n");
        sb.append("    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);\n");
        sb.append("  }\n\n");

        // write(TProtocol oprot)
        sb.append("  @Override\n");
        sb.append("  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {\n");
        sb.append("    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);\n");
        sb.append("  }\n\n");

        // toString()
        sb.append("  @Override\n");
        sb.append("  public String toString() {\n");
        sb.append("    StringBuilder sb = new StringBuilder(\"").append(structName).append("(\");\n");
        sb.append("    boolean first = true;\n");
        // TODO: Append fields
        sb.append("    sb.append(\")\");\n");
        sb.append("    return sb.toString();\n");
        sb.append("  }\n\n");

        // validate()
        sb.append("  public void validate() throws org.apache.thrift.TException {\n");
        sb.append("    // check for required fields\n");
        // TODO: Add validation logic
        sb.append("  }\n\n");

        // writeObject and readObject for serialization
        imports.add("java.io.ObjectOutputStream");
        imports.add("java.io.ObjectInputStream");
        imports.add("java.io.IOException");
        sb.append("  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {\n");
        sb.append("    try {\n");
        sb.append("      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));\n");
        imports.add("org.apache.thrift.protocol.TCompactProtocol");
        imports.add("org.apache.thrift.transport.TIOStreamTransport");
        sb.append("    } catch (org.apache.thrift.TException te) {\n");
        sb.append("      throw new java.io.IOException(te);\n");
        sb.append("    }\n");
        sb.append("  }\n\n");

        sb.append("  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {\n");
        sb.append("    try {\n");
        // Reset isset bitfield if it exists
        if (issetBitfieldDeclaration.length() > 0) {
            sb.append("      __isset_bitfield = 0;\n");
        }
        sb.append("      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));\n");
        sb.append("    } catch (org.apache.thrift.TException te) {\n");
        sb.append("      throw new java.io.IOException(te);\n");
        sb.append("    }\n");
        sb.append("  }\n\n");


        // Scheme inner classes
        sb.append(generateStandardScheme(structName, fields, fieldResolutions));
        sb.append(generateTupleScheme(structName, fields, fieldResolutions));


        sb.append("} // end of class ").append(structName).append("\n");

        // Add collected imports at the placeholder
        StringBuilder importSb = new StringBuilder();
        List<String> sortedImports = new ArrayList<>(imports);
        Collections.sort(sortedImports);
        for (String imp : sortedImports) {
            importSb.append("import ").append(imp).append(";\n");
        }
        importSb.append("\n");

        return sb.toString().replace(importPlaceholder, importSb.toString());
    }

    private boolean isPrimitive(String javaType) {
        return javaType.equals("boolean") || javaType.equals("byte") || javaType.equals("short") ||
               javaType.equals("int") || javaType.equals("long") || javaType.equals("double");
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private String getDefaultValueForPrimitive(String javaType) {
        if (javaType.equals("boolean")) return "false";
        if (javaType.equals("byte") || javaType.equals("short") || javaType.equals("int")) return "0";
        if (javaType.equals("long")) return "0L";
        if (javaType.equals("double")) return "0.0";
        return "null"; // Should not happen for primitives
    }

    private String getConcreteContainerType(String abstractType) {
        if ("java.util.List".equals(abstractType)) return "java.util.ArrayList";
        if ("java.util.Set".equals(abstractType)) return "java.util.HashSet";
        if ("java.util.Map".equals(abstractType)) return "java.util.HashMap";
        return abstractType; // fallback, though ideally should always map
    }

    private String generateStandardScheme(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder sb = new StringBuilder();
        sb.append("  private static class ").append(structName).append("StandardSchemeFactory implements SchemeFactory {\n");
        sb.append("    @Override\n");
        sb.append("    public ").append(structName).append("StandardScheme getScheme() {\n");
        sb.append("      return new ").append(structName).append("StandardScheme();\n");
        sb.append("    }\n");
        sb.append("  }\n\n");

        sb.append("  private static class ").append(structName).append("StandardScheme extends StandardScheme<").append(structName).append("> {\n");
        sb.append("    @Override\n");
        sb.append("    public void read(org.apache.thrift.protocol.TProtocol iprot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        sb.append("      iprot.readStructBegin();\n");
        sb.append("      while (true) {\n");
        sb.append("        org.apache.thrift.protocol.TField schemeField = iprot.readFieldBegin();\n");
        sb.append("        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { \n");
        sb.append("          break;\n");
        sb.append("        }\n");
        sb.append("        switch (schemeField.id) {\n");
        for(FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String fieldName = field.getName();
            sb.append("          case ").append(field.getId()).append(": // ").append(toAllCapsUnderscore(fieldName)).append("\n");
            sb.append("            if (schemeField.type == ").append(typeRes.thriftType).append(") {\n");
            // TODO: Actual reading logic for each type
            sb.append("              // struct.").append(fieldName).append(" = iprot.readX();\n");
            sb.append("              // struct.set").append(capitalize(fieldName)).append("IsSet(true);\n");
            sb.append("            } else { \n");
            sb.append("              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);\n");
            sb.append("            }\n");
            sb.append("            break;\n");
        }
        sb.append("          default:\n");
        sb.append("            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);\n");
        sb.append("        }\n");
        sb.append("        iprot.readFieldEnd();\n");
        sb.append("      }\n");
        sb.append("      iprot.readStructEnd();\n");
        sb.append("      struct.validate();\n"); // Call validate after reading
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public void write(org.apache.thrift.protocol.TProtocol oprot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        sb.append("      struct.validate();\n"); // Call validate before writing
        sb.append("      oprot.writeStructBegin(STRUCT_DESC);\n");
        // TODO: Actual writing logic for each field
        for(FieldNode field : fields) {
            // String fieldName = field.getName();
            // if (struct.isSetFieldName()) {
            // oprot.writeFieldBegin(FIELD_DESC);
            // oprot.writeX(struct.fieldName);
            // oprot.writeFieldEnd();
            // }
        }
        sb.append("      oprot.writeFieldStop();\n");
        sb.append("      oprot.writeStructEnd();\n");
        sb.append("    }\n");
        sb.append("  } // end of StandardScheme\n\n");
        return sb.toString();
    }

    private String generateTupleScheme(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder sb = new StringBuilder();
        sb.append("  private static class ").append(structName).append("TupleSchemeFactory implements SchemeFactory {\n");
        sb.append("    @Override\n");
        sb.append("    public ").append(structName).append("TupleScheme getScheme() {\n");
        sb.append("      return new ").append(structName).append("TupleScheme();\n");
        sb.append("    }\n");
        sb.append("  }\n\n");

        sb.append("  private static class ").append(structName).append("TupleScheme extends TupleScheme<").append(structName).append("> {\n");
        sb.append("    @Override\n");
        sb.append("    public void write(org.apache.thrift.protocol.TProtocol prot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        sb.append("      TTupleProtocol oprot = (TTupleProtocol) prot;\n");
        // TODO: Write actual fields to TTupleProtocol
        // BitSet optionals = new BitSet();
        // if (struct.isSetField1()) optionals.set(0);
        // oprot.writeBitSet(optionals, X); // X is number of optional fields
        // if (struct.isSetField1()) oprot.writeI32(struct.field1);
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public void read(org.apache.thrift.protocol.TProtocol prot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        sb.append("      TTupleProtocol iprot = (TTupleProtocol) prot;\n");
        // TODO: Read actual fields from TTupleProtocol
        // BitSet incoming = iprot.readBitSet(X); // X is number of optional fields
        // if (incoming.get(0)) { struct.field1 = iprot.readI32(); struct.setField1IsSet(true); }
        sb.append("    }\n");
        sb.append("  } // end of TupleScheme\n\n");
        return sb.toString();
    }

}
