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
import org.apache.thrift.TFieldRequirementType;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Locale;
import java.util.BitSet;

public class StructGenerator {
    private final StructNode structNode;
    private final DocumentNode documentNode;
    private final String packageName;
    private final String date;
    private final Set<String> imports = new HashSet<>();
    private final StringBuilder sb = new StringBuilder();

    public StructGenerator(StructNode structNode, DocumentNode documentNode, String packageName, String date) {
        this.structNode = structNode;
        this.documentNode = documentNode;
        this.packageName = packageName;
        this.date = date;
    }

    private static class JavaTypeResolution {
        String javaType;
        byte thriftType;
        String fieldMetaDataType;

        JavaTypeResolution(String javaType, byte thriftType, String fieldMetaDataType) {
            this.javaType = javaType;
            this.thriftType = thriftType;
            this.fieldMetaDataType = fieldMetaDataType;
        }
    }

    private boolean isPrimitive(String javaType) {
        return javaType.equals("boolean") || javaType.equals("byte") || javaType.equals("short") ||
               javaType.equals("int") || javaType.equals("long") || javaType.equals("double");
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }

    private String toAllCapsUnderscore(String name) {
        if (name == null || name.isEmpty()) return "";
        return name.replaceAll("(?<=[a-z0-9])(?=[A-Z])", "_").toUpperCase(Locale.ROOT);
    }

     private void addImport(String importName) {
        if (importName == null || importName.isEmpty() || importName.startsWith("java.lang.") || isPrimitive(importName)) {
            return;
        }
        if (packageName != null && !packageName.isEmpty() && importName.startsWith(packageName + ".")) {
            String potentialClassSimpleName = importName.substring(packageName.length() + 1);
            if (!potentialClassSimpleName.contains(".")) {
                return;
            }
        }
        this.imports.add(importName);
    }

    private String getPackageFromQualifiedName(String qName) {
        if (qName == null || !qName.contains(".")) return null;
        return qName.substring(0, qName.lastIndexOf('.'));
    }

    private String getSimpleName(String qName) {
        if (qName == null) return qName;
        int lastDot = qName.lastIndexOf('.');
        if (lastDot == -1) {
            return qName;
        }
        return qName.substring(lastDot + 1);
    }

    private String getWrapperTypeIfPrimitive(String javaType) {
        switch (javaType) {
            case "boolean": return "java.lang.Boolean";
            case "byte":    return "java.lang.Byte";
            case "short":   return "java.lang.Short";
            case "int":     return "java.lang.Integer";
            case "long":    return "java.lang.Long";
            case "double":  return "java.lang.Double";
            default:        return javaType;
        }
    }

    private JavaTypeResolution resolveType(TypeNode typeNode) {
        if (typeNode instanceof BaseTypeNode) {
            BaseTypeNode baseTypeNode = (BaseTypeNode) typeNode;
            switch (baseTypeNode.getType()) {
                case BOOL:   return new JavaTypeResolution("boolean", TType.BOOL, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.BOOL)");
                case BYTE:   return new JavaTypeResolution("byte", TType.BYTE, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.BYTE)");
                case I16:    return new JavaTypeResolution("short", TType.I16, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.I16)");
                case I32:    return new JavaTypeResolution("int", TType.I32, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.I32)");
                case I64:    return new JavaTypeResolution("long", TType.I64, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.I64)");
                case DOUBLE: return new JavaTypeResolution("double", TType.DOUBLE, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.DOUBLE)");
                case STRING: return new JavaTypeResolution("java.lang.String", TType.STRING, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.STRING)");
                case BINARY:
                    JavaTypeResolution binRes = new JavaTypeResolution("java.nio.ByteBuffer", TType.STRING, "new org.apache.thrift.meta_data.FieldValueMetaData(TType.STRING, true)");
                    addImport("java.nio.ByteBuffer");
                    return binRes;
                default: throw new IllegalArgumentException("Unsupported base type: " + baseTypeNode.getType());
            }
        } else if (typeNode instanceof IdentifierTypeNode) {
            IdentifierTypeNode idType = (IdentifierTypeNode) typeNode;
            String typeName = idType.getName(); // This could be simple ("MyEnum") or FQN ("com.example.MyEnum")
            DefinitionNode definitionNode = null; // The actual AST definition
            String fqnOfType = typeName; // The FQN of the identifier

            // Try to find the definition and establish its FQN
            if (this.documentNode != null && this.documentNode.getDefinitions() != null) {
                for (DefinitionNode def : this.documentNode.getDefinitions()) {
                    if (def.getName().equals(typeName)) { // Direct match (could be simple or FQN)
                        definitionNode = def;
                        fqnOfType = def.getName(); // Ensure fqnOfType is from the definition
                        break;
                    }
                    // If typeName is simple, check if it matches a definition in the current package
                    if (!typeName.contains(".") && (this.packageName + "." + typeName).equals(def.getName())) {
                        definitionNode = def;
                        fqnOfType = def.getName(); // fqnOfType is now the FQN
                        break;
                    }
                }
            }
            // If definitionNode is still null here, it might be a type from an include not directly in docNode.getDefinitions()
            // or a built-in type alias (not handled yet). For now, fqnOfType remains as original typeName.

            String javaTypeForDeclaration;
            String typePackage = getPackageFromQualifiedName(fqnOfType);

            if (typePackage != null && !typePackage.isEmpty() && !typePackage.equals(this.packageName)) {
                // Different package: use FQN for declaration.
                // Import will be added if addImport() deems it necessary (it usually will for different packages).
                addImport(fqnOfType);
                javaTypeForDeclaration = fqnOfType;
            } else {
                // Same package or no package (simple name): use FQN for declaration.
                // javagen style uses FQN in field types even for same package.
                // addImport will handle not importing if fqnOfType is in the same package.
                addImport(fqnOfType);
                javaTypeForDeclaration = fqnOfType;
            }

            JavaTypeResolution idRes;
            String metaDataClassName = fqnOfType; // Metadata always uses FQN
            // Attempt to resolve definition if not found yet (e.g. fqnOfType was a simple name not in current package)
            if (definitionNode == null && this.documentNode != null && this.documentNode.getDefinitions() != null) {
                for (DefinitionNode def : this.documentNode.getDefinitions()) {
                    if (def.getName().equals(fqnOfType)) {
                        definitionNode = def;
                        break;
                    }
                }
            }

            if (definitionNode instanceof EnumNode) {
                idRes = new JavaTypeResolution(javaTypeForDeclaration, TType.ENUM, "new org.apache.thrift.meta_data.EnumMetaData(TType.ENUM, " + metaDataClassName + ".class)");
            } else {
                idRes = new JavaTypeResolution(javaTypeForDeclaration, TType.STRUCT, "new org.apache.thrift.meta_data.StructMetaData(TType.STRUCT, " + metaDataClassName + ".class)");
            }
            return idRes;
        } else if (typeNode instanceof ListTypeNode) {
            ListTypeNode listType = (ListTypeNode) typeNode;
            JavaTypeResolution elementTypeRes = resolveType(listType.getElementType());
            String elementJavaTypeForDecl = getWrapperTypeIfPrimitive(elementTypeRes.javaType);
            JavaTypeResolution listRes = new JavaTypeResolution("java.util.List<" + elementJavaTypeForDecl + ">", TType.LIST, "new org.apache.thrift.meta_data.ListMetaData(TType.LIST, " + elementTypeRes.fieldMetaDataType + ")");
            addImport("java.util.List"); addImport("java.util.ArrayList"); return listRes;
        } else if (typeNode instanceof SetTypeNode) {
            SetTypeNode setType = (SetTypeNode) typeNode;
            JavaTypeResolution elementTypeRes = resolveType(setType.getElementType());
            String elementJavaTypeForDecl = getWrapperTypeIfPrimitive(elementTypeRes.javaType);
            JavaTypeResolution setRes =  new JavaTypeResolution("java.util.Set<" + elementJavaTypeForDecl + ">", TType.SET, "new org.apache.thrift.meta_data.SetMetaData(TType.SET, " + elementTypeRes.fieldMetaDataType + ")");
            addImport("java.util.Set"); addImport("java.util.HashSet"); return setRes;
        } else if (typeNode instanceof MapTypeNode) {
            MapTypeNode mapType = (MapTypeNode) typeNode;
            JavaTypeResolution keyTypeRes = resolveType(mapType.getKeyType());
            JavaTypeResolution valueTypeRes = resolveType(mapType.getValueType());
            String keyJavaTypeForDecl = getWrapperTypeIfPrimitive(keyTypeRes.javaType);
            String valueJavaTypeForDecl = getWrapperTypeIfPrimitive(valueTypeRes.javaType);
            JavaTypeResolution mapRes = new JavaTypeResolution("java.util.Map<" + keyJavaTypeForDecl + ", " + valueJavaTypeForDecl + ">", TType.MAP, "new org.apache.thrift.meta_data.MapMetaData(TType.MAP, " + keyTypeRes.fieldMetaDataType + ", " + valueTypeRes.fieldMetaDataType + ")");
            addImport("java.util.Map"); addImport("java.util.HashMap");return mapRes;
        }
        throw new IllegalArgumentException("Unsupported type node: " + typeNode.getClass().getName() + (typeNode.getName() != null ? " for type name " + typeNode.getName() : ""));
    }

    public String generate() {
        this.imports.clear();
        this.sb.setLength(0);
        String structName = getSimpleName(structNode.getName());

        addImport("org.apache.thrift.protocol.TType"); // Ensure TType is available for simple name usage
        addImport("java.util.Map"); addImport("java.util.HashMap");addImport("java.util.EnumMap");
        addImport("java.util.Set"); addImport("java.util.HashSet"); addImport("java.util.EnumSet");
        addImport("java.util.Collections"); addImport("java.util.BitSet");
        addImport("org.apache.thrift.scheme.IScheme"); addImport("org.apache.thrift.scheme.SchemeFactory");
        addImport("org.apache.thrift.scheme.StandardScheme"); addImport("org.apache.thrift.scheme.TupleScheme");
        addImport("org.apache.thrift.protocol.TTupleProtocol"); addImport("org.apache.thrift.protocol.TProtocolUtil");
        addImport("org.apache.thrift.protocol.TProtocol"); addImport("org.apache.thrift.protocol.TField");
        addImport("org.apache.thrift.protocol.TStruct"); addImport("org.apache.thrift.TBase");
        addImport("org.apache.thrift.TException"); addImport("org.apache.thrift.TFieldIdEnum");
        addImport("org.apache.thrift.meta_data.FieldMetaData");
        addImport("org.apache.thrift.TFieldRequirementType");
        addImport("org.apache.thrift.EncodingUtils"); addImport("org.apache.thrift.TBaseHelper");
        addImport("org.apache.thrift.annotation.Nullable");


        sb.append("package ").append(packageName).append(";\n\n");
        String importPlaceholder = "// IMPORTS_PLACEHOLDER\n";
        sb.append(importPlaceholder);

        sb.append("@javax.annotation.Generated(value = \"Autogenerated by Thrift Compiler (0.20.0)\", date = \"").append(date).append("\")\n");
        sb.append("@SuppressWarnings({\"cast\", \"rawtypes\", \"serial\", \"unchecked\", \"unused\"})\n");
        sb.append("public class ").append(structName)
          .append(" implements org.apache.thrift.TBase<").append(structName).append(", ").append(structName).append("._Fields>, java.io.Serializable, Cloneable, Comparable<").append(structName).append("> {\n");

        sb.append("  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(\"").append(structName).append("\");\n\n");

        List<FieldNode> fields = structNode.getFields();
        Map<FieldNode, JavaTypeResolution> fieldResolutions = new HashMap<>();
        int bitFieldIndex = 0;

        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = resolveType(field.getType());
            fieldResolutions.put(field, typeRes);
            Integer fieldIdInt = field.getId();
            short fieldId = (fieldIdInt != null) ? fieldIdInt.shortValue() : (short)(fields.indexOf(field) + 1);
            sb.append("  private static final org.apache.thrift.protocol.TField ").append(toAllCapsUnderscore(field.getName())).append("_FIELD_DESC = new org.apache.thrift.protocol.TField(\"").append(field.getName()).append("\", ").append(typeRes.thriftType).append(", (short)").append(fieldId).append(");\n");
        }
        sb.append("\n");

        sb.append("  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new ").append(structName).append("StandardSchemeFactory();\n");
        sb.append("  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new ").append(structName).append("TupleSchemeFactory();\n\n");


        StringBuilder issetBitfieldDeclarationSb = new StringBuilder();
        List<String> issetConstants = new ArrayList<>();

        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String nullableAnnotation = "";
            if (typeRes.javaType.equals("java.lang.String") || typeRes.javaType.equals("java.nio.ByteBuffer")) {
                nullableAnnotation = "@org.apache.thrift.annotation.Nullable ";
            }
            sb.append("  public ").append(nullableAnnotation).append(typeRes.javaType).append(" ").append(field.getName()).append(";\n");
            if (isPrimitive(typeRes.javaType)) {
                if (issetBitfieldDeclarationSb.length() == 0) {
                    issetBitfieldDeclarationSb.append("  private byte __isset_bitfield = 0;\n");
                }
                issetConstants.add("  private static final int __" + field.getName().toUpperCase(Locale.ROOT) + "_ISSET_ID = " + bitFieldIndex++ + ";\n");
            }
        }
        sb.append("\n").append(issetBitfieldDeclarationSb);
        for(String s : issetConstants) { sb.append(s); }
        if (!issetConstants.isEmpty()) sb.append("\n");

        sb.append("  public enum _Fields implements org.apache.thrift.TFieldIdEnum {\n");
        for (int i = 0; i < fields.size(); i++) {
            FieldNode field = fields.get(i);
            Integer fieldIdInt = field.getId();
            short fieldId = (fieldIdInt != null) ? fieldIdInt.shortValue() : (short)(i + 1);
            sb.append("    ").append(toAllCapsUnderscore(field.getName())).append("((short)").append(fieldId).append(", \"").append(field.getName()).append("\")").append(i < fields.size() - 1 ? ",\n" : ";\n\n");
        }
        sb.append("    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();\n");
        sb.append("    static { for (_Fields field : EnumSet.allOf(_Fields.class)) { byName.put(field.getFieldName(), field); } }\n");
        sb.append("    @org.apache.thrift.annotation.Nullable public static _Fields findByThriftId(int fieldId) {\n      switch(fieldId) {\n");
        for (FieldNode field : fields) {
            Integer fieldIdInt = field.getId();
            short fieldIdVal = (fieldIdInt != null) ? fieldIdInt.shortValue() : (short)(fields.indexOf(field) + 1);
            sb.append("        case ").append(fieldIdVal).append(": // ").append(toAllCapsUnderscore(field.getName())).append("\n");
            sb.append("          return ").append(toAllCapsUnderscore(field.getName())).append(";\n");
        }
        sb.append("        default: return null;\n      }\n    }\n");
        sb.append("    public static _Fields findByThriftIdOrThrow(int fieldId) { _Fields fields = findByThriftId(fieldId); if (fields == null) throw new IllegalArgumentException(\"Field \" + fieldId + \" doesn't exist!\"); return fields; }\n");
        sb.append("    @org.apache.thrift.annotation.Nullable public static _Fields findByName(String name) { return byName.get(name); }\n");
        sb.append("    private final short _thriftId; private final String _fieldName;\n");
        sb.append("    _Fields(short thriftId, String fieldName) { this._thriftId = thriftId; this._fieldName = fieldName; }\n");
        sb.append("    @Override public short getThriftFieldId() { return _thriftId; }\n");
        sb.append("    @Override public String getFieldName() { return _fieldName; }\n  }\n\n");

        sb.append("  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;\n");
        sb.append("  static {\n    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            FieldNode.Requirement req = field.getRequirement();
            String requirementString;
            if (req == null) requirementString = "TFieldRequirementType.DEFAULT";
            else {
                switch (req) {
                    case REQUIRED: requirementString = "TFieldRequirementType.REQUIRED"; break;
                    case OPTIONAL: requirementString = "TFieldRequirementType.OPTIONAL"; break;
                    default: requirementString = "TFieldRequirementType.DEFAULT"; break;
                }
            }
            sb.append("    tmpMap.put(_Fields.").append(toAllCapsUnderscore(field.getName())).append(", new org.apache.thrift.meta_data.FieldMetaData(\"").append(field.getName()).append("\", ").append(requirementString).append(", ").append(typeRes.fieldMetaDataType).append("));\n");
        }
        sb.append("    metaDataMap = Collections.unmodifiableMap(tmpMap);\n");
        sb.append("    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(").append(structName).append(".class, metaDataMap);\n  }\n\n");

        sb.append("  public ").append(structName).append("() {\n");
        for (FieldNode field : fields) {
            if (field.getDefaultValue() != null) {
                sb.append(generateDefaultValueAssignment("this", field, fieldResolutions.get(field)));
            }
        }
        sb.append("  }\n\n");

        if (!fields.isEmpty()){
            sb.append("  public ").append(structName).append("(\n");
            for (int i = 0; i < fields.size(); i++) {
                FieldNode field = fields.get(i); JavaTypeResolution typeRes = fieldResolutions.get(field);
                String nullableAnnotationInCtor = "";
                if (typeRes.javaType.equals("java.lang.String") || typeRes.javaType.equals("java.nio.ByteBuffer")) {
                    nullableAnnotationInCtor = "@org.apache.thrift.annotation.Nullable ";
                }
                sb.append("    ").append(nullableAnnotationInCtor).append(typeRes.javaType).append(" ").append(field.getName()).append(i < fields.size() - 1 ? ",\n" : "");
            }
            sb.append(") {\n    this();\n");
            for (FieldNode field : fields) {
                sb.append("    this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n");
                if (isPrimitive(fieldResolutions.get(field).javaType)) {
                    sb.append("    set").append(capitalize(field.getName())).append("IsSet(true);\n");
                }
            }
            sb.append("  }\n\n");
        }

        sb.append(generateCopyConstructor(structName, fields, fieldResolutions));
        sb.append("  @Override\n  public ").append(structName).append(" deepCopy() { return new ").append(structName).append("(this); }\n\n");
        sb.append(generateClearMethod(fields, fieldResolutions));
        sb.append(generateGettersSetters(structName, fields, fieldResolutions));
        sb.append(generateFieldValueMethods(structName, fields, fieldResolutions));
        sb.append(generateEqualsMethod(structName, fields, fieldResolutions));
        sb.append(generateHashCodeMethod(fields, fieldResolutions));
        sb.append(generateCompareToMethod(structName, fields, fieldResolutions));
        sb.append(generateFieldForIdMethod());
        sb.append(generateReadWriteMethods(structName));
        sb.append(generateToStringMethod(structName, fields, fieldResolutions));
        sb.append(generateValidateMethod(fields, fieldResolutions));
        sb.append(generateSerializationMethods(issetBitfieldDeclarationSb.length() > 0));

        sb.append(generateStandardScheme(structName, fields, fieldResolutions));
        sb.append(generateTupleScheme(structName, fields, fieldResolutions));
        sb.append("  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {\n");
        sb.append("    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();\n  }\n");

        sb.append("} // end of class ").append(structName).append("\n");

        StringBuilder importSb = new StringBuilder();
        List<String> sortedImports = new ArrayList<>(this.imports);
        Collections.sort(sortedImports);
        sortedImports.forEach(imp -> importSb.append("import ").append(imp).append(";\n"));
        if (!sortedImports.isEmpty()) importSb.append("\n");

        return sb.toString().replace(importPlaceholder, importSb.toString());
    }

    private String generateDefaultValueAssignment(String objectName, FieldNode field, JavaTypeResolution typeRes) {
        StringBuilder assignSb = new StringBuilder();
        String fieldName = field.getName();
        Object defaultValue = field.getDefaultValue();

        if (defaultValue == null) return "";
        String javaLiteral = "";

        if (typeRes.thriftType == TType.ENUM && defaultValue instanceof String) {
            // typeRes.javaType is now FQN, e.g., "com.test.enums.MyColor"
            // defaultValue is "BLUE"
            // Result: "com.test.enums.MyColor.BLUE"
            javaLiteral = typeRes.javaType + "." + defaultValue.toString();
        } else if (defaultValue instanceof String) {
            javaLiteral = "\"" + defaultValue.toString().replace("\"", "\\\"").replace("\n", "\\n") + "\"";
        } else if (defaultValue instanceof Integer || defaultValue instanceof Short || defaultValue instanceof Byte) {
            javaLiteral = defaultValue.toString();
             if (typeRes.javaType.equals("byte")) javaLiteral = "(byte)" + javaLiteral;
             if (typeRes.javaType.equals("short")) javaLiteral = "(short)" + javaLiteral;
        } else if (defaultValue instanceof Long) {
            javaLiteral = defaultValue.toString() + "L";
        } else if (defaultValue instanceof Double) {
            javaLiteral = defaultValue.toString() + "D";
        } else if (defaultValue instanceof Boolean) {
            javaLiteral = defaultValue.toString();
        } else {
            assignSb.append("    // Warning: Could not generate default value for field '").append(fieldName).append("' of type '").append(defaultValue.getClass().getName()).append("': ").append(defaultValue).append("\n");
            return assignSb.toString();
        }

        assignSb.append("    ").append(objectName).append(".").append(fieldName).append(" = ").append(javaLiteral).append(";\n");
        if (isPrimitive(typeRes.javaType)) {
            assignSb.append("    ").append(objectName).append(".set").append(capitalize(fieldName)).append("IsSet(true);\n");
        }
        return assignSb.toString();
    }

    private String generateCopyConstructor(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        s.append("  /**\n   * Performs a deep copy on <i>other</i>.\n   */\n");
        s.append("  public ").append(structName).append("(").append(structName).append(" other) {\n");
        boolean hasIsset = fields.stream().anyMatch(f -> isPrimitive(fieldResolutions.get(f).javaType));
        if (hasIsset) s.append("    __isset_bitfield = other.__isset_bitfield;\n");

        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName(); String capFieldName = capitalize(fieldName);

            if (isPrimitive(typeRes.javaType)) {
                 s.append("    this.").append(fieldName).append(" = other.").append(fieldName).append(";\n");
            } else {
                s.append("    if (other.isSet").append(capFieldName).append("()) {\n");
                if ("java.lang.String".equals(typeRes.javaType)) {
                    s.append("      this.").append(fieldName).append(" = other.").append(fieldName).append(";\n");
                } else if ("java.nio.ByteBuffer".equals(typeRes.javaType)) {
                    s.append("      this.").append(fieldName).append(" = org.apache.thrift.TBaseHelper.copyBinary(other.").append(fieldName).append(");\n");
                } else if (typeRes.thriftType == TType.LIST) {
                    TypeNode elementTypeNode = ((ListTypeNode) field.getType()).getElementType(); JavaTypeResolution elementRes = resolveType(elementTypeNode); String elementJavaType = elementRes.javaType;
                    s.append("      java.util.List<").append(elementJavaType).append("> __this_").append(fieldName).append(" = new java.util.ArrayList<").append(elementJavaType).append(">(other.").append(fieldName).append(".size());\n");
                    s.append("      for (").append(elementJavaType).append(" other_element : other.").append(fieldName).append(") {\n");
                    if (isResolvedToStruct(elementTypeNode) && !elementJavaType.startsWith("java.")) s.append("        __this_").append(fieldName).append(".add(new ").append(elementJavaType).append("(other_element));\n");
                    else if (elementJavaType.equals("java.nio.ByteBuffer"))  s.append("        __this_").append(fieldName).append(".add(org.apache.thrift.TBaseHelper.copyBinary(other_element));\n");
                    else s.append("        __this_").append(fieldName).append(".add(other_element);\n");
                    s.append("      }\n      this.").append(fieldName).append(" = __this_").append(fieldName).append(";\n");
                } else if (typeRes.thriftType == TType.SET) {
                    TypeNode elementTypeNode = ((SetTypeNode) field.getType()).getElementType(); JavaTypeResolution elementRes = resolveType(elementTypeNode); String elementJavaType = elementRes.javaType;
                    s.append("      java.util.Set<").append(elementJavaType).append("> __this_").append(fieldName).append(" = new java.util.HashSet<").append(elementJavaType).append(">(other.").append(fieldName).append(".size());\n");
                    s.append("      for (").append(elementJavaType).append(" other_element : other.").append(fieldName).append(") {\n");
                    if (isResolvedToStruct(elementTypeNode)&& !elementJavaType.startsWith("java.")) s.append("        __this_").append(fieldName).append(".add(new ").append(elementJavaType).append("(other_element));\n");
                    else if (elementJavaType.equals("java.nio.ByteBuffer")) s.append("        __this_").append(fieldName).append(".add(org.apache.thrift.TBaseHelper.copyBinary(other_element));\n");
                    else s.append("        __this_").append(fieldName).append(".add(other_element);\n");
                    s.append("      }\n      this.").append(fieldName).append(" = __this_").append(fieldName).append(";\n");
                } else if (typeRes.thriftType == TType.MAP) {
                    MapTypeNode mapTypeNode = (MapTypeNode) field.getType(); JavaTypeResolution keyRes = resolveType(mapTypeNode.getKeyType()); JavaTypeResolution valRes = resolveType(mapTypeNode.getValueType());
                    String keyJavaType = keyRes.javaType; String valJavaType = valRes.javaType;
                    s.append("      java.util.Map<").append(keyJavaType).append(",").append(valJavaType).append("> __this_").append(fieldName).append(" = new java.util.HashMap<").append(keyJavaType).append(",").append(valJavaType).append(">(other.").append(fieldName).append(".size());\n");
                    s.append("      for (java.util.Map.Entry<").append(keyJavaType).append(", ").append(valJavaType).append("> other_entry : other.").append(fieldName).append(".entrySet()) {\n");
                    s.append("        ").append(keyJavaType).append(" __this_key = other_entry.getKey();\n        ").append(valJavaType).append(" __this_value = other_entry.getValue();\n");
                    if (isResolvedToStruct(mapTypeNode.getKeyType()) && !keyJavaType.startsWith("java.")) s.append("        __this_key = new ").append(keyJavaType).append("(__this_key);\n");
                    else if (keyJavaType.equals("java.nio.ByteBuffer"))  s.append("        __this_key = org.apache.thrift.TBaseHelper.copyBinary((java.nio.ByteBuffer)__this_key);\n");
                    if (isResolvedToStruct(mapTypeNode.getValueType()) && !valJavaType.startsWith("java.")) s.append("        __this_value = new ").append(valJavaType).append("(__this_value);\n");
                    else if (valJavaType.equals("java.nio.ByteBuffer")) s.append("        __this_value = org.apache.thrift.TBaseHelper.copyBinary((java.nio.ByteBuffer)__this_value);\n");
                    s.append("        __this_").append(fieldName).append(".put(__this_key, __this_value);\n      }\n      this.").append(fieldName).append(" = __this_").append(fieldName).append(";\n");
                } else if (typeRes.thriftType == TType.STRUCT || typeRes.thriftType == TType.ENUM ) {
                     s.append("      this.").append(fieldName).append(" = new ").append(getSimpleName(typeRes.javaType)).append("(other.").append(fieldName).append(");\n");
                } else {
                     s.append("      this.").append(fieldName).append(" = other.").append(fieldName).append(";\n");
                }
                s.append("    }\n");
            }
        }
        s.append("  }\n\n"); return s.toString();
    }

    private boolean isResolvedToStruct(TypeNode typeNode) {
        if (typeNode instanceof IdentifierTypeNode && documentNode != null) {
            String typeName = ((IdentifierTypeNode)typeNode).getName();
            for (DefinitionNode def : documentNode.getDefinitions()) {
                if (def.getName().equals(typeName) && (def instanceof StructNode || def instanceof UnionNode || def instanceof ExceptionNode) ) return true;
                String qualifiedNameInPackage = this.packageName + "." + typeName;
                if (!typeName.contains(".") && def.getName().equals(qualifiedNameInPackage) && (def instanceof StructNode || def instanceof UnionNode || def instanceof ExceptionNode)) return true;
            }
        }
        return false;
    }

    private String generateClearMethod(List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        s.append("  @Override\n  public void clear() {\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName();
            if (isPrimitive(typeRes.javaType)) {
                s.append("    set").append(capitalize(fieldName)).append("IsSet(false);\n");
                s.append("    this.").append(fieldName).append(" = ").append(getDefaultPrimitiveValue(typeRes.javaType)).append(";\n");
            } else {
                s.append("    this.").append(fieldName).append(" = null;\n");
            }
        }
        s.append("  }\n\n"); return s.toString();
    }

    private String getDefaultPrimitiveValue(String javaType) {
        if (javaType.equals("boolean")) return "false"; if (javaType.equals("long")) return "0L";
        if (javaType.equals("double")) return "0.0D"; if (isPrimitive(javaType)) return "0";
        return "null";
    }

    private String generateGettersSetters(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName(); String capFieldName = capitalize(fieldName); String javaType = typeRes.javaType;
            String nullableAnnotationForGetterSetter = "";
            if (javaType.equals("java.lang.String") || javaType.equals("java.nio.ByteBuffer")) {
                 nullableAnnotationForGetterSetter = "@org.apache.thrift.annotation.Nullable ";
            }

            s.append("  ").append(nullableAnnotationForGetterSetter).append("public ").append(javaType).append(" get").append(capFieldName).append("() { return this.").append(fieldName).append("; }\n\n");
            s.append("  public ").append(structName).append(" set").append(capFieldName).append("(").append(nullableAnnotationForGetterSetter).append(javaType).append(" ").append(fieldName).append(") {\n    this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            if (isPrimitive(javaType)) s.append("    set").append(capFieldName).append("IsSet(true);\n");
            s.append("    return this;\n  }\n\n");
            s.append("  public void unset").append(capFieldName).append("() {\n");
            if (isPrimitive(javaType)) s.append("    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __").append(fieldName.toUpperCase(Locale.ROOT)).append("_ISSET_ID);\n");
            else s.append("    this.").append(fieldName).append(" = null;\n");
            s.append("  }\n\n");
            s.append("  /** Returns true if field ").append(fieldName).append(" is set (has been assigned a value) and false otherwise */\n");
            s.append("  public boolean isSet").append(capFieldName).append("() {\n");
            if (isPrimitive(javaType)) s.append("    return EncodingUtils.testBit(__isset_bitfield, __").append(fieldName.toUpperCase(Locale.ROOT)).append("_ISSET_ID);\n");
            else s.append("    return this.").append(fieldName).append(" != null;\n");
            s.append("  }\n\n");
            if (isPrimitive(javaType)) s.append("  public void set").append(capFieldName).append("IsSet(boolean value) { __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __").append(fieldName.toUpperCase(Locale.ROOT)).append("_ISSET_ID, value);}\n\n");
        }
        return s.toString();
    }

    private String generateFieldValueMethods(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        s.append("  @Override\n  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {\n    switch (field) {\n");
        for (FieldNode fieldNode : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(fieldNode); String fieldName = fieldNode.getName(); String capFieldName = capitalize(fieldName);
            s.append("    case ").append(toAllCapsUnderscore(fieldName)).append(":\n      if (value == null) {\n        unset").append(capFieldName).append("();\n      } else {\n");
            String castType = isPrimitive(typeRes.javaType) ? getWrapperTypeIfPrimitive(typeRes.javaType) : typeRes.javaType;
            s.append("        set").append(capFieldName).append("((").append(castType).append(")value);\n      }\n      break;\n");
        }
        s.append("    }\n  }\n\n");
        s.append("  @org.apache.thrift.annotation.Nullable\n  @Override\n  public java.lang.Object getFieldValue(_Fields field) {\n    switch (field) {\n");
        for (FieldNode fieldNode : fields) s.append("    case ").append(toAllCapsUnderscore(fieldNode.getName())).append(": return get").append(capitalize(fieldNode.getName())).append("();\n");
        s.append("    }\n    throw new java.lang.IllegalStateException();\n  }\n\n");
        s.append("  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */\n");
        s.append("  @Override\n  public boolean isSet(_Fields field) {\n    if (field == null) {\n      throw new java.lang.IllegalArgumentException();\n    }\n\n    switch (field) {\n");
        for (FieldNode fieldNode : fields) s.append("    case ").append(toAllCapsUnderscore(fieldNode.getName())).append(": return isSet").append(capitalize(fieldNode.getName())).append("();\n");
        s.append("    }\n    throw new java.lang.IllegalStateException();\n  }\n\n");
        return s.toString();
    }

    private String generateEqualsMethod(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        s.append("  @Override\n  public boolean equals(java.lang.Object that) {\n    if (that == null) return false;\n    if (that instanceof ").append(structName).append(") return this.equals((").append(structName).append(")that);\n    return false;\n  }\n\n");
        s.append("  public boolean equals(").append(structName).append(" that) {\n    if (that == null) return false;\n    if (this == that) return true;\n\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName();
            String capFieldName = capitalize(fieldName);
            boolean isReqOrDefaultPrimitive = (field.getRequirement() == FieldNode.Requirement.DEFAULT || field.getRequirement() == FieldNode.Requirement.REQUIRED) && isPrimitive(typeRes.javaType);

            if (isReqOrDefaultPrimitive) {
                s.append("    // Required or default primitive: always present for equals check\n");
                s.append("    if (this.").append(fieldName).append(" != that.").append(fieldName).append(") return false;\n\n");
            } else {
                s.append("    boolean this_present_").append(fieldName).append(" = ").append(isPrimitive(typeRes.javaType) ? "isSet" + capFieldName + "()" : ("this." + fieldName + " != null")).append(";\n");
                s.append("    boolean that_present_").append(fieldName).append(" = ").append(isPrimitive(typeRes.javaType) ? "that.isSet" + capFieldName + "()" : ("that." + fieldName + " != null")).append(";\n");
                s.append("    if (this_present_").append(fieldName).append(" || that_present_").append(fieldName).append(") {\n");
                s.append("      if (!(this_present_").append(fieldName).append(" && that_present_").append(fieldName).append(")) return false;\n");
                if (isPrimitive(typeRes.javaType)) {
                    s.append("      if (this.").append(fieldName).append(" != that.").append(fieldName).append(") return false;\n");
                } else {
                    s.append("      if (!this.").append(fieldName).append(".equals(that.").append(fieldName).append(")) return false;\n");
                }
                s.append("    }\n\n");
            }
        }
        s.append("    return true;\n  }\n\n");
        return s.toString();
     }

    private String generateHashCodeMethod(List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        addImport("java.util.List"); addImport("java.util.ArrayList"); // For TBaseHelper.hashCode list argument
        s.append("  @Override\n  public int hashCode() {\n    int hashCode = 1;\n\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName();
            String capFieldName = capitalize(fieldName);
            boolean isReqOrDefaultPrimitive = (field.getRequirement() == FieldNode.Requirement.DEFAULT || field.getRequirement() == FieldNode.Requirement.REQUIRED) && isPrimitive(typeRes.javaType);

            if (isReqOrDefaultPrimitive) {
                s.append("    hashCode = hashCode * 8191 + ");
                if (typeRes.javaType.equals("boolean")) {
                     s.append("((this.").append(fieldName).append(") ? 1 : 0);\n");
                } else if (typeRes.javaType.equals("long") || typeRes.javaType.equals("double")) {
                     s.append("org.apache.thrift.TBaseHelper.hashCode(this.").append(fieldName).append(");\n");
                } else { // byte, short, int
                     s.append("this.").append(fieldName).append(";\n");
                }
            } else { // Optional primitives or any object type
                String isSetCondition = isPrimitive(typeRes.javaType) ? "isSet" + capFieldName + "()" : ("this." + fieldName + " != null");
                s.append("    hashCode = hashCode * 8191 + ((").append(isSetCondition).append(") ? 131071 : 524287);\n");
                s.append("    if (").append(isSetCondition).append(")\n");
                s.append("      hashCode = hashCode * 8191 + ");
                if (typeRes.thriftType == TType.ENUM) {
                     s.append(fieldName).append(".getValue();\n");
                } else if (typeRes.javaType.equals("boolean")) {
                     s.append("((this.").append(fieldName).append(") ? 1 : 0);\n");
                } else if (typeRes.javaType.equals("long") || typeRes.javaType.equals("double")) {
                     s.append("org.apache.thrift.TBaseHelper.hashCode(this.").append(fieldName).append(");\n");
                } else if (isPrimitive(typeRes.javaType)) {
                     s.append("this.").append(fieldName).append(";\n");
                } else { // Non-primitive objects (String, ByteBuffer, List, Set, Map, Struct)
                     s.append(fieldName).append(".hashCode();\n");
                }
            }
        }
        s.append("\n    return hashCode;\n  }\n\n");
        return s.toString();
    }

    private String generateCompareToMethod(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        s.append("  @Override\n  public int compareTo(").append(structName).append(" other) {\n");
        s.append("    if (!getClass().equals(other.getClass())) {\n      return getClass().getName().compareTo(other.getClass().getName());\n    }\n\n");
        s.append("    int lastComparison = 0;\n\n");
        for (FieldNode field : fields) {
            String fieldName = field.getName(); String capFieldName = capitalize(fieldName);
            s.append("    lastComparison = java.lang.Boolean.compare(isSet").append(capFieldName).append("(), other.isSet").append(capFieldName).append("());\n");
            s.append("    if (lastComparison != 0) {\n      return lastComparison;\n    }\n");
            s.append("    if (isSet").append(capFieldName).append("()) {\n");
            s.append("      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.").append(fieldName).append(", other.").append(fieldName).append(");\n");
            s.append("      if (lastComparison != 0) {\n        return lastComparison;\n      }\n    }\n");
        }
        s.append("    return 0;\n  }\n\n");
        return s.toString();
    }

    private String generateFieldForIdMethod() { return "  @org.apache.thrift.annotation.Nullable\n  @Override\n  public _Fields fieldForId(int fieldId) { return _Fields.findByThriftId(fieldId); }\n\n"; }

    private String generateReadWriteMethods(String structName) {
        StringBuilder s = new StringBuilder();
        s.append("  @Override\n  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {\n");
        s.append("    scheme(iprot).read(iprot, this);\n  }\n\n");
        s.append("  @Override\n  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {\n");
        s.append("    scheme(oprot).write(oprot, this);\n  }\n\n");
        return s.toString();
    }

    private String generateToStringMethod(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        s.append("  @Override\n  public String toString() {\n    java.lang.StringBuilder sb = new java.lang.StringBuilder(\"").append(structName).append("(\");\n    boolean first = true;\n\n");
        for (int i = 0; i < fields.size(); i++) {
            FieldNode field = fields.get(i);
            String fieldName = field.getName(); JavaTypeResolution typeRes = fieldResolutions.get(field);

            boolean isOptional = field.getRequirement() == FieldNode.Requirement.OPTIONAL;

            String condition = isPrimitive(typeRes.javaType) ? "isSet" + capitalize(fieldName) + "()" : "this." + fieldName + " != null";
            boolean alwaysInclude = !isOptional || isPrimitive(typeRes.javaType);


            if (!alwaysInclude) { // Optional non-primitive: only include if set
                s.append("    if (").append(condition).append(") {\n");
                s.append("      if (!first) sb.append(\", \");\n");
                s.append("      sb.append(\"").append(fieldName).append(":\");\n");
                s.append("      if (this.").append(fieldName).append(" == null) {\n        sb.append(\"null\");\n      } else {\n");
                if ("java.nio.ByteBuffer".equals(typeRes.javaType)) s.append("        org.apache.thrift.TBaseHelper.toString(this.").append(fieldName).append(", sb);\n");
                else s.append("        sb.append(this.").append(fieldName).append(");\n");
                s.append("      }\n      first = false;\n");
                s.append("    }\n");
            } else { // Required fields, or optional primitives (which are included if isSet)
                 if (isOptional && isPrimitive(typeRes.javaType)) { // Optional primitive: check isSet
                    s.append("    if (").append(condition).append(") {\n");
                    s.append("      if (!first) sb.append(\", \");\n");
                    s.append("      sb.append(\"").append(fieldName).append(":\");\n");
                    s.append("      sb.append(this.").append(fieldName).append(");\n");
                    s.append("      first = false;\n");
                    s.append("    }\n");
                } else { // Required or default primitive/object
                    s.append("    if (!first) sb.append(\", \");\n");
                    s.append("    sb.append(\"").append(fieldName).append(":\");\n");
                    if (isPrimitive(typeRes.javaType)) {
                        s.append("sb.append(this.").append(fieldName).append(");\n");
                    } else { // Object types
                        s.append("if (this.").append(fieldName).append(" == null) {\n");
                        s.append("      sb.append(\"null\");\n");
                        s.append("    } else {\n");
                        if ("java.nio.ByteBuffer".equals(typeRes.javaType)) {
                            s.append("      org.apache.thrift.TBaseHelper.toString(this.").append(fieldName).append(", sb);\n");
                        } else {
                            s.append("      sb.append(this.").append(fieldName).append(");\n");
                        }
                        s.append("    }\n");
                    }
                    s.append("    first = false;\n");
                }
            }
        }
        s.append("    sb.append(\")\");\n    return sb.toString();\n  }\n\n");
        return s.toString();
    }

    private String generateValidateMethod(List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder s = new StringBuilder();
        s.append("  public void validate() throws org.apache.thrift.TException {\n    // check for required fields\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName();
            if (field.getRequirement() == FieldNode.Requirement.REQUIRED) {
                 if (!isPrimitive(typeRes.javaType)) {
                    s.append("    if (this.").append(fieldName).append(" == null) {\n");
                    s.append("      throw new org.apache.thrift.protocol.TProtocolException(org.apache.thrift.protocol.TProtocolException.MISSING_REQUIRED_FIELD, \"Required field '").append(fieldName).append("' was not present! Struct: \" + toString());\n    }\n");
                }
            }
        }
        s.append("    // check for sub-struct validity\n");
         for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName();
            if (typeRes.thriftType == TType.STRUCT) {
                 s.append("    if (this.").append(fieldName).append(" != null) {\n      try {\n        this.").append(fieldName).append(".validate();\n      } catch (org.apache.thrift.TException te) {\n        throw new org.apache.thrift.TException(te.getMessage() + \" referring to field '").append(fieldName).append("'\", te);\n      }\n    }\n");
            }
        }
        s.append("  }\n\n");
        return s.toString();
    }

    private String generateSerializationMethods(boolean hasIssetBitfield) {
        StringBuilder s = new StringBuilder();
        addImport("java.io.ObjectOutputStream"); addImport("java.io.ObjectInputStream"); addImport("java.io.IOException");
        addImport("org.apache.thrift.protocol.TCompactProtocol"); addImport("org.apache.thrift.transport.TIOStreamTransport");
        s.append("  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {\n    try {\n      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));\n    } catch (org.apache.thrift.TException te) {\n      throw new java.io.IOException(te);\n    }\n  }\n\n");
        s.append("  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {\n    try {\n");
        if (hasIssetBitfield) s.append("      __isset_bitfield = 0;\n");
        s.append("      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));\n    } catch (org.apache.thrift.TException te) {\n      throw new java.io.IOException(te);\n    }\n  }\n\n");
        return s.toString();
    }

    private String generateStandardScheme(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder schemeSb = new StringBuilder();
        schemeSb.append("  private static class ").append(structName).append("StandardSchemeFactory implements SchemeFactory {\n");
        schemeSb.append("    @Override public ").append(structName).append("StandardScheme getScheme() {\n      return new ").append(structName).append("StandardScheme();\n    }\n  }\n\n");
        schemeSb.append("  private static class ").append(structName).append("StandardScheme extends StandardScheme<").append(structName).append("> {\n");
        schemeSb.append("    @Override public void read(org.apache.thrift.protocol.TProtocol iprot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        schemeSb.append("      org.apache.thrift.protocol.TField schemeField;\n      iprot.readStructBegin();\n");
        schemeSb.append("      while (true) {\n        schemeField = iprot.readFieldBegin();\n");
        schemeSb.append("        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { break; }\n");
        schemeSb.append("        switch (schemeField.id) {\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            Integer fieldIdInt = field.getId(); short fieldId = (fieldIdInt != null) ? fieldIdInt.shortValue() : (short)(fields.indexOf(field) + 1);
            schemeSb.append("          case ").append(fieldId).append(": // ").append(toAllCapsUnderscore(field.getName())).append("\n");
            schemeSb.append("            if (schemeField.type == ").append(typeRes.thriftType).append(") {\n");
            schemeSb.append(generateFieldReadLogic("struct", field, typeRes, "iprot", "              "));
            schemeSb.append("            } else { TProtocolUtil.skip(iprot, schemeField.type); }\n            break;\n");
        }
        schemeSb.append("          default: TProtocolUtil.skip(iprot, schemeField.type);\n        }\n        iprot.readFieldEnd();\n      }\n      iprot.readStructEnd();\n");
        schemeSb.append("      struct.validate();\n    }\n\n");
        schemeSb.append("    @Override public void write(org.apache.thrift.protocol.TProtocol oprot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        schemeSb.append("      struct.validate();\n      oprot.writeStructBegin(STRUCT_DESC);\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String fieldName = field.getName(); String capFieldName = capitalize(fieldName); String fieldDescConst = toAllCapsUnderscore(fieldName) + "_FIELD_DESC";

            boolean isOptional = field.getRequirement() == FieldNode.Requirement.OPTIONAL;
            String condition;
            if (isPrimitive(typeRes.javaType)) {
                condition = "struct.isSet" + capFieldName + "()";
            } else {
                condition = "struct." + fieldName + " != null";
            }

            if (isOptional) {
                 schemeSb.append("      if (").append(condition).append(") {\n  ");
            } else if (!isPrimitive(typeRes.javaType)) { // Non-optional object
                 schemeSb.append("      if (").append(condition).append(") {\n  ");
            }
            else { // Required or default primitive
                 schemeSb.append("      ");
            }

            schemeSb.append("oprot.writeFieldBegin(").append(fieldDescConst).append(");\n");
            String writeIndent = "      ";
            if (isOptional || (!isPrimitive(typeRes.javaType) && field.getRequirement() != FieldNode.Requirement.OPTIONAL)) {
                 writeIndent += "  ";
            }
            schemeSb.append(generateFieldWriteLogic("struct", field, typeRes, "oprot", writeIndent));
            schemeSb.append(writeIndent).append("oprot.writeFieldEnd();\n");

            if (isOptional || (!isPrimitive(typeRes.javaType) && field.getRequirement() != FieldNode.Requirement.OPTIONAL) ) {
                 schemeSb.append("      }\n");
            }
        }
        schemeSb.append("      oprot.writeFieldStop();\n      oprot.writeStructEnd();\n    }\n  }\n\n");
        return schemeSb.toString();
    }

    private String generateTupleScheme(String structName, List<FieldNode> fields, Map<FieldNode, JavaTypeResolution> fieldResolutions) {
        StringBuilder schemeSb = new StringBuilder();
        schemeSb.append("  private static class ").append(structName).append("TupleSchemeFactory implements SchemeFactory {\n");
        schemeSb.append("    @Override public ").append(structName).append("TupleScheme getScheme() {\n      return new ").append(structName).append("TupleScheme();\n    }\n  }\n\n");
        schemeSb.append("  private static class ").append(structName).append("TupleScheme extends TupleScheme<").append(structName).append("> {\n");
        schemeSb.append("    @Override\n    public void write(org.apache.thrift.protocol.TProtocol prot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        schemeSb.append("      TTupleProtocol oprot = (TTupleProtocol) prot;\n");
        schemeSb.append("      java.util.BitSet optionals = new java.util.BitSet();\n");
        int bitsetIndex = 0;
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String capFieldName = capitalize(field.getName());
            schemeSb.append("      if (struct.isSet").append(capFieldName).append("()) {\n");
            schemeSb.append("        optionals.set(").append(bitsetIndex).append(");\n");
            schemeSb.append("      }\n");
            bitsetIndex++;
        }
        schemeSb.append("      oprot.writeBitSet(optionals, ").append(fields.size()).append(");\n");

        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String capFieldName = capitalize(field.getName());
            schemeSb.append("      if (struct.isSet").append(capFieldName).append("()) {\n");
            schemeSb.append(generateFieldWriteLogic("struct", field, typeRes, "oprot", "        "));
            schemeSb.append("      }\n");
        }
        schemeSb.append("    }\n\n");

        schemeSb.append("    @Override\n    public void read(org.apache.thrift.protocol.TProtocol prot, ").append(structName).append(" struct) throws org.apache.thrift.TException {\n");
        schemeSb.append("      TTupleProtocol iprot = (TTupleProtocol) prot;\n");
        schemeSb.append("      java.util.BitSet incoming = iprot.readBitSet(").append(fields.size()).append(");\n");
        bitsetIndex = 0;
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            schemeSb.append("      if (incoming.get(").append(bitsetIndex).append(")) {\n");
            schemeSb.append(generateFieldReadLogic("struct", field, typeRes, "iprot", "        "));
            // For read, setXXXIsSet(true) is already called by generateFieldReadLogic if it's a primitive.
            // For objects, being read means it's set.
            schemeSb.append("      }\n");
            bitsetIndex++;
        }
        schemeSb.append("    }\n  }\n\n");
        return schemeSb.toString();
     }

    private String generateFieldReadLogic(String structVarName, FieldNode field, JavaTypeResolution typeRes, String protocolVarName, String indent) {
        StringBuilder sb = new StringBuilder();
        String fieldName = field.getName(); String capFieldName = capitalize(fieldName); String javaType = typeRes.javaType;
        switch (typeRes.thriftType) {
            case TType.BOOL: sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readBool();\n").append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n"); break;
            case TType.BYTE: sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readByte();\n").append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n"); break;
            case TType.I16: sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readI16();\n").append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n"); break;
            case TType.I32:
                if (typeRes.fieldMetaDataType.contains("EnumMetaData")) sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(javaType).append(".findByValue(").append(protocolVarName).append(".readI32());\n");
                else sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readI32();\n");
                sb.append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n"); break;
            case TType.I64: sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readI64();\n").append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n"); break;
            case TType.DOUBLE: sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readDouble();\n").append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n"); break;
            case TType.STRING:
                if (javaType.equals("java.nio.ByteBuffer")) sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readBinary();\n");
                else sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = ").append(protocolVarName).append(".readString();\n");
                 if(!isPrimitive(typeRes.javaType)) sb.append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n");
                break;
            case TType.STRUCT:
                sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = new ").append(javaType).append("();\n");
                sb.append(indent).append(structVarName).append(".").append(fieldName).append(".read(").append(protocolVarName).append(");\n");
                sb.append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n");
                break;
            case TType.LIST:
                sb.append(indent).append("org.apache.thrift.protocol.TList _list = ").append(protocolVarName).append(".readListBegin();\n");
                sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = new ArrayList<").append(resolveType(((ListTypeNode)field.getType()).getElementType()).javaType).append(">(_list.size);\n");
                sb.append(generateListSetMapElementReadWrite(field, typeRes, "read", protocolVarName, "_list.size", "", "", indent));
                sb.append(indent).append(protocolVarName).append(".readListEnd();\n");
                sb.append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n");
                break;
            case TType.SET:
                sb.append(indent).append("org.apache.thrift.protocol.TSet _set = ").append(protocolVarName).append(".readSetBegin();\n");
                sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = new HashSet<").append(resolveType(((SetTypeNode)field.getType()).getElementType()).javaType).append(">(_set.size);\n");
                sb.append(generateListSetMapElementReadWrite(field, typeRes, "read", protocolVarName, "_set.size", "", "", indent));
                sb.append(indent).append(protocolVarName).append(".readSetEnd();\n");
                sb.append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n");
                break;
            case TType.MAP:
                sb.append(indent).append("org.apache.thrift.protocol.TMap _map = ").append(protocolVarName).append(".readMapBegin();\n");
                MapTypeNode mapTypeNode = (MapTypeNode)field.getType();
                String keyJavaType = resolveType(mapTypeNode.getKeyType()).javaType;
                String valJavaType = resolveType(mapTypeNode.getValueType()).javaType;
                sb.append(indent).append(structVarName).append(".").append(fieldName).append(" = new HashMap<").append(keyJavaType).append(",").append(valJavaType).append(">(2*_map.size);\n");
                sb.append(generateListSetMapElementReadWrite(field, typeRes, "read", protocolVarName, "_map.size", "", "", indent));
                sb.append(indent).append(protocolVarName).append(".readMapEnd();\n");
                sb.append(indent).append(structVarName).append(".set").append(capFieldName).append("IsSet(true);\n");
                break;
            default: sb.append(indent).append("TProtocolUtil.skip(").append(protocolVarName).append(", schemeField.type); // Unknown type: ").append(typeRes.thriftType).append("\n"); break;
        }
        return sb.toString();
    }

    private String generateFieldWriteLogic(String structVarName, FieldNode field, JavaTypeResolution typeRes, String protocolVarName, String indent) {
        StringBuilder sb = new StringBuilder(); String fieldName = field.getName(); String javaType = typeRes.javaType;
         switch (typeRes.thriftType) {
            case TType.BOOL: sb.append(indent).append(protocolVarName).append(".writeBool(").append(structVarName).append(".").append(fieldName).append(");\n"); break;
            case TType.BYTE: sb.append(indent).append(protocolVarName).append(".writeByte(").append(structVarName).append(".").append(fieldName).append(");\n"); break;
            case TType.I16: sb.append(indent).append(protocolVarName).append(".writeI16(").append(structVarName).append(".").append(fieldName).append(");\n"); break;
            case TType.I32:
                if (typeRes.fieldMetaDataType.contains("EnumMetaData")) sb.append(indent).append(protocolVarName).append(".writeI32(").append(structVarName).append(".").append(fieldName).append(".getValue());\n");
                else sb.append(indent).append(protocolVarName).append(".writeI32(").append(structVarName).append(".").append(fieldName).append(");\n");
                break;
            case TType.I64: sb.append(indent).append(protocolVarName).append(".writeI64(").append(structVarName).append(".").append(fieldName).append(");\n"); break;
            case TType.DOUBLE: sb.append(indent).append(protocolVarName).append(".writeDouble(").append(structVarName).append(".").append(fieldName).append(");\n"); break;
            case TType.STRING:
                if (javaType.equals("java.nio.ByteBuffer")) sb.append(indent).append(protocolVarName).append(".writeBinary(").append(structVarName).append(".").append(fieldName).append(");\n");
                else sb.append(indent).append(protocolVarName).append(".writeString(").append(structVarName).append(".").append(fieldName).append(");\n");
                break;
            case TType.STRUCT: sb.append(indent).append(structVarName).append(".").append(fieldName).append(".write(").append(protocolVarName).append(");\n"); break;
            case TType.LIST:
                sb.append(indent).append(protocolVarName).append(".writeListBegin(new org.apache.thrift.protocol.TList(").append(resolveType(((ListTypeNode)field.getType()).getElementType()).thriftType).append(", ").append(structVarName).append(".").append(fieldName).append(".size()));\n");
                sb.append(generateListSetMapElementReadWrite(field, typeRes, "write", protocolVarName, "", "", "", indent));
                sb.append(indent).append(protocolVarName).append(".writeListEnd();\n");
                break;
            case TType.SET:
                sb.append(indent).append(protocolVarName).append(".writeSetBegin(new org.apache.thrift.protocol.TSet(").append(resolveType(((SetTypeNode)field.getType()).getElementType()).thriftType).append(", ").append(structVarName).append(".").append(fieldName).append(".size()));\n");
                sb.append(generateListSetMapElementReadWrite(field, typeRes, "write", protocolVarName, "", "", "", indent));
                sb.append(indent).append(protocolVarName).append(".writeSetEnd();\n");
                break;
            case TType.MAP:
                MapTypeNode mapTypeNode = (MapTypeNode)field.getType();
                sb.append(indent).append(protocolVarName).append(".writeMapBegin(new org.apache.thrift.protocol.TMap(").append(resolveType(mapTypeNode.getKeyType()).thriftType).append(", ").append(resolveType(mapTypeNode.getValueType()).thriftType).append(", ").append(structVarName).append(".").append(fieldName).append(".size()));\n");
                sb.append(generateListSetMapElementReadWrite(field, typeRes, "write", protocolVarName, "", "", "", indent));
                sb.append(indent).append(protocolVarName).append(".writeMapEnd();\n");
                break;
        }
        return sb.toString();
     }

    private String generateListSetMapElementReadWrite(FieldNode field, JavaTypeResolution containerTypeRes, String mode, String protocolVarName, String sizeVar, String _elemTypeVar, String _valTypeVar, String baseIndent) {
        StringBuilder sb = new StringBuilder();
        String fieldName = field.getName(); String iterVar = "_" + fieldName + "_iter";
        TypeNode originalElementTypeNode = null; TypeNode originalKeyTypeNode = null; TypeNode originalValueTypeNode = null;
        String loopIndent = baseIndent + "  "; String elementLogicIndent = baseIndent + "    ";

        if (field.getType() instanceof ListTypeNode) originalElementTypeNode = ((ListTypeNode) field.getType()).getElementType();
        else if (field.getType() instanceof SetTypeNode) originalElementTypeNode = ((SetTypeNode) field.getType()).getElementType();
        else if (field.getType() instanceof MapTypeNode) { originalKeyTypeNode = ((MapTypeNode) field.getType()).getKeyType(); originalValueTypeNode = ((MapTypeNode) field.getType()).getValueType(); }

        if (mode.equals("read")) {
            sb.append(loopIndent).append("for (int ").append(iterVar).append("_i = 0; ").append(iterVar).append("_i < ").append(sizeVar).append("; ++").append(iterVar).append("_i) {\n");
            if (containerTypeRes.thriftType == TType.LIST || containerTypeRes.thriftType == TType.SET) {
                JavaTypeResolution elemRes = resolveType(originalElementTypeNode);
                sb.append(elementLogicIndent).append(elemRes.javaType).append(" _elem").append(field.getId()).append(";\n");
                sb.append(generateSingleElementReadLogic(elemRes, protocolVarName, "_elem" + field.getId(), elementLogicIndent));
                sb.append(elementLogicIndent).append("struct.").append(fieldName).append(".add(_elem").append(field.getId()).append(");\n");
            } else { // MAP
                JavaTypeResolution keyRes = resolveType(originalKeyTypeNode); JavaTypeResolution valRes = resolveType(originalValueTypeNode);
                sb.append(elementLogicIndent).append(keyRes.javaType).append(" _key").append(field.getId()).append(";\n");
                sb.append(elementLogicIndent).append(valRes.javaType).append(" _val").append(field.getId()).append(";\n");
                sb.append(generateSingleElementReadLogic(keyRes, protocolVarName, "_key" + field.getId(), elementLogicIndent));
                sb.append(generateSingleElementReadLogic(valRes, protocolVarName, "_val" + field.getId(), elementLogicIndent));
                sb.append(elementLogicIndent).append("struct.").append(fieldName).append(".put(_key").append(field.getId()).append(", _val").append(field.getId()).append(");\n");
            }
            sb.append(loopIndent).append("}\n");
        } else { // WRITE
             if (containerTypeRes.thriftType == TType.LIST || containerTypeRes.thriftType == TType.SET) {
                JavaTypeResolution elemRes = resolveType(originalElementTypeNode);
                sb.append(loopIndent).append("for (").append(elemRes.javaType).append(" ").append(iterVar).append(" : struct.").append(fieldName).append(") {\n");
                sb.append(generateSingleElementWriteLogic(elemRes, protocolVarName, iterVar, elementLogicIndent));
                sb.append(loopIndent).append("}\n");
            } else { // MAP
                JavaTypeResolution keyRes = resolveType(originalKeyTypeNode); JavaTypeResolution valRes = resolveType(originalValueTypeNode);
                sb.append(loopIndent).append("for (java.util.Map.Entry<").append(keyRes.javaType).append(", ").append(valRes.javaType).append("> ").append(iterVar).append("_entry : struct.").append(fieldName).append(".entrySet()) {\n");
                sb.append(generateSingleElementWriteLogic(keyRes, protocolVarName, iterVar + "_entry.getKey()", elementLogicIndent));
                sb.append(generateSingleElementWriteLogic(valRes, protocolVarName, iterVar + "_entry.getValue()", elementLogicIndent));
                sb.append(loopIndent).append("}\n");
            }
        }
        return sb.toString();
    }

    private String generateSingleElementReadLogic(JavaTypeResolution typeRes, String protocolVarName, String varName, String indent) {
        StringBuilder sb = new StringBuilder(); String javaType = typeRes.javaType;
        switch (typeRes.thriftType) {
            case TType.BOOL: sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readBool();\n"); break;
            case TType.BYTE: sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readByte();\n"); break;
            case TType.I16: sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readI16();\n"); break;
            case TType.I32:
                if (typeRes.fieldMetaDataType.contains("EnumMetaData")) sb.append(indent).append(varName).append(" = ").append(javaType).append(".findByValue(").append(protocolVarName).append(".readI32());\n");
                else sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readI32();\n");
                break;
            case TType.I64: sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readI64();\n"); break;
            case TType.DOUBLE: sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readDouble();\n"); break;
            case TType.STRING:
                if (typeRes.javaType.equals("java.nio.ByteBuffer")) sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readBinary();\n");
                else sb.append(indent).append(varName).append(" = ").append(protocolVarName).append(".readString();\n");
                break;
            case TType.STRUCT:
                sb.append(indent).append(varName).append(" = new ").append(javaType).append("();\n");
                sb.append(indent).append(varName).append(".read(").append(protocolVarName).append(");\n");
                break;
            default: sb.append(indent).append("// Unhandled single element read type: ").append(javaType).append("\n"); break;
        }
        return sb.toString();
    }

    private String generateSingleElementWriteLogic(JavaTypeResolution typeRes, String protocolVarName, String varName, String indent) {
        StringBuilder sb = new StringBuilder(); String javaType = typeRes.javaType;
         switch (typeRes.thriftType) {
            case TType.BOOL: sb.append(indent).append(protocolVarName).append(".writeBool(").append(varName).append(");\n"); break;
            case TType.BYTE: sb.append(indent).append(protocolVarName).append(".writeByte(").append(varName).append(");\n"); break;
            case TType.I16: sb.append(indent).append(protocolVarName).append(".writeI16(").append(varName).append(");\n"); break;
            case TType.I32:
                if (typeRes.fieldMetaDataType.contains("EnumMetaData")) sb.append(indent).append(protocolVarName).append(".writeI32(").append(varName).append(".getValue());\n");
                else sb.append(indent).append(protocolVarName).append(".writeI32(").append(varName).append(");\n");
                break;
            case TType.I64: sb.append(indent).append(protocolVarName).append(".writeI64(").append(varName).append(");\n"); break;
            case TType.DOUBLE: sb.append(indent).append(protocolVarName).append(".writeDouble(").append(varName).append(");\n"); break;
            case TType.STRING:
                if (typeRes.javaType.equals("java.nio.ByteBuffer")) sb.append(indent).append(protocolVarName).append(".writeBinary(").append(varName).append(");\n");
                else sb.append(indent).append(protocolVarName).append(".writeString(").append(varName).append(");\n");
                break;
            case TType.STRUCT: sb.append(indent).append(varName).append(".write(").append(protocolVarName).append(");\n"); break;
            default: sb.append(indent).append("// Unhandled single element write type: ").append(javaType).append("\n"); break;
        }
        return sb.toString();
    }

    private String getListSetElementType(JavaTypeResolution typeRes){ return getCollectionElementType(typeRes, "LIST", "SET");}
    private String getMapKeyType(JavaTypeResolution typeRes){ return getCollectionElementType(typeRes, "MAP_KEY");}
    private String getMapValueType(JavaTypeResolution typeRes){ return getCollectionElementType(typeRes, "MAP_VALUE");}

    private String getCollectionElementType(JavaTypeResolution typeRes, String... types) {
        String javaType = typeRes.javaType;
        if (javaType.contains("<") && javaType.contains(">")) {
            String genericPart = javaType.substring(javaType.indexOf('<') + 1, javaType.lastIndexOf('>'));
            if (types[0].equals("MAP_KEY") && genericPart.contains(",")) {
                String keyTypeStr = genericPart.substring(0, genericPart.indexOf(',')).trim();
                if (keyTypeStr.endsWith("Integer")) return "TType.I32";
                if (keyTypeStr.endsWith("String")) return "TType.STRING";
                return "TType.STRUCT";
            } else if (types[0].equals("MAP_VALUE") && genericPart.contains(",")) {
                 String valTypeStr = genericPart.substring(genericPart.indexOf(',') + 1).trim();
                if (valTypeStr.endsWith("Integer")) return "TType.I32";
                if (valTypeStr.endsWith("String")) return "TType.STRING";
                return "TType.STRUCT";
            } else if (!genericPart.contains(",")) {
                if (genericPart.endsWith("Integer")) return "TType.I32";
                if (genericPart.endsWith("String")) return "TType.STRING";
                 return "TType.STRUCT";
            }
        }
        return "TType.STRUCT";
    }

    private String getConcreteContainerTypeFromJavaType(String javaType) {
        if (javaType.startsWith("java.util.List")) return "java.util.ArrayList";
        if (javaType.startsWith("java.util.Set")) return "java.util.HashSet";
        if (javaType.startsWith("java.util.Map")) return "java.util.HashMap";
        return javaType;
    }
}
