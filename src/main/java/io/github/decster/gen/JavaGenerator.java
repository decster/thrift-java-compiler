package io.github.decster.gen;

import io.github.decster.ast.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Java code generator for Thrift. Generates Java classes based on Thrift definitions. */
public class JavaGenerator extends Generator {
  public static class GenResult {
    public String filename;
    public String content;

    public GenResult(String filename, String content) {
      this.filename = filename;
      this.content = content;
    }
  }

  private final TProgram program;
  private final JavaGeneratorOptions options;
  private final String outputDir;
  private String packageDir;

  // Map of Thrift types to Java types
  private static final Map<String, String> PRIMITIVE_TYPE_MAP = new HashMap<>();
  // Set of Java keywords that need to be escaped
  private static final java.util.Set<String> JAVA_KEYWORDS = new java.util.HashSet<>();
  String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

  static {
    PRIMITIVE_TYPE_MAP.put("bool", "boolean");
    PRIMITIVE_TYPE_MAP.put("byte", "byte");
    PRIMITIVE_TYPE_MAP.put("i16", "short");
    PRIMITIVE_TYPE_MAP.put("i32", "int");
    PRIMITIVE_TYPE_MAP.put("i64", "long");
    PRIMITIVE_TYPE_MAP.put("double", "double");
    PRIMITIVE_TYPE_MAP.put("string", "String");
    PRIMITIVE_TYPE_MAP.put("binary", "byte[]");

    // Initialize Java keywords
    String[] keywords = {
        "abstract",  "assert",   "boolean",  "break",    "byte",    "case",         "catch",     "char",       "class",
        "const",     "continue", "default",  "do",       "double",  "else",         "enum",      "extends",    "final",
        "finally",   "float",    "for",      "goto",     "if",      "implements",   "import",    "instanceof", "int",
        "interface", "long",     "native",   "new",      "package", "private",      "protected", "public",     "return",
        "short",     "static",   "strictfp", "super",    "switch",  "synchronized", "this",      "throw",      "throws",
        "transient", "try",      "void",     "volatile", "while",   "true",         "false",     "null"};
    for (String keyword : keywords) {
      JAVA_KEYWORDS.add(keyword);
    }
  }

  /**
   * Constructor for the Java Generator.
   *
   * @param program The Thrift program AST
   * @param outputDir The output directory for generated code
   * @param options Generation options
   */
  public JavaGenerator(TProgram program, String outputDir, JavaGeneratorOptions options) {
    this.program = program;
    this.outputDir = outputDir;
    this.options = options != null ? options : new JavaGeneratorOptions();
  }

  public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

  /**
   * Generate all Java files for the Thrift program.
   *
   * @throws IOException If file operations fail
   * @return The number of files generated
   */
  public int generateAndWriteToFile() throws IOException {
    prepareOutputDirectoryStructure();
    List<GenResult> results = generate();
    // Write all generated files to disk
    for (GenResult result : results) {
      writeToFile(result.filename, result.content);
    }
    return results.size();
  }

  public List<GenResult> generate() throws IOException {
    List<GenResult> results = new ArrayList<>();

    // Generate enums
    for (TEnum enumType : program.getEnums()) {
      results.add(generateEnum(enumType));
    }

    // Generate typedefs (skip for java)

    // Generate structs
    for (TStruct struct : program.getObjects()) {
      if (struct.isXception()) {
        results.add(generateStruct(struct, true));
      } else if (struct.isUnion()) {
        results.add(generateUnion(struct));
      } else {
        results.add(generateStruct(struct, false));
      }
    }

    // Generate constants
    if (!program.getConsts().isEmpty()) {
      results.add(generateConstants());
    }

    // Generate services (will be implemented later)
    for (TService service : program.getServices()) {
      results.add(generateService(service));
    }
    return results;
  }

  private void generateUnionHashCode(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append("@Override\n");
    sb.append(indent()).append("public int hashCode() {\n");
    indent_up();
    sb.append(indent()).append(
        "java.util.List<java.lang.Object> list = new java.util.ArrayList<java.lang.Object>();\n");
    sb.append(indent()).append("list.add(this.getClass().getName());\n");
    sb.append(indent()).append("org.apache.thrift.TFieldIdEnum setField = getSetField();\n");
    sb.append(indent()).append("if (setField != null) {\n");
    indent_up();
    sb.append(indent()).append("list.add(setField.getThriftFieldId());\n");
    sb.append(indent()).append("java.lang.Object value = getFieldValue();\n");
    sb.append(indent()).append("if (value instanceof org.apache.thrift.TEnum) {\n");
    indent_up();
    sb.append(indent()).append("list.add(((org.apache.thrift.TEnum)getFieldValue()).getValue());\n");
    indent_down();
    sb.append(indent()).append("} else {\n");
    indent_up();
    sb.append(indent()).append("list.add(value);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n");
    sb.append(indent()).append("return list.hashCode();\n");
    indent_down();
    sb.append(indent()).append("}\n");
  }

  public GenResult generateUnion(TStruct tstruct) {
    StringBuilder sb = new StringBuilder();

    // Autogen comment and package
    sb.append(autogenComment());
    sb.append(javaPackage());

    // JavaDoc for the union
    generateJavaDoc(sb, tstruct);
    // Suppressions
    sb.append(javaSuppressions());

    // Annotations
    boolean isFinal = (tstruct.getAnnotations() != null && tstruct.getAnnotations().containsKey("final"));
    boolean isDeprecated = isDeprecated(tstruct.getAnnotations());

    if (!options.isSuppressGeneratedAnnotations()) {
      sb.append(getAutogenComment());
    }

    if (isDeprecated) {
      sb.append(indent()).append("@Deprecated\n");
    }

    // Class declaration
    sb.append(indent())
        .append("public ")
        .append(isFinal ? "final " : "")
        .append("class ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" extends org.apache.thrift.TUnion<")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(", ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append("._Fields> ");

    sb.append("{\n");
    indent_up();

    generateStructDesc(sb, tstruct);
    generateFieldDescs(sb, tstruct);
    sb.append("\n");

    generateFieldNameConstants(sb, tstruct);
    sb.append("\n");

    generateJavaMetaDataMap(sb, tstruct);
    // No newline after metadata map as per C++

    generateUnionConstructor(sb, tstruct);
    sb.append("\n");

    generateUnionAbstractMethods(sb, tstruct);
    sb.append("\n");

    generateJavaStructFieldById(sb, tstruct);
    // No newline after fieldForId as per C++ (it adds its own)

    generateUnionGettersAndSetters(sb, tstruct);
    sb.append("\n");

    generateUnionIsSetMethods(sb, tstruct);
    // No newline after isSet methods (they add their own)

    generateUnionComparisons(sb, tstruct);
    sb.append("\n");

    generateUnionHashCode(sb, tstruct);
    sb.append("\n");

    generateJavaStructWriteObject(sb, tstruct);
    // No newline (adds its own)
    generateJavaStructReadObject(sb, tstruct);
    // No newline (adds its own)

    indent_down();
    sb.append(indent()).append("}\n");

    return new GenResult(makeValidJavaFilename(tstruct.getName()) + ".java", sb.toString());
  }

  // ---- Methods for Union ----
  private void generateUnionConstructor(StringBuilder sb, TStruct tstruct) {
    List<TField> members = tstruct.getMembers();

    sb.append(indent()).append("public ").append(typeName(tstruct)).append("() {\n");
    indent_up();
    boolean defaultValueSet = false;
    for (TField field : members) {
      TType type = getTrueType(field.getType());
      if (field.getValue() != null) {
        sb.append(indent())
            .append("super(_Fields.")
            .append(constantName(field.getName()))
            .append(", ")
            .append(renderConstValue(new StringBuilder(), type, field.getValue()))
            .append(");\n");
        defaultValueSet = true;
        break;
      }
    }
    if (!defaultValueSet) {
      sb.append(indent()).append("super();\n");
    }
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent())
        .append("public ")
        .append(typeName(tstruct))
        .append("(_Fields setField, java.lang.Object value) {\n");
    indent_up();
    sb.append(indent()).append("super(setField, value);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent())
        .append("public ")
        .append(typeName(tstruct))
        .append("(")
        .append(typeName(tstruct))
        .append(" other) {\n");
    indent_up();
    sb.append(indent()).append("super(other);\n");
    indent_down();
    sb.append(indent()).append("}\n\n"); // Added newline like C++

    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public ").append(makeValidJavaIdentifier(tstruct.getName())).append(" deepCopy() {\n");
    indent_up();
    sb.append(indent()).append("return new ").append(makeValidJavaIdentifier(tstruct.getName())).append("(this);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    for (TField field : members) {
      TType type = field.getType(); // Not getTrueType here as per C++
      String fieldName = makeValidJavaIdentifier(field.getName());
      sb.append(indent())
          .append("public static ")
          .append(typeName(tstruct))
          .append(" ")
          .append(fieldName)
          .append("(")
          .append(typeName(type))
          .append(" value) {\n");
      indent_up();
      sb.append(indent()).append(typeName(tstruct)).append(" x = new ").append(typeName(tstruct)).append("();\n");
      sb.append(indent()).append("x.set").append(getCapName(field.getName())).append("(value);\n");
      sb.append(indent()).append("return x;\n");
      indent_down();
      sb.append(indent()).append("}\n\n");

      if (type.isBinary()) {
        sb.append(indent())
            .append("public static ")
            .append(typeName(tstruct))
            .append(" ")
            .append(fieldName)
            .append("(byte[] value) {\n");
        indent_up();
        sb.append(indent()).append(typeName(tstruct)).append(" x = new ").append(typeName(tstruct)).append("();\n");
        sb.append(indent()).append("x.set").append(getCapName(field.getName()));
        if (options.isUnsafeBinaries()) {
          sb.append("(java.nio.ByteBuffer.wrap(value));\n");
        } else {
          sb.append("(java.nio.ByteBuffer.wrap(value.clone()));\n");
        }
        sb.append(indent()).append("return x;\n");
        indent_down();
        sb.append(indent()).append("}\n\n");
      }
    }
  }

  private void generateUnionAbstractMethods(StringBuilder sb, TStruct tstruct) {
    generateCheckType(sb, tstruct);
    sb.append("\n");
    generateStandardSchemeReadValue(sb, tstruct);
    sb.append("\n");
    generateStandardSchemeWriteValue(sb, tstruct);
    sb.append("\n");
    generateTupleSchemeReadValue(sb, tstruct);
    sb.append("\n");
    generateTupleSchemeWriteValue(sb, tstruct);
    sb.append("\n");
    generateGetFieldDesc(sb, tstruct);
    sb.append("\n");
    generateGetStructDesc(sb, tstruct); // Already exists, just calling
    sb.append("\n");                    // C++ code has extra newline after getStructDesc

    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("protected _Fields enumForId(short id) {\n");
    indent_up();
    sb.append(indent()).append("return _Fields.findByThriftIdOrThrow(id);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    // No final newline here as per C++
  }

  private void generateUnionGettersAndSetters(StringBuilder sb, TStruct tstruct) {
    List<TField> members = tstruct.getMembers();
    boolean first = true;
    for (TField field : members) {
      if (first) {
        first = false;
      } else {
        sb.append("\n");
      }

      TType type = field.getType();
      String fieldName = field.getName();
      String capName = getCapName(fieldName);
      boolean isDeprecatedField = isDeprecated(field.getAnnotations());

      generateJavaDoc(sb, field);
      if (type.isBinary()) {
        if (isDeprecatedField) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent()).append("public byte[] get").append(capName).append("() {\n");
        indent_up();
        sb.append(indent())
            .append("set")
            .append(capName)
            .append("(org.apache.thrift.TBaseHelper.rightSize(bufferFor")
            .append(capName)
            .append("()));\n");
        sb.append(indent()).append("java.nio.ByteBuffer b = bufferFor").append(capName).append("();\n");
        sb.append(indent()).append("return b == null ? null : b.array();\n");
        indent_down();
        sb.append(indent()).append("}\n\n");

        sb.append(indent()).append("public java.nio.ByteBuffer bufferFor").append(capName).append("() {\n");
        indent_up();
        sb.append(indent()).append("if (getSetField() == _Fields.").append(constantName(fieldName)).append(") {\n");
        indent_up();
        if (options.isUnsafeBinaries()) {
          sb.append(indent()).append("return (java.nio.ByteBuffer)getFieldValue();\n");
        } else {
          sb.append(indent()).append(
              "return "
              + "org.apache.thrift.TBaseHelper.copyBinary((java.nio.ByteBuffer)getFieldValue());\n");
        }
        indent_down();
        sb.append(indent()).append("} else {\n");
        indent_up();
        sb.append(indent())
            .append("throw new java.lang.RuntimeException(\"Cannot get field '")
            .append(fieldName)
            .append("' because union is currently set to \" + getFieldDesc(getSetField()).name);\n");
        indent_down();
        sb.append(indent()).append("}\n");
        indent_down();
        sb.append(indent()).append("}\n");
      } else {
        if (isDeprecatedField) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent()).append("public ").append(typeName(type)).append(" get").append(capName).append("() {\n");
        indent_up();
        sb.append(indent()).append("if (getSetField() == _Fields.").append(constantName(fieldName)).append(") {\n");
        indent_up();
        sb.append(indent()).append("return (").append(typeName(type, true)).append(")getFieldValue();\n");
        indent_down();
        sb.append(indent()).append("} else {\n");
        indent_up();
        sb.append(indent())
            .append("throw new java.lang.RuntimeException(\"Cannot get field '")
            .append(fieldName)
            .append("' because union is currently set to \" + getFieldDesc(getSetField()).name);\n");
        indent_down();
        sb.append(indent()).append("}\n");
        indent_down();
        sb.append(indent()).append("}\n");
      }
      sb.append("\n");

      generateJavaDoc(sb, field);
      if (type.isBinary()) {
        if (isDeprecatedField) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent()).append("public void set").append(capName).append("(byte[] value) {\n");
        indent_up();
        sb.append(indent()).append("set").append(capName);
        if (options.isUnsafeBinaries()) {
          sb.append("(java.nio.ByteBuffer.wrap(value));\n");
        } else {
          sb.append("(java.nio.ByteBuffer.wrap(value.clone()));\n");
        }
        indent_down();
        sb.append(indent()).append("}\n\n");
      }

      if (isDeprecatedField) {
        sb.append(indent()).append("@Deprecated\n");
      }
      sb.append(indent())
          .append("public void set")
          .append(capName)
          .append("(")
          .append(typeName(type))
          .append(" value) {\n");
      indent_up();
      sb.append(indent()).append("setField_ = _Fields.").append(constantName(fieldName)).append(";\n");

      if (typeCanBeNull(type)) {
        sb.append(indent())
            .append("value_ = java.util.Objects.requireNonNull(value, \"_Fields.")
            .append(constantName(fieldName))
            .append("\");\n");
      } else {
        sb.append(indent()).append("value_ = value;\n");
      }
      indent_down();
      sb.append(indent()).append("}\n");
    }
  }

  private void generateCheckType(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("protected void checkType(_Fields setField, java.lang.Object "
                               + "value) throws java.lang.ClassCastException {\n");
    indent_up();
    sb.append(indent()).append("switch (setField) {\n");
    indent_up();

    for (TField field : tstruct.getMembers()) {
      sb.append(indent()).append("case ").append(constantName(field.getName())).append(":\n");
      indent_up();
      // Use typeName with skipGeneric=true for instanceof check
      sb.append(indent())
          .append("if (value instanceof ")
          .append(typeName(field.getType(), true, false, true))
          .append(") {\n");
      indent_up();
      sb.append(indent()).append("break;\n");
      indent_down();
      sb.append(indent()).append("}\n");
      sb.append(indent())
          .append("throw new java.lang.ClassCastException(\"Was expecting value of type ")
          .append(typeName(field.getType(), true, false, false)) // Full type name for error message
          .append(" for field '")
          .append(field.getName())
          .append("', but got \" + value.getClass().getSimpleName());\n");
      indent_down();
    }

    sb.append(indent()).append("default:\n");
    indent_up();
    sb.append(indent()).append("throw new java.lang.IllegalArgumentException(\"Unknown field id \" + setField);\n");
    indent_down();

    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n");
  }

  private void generateStandardSchemeReadValue(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append(
        "protected java.lang.Object standardSchemeReadValue(org.apache.thrift.protocol.TProtocol "
        + "iprot, org.apache.thrift.protocol.TField field) throws org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("_Fields setField = _Fields.findByThriftId(field.id);\n");
    sb.append(indent()).append("if (setField != null) {\n");
    indent_up();
    sb.append(indent()).append("switch (setField) {\n");
    indent_up();

    for (TField field : tstruct.getMembers()) {
      sb.append(indent()).append("case ").append(constantName(field.getName())).append(":\n");
      indent_up();
      sb.append(indent())
          .append("if (field.type == ")
          .append(constantName(field.getName()))
          .append("_FIELD_DESC.type) {\n");
      indent_up();
      // C++ uses typeName(..., true, false) -> typeName(..., true) in Java
      sb.append(indent())
          .append(typeName(field.getType(), true))
          .append(" ")
          .append(makeValidJavaIdentifier(field.getName()))
          .append(";\n");
      generateDeserializeField(sb, field, "", true); // prefix is empty
      sb.append(indent()).append("return ").append(makeValidJavaIdentifier(field.getName())).append(";\n");
      indent_down();
      sb.append(indent()).append("} else {\n");
      indent_up();
      sb.append(indent()).append("org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);\n");
      sb.append(indent()).append("return null;\n");
      indent_down();
      sb.append(indent()).append("}\n");
      indent_down();
    }

    sb.append(indent()).append("default:\n");
    indent_up();
    sb.append(indent()).append("throw new java.lang.IllegalStateException(\"setField wasn't "
                               + "null, but didn't match any of the case statements!\");\n");
    indent_down();

    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("} else {\n");
    indent_up();
    sb.append(indent()).append("org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);\n");
    sb.append(indent()).append("return null;\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n");
  }

  private void generateStandardSchemeWriteValue(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("protected void standardSchemeWriteValue(org.apache.thrift.protocol.TProtocol oprot) "
                               + "throws org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("switch (setField_) {\n"); // Assumes setField_ is available from TUnion
    indent_up();

    for (TField field : tstruct.getMembers()) {
      sb.append(indent()).append("case ").append(constantName(field.getName())).append(":\n");
      indent_up();
      // C++ uses typeName(..., true, false) -> typeName(..., true)
      String fieldVarName = makeValidJavaIdentifier(field.getName());
      sb.append(indent())
          .append(typeName(field.getType(), true))
          .append(" ")
          .append(fieldVarName)
          .append(" = (")
          .append(typeName(field.getType(), true))
          .append(")value_;\n"); // Assumes value_ is available
      // Generate serialize field with prefix "" and variable name as fieldName
      // Need to create a temporary TField object for generateSerializeField
      TField tempField = new TField(field.getType(), fieldVarName, field.getKey());
      tempField.setReq(field.getReq()); // copy requirement
      // The C++ version passes the original field, and the serialize logic uses field->get_name().
      // Here, we want to serialize the local variable `fieldVarName`.
      // The `generateSerializeFieldInternal` expects the direct variable name.
      generateSerializeFieldInternal(sb, field.getType(), fieldVarName, true);

      sb.append(indent()).append("return;\n");
      indent_down();
    }

    sb.append(indent()).append("default:\n");
    indent_up();
    sb.append(indent()).append("throw new java.lang.IllegalStateException(\"Cannot write union "
                               + "with unknown field \" + setField_);\n");
    indent_down();

    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n");
  }

  private void generateTupleSchemeReadValue(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("protected java.lang.Object tupleSchemeReadValue(org.apache.thrift.protocol.TProtocol "
                               + "iprot, short fieldID) throws org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("_Fields setField = _Fields.findByThriftId(fieldID);\n");
    sb.append(indent()).append("if (setField != null) {\n");
    indent_up();
    sb.append(indent()).append("switch (setField) {\n");
    indent_up();

    for (TField field : tstruct.getMembers()) {
      sb.append(indent()).append("case ").append(constantName(field.getName())).append(":\n");
      indent_up();
      sb.append(indent())
          .append(typeName(field.getType(), true))
          .append(" ")
          .append(makeValidJavaIdentifier(field.getName()))
          .append(";\n");
      generateDeserializeField(sb, field, "", true); // hasMetaData = false for tuple
      sb.append(indent()).append("return ").append(makeValidJavaIdentifier(field.getName())).append(";\n");
      indent_down();
    }

    sb.append(indent()).append("default:\n");
    indent_up();
    sb.append(indent()).append("throw new java.lang.IllegalStateException(\"setField wasn't "
                               + "null, but didn't match any of the case statements!\");\n");
    indent_down();

    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("} else {\n");
    indent_up();
    sb.append(indent()).append("throw new org.apache.thrift.protocol.TProtocolException(\"Couldn't find a field with "
                               + "field id \" + fieldID);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n");
  }

  private void generateTupleSchemeWriteValue(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append(
        "protected void tupleSchemeWriteValue(org.apache.thrift.protocol.TProtocol oprot) throws "
        + "org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("switch (setField_) {\n"); // Assumes setField_ from TUnion
    indent_up();

    for (TField field : tstruct.getMembers()) {
      sb.append(indent()).append("case ").append(constantName(field.getName())).append(":\n");
      indent_up();
      String fieldVarName = makeValidJavaIdentifier(field.getName());
      sb.append(indent())
          .append(typeName(field.getType(), true))
          .append(" ")
          .append(fieldVarName)
          .append(" = (")
          .append(typeName(field.getType(), true))
          .append(")value_;\n"); // Assumes value_
      generateSerializeFieldInternal(sb, field.getType(), fieldVarName, true);
      sb.append(indent()).append("return;\n");
      indent_down();
    }

    sb.append(indent()).append("default:\n");
    indent_up();
    sb.append(indent()).append("throw new java.lang.IllegalStateException(\"Cannot write union "
                               + "with unknown field \" + setField_);\n");
    indent_down();

    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n");
  }

  private void generateGetFieldDesc(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("protected org.apache.thrift.protocol.TField getFieldDesc(_Fields setField) {\n");
    indent_up();
    sb.append(indent()).append("switch (setField) {\n");
    indent_up();

    List<TField> members = tstruct.getMembers();
    for (TField field : members) {
      String fieldName = field.getName();
      sb.append(indent()).append("case ").append(constantName(fieldName)).append(":\n");
      indent_up();
      sb.append(indent()).append("return ").append(constantName(fieldName)).append("_FIELD_DESC;\n");
      indent_down();
    }

    sb.append(indent()).append("default:\n");
    indent_up();
    sb.append(indent()).append("throw new java.lang.IllegalArgumentException(\"Unknown field id \" + setField);\n");
    indent_down();

    indent_down();
    sb.append(indent()).append("}\n"); // End switch
    indent_down();
    sb.append(indent()).append("}\n"); // End getFieldDesc
  }

  private void generateGetStructDesc(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("protected org.apache.thrift.protocol.TStruct getStructDesc() {\n");
    indent_up();
    sb.append(indent()).append("return STRUCT_DESC;\n");
    indent_down();
    sb.append(indent()).append("}\n"); // End getStructDesc
  }

  private void generateUnionIsSetMethods(StringBuilder sb, TStruct tstruct) {
    List<TField> members = tstruct.getMembers();
    boolean first = true;
    for (TField field : members) {
      if (first) {
        first = false;
      } else {
        sb.append("\n");
      }
      String fieldName = field.getName();
      sb.append(indent()).append("public boolean isSet").append(getCapName(fieldName)).append("() {\n");
      indent_up();
      sb.append(indent()).append("return setField_ == _Fields.").append(constantName(fieldName)).append(";\n");
      indent_down();
      sb.append(indent()).append("}\n\n");
    }
  }

  private void generateUnionComparisons(StringBuilder sb, TStruct tstruct) {
    String unionName = makeValidJavaIdentifier(tstruct.getName());
    // equals(Object)
    sb.append(indent()).append("public boolean equals(java.lang.Object other) {\n");
    indent_up();
    sb.append(indent()).append("if (other instanceof ").append(unionName).append(") {\n");
    indent_up();
    sb.append(indent()).append("return equals((").append(unionName).append(")other);\n");
    indent_down();
    sb.append(indent()).append("} else {\n");
    indent_up();
    sb.append(indent()).append("return false;\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // equals(ThisType)
    sb.append(indent()).append("public boolean equals(").append(unionName).append(" other) {\n");
    indent_up();
    sb.append(indent()).append("return other != null && getSetField() == other.getSetField() && "
                               + "getFieldValue().equals(other.getFieldValue());\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // compareTo
    sb.append(indent()).append("@Override\n");
    sb.append(indent()).append("public int compareTo(").append(typeName(tstruct)).append(" other) {\n");
    indent_up();
    sb.append(indent()).append("int lastComparison = org.apache.thrift.TBaseHelper.compareTo(getSetField(), "
                               + "other.getSetField());\n");
    sb.append(indent()).append("if (lastComparison == 0) {\n");
    indent_up();
    sb.append(indent()).append("return org.apache.thrift.TBaseHelper.compareTo(getFieldValue(), "
                               + "other.getFieldValue());\n");
    indent_down();
    sb.append(indent()).append("}\n");
    sb.append(indent()).append("return lastComparison;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  public GenResult generateStruct(TStruct struct, boolean isException) {
    StringBuilder sb = new StringBuilder();

    sb.append(autogenComment());
    sb.append(javaPackage());

    generateJavaStructDefinition(sb, struct, isException, false, false);

    return new GenResult(makeValidJavaFilename(struct.getName()) + ".java", sb.toString());
  }

  private boolean typeCanBeNull(TType type) {
    type = getTrueType(type);
    return type.isContainer() || type.isStruct() || type.isXception() || type.isString() || type.isEnum() ||
        type.isUUID();
  }

  private String javaNullableAnnotation() { return "@org.apache.thrift.annotation.Nullable"; }

  private String declareField(TField field, boolean comment) {
    // TODO: do we ever need to initialize the field?
    StringBuilder result = new StringBuilder();
    TType ttype = getTrueType(field.getType());

    if (typeCanBeNull(ttype)) {
      result.append(javaNullableAnnotation()).append(" ");
    }

    result.append(typeName(field.getType())).append(" ").append(makeValidJavaIdentifier(field.getName()));

    result.append(";");

    if (comment) {
      result.append(" // ");
      if (field.getReq() == TField.Requirement.OPTIONAL) {
        result.append("optional");
      } else {
        result.append("required");
      }
    }

    return result.toString();
  }

  String javaSuppressions() {
    return "@SuppressWarnings({\"cast\", \"rawtypes\", \"serial\", \"unchecked\", \"unused\"})\n";
  }

  private void generateJavaStructDefinition(StringBuilder sb, TStruct tstruct, boolean isException, boolean inClass,
                                            boolean isResult) {
    // Class JavaDoc
    generateJavaDoc(sb, tstruct);
    sb.append(javaSuppressions());

    // @Generated annotation
    if (!options.isSuppressGeneratedAnnotations() && !inClass) {
      sb.append(getAutogenComment());
    }

    // Deprecated annotation if applicable
    if (isDeprecated(tstruct.getAnnotations())) {
      sb.append("@Deprecated\n");
    }

    // Class declaration with proper inheritance
    boolean isFinal = (tstruct.getAnnotations().containsKey("final"));
    sb.append("public ")
        .append(isFinal ? "final " : "")
        .append(inClass ? "static " : "")
        .append("class ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" ");

    if (isException) {
      sb.append("extends org.apache.thrift.TException ");
    }
    sb.append("implements org.apache.thrift.TBase<")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(", ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append("._Fields>, java.io.Serializable, Cloneable, Comparable<")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(">");

    if (options.isAndroidStyle()) {
      throw new UnsupportedOperationException("androidStyle is not supported for structs");
    }
    sb.append(indent());
    sb.append(" {\n");

    // Indentation tracking
    indent_up(); // Increase to level 1 for class body content
    // Generate struct description
    generateStructDesc(sb, tstruct);

    sb.append("\n");

    // Generate field descriptors for each field
    generateFieldDescs(sb, tstruct);

    sb.append("\n");

    // Generate scheme map
    generateSchemeMap(sb, tstruct);

    sb.append("\n");

    // Generate fields
    final List<TField> members = tstruct.getMembers();
    for (TField field : members) {
      if (options.isBeans() || options.isPrivateMembers()) {
        sb.append(indent()).append("private ");
      } else {
        generateJavaDocField(sb, field);
        sb.append(indent()).append("public ");
      }
      sb.append(declareField(field, true)).append("\n");
    }

    sb.append("\n");

    // Generate Android parcelable implementation if needed
    if (options.isAndroidStyle()) {
      throw new UnsupportedOperationException("androidStyle is not supported for structs");
    }

    // Generate _Fields enum for the struct
    generateFieldNameConstants(sb, tstruct);

    // Generate isset variables for non-nullable fields
    if (members.size() > 0) {
      sb.append("\n");
      sb.append(indent()).append("// isset id assignments\n");

      int i = 0;
      int optionals = 0;
      for (TField field : members) {
        if (field.getReq() == TField.Requirement.OPTIONAL) {
          optionals++;
        }
        if (!typeCanBeNull(field.getType())) {
          sb.append(indent())
              .append("private static final int ")
              .append(isset_field_id(field))
              .append(" = ")
              .append(i)
              .append(";\n");
          i++;
        }
      }

      StringBuilder primitiveType = new StringBuilder();
      isset_type issetType = needs_isset(tstruct, primitiveType);
      String primitiveTypeStr = primitiveType.toString();
      switch (issetType) {
      case ISSET_NONE:
        break;
      case ISSET_PRIMITIVE:
        sb.append(indent()).append("private ").append(primitiveTypeStr).append(" __isset_bitfield = 0;\n");
        break;
      case ISSET_BITSET:
        sb.append(indent())
            .append("private java.util.BitSet __isset_bit_vector = new java.util.BitSet(")
            .append(i)
            .append(");\n");
        break;
      }

      if (optionals > 0) {
        StringBuilder output = new StringBuilder("private static final _Fields optionals[] = {");
        for (TField field : members) {
          if (field.getReq() == TField.Requirement.OPTIONAL) {
            output.append("_Fields.").append(constantName(field.getName())).append(",");
          }
        }
        sb.append(indent()).append(output.substring(0, output.length() - 1)).append("};\n");
      }
    }

    // Generate metadata map
    generateJavaMetaDataMap(sb, tstruct);

    // Default constructor
    sb.append(indent()).append("public ").append(makeValidJavaIdentifier(tstruct.getName())).append("() {\n");
    indent_up();
    for (TField field : members) {
      TType type = getTrueType(field.getType());
      if (field.getValue() != null) {
        printConstValue(sb, "this." + field.getName(), type, field.getValue(), true, true);
      }
      if (field.getReq() != TField.Requirement.OPTIONAL) {
        // All required fields will be initialized with defaults if needed
      }
    }
    indent_down();
    sb.append(indent()).append("}\n\n");

    // Generate constructor for required fields
    boolean allOptionalMembers = true;
    for (TField field : members) {
      if (field.getReq() != TField.Requirement.OPTIONAL) {
        allOptionalMembers = false;
        break;
      }
    }

    if (!members.isEmpty() && !allOptionalMembers) {
      // Full constructor for all fields
      sb.append(indent()).append("public ").append(makeValidJavaIdentifier(tstruct.getName())).append("(\n");
      indent_up();
      boolean first = true;
      for (TField field : members) {
        if (field.getReq() != TField.Requirement.OPTIONAL) {
          if (!first) {
            sb.append(",\n");
          }
          first = false;
          sb.append(indent())
              .append(typeName(field.getType()))
              .append(" ")
              .append(makeValidJavaIdentifier(field.getName()));
        }
      }
      sb.append(")\n{\n");
      indent_down();
      indent_up();
      sb.append(indent()).append("this();\n");
      for (TField field : members) {
        if (field.getReq() != TField.Requirement.OPTIONAL) {
          TType type = getTrueType(field.getType());
          if (type.isBinary()) {
            if (options.isUnsafeBinaries()) {
              sb.append(indent())
                  .append("this.")
                  .append(makeValidJavaIdentifier(field.getName()))
                  .append(" = ")
                  .append(makeValidJavaIdentifier(field.getName()))
                  .append(";\n");
            } else {
              sb.append(indent())
                  .append("this.")
                  .append(makeValidJavaIdentifier(field.getName()))
                  .append(" = org.apache.thrift.TBaseHelper.copyBinary(")
                  .append(makeValidJavaIdentifier(field.getName()))
                  .append(");\n");
            }
          } else {
            sb.append(indent())
                .append("this.")
                .append(makeValidJavaIdentifier(field.getName()))
                .append(" = ")
                .append(makeValidJavaIdentifier(field.getName()))
                .append(";\n");
          }
          generate_isset_set(sb, field, "");
        }
      }
      indent_down();
      sb.append(indent()).append("}\n\n");
    }

    // Copy constructor
    sb.append(indent()).append("/**\n");
    sb.append(indent()).append(" * Performs a deep copy on <i>other</i>.\n");
    sb.append(indent()).append(" */\n");
    sb.append(indent())
        .append("public ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append("(")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" other) {\n");
    indent_up();

    // Copy isset bitfields first
    switch (needs_isset(tstruct)) {
    case ISSET_NONE:
      break;
    case ISSET_PRIMITIVE:
      sb.append(indent()).append("__isset_bitfield = other.__isset_bitfield;\n");
      break;
    case ISSET_BITSET:
      sb.append(indent()).append("__isset_bit_vector.clear();\n");
      sb.append(indent()).append("__isset_bit_vector.or(other.__isset_bit_vector);\n");
      break;
    }

    // Deep copy all fields
    for (TField field : members) {
      String fieldName = field.getName();
      TType type = field.getType().getTrueType();
      boolean canBeNull = typeCanBeNull(type);

      if (canBeNull) {
        sb.append(indent()).append("if (other.").append(generateIssetCheck(field)).append(") {\n");
        indent_up();
      }

      if (type.isContainer()) {
        generateDeepCopyContainer(sb, "other", fieldName, "__this__" + fieldName, type);
        sb.append(indent())
            .append("this.")
            .append(makeValidJavaIdentifier(fieldName))
            .append(" = ")
            .append("__this__")
            .append(fieldName)
            .append(";\n");
      } else {
        sb.append(indent()).append("this.").append(makeValidJavaIdentifier(fieldName)).append(" = ");
        generateDeepCopyNonContainer(sb, "other." + makeValidJavaIdentifier(fieldName), fieldName, type);
        sb.append(";\n");
      }

      if (canBeNull) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
    }

    indent_down();
    sb.append(indent()).append("}\n\n");

    // DeepCopy method
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public ").append(makeValidJavaIdentifier(tstruct.getName())).append(" deepCopy() {\n");
    sb.append(indent()).append("  return new ").append(makeValidJavaIdentifier(tstruct.getName())).append("(this);\n");
    sb.append(indent()).append("}\n\n");

    // Clear method
    generateJavaStructClear(sb, tstruct);

    // Bean style getters/setters or standard accessors
    generateJavaBeanBoilerplate(sb, tstruct);
    generateGenericFieldGettersSetters(sb, tstruct);
    generateGenericIssetMethod(sb, tstruct);

    // Equality methods
    generateJavaStructEquality(sb, tstruct);
    generateJavaStructCompareTo(sb, tstruct);
    generateJavaStructFieldById(sb, tstruct);

    // Reader and writer methods
    generateJavaStructReader(sb, tstruct);
    if (isResult) {
      generateJavaStructResultWriter(sb, tstruct);
    } else {
      generateJavaStructWriter(sb, tstruct);
    }
    generateJavaStructToString(sb, tstruct);
    generateJavaValidator(sb, tstruct);

    // Serialization helpers
    generateJavaStructWriteObject(sb, tstruct);
    generateJavaStructReadObject(sb, tstruct);

    // Standard and tuple schemes
    generateJavaStructStandardScheme(sb, tstruct, isResult);
    generateJavaStructTupleScheme(sb, tstruct);
    generateJavaSchemeLookup(sb);

    // Close the class
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaSchemeLookup(StringBuilder sb) {
    sb.append(indent())
        .append("private static <S extends org.apache.thrift.scheme.IScheme> S scheme(")
        .append("org.apache.thrift.protocol.TProtocol proto) {")
        .append('\n');
    indent_up();
    sb.append(indent())
        .append("return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ")
        .append("? STANDARD_SCHEME_FACTORY ")
        .append(": TUPLE_SCHEME_FACTORY")
        .append(").getScheme();")
        .append('\n');
    indent_down();
    sb.append(indent()).append("}").append('\n');
  }

  private void generateJavaStructTupleScheme(StringBuilder sb, TStruct tstruct) {
    sb.append(indent())
        .append("private static class ")
        .append(tstruct.getName())
        .append("TupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {")
        .append('\n');
    indent_up();
    sb.append(indent()).append(javaOverrideAnnotation()).append('\n');
    sb.append(indent()).append("public ").append(tstruct.getName()).append("TupleScheme getScheme() {").append('\n');
    indent_up();
    sb.append(indent()).append("return new ").append(tstruct.getName()).append("TupleScheme();").append('\n');
    indent_down();
    sb.append(indent()).append("}").append('\n');
    indent_down();
    sb.append(indent()).append("}").append('\n').append('\n');

    sb.append(indent())
        .append("private static class ")
        .append(tstruct.getName())
        .append("TupleScheme extends org.apache.thrift.scheme.TupleScheme<")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append("> {")
        .append('\n')
        .append('\n');
    indent_up();

    // Generate tuple writer and reader methods
    generateJavaStructTupleWriter(sb, tstruct);
    sb.append('\n');
    generateJavaStructTupleReader(sb, tstruct);

    indent_down();
    sb.append(indent()).append("}").append('\n').append('\n');
  }

  private void generateJavaStructTupleReader(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append('\n');
    sb.append(indent())
        .append("public void read(org.apache.thrift.protocol.TProtocol prot, ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" struct) throws org.apache.thrift.TException {")
        .append('\n');
    indent_up();
    sb.append(indent())
        .append("org.apache.thrift.protocol.TTupleProtocol iprot = ")
        .append("(org.apache.thrift.protocol.TTupleProtocol) prot;")
        .append('\n');

    int optionalCount = 0;
    final List<TField> fields = tstruct.getMembers();

    // First handle required fields
    for (TField field : fields) {
      if (field.getReq() == TField.Requirement.OPTIONAL || field.getReq() == TField.Requirement.OPT_IN_REQ_OUT) {
        optionalCount++;
      }
      if (field.getReq() == TField.Requirement.REQUIRED) {
        generateDeserializeField(sb, field, "struct.", false);
        sb.append(indent())
            .append("struct.set")
            .append(getCapName(field.getName()))
            .append(getCapName("isSet"))
            .append("(true);")
            .append('\n');
      }
    }

    // Then handle optional fields if any exist
    if (optionalCount > 0) {
      sb.append(indent())
          .append("java.util.BitSet incoming = iprot.readBitSet(")
          .append(optionalCount)
          .append(");")
          .append('\n');
      int i = 0;
      for (TField field : fields) {
        if (field.getReq() == TField.Requirement.OPTIONAL || field.getReq() == TField.Requirement.OPT_IN_REQ_OUT) {
          sb.append(indent()).append("if (incoming.get(").append(i).append(")) {").append('\n');
          indent_up();
          generateDeserializeField(sb, field, "struct.", false);
          sb.append(indent())
              .append("struct.set")
              .append(getCapName(field.getName()))
              .append(getCapName("isSet"))
              .append("(true);")
              .append('\n');
          indent_down();
          sb.append(indent()).append("}").append('\n');
          i++;
        }
      }
    }

    indent_down();
    sb.append(indent()).append("}").append('\n');
  }

  private void generateJavaStructTupleWriter(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append('\n');
    sb.append(indent())
        .append("public void write(org.apache.thrift.protocol.TProtocol prot, ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" struct) throws org.apache.thrift.TException {")
        .append('\n');
    indent_up();
    sb.append(indent())
        .append("org.apache.thrift.protocol.TTupleProtocol oprot = ")
        .append("(org.apache.thrift.protocol.TTupleProtocol) prot;")
        .append('\n');

    final List<TField> fields = tstruct.getMembers();
    boolean hasOptional = false;
    int optionalCount = 0;

    // First write all required fields
    for (TField field : fields) {
      if (field.getReq() == TField.Requirement.OPTIONAL || field.getReq() == TField.Requirement.OPT_IN_REQ_OUT) {
        optionalCount++;
        hasOptional = true;
      }
      if (field.getReq() == TField.Requirement.REQUIRED) {
        generateSerializeField(sb, field, "struct.", "", false);
      }
    }

    // Then write optional fields if any exist
    if (hasOptional) {
      sb.append(indent()).append("java.util.BitSet optionals = new java.util.BitSet();").append('\n');
      int i = 0;
      for (TField field : fields) {
        if (field.getReq() == TField.Requirement.OPTIONAL || field.getReq() == TField.Requirement.OPT_IN_REQ_OUT) {
          sb.append(indent()).append("if (struct.").append(generateIssetCheck(field)).append(") {").append('\n');
          indent_up();
          sb.append(indent()).append("optionals.set(").append(i).append(");").append('\n');
          indent_down();
          sb.append(indent()).append("}").append('\n');
          i++;
        }
      }

      sb.append(indent()).append("oprot.writeBitSet(optionals, ").append(optionalCount).append(");").append('\n');

      for (TField field : fields) {
        if (field.getReq() == TField.Requirement.OPTIONAL || field.getReq() == TField.Requirement.OPT_IN_REQ_OUT) {
          sb.append(indent()).append("if (struct.").append(generateIssetCheck(field)).append(") {").append('\n');
          indent_up();
          generateSerializeField(sb, field, "struct.", "", false);
          indent_down();
          sb.append(indent()).append("}").append('\n');
        }
      }
    }

    indent_down();
    sb.append(indent()).append("}").append('\n');
  }

  private List<TField> getSortedMembers(TStruct tstruct) {
    List<TField> members = new ArrayList<>(tstruct.getMembers());
    // C++ sorts by field key.
    members.sort((f1, f2) -> Integer.compare(f1.getKey(), f2.getKey()));
    return members;
  }

  private void generateJavaStructStandardScheme(StringBuilder sb, TStruct tstruct, boolean isResult) {
    sb.append(indent())
        .append("private static class ")
        .append(tstruct.getName())
        .append("StandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {\n");
    indent_up();
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public ").append(tstruct.getName()).append("StandardScheme getScheme() {\n");
    indent_up();
    sb.append(indent()).append("return new ").append(tstruct.getName()).append("StandardScheme();\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent())
        .append("private static class ")
        .append(tstruct.getName())
        .append("StandardScheme extends org.apache.thrift.scheme.StandardScheme<")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append("> {\n\n");
    indent_up();
    generateStandardReader(sb, tstruct);
    sb.append("\n");
    generateStandardWriter(sb, tstruct, isResult);
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateStandardReader(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent())
        .append("public void read(org.apache.thrift.protocol.TProtocol iprot, ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" struct) throws org.apache.thrift.TException {\n");
    indent_up();

    final List<TField> fields = tstruct.getMembers();

    sb.append(indent()).append("org.apache.thrift.protocol.TField schemeField;\n");
    sb.append(indent()).append("iprot.readStructBegin();\n");

    sb.append(indent()).append("while (true)\n");
    scope_up(sb); // while loop

    sb.append(indent()).append("schemeField = iprot.readFieldBegin();\n");
    sb.append(indent()).append("if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { \n");
    indent_up();
    sb.append(indent()).append("break;\n");
    indent_down();
    sb.append(indent()).append("}\n");

    sb.append(indent()).append("switch (schemeField.id) {\n");
    indent_up(); // switch

    for (TField field : fields) {
      sb.append(indent())
          .append("case ")
          .append(field.getKey())
          .append(": // ")
          .append(constantName(field.getName()))
          .append("\n");
      indent_up(); // case
      sb.append(indent()).append("if (schemeField.type == ").append(typeToEnum(field.getType())).append(") {\n");
      indent_up(); // if schemefield.type
      generateDeserializeField(sb, field, "struct.", true);
      sb.append(indent())
          .append("struct.set")
          .append(getCapName(field.getName()))
          .append(getCapName("IsSet"))
          .append("(true);\n");
      indent_down(); // if schemefield.type
      sb.append(indent()).append("} else { \n");
      sb.append(indent()).append("  org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);\n");
      sb.append(indent()).append("}\n");
      sb.append(indent()).append("break;\n");
      indent_down(); // case
    }

    sb.append(indent()).append("default:\n");
    sb.append(indent()).append("  org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);\n");

    indent_down();                     // switch
    sb.append(indent()).append("}\n"); // end switch

    sb.append(indent()).append("iprot.readFieldEnd();\n");

    scope_down(sb); // while loop
    sb.append(indent()).append("iprot.readStructEnd();\n");

    if (!options.isBeans()) {
      sb.append("\n");
      sb.append(indent()).append("// check for required fields of primitive type, which can't be "
                                 + "checked in the validate method\n");
      for (TField field : fields) {
        if (field.getReq() == TField.Requirement.REQUIRED && !typeCanBeNull(field.getType())) {
          sb.append(indent()).append("if (!struct.").append(generateIssetCheck(field)).append(") {\n");
          sb.append(indent())
              .append("  throw new org.apache.thrift.protocol.TProtocolException(\"Required field '")
              .append(field.getName())
              .append("' was not found in serialized data! Struct: \" + toString());\n");
          sb.append(indent()).append("}\n");
        }
      }
    }

    sb.append(indent()).append("struct.validate();\n");

    indent_down();
    sb.append(indent()).append("}\n");
  }

  private void generateStandardWriter(StringBuilder sb, TStruct tstruct, boolean isResult) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent())
        .append("public void write(org.apache.thrift.protocol.TProtocol oprot, ")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" struct) throws org.apache.thrift.TException {\n");
    indent_up();
    final List<TField> fields = getSortedMembers(tstruct);

    sb.append(indent()).append("struct.validate();\n\n");
    sb.append(indent()).append("oprot.writeStructBegin(STRUCT_DESC);\n");

    for (TField field : fields) {
      boolean nullAllowed = typeCanBeNull(field.getType());
      if (nullAllowed) {
        sb.append(indent())
            .append("if (struct.")
            .append(makeValidJavaIdentifier(field.getName()))
            .append(" != null) {\n");
        indent_up();
      }

      boolean optional = (field.getReq() == TField.Requirement.OPTIONAL) || (isResult && !nullAllowed);
      if (optional) {
        sb.append(indent()).append("if (struct.").append(generateIssetCheck(field)).append(") {\n");
        indent_up();
      }

      sb.append(indent())
          .append("oprot.writeFieldBegin(")
          .append(constantName(field.getName()))
          .append("_FIELD_DESC);\n");

      generateSerializeField(sb, field, "struct.", "", true);

      sb.append(indent()).append("oprot.writeFieldEnd();\n");

      if (optional) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
      if (nullAllowed) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
    }
    sb.append(indent()).append("oprot.writeFieldStop();\n");
    sb.append(indent()).append("oprot.writeStructEnd();\n");

    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructReadObject(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append("private void readObject(java.io.ObjectInputStream in) throws "
                               + "java.io.IOException, java.lang.ClassNotFoundException {\n");
    indent_up();
    sb.append(indent()).append("try {\n");
    indent_up();

    if (!tstruct.isUnion()) {
      switch (needs_isset(tstruct)) {
      case ISSET_NONE:
        break;
      case ISSET_PRIMITIVE:
        sb.append(indent()).append("// it doesn't seem like you should have to do this, but java serialization is "
                                   + "wacky, and doesn't call the default constructor.\n");
        sb.append(indent()).append("__isset_bitfield = 0;\n");
        break;
      case ISSET_BITSET:
        sb.append(indent()).append("// it doesn't seem like you should have to do this, but java serialization is "
                                   + "wacky, and doesn't call the default constructor.\n");
        // The size of the BitSet should match the number of primitive fields that need isset
        // tracking. This calculation is done in generateJavaStructDefinition. For now, let's assume
        // it's correctly initialized by the default constructor or that this logic is sufficient.
        // The C++ generator just uses new java.util.BitSet(1) here, which might be an
        // oversimplification. A more robust approach would be to get the actual size, but that
        // requires more context here. For now, replicating the C++ simplified approach.
        int i = 0;
        for (TField field : tstruct.getMembers()) {
          if (!typeCanBeNull(field.getType())) {
            i++;
          }
        }
        sb.append(indent()).append("__isset_bit_vector = new java.util.BitSet(1);\n");
        break;
      }
    }

    sb.append(indent()).append("read(new org.apache.thrift.protocol.TCompactProtocol(new "
                               + "org.apache.thrift.transport.TIOStreamTransport(in)));\n");
    indent_down();
    sb.append(indent()).append("} catch (org.apache.thrift.TException te) {\n");
    indent_up();
    sb.append(indent()).append("throw new java.io.IOException(te);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructWriteObject(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(
        "private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {\n");
    indent_up();
    sb.append(indent()).append("try {\n");
    indent_up();
    sb.append(indent()).append("write(new org.apache.thrift.protocol.TCompactProtocol(new "
                               + "org.apache.thrift.transport.TIOStreamTransport(out)));\n");
    indent_down();
    sb.append(indent()).append("} catch (org.apache.thrift.TException te) {\n");
    indent_up();
    sb.append(indent()).append("throw new java.io.IOException(te);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaValidator(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append("public void validate() throws org.apache.thrift.TException {\n");
    indent_up();

    final List<TField> fields = tstruct.getMembers();

    sb.append(indent()).append("// check for required fields\n");
    for (TField field : fields) {
      if (field.getReq() == TField.Requirement.REQUIRED) {
        if (options.isBeans()) {
          sb.append(indent()).append("if (!").append(generateIssetCheck(field)).append(") {\n");
          sb.append(indent())
              .append("  throw new org.apache.thrift.protocol.TProtocolException(\"Required field '")
              .append(field.getName())
              .append("' is unset! Struct:\" + toString());\n");
          sb.append(indent()).append("}\n\n");
        } else {
          if (typeCanBeNull(field.getType())) {
            sb.append(indent()).append("if (").append(makeValidJavaIdentifier(field.getName())).append(" == null) {\n");
            sb.append(indent())
                .append("  throw new org.apache.thrift.protocol.TProtocolException(\"Required field '")
                .append(field.getName())
                .append("' was not present! Struct: \" + toString());\n");
            sb.append(indent()).append("}\n");
          } else {
            sb.append(indent())
                .append("// alas, we cannot check '")
                .append(field.getName())
                .append("' because it's a primitive and you chose the non-beans generator.\n");
          }
        }
      }
    }

    sb.append(indent()).append("// check for sub-struct validity\n");
    for (TField field : fields) {
      TType type = getTrueType(field.getType());
      if (type.isStruct() && !((TStruct)type).isUnion()) {
        sb.append(indent()).append("if (").append(makeValidJavaIdentifier(field.getName())).append(" != null) {\n");
        sb.append(indent()).append("  ").append(makeValidJavaIdentifier(field.getName())).append(".validate();\n");
        sb.append(indent()).append("}\n");
      }
    }

    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructToString(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public java.lang.String toString() {\n");
    indent_up();

    sb.append(indent())
        .append("java.lang.StringBuilder sb = new java.lang.StringBuilder(\"")
        .append(tstruct.getName())
        .append("(\");\n");
    sb.append(indent()).append("boolean first = true;\n\n");

    final List<TField> fields = tstruct.getMembers();
    boolean firstFieldOutput = true;
    for (TField field : fields) {
      boolean couldBeUnset = field.getReq() == TField.Requirement.OPTIONAL;
      String fieldName = makeValidJavaIdentifier(field.getName());

      if (couldBeUnset) {
        sb.append(indent()).append("if (").append(generateIssetCheck(field)).append(") {\n");
        indent_up();
      }

      if (!firstFieldOutput) {
        sb.append(indent()).append("if (!first) sb.append(\", \");\n");
      }

      sb.append(indent()).append("sb.append(\"").append(field.getName()).append(":\");\n");
      boolean canBeNull = typeCanBeNull(field.getType());
      if (canBeNull) {
        sb.append(indent()).append("if (this.").append(fieldName).append(" == null) {\n");
        sb.append(indent()).append("  sb.append(\"null\");\n");
        sb.append(indent()).append("} else {\n");
        indent_up();
      }

      TType fieldTrueType = getTrueType(field.getType());
      if (fieldTrueType.isBinary() ||
          (fieldTrueType.isSet() && getTrueType(((TSet)fieldTrueType).getElemType()).isBinary()) ||
          (fieldTrueType.isList() && getTrueType(((TList)fieldTrueType).getElemType()).isBinary())) {
        sb.append(indent()).append("org.apache.thrift.TBaseHelper.toString(this.").append(fieldName).append(", sb);\n");
      } else {
        sb.append(indent()).append("sb.append(this.").append(fieldName).append(");\n");
      }

      if (canBeNull) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
      sb.append(indent()).append("first = false;\n");
      firstFieldOutput = false;

      if (couldBeUnset) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
    }
    sb.append(indent()).append("sb.append(\")\");\n");
    sb.append(indent()).append("return sb.toString();\n");

    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructWriter(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public void write(org.apache.thrift.protocol.TProtocol oprot) "
                               + "throws org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("scheme(oprot).write(oprot, this);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructResultWriter(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append("public void write(org.apache.thrift.protocol.TProtocol oprot) "
                               + "throws org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("scheme(oprot).write(oprot, this);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructReader(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public void read(org.apache.thrift.protocol.TProtocol iprot) "
                               + "throws org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("scheme(iprot).read(iprot, this);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructFieldById(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaNullableAnnotation()).append("\n");
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public _Fields fieldForId(int fieldId) {\n");
    indent_up();
    sb.append(indent()).append("return _Fields.findByThriftId(fieldId);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructCompareTo(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public int compareTo(").append(typeName(tstruct)).append(" other) {\n");
    indent_up();

    sb.append(indent()).append("if (!getClass().equals(other.getClass())) {\n");
    indent_up();
    sb.append(indent()).append("return getClass().getName().compareTo(other.getClass().getName());\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent()).append("int lastComparison = 0;\n\n");

    List<TField> members = tstruct.getMembers();
    for (TField field : members) {
      sb.append(indent())
          .append("lastComparison = java.lang.Boolean.compare(")
          .append(generateIssetCheck(field))
          .append(", other.")
          .append(generateIssetCheck(field))
          .append(");\n");
      sb.append(indent()).append("if (lastComparison != 0) {\n");
      indent_up();
      sb.append(indent()).append("return lastComparison;\n");
      indent_down();
      sb.append(indent()).append("}\n");

      sb.append(indent()).append("if (").append(generateIssetCheck(field)).append(") {\n");
      indent_up();
      sb.append(indent())
          .append("lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.")
          .append(makeValidJavaIdentifier(field.getName()))
          .append(", other.")
          .append(makeValidJavaIdentifier(field.getName()))
          .append(");\n");
      sb.append(indent()).append("if (lastComparison != 0) {\n");
      indent_up();
      sb.append(indent()).append("return lastComparison;\n");
      indent_down();
      sb.append(indent()).append("}\n");
      indent_down();
      sb.append(indent()).append("}\n");
    }

    sb.append(indent()).append("return 0;\n");

    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaStructEquality(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public boolean equals(java.lang.Object that) {\n");
    indent_up();
    sb.append(indent()).append("if (that instanceof ").append(makeValidJavaIdentifier(tstruct.getName())).append(")\n");
    sb.append(indent())
        .append("  return this.equals((")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(")that);\n");
    sb.append(indent()).append("return false;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent())
        .append("public boolean equals(")
        .append(makeValidJavaIdentifier(tstruct.getName()))
        .append(" that) {\n");
    indent_up();
    sb.append(indent()).append("if (that == null)\n");
    sb.append(indent()).append("  return false;\n");
    sb.append(indent()).append("if (this == that)\n");
    sb.append(indent()).append("  return true;\n");

    List<TField> members = tstruct.getMembers();
    for (TField field : members) {
      sb.append("\n");

      TType type = getTrueType(field.getType());
      boolean isOptional = field.getReq() == TField.Requirement.OPTIONAL;
      boolean canBeNull = typeCanBeNull(type);
      String name = field.getName();

      String thisPresent = "true";
      String thatPresent = "true";

      if (isOptional || canBeNull) {
        thisPresent += " && this." + generateIssetCheck(field);
        thatPresent += " && that." + generateIssetCheck(field);
      }

      sb.append(indent()).append("boolean this_present_").append(name).append(" = ").append(thisPresent).append(";\n");
      sb.append(indent()).append("boolean that_present_").append(name).append(" = ").append(thatPresent).append(";\n");
      sb.append(indent())
          .append("if (this_present_")
          .append(name)
          .append(" || that_present_")
          .append(name)
          .append(") {\n");
      indent_up();
      sb.append(indent())
          .append("if (!(this_present_")
          .append(name)
          .append(" && that_present_")
          .append(name)
          .append("))\n");
      sb.append(indent()).append("  return false;\n");

      String unequal;
      if (type.isBinary()) {
        unequal = "!this." + makeValidJavaIdentifier(name) + ".equals(that." + makeValidJavaIdentifier(name) + ")";
      } else if (canBeNull) {
        // Use Objects.equals for nullable fields to handle nulls gracefully
        unequal = "!this." + makeValidJavaIdentifier(name) + ".equals(that." + makeValidJavaIdentifier(name) + ")";
      } else {
        // For primitive types
        unequal = "this." + makeValidJavaIdentifier(name) + " != that." + makeValidJavaIdentifier(name);
      }

      sb.append(indent()).append("if (").append(unequal).append(")\n");
      sb.append(indent()).append("  return false;\n");

      indent_down();
      sb.append(indent()).append("}\n");
    }
    sb.append("\n");
    sb.append(indent()).append("return true;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // hashCode
    final int MUL = 8191;
    final int B_YES = 131071;
    final int B_NO = 524287;
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public int hashCode() {\n");
    indent_up();
    sb.append(indent()).append("int hashCode = 1;\n");

    for (TField field : members) {
      sb.append("\n");

      TType type = getTrueType(field.getType());
      boolean isOptionalField = field.getReq() == TField.Requirement.OPTIONAL;
      boolean canBeNullField = typeCanBeNull(type);
      String name = makeValidJavaIdentifier(field.getName());

      if (isOptionalField || canBeNullField) {
        sb.append(indent())
            .append("hashCode = hashCode * ")
            .append(MUL)
            .append(" + ((")
            .append(generateIssetCheck(field))
            .append(") ? ")
            .append(B_YES)
            .append(" : ")
            .append(B_NO)
            .append(");\n");
      }

      if (isOptionalField || canBeNullField) {
        sb.append(indent())
            .append("if (")
            .append(generateIssetCheck(field))
            .append(")\n"); // Only include in hash if set
        indent_up();
      }

      if (type.isEnum()) {
        sb.append(indent())
            .append("hashCode = hashCode * ")
            .append(MUL)
            .append(" + ")
            .append(name)
            .append(".getValue();\n");
      } else if (type.isBaseType()) {
        TBaseType.Base base = ((TBaseType)type).getBase();
        switch (base) {
        case TYPE_STRING:
        case TYPE_UUID: // UUIDs are typically represented as Strings or specific UUID objects
          sb.append(indent())
              .append("hashCode = hashCode * ")
              .append(MUL)
              .append(" + ")
              .append(name)
              .append(".hashCode();\n");
          break;
        case TYPE_BOOL:
          sb.append(indent())
              .append("hashCode = hashCode * ")
              .append(MUL)
              .append(" + ((")
              .append(name)
              .append(") ? ")
              .append(B_YES)
              .append(" : ")
              .append(B_NO)
              .append(");\n");
          break;
        case TYPE_I8:
          sb.append(indent())
              .append("hashCode = hashCode * ")
              .append(MUL)
              .append(" + (int) (")
              .append(name)
              .append(");\n");
          break;
        case TYPE_I16:
        case TYPE_I32:
          sb.append(indent()).append("hashCode = hashCode * ").append(MUL).append(" + ").append(name).append(";\n");
          break;
        case TYPE_I64:
          sb.append(indent())
              .append("hashCode = hashCode * ")
              .append(MUL)
              .append(" + org.apache.thrift.TBaseHelper.hashCode(")
              .append(name)
              .append(");\n");
          break;
        case TYPE_DOUBLE:
          sb.append(indent())
              .append("hashCode = hashCode * ")
              .append(MUL)
              .append(" + org.apache.thrift.TBaseHelper.hashCode(")
              .append(name)
              .append(");\n");
          break;
        case TYPE_VOID: // Should not happen for a field
          throw new RuntimeException("compiler error: a struct field cannot be void");
        default:
          throw new RuntimeException("compiler error: the following base type has no hashcode generator: " + base);
        }
      } else { // Containers, Structs, Exceptions (which are structs)
        sb.append(indent())
            .append("hashCode = hashCode * ")
            .append(MUL)
            .append(" + ")
            .append(name)
            .append(".hashCode();\n");
      }

      if (isOptionalField || canBeNullField) {
        indent_down(); // Closes the `if (isSet)` block
      }
    }

    sb.append("\n");
    sb.append(indent()).append("return hashCode;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateGenericIssetMethod(StringBuilder sb, TStruct tstruct) {
    List<TField> fields = tstruct.getMembers();

    sb.append(indent()).append("/** Returns true if field corresponding to fieldID is set (has "
                               + "been assigned a value) and false otherwise */\n");
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public boolean isSet(_Fields field) {\n");
    indent_up();
    sb.append(indent()).append("if (field == null) {\n");
    indent_up();
    sb.append(indent()).append("throw new java.lang.IllegalArgumentException();\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent()).append("switch (field) {\n");
    indent_up();

    for (TField field : fields) {
      sb.append(indent()).append("case ").append(constantName(field.getName())).append(":\n");
      indent_up();
      sb.append(indent()).append("return ").append(generateIssetCheck(field)).append(";\n");
      indent_down();
    }

    indent_down();                     // end switch cases
    sb.append(indent()).append("}\n"); // end switch
    sb.append(indent()).append("throw new java.lang.IllegalStateException();\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateReflectionGetters(StringBuilder sb, TType type, String fieldName, String capName) {
    sb.append(indent()).append("case ").append(constantName(fieldName)).append(":\n");
    indent_up();
    sb.append(indent()).append("return ").append(type.isBool() ? "is" : "get").append(capName).append("();\n");
    indent_down();
  }

  private void generateReflectionSetters(StringBuilder sb, TType type, String fieldName, String capName) {
    boolean isBinary = type.isBinary(); // Assuming TType has an isBinary() method
    sb.append(indent()).append("case ").append(constantName(fieldName)).append(":\n");
    indent_up();
    sb.append(indent()).append("if (value == null) {\n");
    indent_up();
    sb.append(indent()).append("unset").append(getCapName(fieldName)).append("();\n");
    indent_down();
    sb.append(indent()).append("} else {\n");
    indent_up();
    if (isBinary) {
      sb.append(indent()).append("if (value instanceof byte[]) {\n");
      indent_up();
      sb.append(indent()).append("set").append(capName).append("((byte[])value);\n");
      indent_down();
      sb.append(indent()).append("} else {\n");
      indent_up();
      sb.append(indent()).append("set").append(capName).append("((java.nio.ByteBuffer)value);\n");
      indent_down();
      sb.append(indent()).append("}\n");
    } else {
      sb.append(indent())
          .append("set")
          .append(capName)
          .append("((")
          .append(typeName(type, true, false))
          .append(")value);\n");
    }
    indent_down();
    sb.append(indent()).append("}\n");
    sb.append(indent()).append("break;\n");
    indent_down();
  }

  private void generateGenericFieldGettersSetters(StringBuilder sb, TStruct tstruct) {
    StringBuilder getterStream = new StringBuilder();
    StringBuilder setterStream = new StringBuilder();

    List<TField> fields = tstruct.getMembers();
    for (TField field : fields) {
      TType type = getTrueType(field.getType());
      String fieldName = field.getName();
      String capName = getCapName(fieldName);

      generateReflectionSetters(setterStream, type, fieldName, capName);
      generateReflectionGetters(getterStream, type, fieldName, capName);
    }

    // Create the setter
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent())
        .append("public void setFieldValue(_Fields field, ")
        .append(javaNullableAnnotation())
        .append(" java.lang.Object value) {\n");
    indent_up();
    sb.append(indent()).append("switch (field) {\n");
    sb.append(setterStream.toString()); // Append the generated cases
    sb.append(indent()).append("}\n");  // end switch
    indent_down();
    sb.append(indent()).append("}\n\n");

    // Create the getter
    sb.append(indent()).append(javaNullableAnnotation()).append("\n");
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public java.lang.Object getFieldValue(_Fields field) {\n");
    indent_up();
    sb.append(indent()).append("switch (field) {\n");
    sb.append(getterStream.toString()); // Append the generated cases
    sb.append(indent()).append("}\n");  // end switch
    sb.append(indent()).append("throw new java.lang.IllegalStateException();\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateJavaBeanBoilerplate(StringBuilder sb, TStruct tstruct) {
    isset_type issetType = needs_isset(tstruct);
    List<TField> fields = tstruct.getMembers();
    for (TField field : fields) {
      TType type = getTrueType(field.getType());
      String fieldName = field.getName();
      String capName = getCapName(fieldName);
      boolean optional = options.getOptionType() != null && field.getReq() == TField.Requirement.OPTIONAL;
      boolean isDeprecated = isDeprecated(field.getAnnotations());

      if (type.isContainer()) {
        // Method to return the size of the collection
        if (optional) {
          if (isDeprecated) {
            sb.append(indent()).append("@Deprecated\n");
          }

          String optionClass = "java.util.Optional".equals(options.getOptionType()) ? "java.util.Optional"
                                                                                      : "org.apache.thrift.Option";
          String optionEmpty = "java.util.Optional".equals(options.getOptionType()) ? ".empty()" : ".none()";
          String optionOf = "java.util.Optional".equals(options.getOptionType()) ? ".of(" : ".some(";

          sb.append(indent())
              .append("public ")
              .append(optionClass)
              .append("<Integer> get")
              .append(capName)
              .append(getCapName("Size() {\n")); // Corrected: Added () for method
          indent_up();
          sb.append(indent()).append("if (this.").append(makeValidJavaIdentifier(fieldName)).append(" == null) {\n");
          indent_up();
          sb.append(indent()).append("return ").append(optionClass).append(optionEmpty).append(";\n");
          indent_down();
          sb.append(indent()).append("} else {\n");
          indent_up();
          sb.append(indent())
              .append("return ")
              .append(optionClass)
              .append(optionOf)
              .append("this.")
              .append(makeValidJavaIdentifier(fieldName))
              .append(".size());\n");
          indent_down();
          sb.append(indent()).append("}\n");
          indent_down();
          sb.append(indent()).append("}\n\n");
        } else {
          if (isDeprecated) {
            sb.append(indent()).append("@Deprecated\n");
          }
          sb.append(indent())
              .append("public int get")
              .append(capName)
              .append(getCapName("Size() {\n")); // Corrected: Added () for method
          indent_up();
          sb.append(indent())
              .append("return (this.")
              .append(makeValidJavaIdentifier(fieldName))
              .append(" == null) ? 0 : ")
              .append("this.")
              .append(makeValidJavaIdentifier(fieldName))
              .append(".size();\n");
          indent_down();
          sb.append(indent()).append("}\n\n");
        }
      }

      if (type.isSet() || type.isList()) {
        TType elementType;
        if (type.isSet()) {
          elementType = ((TSet)type).getElemType();
        } else {
          elementType = ((TList)type).getElemType();
        }

        // Iterator getter for sets and lists
        if (optional) {
          if (isDeprecated) {
            sb.append(indent()).append("@Deprecated\n");
          }
          String optionClass = "java.util.Optional".equals(options.getOptionType()) ? "java.util.Optional"
                                                                                      : "org.apache.thrift.Option";
          String optionEmpty = "java.util.Optional".equals(options.getOptionType()) ? ".empty()" : ".none()";
          String optionOf = "java.util.Optional".equals(options.getOptionType()) ? ".of(" : ".some(";

          sb.append(indent())
              .append("public ")
              .append(optionClass)
              .append("<")
              .append("java.util.Iterator<")
              .append(typeName(elementType, true, false))
              .append(">> get")
              .append(capName)
              .append(getCapName("Iterator() {\n")); // Corrected: Added ()
          indent_up();
          sb.append(indent()).append("if (this.").append(makeValidJavaIdentifier(fieldName)).append(" == null) {\n");
          indent_up();
          sb.append(indent()).append("return ").append(optionClass).append(optionEmpty).append(";\n");
          indent_down();
          sb.append(indent()).append("} else {\n");
          indent_up();
          sb.append(indent())
              .append("return ")
              .append(optionClass)
              .append(optionOf)
              .append("this.")
              .append(makeValidJavaIdentifier(fieldName))
              .append(".iterator());\n");
          indent_down();
          sb.append(indent()).append("}\n");
          indent_down();
          sb.append(indent()).append("}\n\n");
        } else {
          if (isDeprecated) {
            sb.append(indent()).append("@Deprecated\n");
          }
          sb.append(indent()).append(javaNullableAnnotation()).append("\n");
          sb.append(indent())
              .append("public java.util.Iterator<")
              .append(typeName(elementType, true, false))
              .append("> get")
              .append(capName)
              .append(getCapName("Iterator() {\n")); // Corrected: Added ()
          indent_up();
          sb.append(indent())
              .append("return (this.")
              .append(makeValidJavaIdentifier(fieldName))
              .append(" == null) ? null : ")
              .append("this.")
              .append(makeValidJavaIdentifier(fieldName))
              .append(".iterator();\n");
          indent_down();
          sb.append(indent()).append("}\n\n");
        }

        // Add to set or list, create if the set/list is null
        if (isDeprecated) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent())
            .append("public void add")
            .append(getCapName("To"))
            .append(capName)
            .append("(")
            .append(typeName(elementType))
            .append(" elem) {\n");
        indent_up();
        sb.append(indent()).append("if (this.").append(makeValidJavaIdentifier(fieldName)).append(" == null) {\n");
        indent_up();
        sb.append(indent()).append("this.").append(makeValidJavaIdentifier(fieldName));
        if (isEnumSet(type)) {
          sb.append(" = ")
              .append(typeName(type, false, true, true))
              .append(".noneOf(")
              .append(innerEnumTypeName(type))
              .append(");\n");
        } else {
          sb.append(" = new ").append(typeName(type, false, true)).append("();\n");
        }
        indent_down();
        sb.append(indent()).append("}\n");
        sb.append(indent()).append("this.").append(makeValidJavaIdentifier(fieldName)).append(".add(elem);\n");
        indent_down();
        sb.append(indent()).append("}\n\n");

      } else if (type.isMap()) {
        TType keyType = ((TMap)type).getKeyType();
        TType valType = ((TMap)type).getValType();

        if (isDeprecated) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent())
            .append("public void put")
            .append(getCapName("To"))
            .append(capName)
            .append("(")
            .append(typeName(keyType))
            .append(" key, ")
            .append(typeName(valType))
            .append(" val) {\n");
        indent_up();
        sb.append(indent()).append("if (this.").append(makeValidJavaIdentifier(fieldName)).append(" == null) {\n");
        indent_up();
        String constructorArgs = "";
        if (isEnumMap(type)) {
          constructorArgs = innerEnumTypeName(type);
        }
        sb.append(indent())
            .append("this.")
            .append(makeValidJavaIdentifier(fieldName))
            .append(" = new ")
            .append(typeName(type, false, true))
            .append("(")
            .append(constructorArgs)
            .append(");\n");
        indent_down();
        sb.append(indent()).append("}\n");
        sb.append(indent()).append("this.").append(makeValidJavaIdentifier(fieldName)).append(".put(key, val);\n");
        indent_down();
        sb.append(indent()).append("}\n\n");
      }

      // Simple getter
      generateJavaDocField(sb, field);
      if (type.isBinary()) {
        if (isDeprecated) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent()).append("public byte[] get").append(capName).append("() {\n");
        indent_up();
        sb.append(indent())
            .append("set")
            .append(capName)
            .append("(org.apache.thrift.TBaseHelper.rightSize(")
            .append(makeValidJavaIdentifier(fieldName))
            .append("));\n");
        sb.append(indent())
            .append("return ")
            .append(makeValidJavaIdentifier(fieldName))
            .append(" == null ? null : ")
            .append(makeValidJavaIdentifier(fieldName))
            .append(".array();\n");
        indent_down();
        sb.append(indent()).append("}\n\n");

        sb.append(indent())
            .append("public java.nio.ByteBuffer buffer")
            .append(getCapName("For"))
            .append(capName) // Corrected: For -> For
            .append("() {\n");
        indent_up();
        if (options.isUnsafeBinaries()) {
          sb.append(indent()).append("return ").append(makeValidJavaIdentifier(fieldName)).append(";\n");
        } else {
          sb.append(indent())
              .append("return org.apache.thrift.TBaseHelper.copyBinary(")
              .append(makeValidJavaIdentifier(fieldName))
              .append(");\n");
        }
        indent_down();
        sb.append(indent()).append("}\n\n");

      } else {
        if (optional) {
          if (isDeprecated) {
            sb.append(indent()).append("@Deprecated\n");
          }
          String optionClass = "java.util.Optional".equals(options.getOptionType()) ? "java.util.Optional"
                                                                                      : "org.apache.thrift.Option";
          String optionEmpty = "java.util.Optional".equals(options.getOptionType()) ? ".empty()" : ".none()";
          String optionOf = "java.util.Optional".equals(options.getOptionType()) ? ".of(" : ".some(";

          sb.append(indent())
              .append("public ")
              .append(optionClass)
              .append("<")
              .append(typeName(type, true))
              .append(">");
          if (type.isBaseType() && ((TBaseType)type).getBase() == TBaseType.Base.TYPE_BOOL) {
            sb.append(" is");
          } else {
            sb.append(" get");
          }
          sb.append(capName).append("() {\n");
          indent_up();
          sb.append(indent()).append("if (this.isSet").append(capName).append("()) {\n");
          indent_up();
          sb.append(indent())
              .append("return ")
              .append(optionClass)
              .append(optionOf)
              .append("this.")
              .append(makeValidJavaIdentifier(fieldName))
              .append(");\n");
          indent_down();
          sb.append(indent()).append("} else {\n");
          indent_up();
          sb.append(indent()).append("return ").append(optionClass).append(optionEmpty).append(";\n");
          indent_down();
          sb.append(indent()).append("}\n");
          indent_down();
          sb.append(indent()).append("}\n\n");
        } else {
          if (isDeprecated) {
            sb.append(indent()).append("@Deprecated\n");
          }
          if (typeCanBeNull(type)) {
            sb.append(indent()).append(javaNullableAnnotation()).append("\n");
          }
          sb.append(indent()).append("public ").append(typeName(type));
          if (type.isBaseType() && ((TBaseType)type).getBase() == TBaseType.Base.TYPE_BOOL) {
            sb.append(" is");
          } else {
            sb.append(" get");
          }
          sb.append(capName).append("() {\n");
          indent_up();
          sb.append(indent()).append("return this.").append(makeValidJavaIdentifier(fieldName)).append(";\n");
          indent_down();
          sb.append(indent()).append("}\n\n");
        }
      }

      // Simple setter
      generateJavaDocField(sb, field);
      if (type.isBinary()) {
        if (isDeprecated) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent()).append("public ");
        if (options.isBeans()) {
          sb.append("void");
        } else {
          sb.append(typeName(tstruct));
        }
        sb.append(" set").append(capName).append("(byte[] ").append(makeValidJavaIdentifier(fieldName)).append(") {\n");

        sb.append(indent())
            .append("this.")
            .append(makeValidJavaIdentifier(fieldName))
            .append(" = ")
            .append(makeValidJavaIdentifier(fieldName))
            .append(" == null ? (java.nio.ByteBuffer)null");
        sb.append(indent());
        if (options.isUnsafeBinaries()) {
          sb.append(" : java.nio.ByteBuffer.wrap(").append(makeValidJavaIdentifier(fieldName)).append(");\n");
        } else {
          sb.append(" : java.nio.ByteBuffer.wrap(").append(makeValidJavaIdentifier(fieldName)).append(".clone());\n");
        }

        if (!options.isBeans()) {
          sb.append(indent()).append("return this;\n");
        }
        indent_down();
        sb.append(indent()).append("}\n\n");
        // Setter for ByteBuffer
        if (isDeprecated) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent()).append("public ");
        if (options.isBeans()) {
          sb.append("void");
        } else {
          sb.append(typeName(tstruct));
        }
        sb.append(" set")
            .append(capName)
            .append("(@org.apache.thrift.annotation.Nullable java.nio.ByteBuffer ")
            .append(makeValidJavaIdentifier(fieldName))
            .append(") {\n");
        indent_up();
        sb.append(indent()).append("this.").append(makeValidJavaIdentifier(fieldName)).append(" = ");
        if (!options.isUnsafeBinaries()) {
          sb.append("org.apache.thrift.TBaseHelper.copyBinary(").append(makeValidJavaIdentifier(fieldName)).append(")");
        } else {
          sb.append(makeValidJavaIdentifier(fieldName));
        }
        sb.append(";\n");
        if (!options.isBeans()) {
          sb.append(indent()).append("return this;\n");
        }
        indent_down();
        sb.append(indent()).append("}\n\n");

      } else { // Common setter part for non-binary
        if (isDeprecated) {
          sb.append(indent()).append("@Deprecated\n");
        }
        sb.append(indent()).append("public ");
        if (options.isBeans()) {
          sb.append("void");
        } else {
          sb.append(typeName(tstruct));
        }
        sb.append(" set")
            .append(capName)
            .append("(")
            .append(typeCanBeNull(type) ? (javaNullableAnnotation() + " ") : "")
            .append(typeName(type))
            .append(" ")
            .append(makeValidJavaIdentifier(fieldName))
            .append(") {\n");
        indent_up();
        sb.append(indent())
            .append("this.")
            .append(makeValidJavaIdentifier(fieldName))
            .append(" = ")
            .append(makeValidJavaIdentifier(fieldName))
            .append(";\n");
        generate_isset_set(sb, field, "");
        if (!options.isBeans()) {
          sb.append(indent()).append("return this;\n");
        }
        indent_down();
        sb.append(indent()).append("}\n\n");
      }

      // Unsetter
      if (isDeprecated) {
        sb.append(indent()).append("@Deprecated\n");
      }
      sb.append(indent()).append("public void unset").append(capName).append("() {\n");
      indent_up();
      if (typeCanBeNull(type)) {
        sb.append(indent()).append("this.").append(makeValidJavaIdentifier(fieldName)).append(" = null;\n");
      } else { // Non-nullable primitive
        isset_type currentIssetType =
            needs_isset(tstruct, new StringBuilder()); // Check specific type for current field
        if (currentIssetType == isset_type.ISSET_PRIMITIVE) {
          sb.append(indent())
              .append("__isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, ")
              .append(isset_field_id(field))
              .append(");\n");
        } else if (currentIssetType == isset_type.ISSET_BITSET) {
          sb.append(indent()).append("__isset_bit_vector.clear(").append(isset_field_id(field)).append(");\n");
        }
        // If ISSET_NONE, nothing to do for unsetting a primitive.
      }
      indent_down();
      sb.append(indent()).append("}\n\n");

      // isSet method
      sb.append(indent())
          .append("/** Returns true if field ")
          .append(fieldName)
          .append(" is set (has been assigned a value) and false otherwise */\n");
      if (isDeprecated) {
        sb.append(indent()).append("@Deprecated\n");
      }
      sb.append(indent())
          .append("public boolean is")
          .append(getCapName("Set"))
          .append(capName)
          .append("() {\n"); // Corrected: Set -> Set
      indent_up();
      if (typeCanBeNull(type)) {
        sb.append(indent()).append("return this.").append(makeValidJavaIdentifier(fieldName)).append(" != null;\n");
      } else {
        isset_type currentIssetType = needs_isset(tstruct, new StringBuilder());
        if (currentIssetType == isset_type.ISSET_PRIMITIVE) {
          sb.append(indent())
              .append("return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, ")
              .append(isset_field_id(field))
              .append(");\n");
        } else if (currentIssetType == isset_type.ISSET_BITSET) {
          sb.append(indent()).append("return __isset_bit_vector.get(").append(isset_field_id(field)).append(");\n");
        } else { // ISSET_NONE - field is always "set" if it's a primitive and not part of a bitmask
          sb.append(indent()).append("return true; // Primitive type without explicit isset tracking\n");
        }
      }
      indent_down();
      sb.append(indent()).append("}\n\n");

      if (isDeprecated) {
        sb.append(indent()).append("@Deprecated\n");
      }
      sb.append(indent())
          .append("public void set")
          .append(capName)
          .append(getCapName("IsSet"))
          .append("(boolean value) {\n"); // Corrected: IsSet -> IsSet
      indent_up();
      if (typeCanBeNull(type)) {
        sb.append(indent()).append("if (!value) {\n");
        indent_up();
        sb.append(indent()).append("this.").append(makeValidJavaIdentifier(fieldName)).append(" = null;\n");
        indent_down();
        sb.append(indent()).append("}\n");
      } else {
        isset_type currentIssetType = needs_isset(tstruct, new StringBuilder());
        if (currentIssetType == isset_type.ISSET_PRIMITIVE) {
          sb.append(indent())
              .append("__isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, ")
              .append(isset_field_id(field))
              .append(", value);\n");
        } else if (currentIssetType == isset_type.ISSET_BITSET) {
          sb.append(indent()).append("__isset_bit_vector.set(").append(isset_field_id(field)).append(", value);\n");
        }
        // If ISSET_NONE, this method might be a no-op or throw an error for primitives.
        // C++ version just sets the bit; for ISSET_NONE this has no effect if no bitmask exists.
      }
      indent_down();
      sb.append(indent()).append("}\n\n");
    }
  }

  private void generateJavaStructClear(StringBuilder sb, TStruct tstruct) {
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public void clear() {\n");
    indent_up();

    for (TField field : tstruct.getMembers()) {
      TType type = getTrueType(field.getType());
      String fieldName = makeValidJavaIdentifier(field.getName());

      if (field.getValue() != null) {
        // This assumes printConstValue correctly handles StringBuilder
        printConstValue(sb, "this." + fieldName, type, field.getValue(), true, true);
        continue;
      }

      if (typeCanBeNull(type)) {
        if (options.isReuseObjects() && (type.isContainer() || type.isStruct())) {
          sb.append(indent()).append("if (this.").append(fieldName).append(" != null) {\n");
          indent_up();
          sb.append(indent()).append("this.").append(fieldName).append(".clear();\n");
          indent_down();
          sb.append(indent()).append("}\n");
        } else {
          sb.append(indent()).append("this.").append(fieldName).append(" = null;\n");
        }
        continue;
      }

      // Must be a base type, needs explicit unsetting
      // Use the proper setter for "isSet" which handles the bitmask
      sb.append(indent()).append("set").append(getCapName(field.getName())).append("IsSet").append("(false);\n");

      TBaseType baseType = (TBaseType)type;
      switch (baseType.getBase()) {
      case TYPE_I8:
      case TYPE_I16:
      case TYPE_I32:
      case TYPE_I64:
        sb.append(indent()).append("this.").append(fieldName).append(" = 0;\n");
        break;
      case TYPE_DOUBLE:
        sb.append(indent()).append("this.").append(fieldName).append(" = 0.0;\n");
        break;
      case TYPE_BOOL:
        sb.append(indent()).append("this.").append(fieldName).append(" = false;\n");
        break;
      case TYPE_VOID:   // Should not happen
      case TYPE_STRING: // Nullable, handled above
      case TYPE_UUID:   // Nullable, handled above
      default:
        // This case should ideally not be reached for non-nullable base types that aren't
        // numeric/bool as those are covered by the explicit cases. binary is nullable.
        break; // No default assignment for other non-nullable base types if any.
      }
    }
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateDeepCopyNonContainer(StringBuilder sb, String sourceName, String destName, TType type) {
    // destName is not used in the C++ version, so it's omitted here too for directness.
    type = getTrueType(type);
    if (type.isBaseType() || type.isEnum()) { // Typedefs are resolved by getTrueType
      if (type.isBinary()) {                  // Assumes TType.isBinary() exists
        sb.append("org.apache.thrift.TBaseHelper.copyBinary(").append(sourceName).append(")");
      } else {
        sb.append(sourceName); // Everything else can be copied directly
      }
    } else { // Structs, Xceptions
      sb.append("new ").append(typeName(type, true, true)).append("(").append(sourceName).append(")");
    }
  }

  private void generateDeepCopyContainer(StringBuilder sb, String sourceNameP1, String sourceNameP2, String resultName,
                                         TType type) {

    io.github.decster.ast.TContainer container = (io.github.decster.ast.TContainer)type;
    String sourceName;
    if (sourceNameP2 == null || sourceNameP2.isEmpty()) {
      sourceName = sourceNameP1;
    } else {
      sourceName = sourceNameP1 + "." + makeValidJavaIdentifier(sourceNameP2);
    }

    boolean copyConstructContainer;
    if (container.isMap()) {
      TMap tmap = (TMap)container;
      //      copyConstructContainer = getTrueType(tmap.getKeyType()).isBaseType() &&
      //                               getTrueType(tmap.getValType()).isBaseType();
      copyConstructContainer = tmap.getKeyType().isBaseType() && tmap.getValType().isBaseType();
    } else { // List or Set
      TType elemType;
      if (container.isList()) {
        elemType = ((TList)container).getElemType();
      } else { // isSet
        elemType = ((TSet)container).getElemType();
      }
      // copyConstructContainer = getTrueType(elemType).isBaseType();
      copyConstructContainer = elemType.isBaseType();
    }

    if (copyConstructContainer) {
      sb.append(indent())
          .append(typeName(type, true, false))
          .append(" ")
          .append(resultName)
          .append(" = new ")
          .append(typeName(container, false, true))
          .append("(")
          .append(sourceName)
          .append(");\n");
      return;
    }

    String constructorArgs = "";
    if (isEnumSet(container) || isEnumMap(container)) {
      constructorArgs = innerEnumTypeName(container);
    } else if (!(options.isSortedContainers() && (container.isMap() || container.isSet()))) {
      constructorArgs = sourceName + ".size()";
    }

    if (isEnumSet(container)) {
      sb.append(indent())
          .append(typeName(type, true, false))
          .append(" ")
          .append(resultName)
          .append(" = ")
          .append(typeName(container, false, true, true))
          .append(".noneOf(")
          .append(constructorArgs)
          .append(");\n");
    } else {
      sb.append(indent())
          .append(typeName(type, true, false))
          .append(" ")
          .append(resultName)
          .append(" = new ")
          .append(typeName(container, false, true))
          .append("(")
          .append(constructorArgs)
          .append(");\n");
    }

    String iteratorElementName = sourceNameP1.replace(".", "_") + "_element";
    String resultElementName = resultName + "_copy";

    if (container.isMap()) {
      TMap tmap = (TMap)container;
      TType keyType = tmap.getKeyType();
      TType valType = tmap.getValType();

      sb.append(indent())
          .append("for (java.util.Map.Entry<")
          .append(typeName(keyType, true, false))
          .append(", ")
          .append(typeName(valType, true, false))
          .append("> ")
          .append(iteratorElementName)
          .append(" : ")
          .append(sourceName)
          .append(".entrySet()) {\n");
      indent_up();
      sb.append("\n"); // C++ code has a blank line here

      sb.append(indent())
          .append(typeName(keyType, true, false))
          .append(" ")
          .append(iteratorElementName)
          .append("_key = ")
          .append(iteratorElementName)
          .append(".getKey();\n");
      sb.append(indent())
          .append(typeName(valType, true, false))
          .append(" ")
          .append(iteratorElementName)
          .append("_value = ")
          .append(iteratorElementName)
          .append(".getValue();\n");
      sb.append("\n"); // C++ code has a blank line here

      if (getTrueType(keyType).isContainer()) {
        generateDeepCopyContainer(sb, iteratorElementName + "_key", "", resultElementName + "_key", keyType);
      } else {
        sb.append(indent())
            .append(typeName(keyType, true, false))
            .append(" ")
            .append(resultElementName)
            .append("_key = ");
        generateDeepCopyNonContainer(sb, iteratorElementName + "_key", resultElementName + "_key", keyType);
        sb.append(";\n");
      }
      sb.append("\n"); // C++ code has a blank line here

      if (getTrueType(valType).isContainer()) {
        generateDeepCopyContainer(sb, iteratorElementName + "_value", "", resultElementName + "_value", valType);
      } else {
        sb.append(indent())
            .append(typeName(valType, true, false))
            .append(" ")
            .append(resultElementName)
            .append("_value = ");
        generateDeepCopyNonContainer(sb, iteratorElementName + "_value", resultElementName + "_value", valType);
        sb.append(";\n");
      }
      sb.append("\n"); // C++ code has a blank line here

      sb.append(indent())
          .append(resultName)
          .append(".put(")
          .append(resultElementName)
          .append("_key, ")
          .append(resultElementName)
          .append("_value);\n");

      indent_down();
      sb.append(indent()).append("}\n");

    } else { // List or Set
      TType elemType;
      if (container.isSet()) {
        elemType = ((TSet)container).getElemType();
      } else { // isList
        elemType = ((TList)container).getElemType();
      }

      sb.append(indent())
          .append("for (")
          .append(typeName(elemType, true, false))
          .append(" ")
          .append(iteratorElementName)
          .append(" : ")
          .append(sourceName)
          .append(") {\n");
      indent_up();

      if (getTrueType(elemType).isContainer()) {
        generateDeepCopyContainer(sb, iteratorElementName, "", resultElementName, elemType);
        sb.append(indent()).append(resultName).append(".add(").append(resultElementName).append(");\n");
      } else {
        if (getTrueType(elemType).isBinary()) {
          sb.append(indent()).append("java.nio.ByteBuffer temp_binary_element = ");
          generateDeepCopyNonContainer(sb, iteratorElementName, "temp_binary_element", elemType);
          sb.append(";\n");
          sb.append(indent()).append(resultName).append(".add(temp_binary_element);\n");
        } else {
          sb.append(indent()).append(resultName).append(".add(");
          generateDeepCopyNonContainer(sb, iteratorElementName, resultName, elemType);
          sb.append(");\n");
        }
      }
      indent_down();
      sb.append(indent()).append("}\n");
    }
  }

  private void generateJavaMetaDataMap(StringBuilder sb, TStruct tstruct) {
    List<TField> fields = tstruct.getMembers(); // Assuming getMembers returns them in declaration order

    sb.append(indent()).append("public static final java.util.Map<_Fields, "
                               + "org.apache.thrift.meta_data.FieldMetaData> metaDataMap;\n");
    sb.append(indent()).append("static {\n");
    indent_up();

    sb.append(indent())
        .append("java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new ")
        .append("java.util.EnumMap<_Fields, "
                + "org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);\n");

    for (TField field : fields) {
      String fieldName = field.getName();
      sb.append(indent())
          .append("tmpMap.put(_Fields.")
          .append(constantName(fieldName))
          .append(", new org.apache.thrift.meta_data.FieldMetaData(\"")
          .append(fieldName)
          .append("\", ");

      // Set field requirement type
      switch (field.getReq()) {
      case REQUIRED:
        sb.append("org.apache.thrift.TFieldRequirementType.REQUIRED, ");
        break;
      case OPTIONAL:
        sb.append("org.apache.thrift.TFieldRequirementType.OPTIONAL, ");
        break;
      default: // OPT_IN_REQ_OUT and others
        sb.append("org.apache.thrift.TFieldRequirementType.DEFAULT, ");
        break;
      }

      // Create value meta data
      generateFieldValueMetaData(sb, field.getType());

      // Include annotations if requested
      if (options.isAnnotationsAsMetadata()) {
        generateMetadataForFieldAnnotations(sb, field);
      }
      sb.append("));\n");
    }

    sb.append(indent()).append("metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);\n");
    sb.append(indent())
        .append("org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(")
        .append(typeName(tstruct))
        .append(".class, metaDataMap);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  private void generateFieldValueMetaData(StringBuilder sb, TType type) {
    TType ttype = getTrueType(type); // Resolve typedefs

    sb.append("\n"); // Start on a new line for readability
    indent_up();     // Match C++ indent(out)
    indent_up();     // Match C++ indent_up()

    sb.append(indent()); // Actual content starts here

    if (ttype.isStruct() || ttype.isXception()) {
      sb.append("new "
                + "org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ")
          .append(typeName(ttype))
          .append(".class");
    } else if (ttype.isContainer()) {
      if (ttype.isList()) {
        sb.append("new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, ");
        generateFieldValueMetaData(sb, ((TList)ttype).getElemType());
      } else if (ttype.isSet()) {
        sb.append("new org.apache.thrift.meta_data.SetMetaData(org.apache.thrift.protocol.TType.SET, ");
        generateFieldValueMetaData(sb, ((TSet)ttype).getElemType());
      } else { // map
        sb.append("new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, ");
        generateFieldValueMetaData(sb, ((TMap)ttype).getKeyType());
        sb.append(", ");
        generateFieldValueMetaData(sb, ((TMap)ttype).getValType());
      }
    } else if (ttype.isEnum()) {
      sb.append("new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, ")
          .append(typeName(ttype))
          .append(".class");
    } else { // Base type
      sb.append("new org.apache.thrift.meta_data.FieldValueMetaData(").append(typeToEnum(ttype));
      if (ttype.isBinary()) {
        sb.append(indent()).append(", true"); // Mark as binary
      } else if (type.isTypedef()) {          // Use original type for typedef name
        // In Java AST, TTypedef holds symbolic name. `type` here is the original typedef, not the
        // resolved one. `ttype` is already resolved. We need original `type` if it was a typedef.
        if (type instanceof io.github.decster.ast.TTypedef) {
          sb.append(indent()).append(", \"").append(((io.github.decster.ast.TTypedef)type).getSymbolic()).append("\"");
        }
      }
      // Note: The C++ version has an `else if (type->is_typedef())` clause, which is
      // implicitly handled here because `ttype` is already the resolved true type.
      // If the original `type` was a typedef, its symbolic name should be used.
    }
    sb.append(")");

    indent_down(); // Match C++ indent_down()
    indent_down(); // Match C++ indent_down()
  }

  private void generateMetadataForFieldAnnotations(StringBuilder sb, TField field) {
    if (field.getAnnotations() == null || field.getAnnotations().isEmpty()) {
      return;
    }
    sb.append(",\n"); // Add comma separator from previous metadata argument
    indent_up();      // For alignment with previous argument
    indent_up();
    sb.append(indent()).append("java.util.stream.Stream.<java.util.Map.Entry<String, String>>builder()\n");
    indent_up(); // For .add calls
    indent_up();

    for (Map.Entry<String, List<String>> annotationEntry : field.getAnnotations().entrySet()) {
      String key = annotationEntry.getKey();
      List<String> values = annotationEntry.getValue();
      if (values != null && !values.isEmpty()) {
        // C++ thrift takes the last value for an annotation key: `annotation.second.back()`
        String value = values.get(values.size() - 1);
        sb.append(indent())
            .append(".add(new java.util.AbstractMap.SimpleImmutableEntry<>(\"")
            .append(getEscapedString(key))
            .append("\", \"")
            .append(getEscapedString(value))
            .append("\"))\n");
      }
    }

    sb.append(indent()).append(".build().collect(java.util.stream.Collectors.toMap(java.util.Map."
                               + "Entry::getKey, java.util.Map.Entry::getValue))");

    indent_down(); // for .add
    indent_down();
    indent_down(); // for builder
    indent_down(); // for alignment with new FieldMetaData(...)
  }

  /**
   * Generates a struct descriptor for a Thrift struct.
   *
   * @param sb The StringBuilder to append to
   * @param tstruct The struct definition
   */
  public void generateStructDesc(StringBuilder sb, TStruct tstruct) {
    sb.append(indent())
        .append("private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new ")
        .append("org.apache.thrift.protocol.TStruct(\"")
        .append(tstruct.getName())
        .append("\");\n");
  }

  // Enum for isset type
  public enum isset_type { ISSET_NONE, ISSET_PRIMITIVE, ISSET_BITSET }

  String generateIssetCheck(String fieldName) {
    return "isSet" + getCapName(makeValidJavaIdentifier(fieldName)) + "()";
  }

  String generateIssetCheck(TField field) { return generateIssetCheck(field.getName()); }

  /**
   * Determines the type of isset implementation needed for a struct
   *
   * @param tstruct The struct to analyze
   * @return The type of isset implementation needed
   */
  private isset_type needs_isset(TStruct tstruct) { return needs_isset(tstruct, new StringBuilder()); }

  /**
   * Determines the type of isset implementation needed for a struct
   *
   * @param tstruct The struct to analyze
   * @param primitiveType StringBuilder to store the primitive type
   * @return The type of isset implementation needed
   */
  private isset_type needs_isset(TStruct tstruct, StringBuilder primitiveType) {
    // Count the number of non-nullable fields
    int num_fields = 0;
    for (TField field : tstruct.getMembers()) {
      if (!typeCanBeNull(field.getType())) {
        num_fields++;
      }
    }

    if (num_fields == 0) {
      return isset_type.ISSET_NONE;
    } else if (num_fields <= 8) {
      primitiveType.append("byte");
      return isset_type.ISSET_PRIMITIVE;
    } else if (num_fields <= 16) {
      primitiveType.append("short");
      return isset_type.ISSET_PRIMITIVE;
    } else if (num_fields <= 32) {
      primitiveType.append("int");
      return isset_type.ISSET_PRIMITIVE;
    } else if (num_fields <= 64) {
      primitiveType.append("long");
      return isset_type.ISSET_PRIMITIVE;
    } else {
      return isset_type.ISSET_BITSET;
    }
  }

  /**
   * Generates code to set the isset field for a field
   *
   * @param sb The StringBuilder to append to
   * @param field The field to generate code for
   * @param prefix The prefix to use
   */
  private void generate_isset_set(StringBuilder sb, TField field, String prefix) {
    if (!typeCanBeNull(field.getType())) {
      sb.append(indent()).append(prefix).append("set").append(getCapName(field.getName())).append("IsSet(true);\n");
    }
  }

  String isset_field_id(TField field) { return "__" + field.getName().toUpperCase() + "_ISSET_ID"; }

  /**
   * Generates field descriptors for all the members of a struct.
   *
   * @param sb The StringBuilder to append to
   * @param tstruct The struct definition
   */
  public void generateFieldDescs(StringBuilder sb, TStruct tstruct) {
    List<TField> members = tstruct.getMembers();

    for (TField field : members) {
      sb.append(indent())
          .append("private static final org.apache.thrift.protocol.TField ")
          .append(constantName(field.getName()))
          .append("_FIELD_DESC = new org.apache.thrift.protocol.TField(\"")
          .append(field.getName())
          .append("\", ")
          .append(typeToEnum(field.getType()))
          .append(", ")
          .append("(short)")
          .append(field.getKey())
          .append(");\n");
    }
  }

  /**
   * Generates a scheme map for a struct.
   *
   * @param sb The StringBuilder to append to
   * @param tstruct The struct definition
   */
  public void generateSchemeMap(StringBuilder sb, TStruct tstruct) {
    sb.append(indent())
        .append("private static final org.apache.thrift.scheme.SchemeFactory ")
        .append("STANDARD_SCHEME_FACTORY = new ")
        .append(tstruct.getName())
        .append("StandardSchemeFactory();\n");

    sb.append(indent())
        .append("private static final org.apache.thrift.scheme.SchemeFactory ")
        .append("TUPLE_SCHEME_FACTORY = new ")
        .append(tstruct.getName())
        .append("TupleSchemeFactory();\n");
  }

  /**
   * Generates field name constants for a struct.
   *
   * @param sb The StringBuilder to append to
   * @param tstruct The struct definition
   */
  public void generateFieldNameConstants(StringBuilder sb, TStruct tstruct) {
    sb.append(indent())
        .append("/** The set of fields this struct contains, along with convenience methods for ")
        .append("finding and manipulating them. */\n");

    sb.append(indent()).append("public enum _Fields implements org.apache.thrift.TFieldIdEnum {\n");

    indent_up();
    boolean first = true;
    List<TField> members = tstruct.getMembers();

    for (TField field : members) {
      if (!first) {
        sb.append(",\n");
      }
      first = false;

      generateJavaDocField(sb, field);

      sb.append(indent())
          .append(constantName(field.getName()))
          .append("((short)")
          .append(field.getKey())
          .append(", \"")
          .append(field.getName())
          .append("\")");
    }

    sb.append(";\n\n");

    sb.append(indent())
        .append("private static final java.util.Map<java.lang.String, _Fields> byName = new ")
        .append("java.util.HashMap<java.lang.String, _Fields>();\n\n");

    sb.append(indent()).append("static {\n");
    indent_up();
    sb.append(indent()).append("for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {\n");
    indent_up();
    sb.append(indent()).append("byName.put(field.getFieldName(), field);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // Find by thrift id method
    sb.append(indent()).append("/**\n");
    sb.append(indent()).append(" * Find the _Fields constant that matches fieldId, or null if its not found.\n");
    sb.append(indent()).append(" */\n");
    sb.append(indent()).append(javaNullableAnnotation()).append("\n");
    sb.append(indent()).append("public static _Fields findByThriftId(int fieldId) {\n");
    indent_up();
    sb.append(indent()).append("switch(fieldId) {\n");
    indent_up();

    for (TField field : members) {
      sb.append(indent())
          .append("case ")
          .append(field.getKey())
          .append(": // ")
          .append(constantName(field.getName()))
          .append("\n");
      sb.append(indent()).append("  return ").append(constantName(field.getName())).append(";\n");
    }

    sb.append(indent()).append("default:\n");
    sb.append(indent()).append("  return null;\n");

    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // Find by thrift id or throw method
    sb.append(indent()).append("/**\n");
    sb.append(indent()).append(" * Find the _Fields constant that matches fieldId, throwing an exception\n");
    sb.append(indent()).append(" * if it is not found.\n");
    sb.append(indent()).append(" */\n");
    sb.append(indent()).append("public static _Fields findByThriftIdOrThrow(int fieldId) {\n");
    indent_up();
    sb.append(indent()).append("_Fields fields = findByThriftId(fieldId);\n");
    sb.append(indent())
        .append("if (fields == null) throw new java.lang.IllegalArgumentException(\"Field \" + ")
        .append("fieldId + \" doesn't exist!\");\n");
    sb.append(indent()).append("return fields;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // Find by name method
    sb.append(indent()).append("/**\n");
    sb.append(indent()).append(" * Find the _Fields constant that matches name, or null if its not found.\n");
    sb.append(indent()).append(" */\n");
    sb.append(indent()).append(javaNullableAnnotation()).append("\n");
    sb.append(indent()).append("public static _Fields findByName(java.lang.String name) {\n");
    indent_up();
    sb.append(indent()).append("return byName.get(name);\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // Field properties
    sb.append(indent()).append("private final short _thriftId;\n");
    sb.append(indent()).append("private final java.lang.String _fieldName;\n\n");

    // Constructor
    sb.append(indent()).append("_Fields(short thriftId, java.lang.String fieldName) {\n");
    indent_up();
    sb.append(indent()).append("_thriftId = thriftId;\n");
    sb.append(indent()).append("_fieldName = fieldName;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // getThriftFieldId method
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public short getThriftFieldId() {\n");
    indent_up();
    sb.append(indent()).append("return _thriftId;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // getFieldName method
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public java.lang.String getFieldName() {\n");
    indent_up();
    sb.append(indent()).append("return _fieldName;\n");
    indent_down();
    sb.append(indent()).append("}\n");

    indent_down();
    sb.append(indent()).append("}\n");
  }

  /**
   * Prepares the output directory structure based on package name.
   *
   * @throws IOException If directory creation fails
   */
  private void prepareOutputDirectoryStructure() throws IOException {
    // Create the base output directory
    Files.createDirectories(Paths.get(outputDir));

    // Get the Java namespace/package from program
    String packageName = program.getNamespace("java");

    // Set up package directory structure
    String subdir = outputDir;
    if (packageName != null && !packageName.isEmpty()) {
      String[] parts = packageName.split("\\.");
      for (String part : parts) {
        subdir = subdir + File.separator + part;
        Files.createDirectories(Paths.get(subdir));
      }
    }

    // Store the package directory for later use
    this.packageDir = subdir;
  }

  private String javaPackage() { return "package " + getJavaPackage() + ";\n\n"; }

  public GenResult generateConstants() throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(autogenComment());
    sb.append(javaPackage());

    // Write suppressions
    sb.append("@SuppressWarnings({\"cast\", \"rawtypes\", \"serial\", \"unchecked\", \"unused\"})\n");

    // Define the constants class
    sb.append("public class ").append(makeValidJavaIdentifier(program.getName())).append("Constants {\n\n");

    // Generate constants
    indent_level = 1;

    for (TConst constant : program.getConsts()) {
      // Generate JavaDoc for the constant
      generateJavaDoc(sb, constant);
      // Print the constant value
      printConstValue(sb, constant.getName(), constant.getType(), constant.getValue(), false);
    }

    // Close the class
    indent_down();
    sb.append("}\n");

    return new GenResult(makeValidJavaFilename(program.getName()) + "Constants.java", sb.toString());
  }

  /**
   * Prints the value of a constant with the given type.
   *
   * @param sb The output stream
   * @param name The name of the constant
   * @param type The type of the constant
   * @param value The value of the constant
   * @param inStatic Whether the constant is in a static context
   * @param defval Whether this is a default value assignment
   */
  private void printConstValue(StringBuilder sb, String name, TType type, TConstValue value, boolean inStatic,
                               boolean defval) {
    type = getTrueType(type);

    sb.append(indent());
    if (!defval) {
      sb.append(inStatic ? "" : "public static final ").append(typeName(type)).append(" ");
    }

    if (type.isBaseType() || type.isEnum()) {
      String renderedValue = renderConstValue(sb, type, value);
      sb.append(name).append(" = ").append(renderedValue).append(";\n\n");
    } else if (type.isStruct() || type.isXception()) {
      TStruct struct = (TStruct)type;
      List<TField> fields = new ArrayList<>(struct.getMembers());
      Collections.sort(fields, (a, b) -> Integer.compare(a.getKey(), b.getKey()));

      Map<TConstValue, TConstValue> valueMap = value.getMap();
      sb.append(name).append(" = new ").append(typeName(type, false, true)).append("();\n");

      if (!inStatic) {
        sb.append(indent()).append("static {\n");
        indent_up();
      }

      for (Map.Entry<TConstValue, TConstValue> entry : valueMap.entrySet()) {
        String fieldName = entry.getKey().getString();
        TConstValue fieldValue = entry.getValue();

        TType fieldType = null;
        for (TField field : fields) {
          if (field.getName().equals(fieldName)) {
            fieldType = field.getType();
            break;
          }
        }

        if (fieldType == null) {
          throw new RuntimeException("Type error: " + type.getName() + " has no field " + fieldName);
        }

        String val = renderConstValue(sb, fieldType, fieldValue);
        sb.append(indent())
            .append(name)
            .append(".set")
            .append(getCapName(fieldName))
            .append("(")
            .append(val)
            .append(");\n");
      }

      if (!inStatic) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
      sb.append("\n");
    } else if (type.isMap()) {
      TMap tMap = (TMap)type;
      TType keyType = tMap.getKeyType();
      TType valueType = tMap.getValType();

      String constructorArgs = "";
      if (isEnumMap(type)) {
        constructorArgs = innerEnumTypeName(type);
      }

      sb.append(name)
          .append(" = new ")
          .append(typeName(type, false, true))
          .append("(")
          .append(constructorArgs)
          .append(");\n");

      if (!inStatic) {
        sb.append(indent()).append("static {\n");
        indent_up();
      }

      Map<TConstValue, TConstValue> valueMap = value.getMap();
      for (Map.Entry<TConstValue, TConstValue> entry : valueMap.entrySet()) {
        String key = renderConstValue(sb, keyType, entry.getKey());
        String val = renderConstValue(sb, valueType, entry.getValue());
        sb.append(indent()).append(name).append(".put(").append(key).append(", ").append(val).append(");\n");
      }

      if (!inStatic) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
      sb.append("\n");
    } else if (type.isList() || type.isSet()) {
      TType elemType;
      if (type.isList()) {
        elemType = ((TList)type).getElemType();
      } else {
        elemType = ((TSet)type).getElemType();
      }

      if (isEnumSet(type)) {
        sb.append(name)
            .append(" = ")
            .append(typeName(type, false, true, true))
            .append(".noneOf(")
            .append(innerEnumTypeName(type))
            .append(");\n");
      } else {
        sb.append(name).append(" = new ").append(typeName(type, false, true)).append("();\n");
      }

      if (!inStatic) {
        sb.append(indent()).append("static {\n");
        indent_up();
      }

      for (TConstValue elem : value.getList()) {
        String val = renderConstValue(sb, elemType, elem);
        sb.append(indent()).append(name).append(".add(").append(val).append(");\n");
      }

      if (!inStatic) {
        indent_down();
        sb.append(indent()).append("}\n");
      }
      sb.append("\n");
    } else {
      throw new RuntimeException("Compiler error: no const of type " + type.getName());
    }
  }

  /** Overloaded version of printConstValue that defaults defval to false */
  private void printConstValue(StringBuilder out, String name, TType type, TConstValue value, boolean inStatic) {
    printConstValue(out, name, type, value, inStatic, false);
  }

  /**
   * Renders a constant value as a Java code string
   *
   * @param out The output StringBuilder
   * @param type The type of the constant
   * @param value The value of the constant
   * @return A string representation of the constant value
   */
  private String renderConstValue(StringBuilder out, TType type, TConstValue value) {
    type = getTrueType(type);
    StringBuilder render = new StringBuilder();

    if (type.isBaseType()) {
      TBaseType baseType = (TBaseType)type;
      TBaseType.Base tBase = baseType.getBase();

      switch (tBase) {
      case TYPE_STRING:
        if (baseType.isBinary()) {
          render.append("java.nio.ByteBuffer.wrap(\"")
              .append(getEscapedString(value.getString()))
              .append("\".getBytes())");
        } else {
          render.append("\"").append(getEscapedString(value.getString())).append("\"");
        }
        break;
      case TYPE_UUID:
        render.append("java.util.UUID.fromString(\"").append(getEscapedString(value.getString())).append("\")");
        break;
      case TYPE_BOOL:
        render.append(value.getInteger() > 0 ? "true" : "false");
        break;
      case TYPE_I8:
        render.append("(byte)").append(value.getInteger());
        break;
      case TYPE_I16:
        render.append("(short)").append(value.getInteger());
        break;
      case TYPE_I32:
        render.append(value.getInteger());
        break;
      case TYPE_I64:
        render.append(value.getInteger()).append("L");
        break;
      case TYPE_DOUBLE:
        if (value.getType() == TConstValue.Type.CV_INTEGER) {
          render.append(value.getInteger()).append("d");
        } else {
          render.append(emitDoubleAsString(value.getDouble()));
        }
        break;
      default:
        throw new RuntimeException("Compiler error: no const of base type " + tBase);
      }
    } else if (type.isEnum()) {
      String namespacePrefix = type.getProgram().getNamespace("java");
      if (namespacePrefix != null && !namespacePrefix.isEmpty()) {
        namespacePrefix += ".";
      } else {
        namespacePrefix = "";
      }
      render.append(namespacePrefix).append(value.getIdentifierWithParent());
    } else {
      // For complex types, generate a temporary variable and use printConstValue
      String tmpVar = tmp("tmp");
      printConstValue(out, tmpVar, type, value, true);
      render.append(tmpVar);
    }

    return render.toString();
  }

  /**
   * Generate a Java enum class from a Thrift enum definition.
   *
   * @param enumType The Thrift enum AST node
   * @throws IOException If file operations fail
   */
  public GenResult generateEnum(TEnum enumType) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(autogenComment());
    sb.append(javaPackage());

    // Class JavaDoc
    generateJavaDoc(sb, enumType);

    // @Generated annotation
    if (!options.isSuppressGeneratedAnnotations()) {
      sb.append(getAutogenComment());
    }

    // Deprecated annotation if applicable
    if (isDeprecated(enumType.getAnnotations())) {
      sb.append("@Deprecated\n");
    }

    // Enum declaration - implements TEnum interface
    sb.append("public enum ").append(enumType.getName()).append(" implements org.apache.thrift.TEnum {\n");

    // Reset indentation level
    indent_level = 0;
    indent_up(); // Increase to level 1 for enum members

    // Enum constants
    List<TEnumValue> constants = enumType.getConstants();
    for (int i = 0; i < constants.size(); i++) {
      TEnumValue value = constants.get(i);

      // JavaDoc for the enum constant if present
      generateJavaDoc(sb, value);

      // Deprecated annotation if present
      if (isDeprecated(value.getAnnotations())) {
        sb.append(indent()).append("@Deprecated\n");
      }

      // Enum constant declaration
      sb.append(indent()).append(value.getName()).append("(").append(value.getValue()).append(")");

      // Add semicolon or comma
      if (i < constants.size() - 1) {
        sb.append(",");
      } else {
        sb.append(";");
      }
      sb.append("\n\n");
    }

    // Private value field
    sb.append(indent()).append("private final int value;\n\n");

    // Constructor
    sb.append(indent()).append("private ").append(enumType.getName()).append("(int value) {\n");
    indent_up();
    sb.append(indent()).append("this.value = value;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // getValue method (from TEnum interface)
    sb.append(indent())
        .append("/**\n")
        .append(indent())
        .append(" * Get the integer value of this enum value, as defined in the Thrift IDL.\n")
        .append(indent())
        .append(" */\n")
        .append(indent())
        .append("@Override\n")
        .append(indent())
        .append("public int getValue() {\n");
    indent_up();
    sb.append(indent()).append("return value;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    // findByValue method
    sb.append(indent())
        .append("/**\n")
        .append(indent())
        .append(" * Find a the enum type by its integer value, as defined in the Thrift IDL.\n")
        .append(indent())
        .append(" * @return null if the value is not found.\n")
        .append(indent())
        .append(" */\n")
        .append(indent())
        .append("@org.apache.thrift.annotation.Nullable\n")
        .append(indent())
        .append("public static ")
        .append(enumType.getName())
        .append(" findByValue(int value) {\n");
    indent_up();
    sb.append(indent()).append("switch (value) {\n");
    indent_up();

    // Case statements for each enum value
    for (TEnumValue value : constants) {
      sb.append(indent()).append("case ").append(value.getValue()).append(":\n");
      indent_up();
      sb.append(indent()).append("return ").append(value.getName()).append(";\n");
      indent_down();
    }

    // Default case
    sb.append(indent()).append("default:\n");
    indent_up();
    sb.append(indent()).append("return null;\n");
    indent_down();

    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n");

    // Close the class
    indent_down(); // Back to level 0
    sb.append("}");

    return new GenResult(makeValidJavaFilename(enumType.getName()) + ".java", sb.toString());
  }

  /**
   * Generate JavaDoc comment for a documented element.
   *
   * @param sb The StringBuilder to append to
   * @param element The documented element
   */
  private void generateJavaDoc(StringBuilder sb, TDoc element) {
    String doc = element.getDoc();
    generateJavaStringComment(sb, doc);
  }

  private static void generateJavaStringComment(StringBuilder sb, String doc) {
    if (doc != null && !doc.isEmpty()) {
      sb.append("/**\n");
      for (String line : doc.split("\n", -1)) {
        sb.append(" * ").append(line).append("\n");
      }
      sb.append(" */\n");
    }
  }

  private String getEnumClassName(TType type) {
    String packagePrefix = "";
    TProgram program = type.getProgram();
    if (program != null && program != this.program) {
      String namespace = program.getNamespace("java");
      if (namespace != null && !namespace.isEmpty()) {
        packagePrefix = namespace + ".";
      }
    }
    return packagePrefix + type.getName();
  }

  private void generateJavaDocField(StringBuilder sb, TField tfield) {
    if (tfield.getType().isEnum()) {
      String combined =
          (tfield.getDoc() == null ? "" : (tfield.getDoc() + "\n")) + "\n@see " + getEnumClassName(tfield.getType());
      generateJavaStringComment(sb, combined);
    } else {
      if (tfield.hasDoc()) {
        generateJavaStringComment(sb, tfield.getDoc());
      }
    }
  }

  private void generateJavaDocFunc(StringBuilder sb, TFunction tfunction) {
    if (tfunction.hasDoc()) {
      StringBuilder docBuilder = new StringBuilder();
      docBuilder.append(tfunction.getDoc());

      // Add parameter documentation
      List<TField> parameters = tfunction.getArglist().getMembers();
      if (!parameters.isEmpty()) {
        docBuilder.append("\n");
      }
      for (TField param : parameters) {
        docBuilder.append("\n@param ").append(param.getName());
        if (param instanceof TDoc && ((TDoc)param).hasDoc()) {
          docBuilder.append(" ").append(((TDoc)param).getDoc());
        }
      }
      generateJavaStringComment(sb, docBuilder.toString());
    }
  }

  /**
   * Check if an element is deprecated based on its annotations.
   *
   * @param annotations The list of annotations
   * @return True if the element is deprecated
   */
  private boolean isDeprecated(Map<String, List<String>> annotations) {
    if (annotations == null) {
      return false;
    }
    return annotations.containsKey("deprecated");
  }

  /**
   * Get the Java package name for the generated code.
   *
   * @return The Java package name
   */
  private String getJavaPackage() {
    String namespace = program.getNamespace("java");
    if (namespace == null || namespace.isEmpty()) {
      namespace = program.getNamespace("*");
    }

    // Default package if none specified
    if (namespace == null || namespace.isEmpty()) {
      namespace = "thrift.gen";
    }

    return namespace;
  }

  /**
   * Get the autogenerated comment annotation.
   *
   * @return The @Generated annotation string
   */
  private String getAutogenComment() {
    StringBuilder sb = new StringBuilder();

    String annoClass = options.isJakartaAnnotations() ? "@jakarta.annotation.Generated" : "@javax.annotation.Generated";

    sb.append(annoClass).append("(value = \"").append(getAutogenSummary()).append("\"");

    if (!options.isUndatedGeneratedAnnotations()) {
      sb.append(", date = \"").append(timestamp).append("\"");
    }

    sb.append(")\n");
    return sb.toString();
  }

  /**
   * Get the generator summary for the @Generated annotation.
   *
   * @return The generator summary
   */
  private String getAutogenSummary() { return "Autogenerated by Thrift Compiler (0.20.0)"; }

  /**
   * Write the generated code to a file.
   *
   * @param fileName The name of the file
   * @param content The file content
   * @throws IOException If file operations fail
   */
  private void writeToFile(String fileName, String content) throws IOException {
    Path filePath = Paths.get(packageDir, fileName);

    try (FileWriter writer = new FileWriter(filePath.toFile())) {
      writer.write(content);
    }
  }

  /**
   * Get the Java type name for a Thrift type.
   *
   * @param type The Thrift type
   * @return The corresponding Java type name
   */
  public String getJavaTypeName(TType type) {
    if (type instanceof TBaseType) {
      TBaseType baseType = (TBaseType)type;
      String thriftTypeName = baseType.getName();
      return PRIMITIVE_TYPE_MAP.getOrDefault(thriftTypeName, "Object");
    } else if (type instanceof TEnum) {
      return ((TEnum)type).getName();
    }

    // More complex types will be handled in later phases
    return "Object";
  }

  /**
   * Converts a Thrift type to its corresponding TType enum value.
   *
   * @param type The Thrift type
   * @return The corresponding TType enum value string
   * @throws IllegalArgumentException if the type is not supported
   */
  public String typeToEnum(TType type) {
    // Get the true type if it's a typedef
    // Note: This would need to be expanded when typedef support is added
    type = getTrueType(type);
    if (type instanceof TBaseType) {
      TBaseType baseType = (TBaseType)type;
      String typeName = baseType.getName().toLowerCase();

      switch (typeName) {
      case "void":
        throw new IllegalArgumentException("NO T_VOID CONSTRUCT");
      case "bool":
        return "org.apache.thrift.protocol.TType.BOOL";
      case "byte":
      case "i8":
        return "org.apache.thrift.protocol.TType.BYTE";
      case "i16":
        return "org.apache.thrift.protocol.TType.I16";
      case "i32":
        return "org.apache.thrift.protocol.TType.I32";
      case "i64":
        return "org.apache.thrift.protocol.TType.I64";
      case "uuid":
        return "org.apache.thrift.protocol.TType.UUID";
      case "double":
        return "org.apache.thrift.protocol.TType.DOUBLE";
      case "string":
      case "binary":
        return "org.apache.thrift.protocol.TType.STRING";
      default:
        throw new IllegalArgumentException("Compiler error: unhandled type: " + typeName);
      }
    } else if (type instanceof TEnum) {
      return "org.apache.thrift.protocol.TType.I32";
    } else if (type instanceof TStruct) {
      return "org.apache.thrift.protocol.TType.STRUCT";
    } else if (type instanceof TMap) {
      return "org.apache.thrift.protocol.TType.MAP";
    } else if (type instanceof TSet) {
      return "org.apache.thrift.protocol.TType.SET";
    } else if (type instanceof TList) {
      return "org.apache.thrift.protocol.TType.LIST";
    }

    throw new IllegalArgumentException("INVALID TYPE IN typeToEnum: " + type.getClass().getSimpleName());
  }

  private String toUpperCaseIdentifier(String identifier) { return identifier.toUpperCase(); }

  private void appendLine(StringBuilder sb, int level, String line) {
    sb.append(indent(level)).append(line).append("\n");
  }

  private String normalizeName(String name) {
    // Un-conflict keywords by prefixing with "$"
    if (JAVA_KEYWORDS.contains(name)) {
      return "$" + name;
    }

    // No changes necessary
    return name;
  }

  private String makeValidJavaFilename(String fromName) {
    // If any further rules apply to source file names in Java, modify as necessary
    return makeValidJavaIdentifier(fromName);
  }

  private String makeValidJavaIdentifier(String fromName) {
    if (fromName == null || fromName.isEmpty()) {
      return fromName;
    }

    StringBuilder str = new StringBuilder(fromName);

    // If the first letter is a number, add an additional underscore in front of it
    char c = str.charAt(0);
    if (c >= '0' && c <= '9') {
      str.insert(0, '_');
    }

    // Following chars: letter, number or underscore
    for (int i = 0; i < str.length(); i++) {
      c = str.charAt(i);
      if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_')) {
        str.setCharAt(i, '_');
      }
    }

    return normalizeName(str.toString());
  }

  private String asCamelCase(String name) { return asCamelCase(name, true); }

  private String asCamelCase(String name, boolean ucfirst) {
    if (name == null || name.isEmpty()) {
      return name;
    }

    StringBuilder newName = new StringBuilder();
    int i = 0;

    // Skip leading underscores
    while (i < name.length() && name.charAt(i) == '_') {
      i++;
    }

    if (i >= name.length()) {
      return name;
    }

    // Handle first character
    if (ucfirst) {
      newName.append(Character.toUpperCase(name.charAt(i++)));
    } else {
      newName.append(Character.toLowerCase(name.charAt(i++)));
    }

    // Process the rest of the characters
    for (; i < name.length(); i++) {
      if (name.charAt(i) == '_') {
        if (i < name.length() - 1) {
          i++;
          newName.append(Character.toUpperCase(name.charAt(i)));
        }
      } else {
        newName.append(name.charAt(i));
      }
    }

    return newName.toString();
  }

  private String getRpcMethodName(String name) {
    if (options.isFullcamel()) {
      return makeValidJavaIdentifier(asCamelCase(name, false));
    } else {
      return makeValidJavaIdentifier(name);
    }
  }

  private String getCapName(String name) {
    if (options.isNocamel()) {
      return "_" + name;
    } else if (options.isFullcamel()) {
      return asCamelCase(name, true);
    } else {
      return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
  }

  private String constantName(String name) {
    if (name == null || name.isEmpty()) {
      return name;
    }

    StringBuilder constantName = new StringBuilder();

    boolean isFirst = true;
    boolean wasPreviousCharUpper = false;

    for (char character : name.toCharArray()) {
      boolean isUpper = Character.isUpperCase(character);

      if (isUpper && !isFirst && !wasPreviousCharUpper) {
        constantName.append('_');
      }

      constantName.append(Character.toUpperCase(character));

      isFirst = false;
      wasPreviousCharUpper = isUpper;
    }

    return constantName.toString();
  }

  /**
   * Checks if the given type is a set with an enum element type.
   *
   * @param type The type to check
   * @return true if the type is a set of enums, false otherwise
   */
  public boolean isEnumSet(TType type) {
    if (!options.isSortedContainers()) {
      type = type.getTrueType();
      if (type.isSet()) {
        TSet tset = (TSet)type;
        TType elemType = tset.getElemType().getTrueType();
        return elemType.isEnum();
      }
    }
    return false;
  }

  /**
   * Checks if the given type is a map with an enum key type.
   *
   * @param type The type to check
   * @return true if the type is a map with enum keys, false otherwise
   */
  public boolean isEnumMap(TType type) {
    if (!options.isSortedContainers()) {
      type = type.getTrueType();
      if (type.isMap()) {
        TMap tmap = (TMap)type;
        TType keyType = tmap.getKeyType().getTrueType();
        return keyType.isEnum();
      }
    }
    return false;
  }

  /**
   * Gets the name of the inner enum type for a container.
   *
   * @param type The container type
   * @return The name of the inner enum type with ".class" appended
   */
  public String innerEnumTypeName(TType type) {
    type = type.getTrueType();
    if (type.isMap()) {
      TMap tmap = (TMap)type;
      TType keyType = tmap.getKeyType().getTrueType();
      return typeName(keyType, true) + ".class";
    } else if (type.isSet()) {
      TSet tset = (TSet)type;
      TType elemType = tset.getElemType().getTrueType();
      return typeName(elemType, true) + ".class";
    }
    return "";
  }

  /**
   * Returns a Java type name
   *
   * @param ttype The type
   * @param inContainer Is the type going inside a container?
   * @param inInit Is the type being used in an initialization context?
   * @param skipGeneric Whether to skip adding generic type parameters
   * @param forceNamespace Whether to force including the namespace
   * @return Java type name, i.e. java.util.HashMap<Key,Value>
   */
  public String typeName(TType ttype, boolean inContainer, boolean inInit, boolean skipGeneric,
                         boolean forceNamespace) {
    // In Java typedefs are just resolved to their real type
    ttype = ttype.getTrueType();
    String prefix;

    if (ttype.isBaseType()) {
      return baseTypeName((TBaseType)ttype, inContainer);
    } else if (ttype.isMap()) {
      TMap tmap = (TMap)ttype;
      if (inInit) {
        if (isEnumMap(tmap)) {
          prefix = "java.util.EnumMap";
        } else if (options.isSortedContainers()) {
          prefix = "java.util.TreeMap";
        } else {
          prefix = "java.util.HashMap";
        }
      } else {
        prefix = "java.util.Map";
      }
      return prefix +
          (skipGeneric ? "" : "<" + typeName(tmap.getKeyType(), true) + "," + typeName(tmap.getValType(), true) + ">");
    } else if (ttype.isSet()) {
      TSet tset = (TSet)ttype;
      if (inInit) {
        if (isEnumSet(tset)) {
          prefix = "java.util.EnumSet";
        } else if (options.isSortedContainers()) {
          prefix = "java.util.TreeSet";
        } else {
          prefix = "java.util.HashSet";
        }
      } else {
        prefix = "java.util.Set";
      }
      return prefix + (skipGeneric ? "" : "<" + typeName(tset.getElemType(), true) + ">");
    } else if (ttype.isList()) {
      TList tlist = (TList)ttype;
      if (inInit) {
        prefix = "java.util.ArrayList";
      } else {
        prefix = "java.util.List";
      }
      return prefix + (skipGeneric ? "" : "<" + typeName(tlist.getElemType(), true) + ">");
    }

    // Check for namespacing
    TProgram program = ttype.getProgram();
    if ((program != null) && ((program != this.program) || forceNamespace)) {
      String packageName = program.getNamespace("java");
      if (packageName != null && !packageName.isEmpty()) {
        return packageName + "." + makeValidJavaIdentifier(ttype.getName());
      }
    }

    return makeValidJavaIdentifier(ttype.getName());
  }

  public String typeName(TType ttype, boolean inContainer, boolean inInit, boolean skipGeneric) {
    return typeName(ttype, inContainer, inInit, skipGeneric, false);
  }

  public String typeName(TType ttype, boolean inContainer, boolean inInit) {
    return typeName(ttype, inContainer, inInit, false, false);
  }

  /** Simplified version of typeName that uses default parameters */
  public String typeName(TType ttype, boolean inContainer) { return typeName(ttype, inContainer, false, false, false); }

  /** Most basic version of typeName with default parameters */
  public String typeName(TType ttype) { return typeName(ttype, false, false, false, false); }

  public String javaOverrideAnnotation() { return "@Override"; }

  /**
   * Returns the Java type name for a base type
   *
   * @param tbase The base type
   * @param inContainer Whether the type is in a container
   * @return The Java type name
   */
  private String baseTypeName(TBaseType tbase, boolean inContainer) {
    switch (tbase.getBase()) {
    case TYPE_VOID:
      return (inContainer ? "Void" : "void");
    case TYPE_STRING:
      return (tbase.isBinary() ? "java.nio.ByteBuffer" : "java.lang.String");
    case TYPE_UUID:
      return "java.util.UUID";
    case TYPE_BOOL:
      return (inContainer ? "java.lang.Boolean" : "boolean");
    case TYPE_I8:
      return (inContainer ? "java.lang.Byte" : "byte");
    case TYPE_I16:
      return (inContainer ? "java.lang.Short" : "short");
    case TYPE_I32:
      return (inContainer ? "java.lang.Integer" : "int");
    case TYPE_I64:
      return (inContainer ? "java.lang.Long" : "long");
    case TYPE_DOUBLE:
      return (inContainer ? "java.lang.Double" : "double");
    default:
      throw new RuntimeException("Compiler error: unhandled base type " + tbase.getBase());
    }
  }

  /**
   * Serializes a field of any type.
   *
   * @param sb The StringBuilder to append to
   * @param field The field to serialize
   * @param prefix Name to prepend to field name (e.g., "struct.")
   * @param postfix Name to append to field name (e.g., ".getKey()")
   * @param hasMetaData Whether protocol metadata is present (relevant for tuple protocol)
   */
  private void generateSerializeField(StringBuilder sb, TField field, String prefix, String postfix,
                                      boolean hasMetaData) {
    String variableName = prefix + makeValidJavaIdentifier(field.getName()) + postfix;
    generateSerializeFieldInternal(sb, field.getType(), variableName, hasMetaData);
  }

  /**
   * Internal helper to serialize a TType variable.
   *
   * @param sb The StringBuilder to append to
   * @param type The type to serialize
   * @param variableName The name of the variable holding the value
   * @param hasMetaData Whether protocol metadata is present
   */
  private void generateSerializeFieldInternal(StringBuilder sb, TType type, String variableName, boolean hasMetaData) {
    type = getTrueType(type);

    if (type.isVoid()) {
      throw new RuntimeException("CANNOT GENERATE SERIALIZE CODE FOR void TYPE: " + variableName);
    }

    if (type.isStruct() || type.isXception()) {
      generateSerializeStruct(sb, (TStruct)type, variableName);
    } else if (type.isContainer()) {
      generateSerializeContainer(sb, type, variableName, hasMetaData);
    } else if (type.isEnum()) {
      sb.append(indent()).append("oprot.writeI32(").append(variableName).append(".getValue());\n");
    } else if (type.isBaseType()) {
      sb.append(indent()).append("oprot.");
      TBaseType baseType = (TBaseType)type;
      TBaseType.Base tbase = baseType.getBase();
      switch (tbase) {
      case TYPE_STRING:
        if (type.isBinary()) {
          sb.append("writeBinary(").append(variableName).append(");");
        } else {
          sb.append("writeString(").append(variableName).append(");");
        }
        break;
      case TYPE_BOOL:
        sb.append("writeBool(").append(variableName).append(");");
        break;
      case TYPE_I8:
        sb.append("writeByte(").append(variableName).append(");");
        break;
      case TYPE_I16:
        sb.append("writeI16(").append(variableName).append(");");
        break;
      case TYPE_I32:
        sb.append("writeI32(").append(variableName).append(");");
        break;
      case TYPE_I64:
        sb.append("writeI64(").append(variableName).append(");");
        break;
      case TYPE_UUID:
        sb.append("writeUuid(").append(variableName).append(");");
        break;
      case TYPE_DOUBLE:
        sb.append("writeDouble(").append(variableName).append(");");
        break;
      default:
        throw new RuntimeException("Compiler error: no Java name for base type " + tbase + " for " + variableName);
      }
      sb.append("\n");
    } else {
      throw new RuntimeException("DO NOT KNOW HOW TO SERIALIZE FIELD '" + variableName + "' TYPE '" + typeName(type) +
                                 "'");
    }
  }

  /**
   * Serializes all the members of a struct.
   *
   * @param sb The StringBuilder to append to
   * @param tstruct The struct to serialize
   * @param variableName String name of the struct variable
   */
  private void generateSerializeStruct(StringBuilder sb, TStruct tstruct, String variableName) {
    sb.append(indent()).append(variableName).append(".write(oprot);\n");
  }

  /**
   * Serializes a container by writing its size then the elements.
   *
   * @param sb The StringBuilder to append to
   * @param ttype The type of container
   * @param variableName String name of the container variable
   * @param hasMetaData Whether protocol metadata is present
   */
  private void generateSerializeContainer(StringBuilder sb, TType ttype, String variableName, boolean hasMetaData) {
    scope_up(sb);

    if (hasMetaData) { // Standard protocols usually have metadata
      if (ttype.isMap()) {
        TMap tmap = (TMap)ttype;
        sb.append(indent())
            .append("oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(")
            .append(typeToEnum(tmap.getKeyType()))
            .append(", ")
            .append(typeToEnum(tmap.getValType()))
            .append(", ")
            .append(variableName)
            .append(".size()));\n");
      } else if (ttype.isSet()) {
        TSet tset = (TSet)ttype;
        sb.append(indent())
            .append("oprot.writeSetBegin(new org.apache.thrift.protocol.TSet(")
            .append(typeToEnum(tset.getElemType()))
            .append(", ")
            .append(variableName)
            .append(".size()));\n");
      } else if (ttype.isList()) {
        TList tlist = (TList)ttype;
        sb.append(indent())
            .append("oprot.writeListBegin(new org.apache.thrift.protocol.TList(")
            .append(typeToEnum(tlist.getElemType()))
            .append(", ")
            .append(variableName)
            .append(".size()));\n");
      }
    } else { // Typically for tuple protocol or similar compact forms
      sb.append(indent()).append("oprot.writeI32(").append(variableName).append(".size());\n");
    }

    String iterVar = tmp("_iter");
    if (ttype.isMap()) {
      TMap tmap = (TMap)ttype;
      sb.append(indent())
          .append("for (java.util.Map.Entry<")
          .append(typeName(tmap.getKeyType(), true))
          .append(", ")
          .append(typeName(tmap.getValType(), true))
          .append("> ")
          .append(iterVar)
          .append(" : ")
          .append(variableName)
          .append(".entrySet())\n");
    } else if (ttype.isSet()) {
      TSet tset = (TSet)ttype;
      sb.append(indent())
          .append("for (")
          .append(typeName(tset.getElemType()))
          .append(" ")
          .append(iterVar)
          .append(" : ")
          .append(variableName)
          .append(")\n");
    } else if (ttype.isList()) {
      TList tlist = (TList)ttype;
      sb.append(indent())
          .append("for (")
          .append(typeName(tlist.getElemType()))
          .append(" ")
          .append(iterVar)
          .append(" : ")
          .append(variableName)
          .append(")\n");
    }
    sb.append(indent()).append("{\n"); // Start of for-loop block
    indent_up();

    if (ttype.isMap()) {
      generateSerializeMapElement(sb, (TMap)ttype, iterVar, hasMetaData);
    } else if (ttype.isSet()) {
      generateSerializeSetElement(sb, (TSet)ttype, iterVar, hasMetaData);
    } else if (ttype.isList()) {
      generateSerializeListElement(sb, (TList)ttype, iterVar, hasMetaData);
    }

    indent_down();
    sb.append(indent()).append("}\n"); // End of for-loop block

    if (hasMetaData) {
      if (ttype.isMap()) {
        sb.append(indent()).append("oprot.writeMapEnd();\n");
      } else if (ttype.isSet()) {
        sb.append(indent()).append("oprot.writeSetEnd();\n");
      } else if (ttype.isList()) {
        sb.append(indent()).append("oprot.writeListEnd();\n");
      }
    }
    scope_down(sb);
  }

  private void generateSerializeMapElement(StringBuilder sb, TMap tmap, String iterVar, boolean hasMetaData) {
    generateSerializeFieldInternal(sb, tmap.getKeyType(), iterVar + ".getKey()", hasMetaData);
    generateSerializeFieldInternal(sb, tmap.getValType(), iterVar + ".getValue()", hasMetaData);
  }

  private void generateSerializeSetElement(StringBuilder sb, TSet tset, String iterVar, boolean hasMetaData) {
    generateSerializeFieldInternal(sb, tset.getElemType(), iterVar, hasMetaData);
  }

  private void generateSerializeListElement(StringBuilder sb, TList tlist, String iterVar, boolean hasMetaData) {
    generateSerializeFieldInternal(sb, tlist.getElemType(), iterVar, hasMetaData);
  }

  /**
   * Deserializes a field of any type.
   *
   * @param sb The StringBuilder to append to
   * @param field The field to deserialize
   * @param prefix The variable name or container for this field (e.g., "struct.")
   * @param hasMetaData Whether protocol metadata is present
   */
  private void generateDeserializeField(StringBuilder sb, TField field, String prefix, boolean hasMetaData) {
    String variableName = prefix + makeValidJavaIdentifier(field.getName());
    // Declaration of variableName (e.g., struct.fieldName) is assumed to exist.
    // This method generates the assignment part: variableName = iprot.readXXX();
    generateDeserializeFieldInternal(sb, field.getType(), variableName, hasMetaData);
  }

  /**
   * Internal helper to deserialize a TType into a given variable.
   *
   * @param sb The StringBuilder to append to
   * @param type The type to deserialize
   * @param variableName The name of the variable to assign the deserialized value to
   * @param hasMetaData Whether protocol metadata is present
   */
  private void generateDeserializeFieldInternal(StringBuilder sb, TType type, String variableName,
                                                boolean hasMetaData) {
    type = getTrueType(type);

    if (type.isVoid()) {
      throw new RuntimeException("CANNOT GENERATE DESERIALIZE CODE FOR void TYPE: " + variableName);
    }

    if (type.isStruct() || type.isXception()) {
      generateDeserializeStruct(sb, (TStruct)type, variableName);
    } else if (type.isContainer()) {
      generateDeserializeContainer(sb, type, variableName, hasMetaData);
    } else if (type.isBaseType()) {
      sb.append(indent()).append(variableName).append(" = iprot.");
      TBaseType baseType = (TBaseType)type;
      TBaseType.Base tbase = baseType.getBase();
      switch (tbase) {
      case TYPE_STRING:
        if (type.isBinary()) {
          sb.append("readBinary();");
        } else {
          sb.append("readString();");
        }
        break;
      case TYPE_BOOL:
        sb.append("readBool();");
        break;
      case TYPE_I8:
        sb.append("readByte();");
        break;
      case TYPE_I16:
        sb.append("readI16();");
        break;
      case TYPE_I32:
        sb.append("readI32();");
        break;
      case TYPE_I64:
        sb.append("readI64();");
        break;
      case TYPE_UUID:
        sb.append("readUuid();");
        break;
      case TYPE_DOUBLE:
        sb.append("readDouble();");
        break;
      default:
        throw new RuntimeException("compiler error: no Java name for base type " + tbase + " for " + variableName);
      }
      sb.append("\n");
    } else if (type.isEnum()) {
      sb.append(indent())
          .append(variableName)
          .append(" = ")
          .append(typeName(type, true, false, false, true)) // Use full type name for enum
          .append(".findByValue(iprot.readI32());\n");
    } else {
      throw new RuntimeException("DO NOT KNOW HOW TO DESERIALIZE FIELD '" + variableName + "' TYPE '" + typeName(type) +
                                 "'");
    }
  }

  /**
   * Generates an unserializer for a struct, invokes read()
   *
   * @param sb The StringBuilder to append to
   * @param tstruct The struct to deserialize
   * @param variableName The name of the variable to hold the struct instance
   */
  private void generateDeserializeStruct(StringBuilder sb, TStruct tstruct, String variableName) {
    if (options.isReuseObjects()) {
      sb.append(indent()).append("if (").append(variableName).append(" == null) {\n");
      indent_up();
      sb.append(indent()).append(variableName).append(" = new ").append(typeName(tstruct)).append("();\n");
      indent_down();
      sb.append(indent()).append("}\n");
    } else {
      sb.append(indent()).append(variableName).append(" = new ").append(typeName(tstruct)).append("();\n");
    }
    sb.append(indent()).append(variableName).append(".read(iprot);\n");
  }

  private void generateContainerInitialization(StringBuilder sb, TType ttype, String variableName,
                                               String sizeExpression) {
    sb.append(indent()).append(variableName).append(" = new ");
    if (isEnumSet(ttype)) {
      sb.append(typeName(ttype, false, true, true)).append(".noneOf(").append(innerEnumTypeName(ttype)).append(");\n");
    } else {
      sb.append(typeName(ttype, false, true));
      String constructorArgs = "";
      if (isEnumMap(ttype)) {
        constructorArgs = innerEnumTypeName(ttype);
      } else if (!(options.isSortedContainers() && (ttype.isMap() || ttype.isSet()))) {
        // Non-sorted containers (HashMap, ArrayList, HashSet) can take capacity
        constructorArgs = (ttype.isList() ? "" : "2*") + sizeExpression;
      }
      sb.append("(").append(constructorArgs).append(");\n");
    }
  }
  private void generateDeserializeContainer(StringBuilder sb, TType ttype, String variableName, boolean hasMetaData) {
    scope_up(sb);

    String protoContainerVar; // e.g., _map, _set, _list from TProtocol
    if (ttype.isMap()) {
      protoContainerVar = tmp("_map");
      sb.append(indent())
          .append("org.apache.thrift.protocol.TMap ")
          .append(protoContainerVar)
          .append(" = iprot.readMapBegin(");
      if (!hasMetaData) {
        TMap tmap = (TMap)ttype;
        sb.append(typeToEnum(tmap.getKeyType())).append(", ").append(typeToEnum(tmap.getValType()));
      }
      sb.append(");\n");
    } else if (ttype.isSet()) {
      protoContainerVar = tmp("_set");
      sb.append(indent())
          .append("org.apache.thrift.protocol.TSet ")
          .append(protoContainerVar)
          .append(" = iprot.readSetBegin(");
      if (!hasMetaData) {
        TSet tset = (TSet)ttype;
        sb.append(typeToEnum(tset.getElemType()));
      }
      sb.append(");\n");
    } else if (ttype.isList()) {
      protoContainerVar = tmp("_list");
      sb.append(indent())
          .append("org.apache.thrift.protocol.TList ")
          .append(protoContainerVar)
          .append(" = iprot.readListBegin(");
      if (!hasMetaData) {
        TList tlist = (TList)ttype;
        sb.append(typeToEnum(tlist.getElemType()));
      }
      sb.append(");\n");
    } else {
      throw new RuntimeException("Unknown container type: " + ttype.getName());
    }
    // Initialize or clear the container
    if (options.isReuseObjects()) {
      sb.append(indent()).append("if (").append(variableName).append(" == null) {\n");
      indent_up();
      generateContainerInitialization(sb, ttype, variableName, protoContainerVar + ".size");
      indent_down();
      sb.append(indent()).append("} else {\n");
      indent_up();
      sb.append(indent()).append(variableName).append(".clear();\n");
      indent_down();
      sb.append(indent()).append("}\n");
    } else {
      generateContainerInitialization(sb, ttype, variableName, protoContainerVar + ".size");
    }

    // Declare variables before loop
    String keyVar = null;
    String valVar = null;
    String elemVar = null;
    if (ttype.isMap()) {
      TMap tmap = (TMap)ttype;
      keyVar = tmp("_key");
      valVar = tmp("_val");
      TType keyType = getTrueType(tmap.getKeyType());
      TType valType = getTrueType(tmap.getValType());

      // Declare key and value variables
      sb.append(indent());
      if (typeCanBeNull(keyType)) {
        sb.append(javaNullableAnnotation()).append(" ");
      }
      sb.append(typeName(keyType)).append(" ").append(keyVar);
      if (options.isReuseObjects() && !keyType.isBaseType()) {
        sb.append(" = null");
      }
      sb.append(";\n");

      sb.append(indent());
      if (typeCanBeNull(valType)) {
        sb.append(javaNullableAnnotation()).append(" ");
      }
      sb.append(typeName(valType)).append(" ").append(valVar);
      if (options.isReuseObjects() && !valType.isBaseType()) {
        sb.append(" = null");
      }
      sb.append(";\n");
    } else { // List or Set
      TType elementType;
      if (ttype.isSet()) {
        elementType = ((TSet)ttype).getElemType();
      } else { // isList
        elementType = ((TList)ttype).getElemType();
      }
      elemVar = tmp("_elem");
      TType elemType = getTrueType(elementType);

      // Declare element variable
      sb.append(indent());
      if (typeCanBeNull(elemType)) {
        sb.append(javaNullableAnnotation()).append(" ");
      }
      sb.append(typeName(elemType)).append(" ").append(elemVar);
      if (options.isReuseObjects() && !elemType.isBaseType()) {
        sb.append(" = null");
      }
      sb.append(";\n");
    }

    // Loop and deserialize elements
    String iVar = tmp("_i");
    sb.append(indent())
        .append("for (int ")
        .append(iVar)
        .append(" = 0; ")
        .append(iVar)
        .append(" < ")
        .append(protoContainerVar)
        .append(".size; ++")
        .append(iVar)
        .append(")\n");
    sb.append(indent()).append("{\n");
    indent_up();

    if (ttype.isMap()) {
      generateDeserializeMapElement(sb, (TMap)ttype, variableName, hasMetaData, keyVar, valVar);
    } else if (ttype.isSet()) {
      generateDeserializeSetElement(sb, (TSet)ttype, variableName, hasMetaData, elemVar);
    } else if (ttype.isList()) {
      generateDeserializeListElement(sb, (TList)ttype, variableName, hasMetaData, elemVar);
    }

    indent_down();
    sb.append(indent()).append("}\n"); // End of for-loop

    if (hasMetaData) {
      // Read container end
      if (ttype.isMap()) {
        sb.append(indent()).append("iprot.readMapEnd();\n");
      } else if (ttype.isSet()) {
        sb.append(indent()).append("iprot.readSetEnd();\n");
      } else if (ttype.isList()) {
        sb.append(indent()).append("iprot.readListEnd();\n");
      }
    }
    scope_down(sb);
  }

  private void generateDeserializeMapElement(StringBuilder sb, TMap tmap, String mapVarName, boolean hasMetaData,
                                             String keyVar, String valVar) {
    TType keyType = getTrueType(tmap.getKeyType());
    TType valType = getTrueType(tmap.getValType());

    // Deserialize key and value
    generateDeserializeFieldInternal(sb, keyType, keyVar, hasMetaData);
    generateDeserializeFieldInternal(sb, valType, valVar, hasMetaData);

    // Put into map
    if (keyType.isEnum()) {
      sb.append(indent()).append("if (").append(keyVar).append(" != null)\n");
      sb.append(indent()).append("{\n");
      indent_up();
    }
    sb.append(indent()).append(mapVarName).append(".put(").append(keyVar).append(", ").append(valVar).append(");\n");
    if (keyType.isEnum()) {
      indent_down();
      sb.append(indent()).append("}\n");
    }

    // Handle reuse_objects for complex types
    if (options.isReuseObjects()) {
      if (!keyType.isBaseType()) {
        sb.append(indent()).append(keyVar).append(" = null;\n");
      }
      if (!valType.isBaseType()) {
        sb.append(indent()).append(valVar).append(" = null;\n");
      }
    }
  }

  private void generateDeserializeSetElement(StringBuilder sb, TSet tset, String setVarName, boolean hasMetaData,
                                             String elemVar) {
    TType elemType = getTrueType(tset.getElemType());

    // Deserialize element
    generateDeserializeFieldInternal(sb, elemType, elemVar, hasMetaData);

    // Add to set
    if (elemType.isEnum()) {
      sb.append(indent()).append("if (").append(elemVar).append(" != null)\n");
      sb.append(indent()).append("{\n");
      indent_up();
    }
    sb.append(indent()).append(setVarName).append(".add(").append(elemVar).append(");\n");
    if (elemType.isEnum()) {
      indent_down();
      sb.append(indent()).append("}\n");
    }

    // Handle reuse_objects for complex types
    if (options.isReuseObjects() && !elemType.isBaseType()) {
      sb.append(indent()).append(elemVar).append(" = null;\n");
    }
  }

  private void generateDeserializeListElement(StringBuilder sb, TList tlist, String listVarName, boolean hasMetaData,
                                              String elemVar) {
    TType elemType = getTrueType(tlist.getElemType());

    // Deserialize element
    generateDeserializeFieldInternal(sb, elemType, elemVar, hasMetaData);

    // Add to list
    if (elemType.isEnum()) {
      sb.append(indent()).append("if (").append(elemVar).append(" != null)\n");
      sb.append(indent()).append("{\n");
      indent_up();
    }
    sb.append(indent()).append(listVarName).append(".add(").append(elemVar).append(");\n");
    if (elemType.isEnum()) {
      indent_down();
      sb.append(indent()).append("}\n");
    }
    // Handle reuse_objects for complex types
    if (options.isReuseObjects() && !elemType.isBaseType()) {
      sb.append(indent()).append(elemVar).append(" = null;\n");
    }
  }

  public GenResult generateService(TService tservice) {
    String serviceName = tservice.getName();
    StringBuilder sb = new StringBuilder();

    sb.append(autogenComment());
    sb.append(javaPackage());

    if (!options.isSuppressGeneratedAnnotations()) {
      sb.append(getAutogenComment());
    }
    sb.append(javaSuppressions());
    sb.append("public class ").append(makeValidJavaIdentifier(serviceName)).append(" {\n\n");
    indent_up();

    generateServiceInterface(sb, tservice);
    generateServiceAsyncInterface(sb, tservice);
    if (options.isFutureIface()) {
      generateServiceFutureInterface(sb, tservice);
    }
    generateServiceClient(sb, tservice);
    generateServiceAsyncClient(sb, tservice);
    if (options.isFutureIface()) {
      generateServiceFutureClient(sb, tservice);
    }
    generateServiceServer(sb, tservice);
    generateServiceAsyncServer(sb, tservice);
    generateServiceHelpers(sb, tservice);

    indent_down();
    sb.append("}\n");
    return new GenResult(makeValidJavaFilename(serviceName) + ".java", sb.toString());
  }

  /**
   * Generates a service interface definition.
   *
   * @param tservice The service to generate an interface for
   */
  protected void generateServiceInterface(StringBuilder sb, TService tservice) {
    String extends_iface = "";
    if (tservice.getExtends() != null) {
      extends_iface = " extends " + typeName(tservice.getExtends()) + ".Iface";
    }

    generateJavaDoc(sb, tservice);
    sb.append(indent()).append("public interface Iface").append(extends_iface).append(" {\n\n");
    indent_up();
    for (TFunction function : tservice.getFunctions()) {
      generateJavaDocFunc(sb, function);
      sb.append(indent()).append("public ").append(functionSignature(function)).append(";\n\n");
    }
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  protected void generateServiceAsyncInterface(StringBuilder sb, TService tservice) {
    String extends_iface = "";
    if (tservice.getExtends() != null) {
      extends_iface = " extends " + typeName(tservice.getExtends()) + ".AsyncIface";
    }

    sb.append(indent()).append("public interface AsyncIface").append(extends_iface).append(" {\n\n");
    indent_up();
    for (TFunction function : tservice.getFunctions()) {
      sb.append(indent())
          .append("public ")
          .append(functionSignatureAsync(function, true, ""))
          .append(" throws org.apache.thrift.TException;\n\n");
    }
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  protected void generateServiceFutureInterface(StringBuilder sb, TService tservice) {
    String extends_iface = "";
    if (tservice.getExtends() != null) {
      extends_iface = " extends " + typeName(tservice.getExtends()) + ".FutureIface";
    }

    sb.append(indent()).append("public interface FutureIface").append(extends_iface).append(" {\n\n");
    indent_up();
    for (TFunction tfunc : tservice.getFunctions()) {
      sb.append(indent())
          .append("public ")
          .append(functionSignatureFuture(tfunc))
          .append(" throws org.apache.thrift.TException;\n\n");
    }
    scope_down(sb);
    sb.append("\n\n");
  }

  /**
   * Generates structs for all the service args and return types
   *
   * @param tservice The service
   */
  protected void generateServiceHelpers(StringBuilder sb, TService tservice) {
    for (TFunction function : tservice.getFunctions()) {
      TStruct ts = function.getArglist();
      generateJavaStructDefinition(sb, ts, false, true, false);
      generateFunctionHelpers(sb, tservice.getProgram(), function);
    }
  }

  /**
   * Generates a service client definition.
   *
   * @param tservice The service to generate a client for.
   */
  protected void generateServiceClient(StringBuilder sb, TService tservice) {
    String extends_client = "org.apache.thrift.TServiceClient";
    if (tservice.getExtends() != null) {
      extends_client = typeName(tservice.getExtends()) + ".Client";
    }

    sb.append(indent())
        .append("public static class Client extends ")
        .append(extends_client)
        .append(" implements Iface {\n");
    indent_up();

    sb.append(indent()).append("public static class Factory implements "
                               + "org.apache.thrift.TServiceClientFactory<Client> {\n");
    indent_up();
    sb.append(indent()).append("public Factory() {}\n");
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public Client getClient(org.apache.thrift.protocol.TProtocol prot) {\n");
    indent_up();
    sb.append(indent()).append("return new Client(prot);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("public Client getClient(org.apache.thrift.protocol.TProtocol "
                               + "iprot, org.apache.thrift.protocol.TProtocol oprot) {\n");
    indent_up();
    sb.append(indent()).append("return new Client(iprot, oprot);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent()).append("public Client(org.apache.thrift.protocol.TProtocol prot)\n");
    scope_up(sb);
    sb.append(indent()).append("super(prot, prot);\n");
    scope_down(sb);
    sb.append("\n");

    sb.append(indent()).append("public Client(org.apache.thrift.protocol.TProtocol iprot, "
                               + "org.apache.thrift.protocol.TProtocol oprot) {\n");
    sb.append(indent()).append("  super(iprot, oprot);\n");
    sb.append(indent()).append("}\n\n");

    for (TFunction function : tservice.getFunctions()) {
      String funname = function.getName();
      String sep = "_";
      String javaname = funname;
      if (options.isFullcamel()) {
        sep = "";
        javaname = asCamelCase(funname);
      }

      sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
      sb.append(indent()).append("public ").append(functionSignature(function)).append("\n");
      scope_up(sb);
      sb.append(indent()).append("send").append(sep).append(javaname).append("(");

      TStruct arg_struct = function.getArglist();
      List<TField> fields = arg_struct.getMembers();
      boolean first = true;
      for (TField field : fields) {
        if (first) {
          first = false;
        } else {
          sb.append(", ");
        }
        sb.append(makeValidJavaIdentifier(field.getName()));
      }
      sb.append(");\n");

      if (!function.isOneway()) {
        sb.append(indent());
        if (!function.getReturnType().isVoid()) {
          sb.append("return ");
        }
        sb.append("recv").append(sep).append(javaname).append("();\n");
      }
      scope_down(sb);
      sb.append("\n");

      TFunction send_function =
          new TFunction(new TBaseType("", TBaseType.Base.TYPE_VOID), "send" + sep + javaname, function.getArglist());

      sb.append(indent()).append("public ").append(functionSignature(send_function)).append("\n");
      scope_up(sb);

      String argsname = function.getName() + "_args";
      sb.append(indent()).append(argsname).append(" args = new ").append(argsname).append("();\n");

      for (TField field : fields) {
        sb.append(indent())
            .append("args.set")
            .append(getCapName(field.getName()))
            .append("(")
            .append(makeValidJavaIdentifier(field.getName()))
            .append(");\n");
      }

      String sendBaseName = function.isOneway() ? "sendBaseOneway" : "sendBase";
      sb.append(indent()).append(sendBaseName).append("(\"").append(funname).append("\", args);\n");

      scope_down(sb);
      sb.append("\n");

      if (!function.isOneway()) {
        String resultname = function.getName() + "_result";
        TFunction recv_function = new TFunction(function.getReturnType(), "recv" + sep + javaname,
                                                new TStruct(tservice.getProgram()), function.getXceptions(), false);
        sb.append(indent()).append("public ").append(functionSignature(recv_function)).append("\n");
        scope_up(sb);

        sb.append(indent()).append(resultname).append(" result = new ").append(resultname).append("();\n");
        sb.append(indent()).append("receiveBase(result, \"").append(funname).append("\");\n");

        if (!function.getReturnType().isVoid()) {
          sb.append(indent()).append("if (result.").append(generateIssetCheck("success")).append(") {\n");
          sb.append(indent()).append("  return result.success;\n");
          sb.append(indent()).append("}\n");
        }

        for (TField x_iter : function.getXceptions().getMembers()) {
          sb.append(indent())
              .append("if (result.")
              .append(makeValidJavaIdentifier(x_iter.getName()))
              .append(" != null) {\n");
          sb.append(indent()).append("  throw result.").append(makeValidJavaIdentifier(x_iter.getName())).append(";\n");
          sb.append(indent()).append("}\n");
        }

        if (function.getReturnType().isVoid()) {
          sb.append(indent()).append("return;\n");
        } else {
          sb.append(indent())
              .append("throw new "
                      + "org.apache.thrift.TApplicationException(org.apache.thrift."
                      + "TApplicationException.MISSING_RESULT, \"")
              .append(funname)
              .append(" failed: unknown result\");\n");
        }
        scope_down(sb);
        sb.append("\n");
      }
    }
    indent_down();
    sb.append(indent()).append("}\n");
  }

  protected void generateServiceFutureClient(StringBuilder sb, TService tservice) {
    String extends_client = "";
    if (tservice.getExtends() != null) {
      extends_client = "extends " + typeName(tservice.getExtends()) + ".FutureClient ";
    }

    final String adapter_class = "org.apache.thrift.async.AsyncMethodFutureAdapter";
    sb.append(indent())
        .append("public static class FutureClient ")
        .append(extends_client)
        .append("implements FutureIface {\n");
    indent_up();
    sb.append(indent()).append("public FutureClient(AsyncIface delegate) {\n");
    indent_up();
    sb.append(indent()).append("this.delegate = delegate;\n");
    scope_down(sb);
    sb.append(indent()).append("private final AsyncIface delegate;\n\n");

    for (TFunction tfunc : tservice.getFunctions()) {
      String funname = tfunc.getName();
      String ret_typeName = typeName(tfunc.getReturnType(), true);

      sb.append(indent()).append("@Override\n");
      sb.append(indent())
          .append("public ")
          .append(functionSignatureFuture(tfunc))
          .append(" throws org.apache.thrift.TException {\n");
      indent_up();
      String adapter = tmp("asyncMethodFutureAdapter");
      sb.append(indent())
          .append(adapter_class)
          .append("<")
          .append(ret_typeName)
          .append("> ")
          .append(adapter)
          .append(" = ")
          .append(adapter_class)
          .append(".<")
          .append(ret_typeName)
          .append(">create();\n");

      boolean empty_args = tfunc.getArglist().getMembers().isEmpty();
      sb.append(indent())
          .append("delegate.")
          .append(getRpcMethodName(funname))
          .append("(")
          .append(argumentList(tfunc.getArglist(), false))
          .append(empty_args ? "" : ", ")
          .append(adapter)
          .append(");\n");
      sb.append(indent()).append("return ").append(adapter).append(".getFuture();\n");
      scope_down(sb);
      sb.append("\n");
    }
    scope_down(sb);
    sb.append("\n");
  }

  protected void generateServiceAsyncClient(StringBuilder sb, TService tservice) {
    String extends_client = "org.apache.thrift.async.TAsyncClient";
    if (tservice.getExtends() != null) {
      extends_client = typeName(tservice.getExtends()) + ".AsyncClient";
    }

    sb.append(indent())
        .append("public static class AsyncClient extends ")
        .append(extends_client)
        .append(" implements AsyncIface {\n");
    indent_up();

    sb.append(indent()).append("public static class Factory implements "
                               + "org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {\n");
    sb.append(indent()).append("  private org.apache.thrift.async.TAsyncClientManager clientManager;\n");
    sb.append(indent()).append("  private org.apache.thrift.protocol.TProtocolFactory protocolFactory;\n");
    sb.append(indent()).append("  public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, "
                               + "org.apache.thrift.protocol.TProtocolFactory protocolFactory) {\n");
    sb.append(indent()).append("    this.clientManager = clientManager;\n");
    sb.append(indent()).append("    this.protocolFactory = protocolFactory;\n");
    sb.append(indent()).append("  }\n");
    sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
    sb.append(indent()).append("  public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport "
                               + "transport) {\n");
    sb.append(indent()).append("    return new AsyncClient(protocolFactory, clientManager, transport);\n");
    sb.append(indent()).append("  }\n");
    sb.append(indent()).append("}\n\n");

    sb.append(indent()).append("public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, "
                               + "org.apache.thrift.async.TAsyncClientManager clientManager, "
                               + "org.apache.thrift.transport.TNonblockingTransport transport) {\n");
    sb.append(indent()).append("  super(protocolFactory, clientManager, transport);\n");
    sb.append(indent()).append("}\n\n");

    for (TFunction function : tservice.getFunctions()) {
      String funname = function.getName();
      String sep = "_";
      String javaname = funname;
      if (options.isFullcamel()) {
        sep = "";
        javaname = asCamelCase(javaname);
      }
      TType ret_type = function.getReturnType();
      TStruct arg_struct = function.getArglist();
      String funclassname = funname + "_call";
      List<TField> fields = arg_struct.getMembers();
      List<TField> xceptions = function.getXceptions().getMembers();
      String args_name = function.getName() + "_args";

      sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
      sb.append(indent())
          .append("public ")
          .append(functionSignatureAsync(function, false, ""))
          .append(" throws org.apache.thrift.TException {\n");
      sb.append(indent()).append("  checkReady();\n");
      sb.append(indent())
          .append("  ")
          .append(funclassname)
          .append(" method_call = new ")
          .append(funclassname)
          .append("(")
          .append(asyncArgumentList(function, arg_struct, ret_type, false))
          .append(", this, ___protocolFactory, ___transport);\n");
      sb.append(indent()).append("  this.___currentMethod = method_call;\n");
      sb.append(indent()).append("  ___manager.call(method_call);\n");
      sb.append(indent()).append("}\n\n");

      sb.append(indent())
          .append("public static class ")
          .append(funclassname)
          .append(" extends org.apache.thrift.async.TAsyncMethodCall<")
          .append(typeName(function.getReturnType(), true))
          .append("> {\n");
      indent_up();

      for (TField field : fields) {
        sb.append(indent())
            .append("private ")
            .append(typeName(field.getType()))
            .append(" ")
            .append(makeValidJavaIdentifier(field.getName()))
            .append(";\n");
      }

      sb.append(indent())
          .append("public ")
          .append(funclassname)
          .append("(")
          .append(asyncArgumentList(function, arg_struct, ret_type, true))
          .append(", org.apache.thrift.async.TAsyncClient client, "
                  + "org.apache.thrift.protocol.TProtocolFactory protocolFactory, "
                  + "org.apache.thrift.transport.TNonblockingTransport transport) throws "
                  + "org.apache.thrift.TException {\n");
      sb.append(indent())
          .append("  super(client, protocolFactory, transport, resultHandler, ")
          .append(function.isOneway() ? "true" : "false")
          .append(");\n");
      for (TField field : fields) {
        sb.append(indent())
            .append("  this.")
            .append(makeValidJavaIdentifier(field.getName()))
            .append(" = ")
            .append(makeValidJavaIdentifier(field.getName()))
            .append(";\n");
      }
      sb.append(indent()).append("}\n\n");

      sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
      sb.append(indent()).append("public void write_args(org.apache.thrift.protocol.TProtocol "
                                 + "prot) throws org.apache.thrift.TException {\n");
      indent_up();
      String msgType = function.isOneway() ? "TMessageType.ONEWAY" : "TMessageType.CALL";
      sb.append(indent())
          .append("prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage(\"")
          .append(funname)
          .append("\", org.apache.thrift.protocol.")
          .append(msgType)
          .append(", 0));\n");
      sb.append(indent()).append(args_name).append(" args = new ").append(args_name).append("();\n");
      for (TField field : fields) {
        sb.append(indent())
            .append("args.set")
            .append(getCapName(field.getName()))
            .append("(")
            .append(makeValidJavaIdentifier(field.getName()))
            .append(");\n");
      }
      sb.append(indent()).append("args.write(prot);\n");
      sb.append(indent()).append("prot.writeMessageEnd();\n");
      indent_down();
      sb.append(indent()).append("}\n\n");

      sb.append(indent()).append(javaOverrideAnnotation()).append("\n");
      sb.append(indent()).append("public ").append(typeName(ret_type, true)).append(" getResult() throws ");
      for (int i = 0; i < xceptions.size(); i++) {
        sb.append(typeName(xceptions.get(i).getType(), false, false)).append(", ");
      }
      sb.append("org.apache.thrift.TException {\n");
      indent_up();
      sb.append(indent()).append("if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {\n");
      sb.append(indent()).append("  throw new java.lang.IllegalStateException(\"Method call not finished!\");\n");
      sb.append(indent()).append("}\n");
      sb.append(indent()).append("org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new "
                                 + "org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());\n");
      sb.append(indent()).append("org.apache.thrift.protocol.TProtocol prot = "
                                 + "client.getProtocolFactory().getProtocol(memoryTransport);\n");
      sb.append(indent());
      if (ret_type.isVoid()) {
        if (!function.isOneway()) {
          sb.append("(new Client(prot)).recv").append(sep).append(javaname).append("();\n");
          sb.append(indent());
        }
        sb.append("return null;\n");
      } else {
        sb.append("return (new Client(prot)).recv").append(sep).append(javaname).append("();\n");
      }
      indent_down();
      sb.append(indent()).append("}\n");
      indent_down();
      sb.append(indent()).append("}\n\n");
    }
    scope_down(sb);
    sb.append("\n");
  }

  /**
   * Generates a service server definition.
   *
   * @param tservice The service to generate a server for.
   */
  protected void generateServiceServer(StringBuilder sb, TService tservice) {
    String extends_processor = "org.apache.thrift.TBaseProcessor<I>";
    if (tservice.getExtends() != null) {
      extends_processor = typeName(tservice.getExtends()) + ".Processor<I>";
    }

    sb.append(indent())
        .append("public static class Processor<I extends Iface> extends ")
        .append(extends_processor)
        .append(" implements org.apache.thrift.TProcessor {\n");
    indent_up();
    sb.append(indent()).append("private static final org.slf4j.Logger _LOGGER = "
                               + "org.slf4j.LoggerFactory.getLogger(Processor.class.getName());\n");
    sb.append(indent()).append("public Processor(I iface) {\n");
    sb.append(indent()).append("  super(iface, getProcessMap(new java.util.HashMap<java.lang.String, "
                               + "org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));\n");
    sb.append(indent()).append("}\n\n");
    sb.append(indent()).append(
        "protected Processor(I iface, java.util.Map<java.lang.String, "
        + "org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {\n");
    sb.append(indent()).append("  super(iface, getProcessMap(processMap));\n");
    sb.append(indent()).append("}\n\n");
    sb.append(indent()).append("private static <I extends Iface> java.util.Map<java.lang.String,  "
                               + "org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> "
                               + "getProcessMap(java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? "
                               + "extends  org.apache.thrift.TBase>> processMap) {\n");
    indent_up();
    for (TFunction function : tservice.getFunctions()) {
      sb.append(indent())
          .append("processMap.put(\"")
          .append(function.getName())
          .append("\", new ")
          .append(makeValidJavaIdentifier(function.getName()))
          .append("());\n");
    }
    sb.append(indent()).append("return processMap;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    for (TFunction function : tservice.getFunctions()) {
      generateProcessFunction(sb, tservice, function);
    }
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  protected void generateServiceAsyncServer(StringBuilder sb, TService tservice) {
    String extends_processor = "org.apache.thrift.TBaseAsyncProcessor<I>";
    if (tservice.getExtends() != null) {
      extends_processor = typeName(tservice.getExtends()) + ".AsyncProcessor<I>";
    }

    sb.append(indent())
        .append("public static class AsyncProcessor<I extends AsyncIface> extends ")
        .append(extends_processor)
        .append(" {\n");
    indent_up();
    sb.append(indent()).append("private static final org.slf4j.Logger _LOGGER = "
                               + "org.slf4j.LoggerFactory.getLogger(AsyncProcessor.class.getName());\n");
    sb.append(indent()).append("public AsyncProcessor(I iface) {\n");
    sb.append(indent()).append(
        "  super(iface, getProcessMap(new java.util.HashMap<java.lang.String, "
        + "org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));\n");
    sb.append(indent()).append("}\n\n");
    sb.append(indent()).append("protected AsyncProcessor(I iface, java.util.Map<java.lang.String,  "
                               + "org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> "
                               + "processMap) {\n");
    sb.append(indent()).append("  super(iface, getProcessMap(processMap));\n");
    sb.append(indent()).append("}\n\n");
    sb.append(indent()).append("private static <I extends AsyncIface> java.util.Map<java.lang.String,  "
                               + "org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase,?>> "
                               + "getProcessMap(java.util.Map<java.lang.String,  "
                               + "org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> "
                               + "processMap) {\n");
    indent_up();
    for (TFunction function : tservice.getFunctions()) {
      sb.append(indent())
          .append("processMap.put(\"")
          .append(function.getName())
          .append("\", new ")
          .append(makeValidJavaIdentifier(function.getName()))
          .append("());\n");
    }
    sb.append(indent()).append("return processMap;\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    for (TFunction function : tservice.getFunctions()) {
      generateProcessAsyncFunction(sb, tservice, function);
    }
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  /**
   * Generates a struct and helpers for a function.
   *
   * @param tfunction The function
   */
  protected void generateFunctionHelpers(StringBuilder sb, TProgram program, TFunction tfunction) {
    if (tfunction.isOneway()) {
      return;
    }

    TStruct result = new TStruct(program, tfunction.getName() + "_result");
    if (!tfunction.getReturnType().isVoid()) {
      TField success = new TField(tfunction.getReturnType(), "success", 0);
      result.append(success);
    }

    for (TField field : tfunction.getXceptions().getMembers()) {
      result.append(field);
    }
    generateJavaStructDefinition(sb, result, false, true, true);
  }

  /**
   * Generates a process function definition.
   *
   * @param tfunction The function to write a dispatcher for
   */
  protected void generateProcessAsyncFunction(StringBuilder sb, TService tservice, TFunction tfunction) {
    String argsname = tfunction.getName() + "_args";
    String resultname = tfunction.getName() + "_result";
    if (tfunction.isOneway()) {
      resultname = "org.apache.thrift.TBase";
    }
    String resulttype = typeName(tfunction.getReturnType(), true);

    sb.append(indent())
        .append("public static class ")
        .append(makeValidJavaIdentifier(tfunction.getName()))
        .append("<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, ")
        .append(argsname)
        .append(", ")
        .append(resulttype)
        .append("> {\n");
    indent_up();
    sb.append(indent())
        .append("public ")
        .append(makeValidJavaIdentifier(tfunction.getName()))
        .append("() {\n  super(\"")
        .append(tfunction.getName())
        .append("\");\n}\n\n");
    // sb.append(indent()).append(javaOverrideAnnotation()).append("\npublic
    // ").append(resultname).append(" getEmptyResultInstance() {\n").append(tfunction.isOneway() ? "
    // return null;\n" : "  return new " + resultname + "();\n").append("}\n\n");
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\npublic ")
        .append(argsname)
        .append(" getEmptyArgsInstance() {\n  return new ")
        .append(argsname)
        .append("();\n}\n\n");
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\npublic org.apache.thrift.async.AsyncMethodCallback<")
        .append(resulttype)
        .append("> getResultHandler(final "
                + "org.apache.thrift.server.AbstractNonblockingServer.AsyncFrameBuffer fb, final "
                + "int seqid) {\n");
    indent_up();
    sb.append(indent()).append("final org.apache.thrift.AsyncProcessFunction fcall = this;\n");
    sb.append(indent())
        .append("return new org.apache.thrift.async.AsyncMethodCallback<")
        .append(resulttype)
        .append(">() {\n");
    indent_up();
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\npublic void onComplete(")
        .append(resulttype)
        .append(" o) {\n");
    indent_up();
    if (!tfunction.isOneway()) {
      sb.append(indent()).append(resultname).append(" result = new ").append(resultname).append("();\n");
      if (!tfunction.getReturnType().isVoid()) {
        sb.append(indent()).append("result.success = o;\n");
        if (!typeCanBeNull(tfunction.getReturnType())) {
          sb.append(indent()).append("result.set").append(getCapName("success")).append("IsSet(true);\n");
        }
      }
      sb.append(indent()).append("try {\n");
      sb.append(indent()).append("  fcall.sendResponse(fb, result, "
                                 + "org.apache.thrift.protocol.TMessageType.REPLY,seqid);\n");
      sb.append(indent())
          .append("} catch (org.apache.thrift.transport.TTransportException e) {\n")
          .append(indent())
          .append("  _LOGGER.error(\"TTransportException writing to internal frame buffer\", e);\n")
          .append(indent())
          .append("  fb.close();\n");
      sb.append(indent())
          .append("} catch (java.lang.Exception e) {\n")
          .append(indent())
          .append("  _LOGGER.error(\"Exception writing to internal frame buffer\", e);\n")
          .append(indent())
          .append("  onError(e);\n")
          .append(indent())
          .append("}\n");
    }
    indent_down();
    sb.append(indent()).append("}\n");

    sb.append(indent()).append(javaOverrideAnnotation()).append("\npublic void onError(java.lang.Exception e) {\n");
    indent_up();
    if (tfunction.isOneway()) {
      sb.append(indent())
          .append("if (e instanceof org.apache.thrift.transport.TTransportException) {\n")
          .append(indent())
          .append("  _LOGGER.error(\"TTransportException inside handler\", e);\n")
          .append(indent())
          .append("  fb.close();\n")
          .append(indent())
          .append("} else {\n")
          .append(indent())
          .append("  _LOGGER.error(\"Exception inside oneway handler\", e);\n")
          .append(indent())
          .append("}\n");
    } else {
      sb.append(indent()).append("byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;\n");
      sb.append(indent()).append("org.apache.thrift.TSerializable msg;\n");
      sb.append(indent()).append(resultname).append(" result = new ").append(resultname).append("();\n");
      List<TField> xceptions = tfunction.getXceptions().getMembers();
      if (!xceptions.isEmpty()) {
        boolean first = true;
        for (TField x_iter : xceptions) {
          if (first) {
            sb.append(indent());
            first = false;
          }
          String type = typeName(x_iter.getType(), false, false);
          String name = x_iter.getName();
          sb.append("if (e instanceof ").append(type).append(") {\n");
          indent_up();
          sb.append(indent())
              .append("result.")
              .append(makeValidJavaIdentifier(name))
              .append(" = (")
              .append(type)
              .append(") e;\n");
          sb.append(indent()).append("result.set").append(getCapName(name)).append("IsSet(true);\n");
          sb.append(indent()).append("msg = result;\n");
          indent_down();
          sb.append(indent()).append("} else ");
        }
      } else {
        sb.append(indent());
      }
      sb.append("if (e instanceof org.apache.thrift.transport.TTransportException) {\n")
          .append(indent())
          .append("  _LOGGER.error(\"TTransportException inside handler\", e);\n")
          .append(indent())
          .append("  fb.close();\n")
          .append(indent())
          .append("  return;\n")
          .append(indent())
          .append("} else if (e instanceof org.apache.thrift.TApplicationException) {\n")
          .append(indent())
          .append("  _LOGGER.error(\"TApplicationException inside handler\", e);\n")
          .append(indent())
          .append("  msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;\n")
          .append(indent())
          .append("  msg = (org.apache.thrift.TApplicationException)e;\n")
          .append(indent())
          .append("} else {\n")
          .append(indent())
          .append("  _LOGGER.error(\"Exception inside handler\", e);\n")
          .append(indent())
          .append("  msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;\n")
          .append(indent())
          .append("  msg = new "
                  + "org.apache.thrift.TApplicationException(org.apache.thrift."
                  + "TApplicationException.INTERNAL_ERROR, e.getMessage());\n")
          .append(indent())
          .append("}\n");
      sb.append(indent())
          .append("try {\n")
          .append(indent())
          .append("  fcall.sendResponse(fb,msg,msgType,seqid);\n")
          .append(indent())
          .append("} catch (java.lang.Exception ex) {\n")
          .append(indent())
          .append("  _LOGGER.error(\"Exception writing to internal frame buffer\", ex);\n")
          .append(indent())
          .append("  fb.close();\n")
          .append(indent())
          .append("}\n");
    }
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("};\n");
    indent_down();
    sb.append(indent()).append("}\n\n");

    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\nprotected boolean isOneway() {\n  return ")
        .append(tfunction.isOneway() ? "true" : "false")
        .append(";\n}\n\n");
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\npublic void start(I iface, ")
        .append(argsname)
        .append(" args, org.apache.thrift.async.AsyncMethodCallback<")
        .append(resulttype)
        .append("> resultHandler) throws org.apache.thrift.TException {\n");
    indent_up();
    sb.append(indent()).append("iface.").append(getRpcMethodName(tfunction.getName())).append("(");
    boolean first = true;
    for (TField field : tfunction.getArglist().getMembers()) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      sb.append("args.").append(makeValidJavaIdentifier(field.getName()));
    }
    if (!first)
      sb.append(",");
    sb.append("resultHandler);\n");
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  protected void generateProcessFunction(StringBuilder sb, TService tservice, TFunction tfunction) {
    String argsname = tfunction.getName() + "_args";
    String resultname = tfunction.getName() + "_result";
    if (tfunction.isOneway()) {
      resultname = "org.apache.thrift.TBase";
    }

    sb.append(indent())
        .append("public static class ")
        .append(makeValidJavaIdentifier(tfunction.getName()))
        .append("<I extends Iface> extends org.apache.thrift.ProcessFunction<I, ")
        .append(argsname)
        .append("> {\n");
    indent_up();
    sb.append(indent())
        .append("public ")
        .append(makeValidJavaIdentifier(tfunction.getName()))
        .append("() {\n  super(\"")
        .append(tfunction.getName())
        .append("\");\n}\n\n");
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\npublic ")
        .append(argsname)
        .append(" getEmptyArgsInstance() {\n  return new ")
        .append(argsname)
        .append("();\n}\n\n");
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\nprotected boolean isOneway() {\n  return ")
        .append(tfunction.isOneway() ? "true" : "false")
        .append(";\n}\n\n");
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\nprotected boolean rethrowUnhandledExceptions() {\n  return ")
        .append(options.isRethrowUnhandledExceptions() ? "true" : "false")
        .append(";\n}\n\n");
    // sb.append(indent()).append(javaOverrideAnnotation()).append("\npublic
    // ").append(resultname).append(" getEmptyResultInstance() {\n").append(tfunction.isOneway() ? "
    // return null;\n" : "  return new " + resultname + "();\n").append("}\n\n");
    sb.append(indent())
        .append(javaOverrideAnnotation())
        .append("\npublic ")
        .append(resultname)
        .append(" getResult(I iface, ")
        .append(argsname)
        .append(" args) throws org.apache.thrift.TException {\n");
    indent_up();
    if (!tfunction.isOneway()) {
      if (options.isReuseObjects()) {
        sb.append(indent()).append(resultname).append(" result = getEmptyResultInstance();\n");
      } else {
        sb.append(indent()).append(resultname).append(" result = new ").append(resultname).append("();\n");
      }
    }
    List<TField> xceptions = tfunction.getXceptions().getMembers();
    if (!xceptions.isEmpty()) {
      sb.append(indent()).append("try {\n");
      indent_up();
    }

    sb.append(indent());
    if (!tfunction.isOneway() && !tfunction.getReturnType().isVoid()) {
      sb.append("result.success = ");
    }
    sb.append("iface.").append(getRpcMethodName(tfunction.getName())).append("(");
    boolean first = true;
    for (TField field : tfunction.getArglist().getMembers()) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      sb.append("args.").append(makeValidJavaIdentifier(field.getName()));
    }
    sb.append(");\n");

    if (!tfunction.isOneway() && !tfunction.getReturnType().isVoid() && !typeCanBeNull(tfunction.getReturnType())) {
      sb.append(indent()).append("result.set").append(getCapName("success")).append("IsSet(true);\n");
    }
    if (!xceptions.isEmpty()) {
      indent_down();
      sb.append(indent()).append("}");
      for (TField x_iter : xceptions) {
        sb.append(" catch (")
            .append(typeName(x_iter.getType(), false, false))
            .append(" ")
            .append(makeValidJavaIdentifier(x_iter.getName()))
            .append(") {\n");
        if (!tfunction.isOneway()) {
          indent_up();
          sb.append(indent())
              .append("result.")
              .append(makeValidJavaIdentifier(x_iter.getName()))
              .append(" = ")
              .append(makeValidJavaIdentifier(x_iter.getName()))
              .append(";\n");
          indent_down();
        }
        sb.append(indent()).append("}");
      }
      sb.append("\n");
    }

    if (tfunction.isOneway()) {
      sb.append(indent()).append("return null;\n");
    } else {
      sb.append(indent()).append("return result;\n");
    }
    indent_down();
    sb.append(indent()).append("}\n");
    indent_down();
    sb.append(indent()).append("}\n\n");
  }

  protected String functionSignature(TFunction tfunction) { return functionSignature(tfunction, ""); }
  /**
   * Generates a synchronous function signature.
   *
   * @param tfunction The function
   * @param prefix A prefix for the function name, e.g., "override "
   * @return A string of the form "ReturnType methodName(args) throws X, TException"
   */
  protected String functionSignature(TFunction tfunction, String prefix) {
    TType ttype = tfunction.getReturnType();
    String fnName = getRpcMethodName(tfunction.getName());
    StringBuilder result = new StringBuilder();
    result.append(typeName(ttype))
        .append(" ")
        .append(prefix)
        .append(fnName)
        .append("(")
        .append(argumentList(tfunction.getArglist(), true))
        .append(") throws ");

    TStruct xs = tfunction.getXceptions();
    for (TField xception : xs.getMembers()) {
      result.append(typeName(xception.getType())).append(", ");
    }
    result.append("org.apache.thrift.TException");
    return result.toString();
  }

  /**
   * Renders a function signature of the form 'void name(args, resultHandler)'
   *
   * @param tfunction Function definition
   * @param useBaseMethod Passed to async arglist generator
   * @param prefix A prefix for the function name
   * @return String of rendered function definition
   */
  protected String functionSignatureAsync(TFunction tfunction, boolean useBaseMethod, String prefix) {
    String arglist = asyncFunctionCallArglist(tfunction, useBaseMethod, true);
    String fnName = getRpcMethodName(tfunction.getName());
    return prefix + "void " + fnName + "(" + arglist + ")";
  }

  protected String functionSignatureFuture(TFunction tfunction) { return functionSignatureFuture(tfunction, ""); }

  /**
   * Renders a function signature of the form 'CompletableFuture<R> name(args)'
   *
   * @param tfunction Function definition
   * @param prefix A prefix for the function name
   * @return String of rendered function definition
   */
  protected String functionSignatureFuture(TFunction tfunction, String prefix) {
    TType ttype = tfunction.getReturnType();
    String fnName = getRpcMethodName(tfunction.getName());
    String argList = argumentList(tfunction.getArglist(), true);

    String returnType = "java.util.concurrent.CompletableFuture<" + typeName(ttype, true) + ">";

    return returnType + " " + prefix + fnName + "(" + argList + ")";
  }

  /**
   * Generates the argument list for an asynchronous Thrift function call.
   * This includes the function parameters and the final AsyncMethodCallback parameter.
   *
   * @param tfunc The function
   * @param useBaseMethod (unused per C++ implementation)
   * @param includeTypes Whether to include types in the signature
   * @return The argument list string
   */
  protected String asyncFunctionCallArglist(TFunction tfunc, boolean useBaseMethod, boolean includeTypes) {
    StringBuilder arglist = new StringBuilder();
    if (!tfunc.getArglist().getMembers().isEmpty()) {
      arglist.append(argumentList(tfunc.getArglist(), includeTypes));
      arglist.append(", ");
    }

    if (includeTypes) {
      arglist.append("org.apache.thrift.async.AsyncMethodCallback<");
      arglist.append(typeName(tfunc.getReturnType(), true));
      arglist.append("> ");
    }
    arglist.append("resultHandler");

    return arglist.toString();
  }

  /**
   * Renders a comma separated field list, with type names.
   *
   * @param tstruct The struct containing the fields
   * @param includeTypes Whether to include the type names
   * @return A string of the form "type1 name1, type2 name2"
   */
  protected String argumentList(TStruct tstruct, boolean includeTypes) {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for (TField field : tstruct.getMembers()) {
      if (first) {
        first = false;
      } else {
        result.append(", ");
      }
      if (includeTypes) {
        result.append(typeName(field.getType())).append(" ");
      }
      result.append(makeValidJavaIdentifier(field.getName()));
    }
    return result.toString();
  }

  /**
   * Generates an argument list for an async method, taking a struct for the arguments
   * and a function for the callback's return type.
   *
   * @param tfunct The function (for return type)
   * @param tstruct The struct containing the arguments
   * @param ttype (unused per C++ implementation)
   * @param includeTypes Whether to include types
   * @return The argument list string
   */
  protected String asyncArgumentList(TFunction tfunct, TStruct tstruct, TType ttype, boolean includeTypes) {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for (TField field : tstruct.getMembers()) {
      if (first) {
        first = false;
      } else {
        result.append(", ");
      }
      if (includeTypes) {
        result.append(typeName(field.getType())).append(" ");
      }
      result.append(makeValidJavaIdentifier(field.getName()));
    }

    if (!first) {
      result.append(", ");
    }

    if (includeTypes) {
      result.append("org.apache.thrift.async.AsyncMethodCallback<");
      result.append(typeName(tfunct.getReturnType(), true));
      result.append("> ");
    }
    result.append("resultHandler");
    return result.toString();
  }
}
