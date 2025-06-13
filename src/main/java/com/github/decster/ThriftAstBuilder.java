package com.github.decster;

import com.github.decster.ast.*;
import com.github.decster.parser.ThriftBaseVisitor;
import com.github.decster.parser.ThriftLexer;
import com.github.decster.parser.ThriftParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ThriftAstBuilder is responsible for parsing Thrift files and building
 * a TProgram AST representation.
 */
public class ThriftAstBuilder {
    /**
     * Parse a Thrift document from a file path and build a TProgram object.
     *
     * @param filePath Path to the Thrift file
     * @return The parsed TProgram object
     * @throws IOException If an I/O error occurs
     */
    public static TProgram parseFile(String filePath) throws IOException {
        CharStream input = CharStreams.fromFileName(filePath);
        return parse(input, filePath);
    }

    /**
     * Parse a Thrift document from a string and build a TProgram object.
     *
     * @param content The Thrift document content as a string
     * @param name The name to use for the program
     * @return The parsed TProgram object
     */
    public static TProgram parseString(String content, String name) {
        CharStream input = CharStreams.fromString(content);
        return parse(input, name);
    }

    /**
     * Parse a Thrift document from a CharStream and build a TProgram object.
     *
     * @param input The input CharStream
     * @param programName The name to use for the program
     * @return The parsed TProgram object
     */
    private static TProgram parse(CharStream input, String programName) {
        ThriftLexer lexer = new ThriftLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ThriftParser parser = new ThriftParser(tokens);
        ThriftParser.DocumentContext documentContext = parser.document();

        Path path = Path.of(programName);
        String name = path.getFileName().toString();
        if (name.endsWith(".thrift")) {
            name = name.substring(0, name.length() - 7);
        }

        TProgram program = new TProgram(programName, name);
        AstVisitor visitor = new AstVisitor(program);
        visitor.visit(documentContext);
        return program;
    }

    /**
     * Visitor implementation for building the AST from the ANTLR parse tree.
     */
    static class AstVisitor extends ThriftBaseVisitor<Object> {
        private final TProgram program;
        private final TScope scope;

        public AstVisitor(TProgram program) {
            this.program = program;
            this.scope = program.getScope();
        }

        @Override
        public Object visitDocument(ThriftParser.DocumentContext ctx) {
            // Process headers first
            for (ThriftParser.HeaderContext header : ctx.header()) {
                visit(header);
            }

            // Then process definitions
            for (ThriftParser.DefinitionContext definition : ctx.definition()) {
                visit(definition);
            }
            return null;
        }

        @Override
        public Object visitInclude_(ThriftParser.Include_Context ctx) {
            String includePath = getStringLiteral(ctx.LITERAL().getText());
            // In a real implementation, this would recursively parse the included file
            // and add it to the program's includes list
            // For simplicity, we just record the include path for now
            // program.addInclude(new TProgram(includePath));
            return null;
        }

        @Override
        public Object visitNamespace_(ThriftParser.Namespace_Context ctx) {
            String language;
            String namespace;

            if (ctx.getText().startsWith("namespace*")) {
                language = "*";
                if (ctx.IDENTIFIER() != null && !ctx.IDENTIFIER().isEmpty()) {
                    namespace = ctx.IDENTIFIER().get(0).getText();
                } else {
                    namespace = getStringLiteral(ctx.LITERAL().getText());
                }
            } else if (ctx.getText().startsWith("cpp_namespace")) {
                language = "cpp";
                namespace = ctx.IDENTIFIER().get(0).getText();
            } else if (ctx.getText().startsWith("php_namespace")) {
                language = "php";
                namespace = ctx.IDENTIFIER().get(0).getText();
            } else {
                language = ctx.IDENTIFIER().get(0).getText();
                if (ctx.IDENTIFIER().size() > 1) {
                    namespace = ctx.IDENTIFIER().get(1).getText();
                } else {
                    namespace = getStringLiteral(ctx.LITERAL().getText());
                }
            }

            program.setNamespace(language, namespace);
            return null;
        }

        @Override
        public Object visitCpp_include(ThriftParser.Cpp_includeContext ctx) {
            // CPP includes are not processed in Java generator
            return null;
        }

        @Override
        public Object visitConst_rule(ThriftParser.Const_ruleContext ctx) {
            String name = ctx.IDENTIFIER().getText();
            TType type = (TType) visit(ctx.field_type());
            TConstValue value = null;

            if (ctx.const_value() != null) {
                value = (TConstValue) visit(ctx.const_value());
            }

            TConst constant = new TConst(type, name, value);
            program.addConst(constant);
            return constant;
        }

        @Override
        public Object visitTypedef_(ThriftParser.Typedef_Context ctx) {
            String name = ctx.IDENTIFIER().getText();
            TType type = (TType) visit(ctx.field_type());

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());

            TTypedef typedef = new TTypedef(program, type, name);
            if (!annotations.isEmpty()) {
                typedef.setAnnotations(annotations);
            }

            program.addTypedef(typedef);
            return typedef;
        }

        @Override
        public Object visitEnum_rule(ThriftParser.Enum_ruleContext ctx) {
            String name = ctx.IDENTIFIER().getText();
            TEnum enumType = new TEnum(program);
            enumType.setName(name);

            int nextValue = 0;
            for (ThriftParser.Enum_fieldContext fieldCtx : ctx.enum_field()) {
                String fieldName = fieldCtx.IDENTIFIER().getText();
                int value = nextValue++;

                if (fieldCtx.integer() != null) {
                    value = Integer.parseInt(fieldCtx.integer().getText());
                    nextValue = value + 1;
                }

                TEnumValue enumValue = new TEnumValue(fieldName, value);

                if (fieldCtx.type_annotations() != null) {
                    Map<String, List<String>> fieldAnnotations = processTypeAnnotations(fieldCtx.type_annotations());
                    if (!fieldAnnotations.isEmpty()) {
                        enumValue.setAnnotations(fieldAnnotations);
                    }
                }

                enumType.append(enumValue);
            }

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());
            if (!annotations.isEmpty()) {
                enumType.setAnnotations(annotations);
            }

            program.addEnum(enumType);
            return enumType;
        }

        @Override
        public Object visitStruct_(ThriftParser.Struct_Context ctx) {
            String name = ctx.IDENTIFIER().getText();
            TStruct struct = new TStruct(program, name);

            for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
                TField field = (TField) visit(fieldCtx);
                struct.append(field);
            }

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());
            if (!annotations.isEmpty()) {
                struct.setAnnotations(annotations);
            }

            program.addStruct(struct);
            return struct;
        }

        @Override
        public Object visitUnion_(ThriftParser.Union_Context ctx) {
            String name = ctx.IDENTIFIER().getText();
            TStruct struct = new TStruct(program, name);
            struct.setUnion(true);

            for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
                TField field = (TField) visit(fieldCtx);
                struct.append(field);
            }

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());
            if (!annotations.isEmpty()) {
                struct.setAnnotations(annotations);
            }

            program.addStruct(struct);
            return struct;
        }

        @Override
        public Object visitException(ThriftParser.ExceptionContext ctx) {
            String name = ctx.IDENTIFIER().getText();
            TStruct struct = new TStruct(program, name);
            struct.setXception(true);

            for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
                TField field = (TField) visit(fieldCtx);
                struct.append(field);
            }

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());
            if (!annotations.isEmpty()) {
                struct.setAnnotations(annotations);
            }

            program.addXception(struct);
            return struct;
        }

        @Override
        public Object visitService(ThriftParser.ServiceContext ctx) {
            String name = ctx.IDENTIFIER().get(0).getText();
            TService service = new TService(program);
            service.setName(name);

            if (ctx.IDENTIFIER().size() > 1) {
                // Create a reference to the parent service by name
                // In a real implementation, we would resolve this to the actual TService object
                TService parentService = new TService(program);
                parentService.setName(ctx.IDENTIFIER().get(1).getText());
                service.setExtends(parentService);
            }

            for (ThriftParser.Function_Context funcCtx : ctx.function_()) {
                TFunction function = (TFunction) visit(funcCtx);
                service.addFunction(function);
            }

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());
            if (!annotations.isEmpty()) {
                service.setAnnotations(annotations);
            }

            program.addService(service);
            return service;
        }

        @Override
        public Object visitField(ThriftParser.FieldContext ctx) {
            int id = 0;
            if (ctx.field_id() != null) {
                id = Integer.parseInt(ctx.field_id().integer().getText());
            }

            TField.Requirement req = TField.Requirement.OPT_IN_REQ_OUT;
            if (ctx.field_req() != null) {
                if (ctx.field_req().getText().equals("required")) {
                    req = TField.Requirement.REQUIRED;
                } else if (ctx.field_req().getText().equals("optional")) {
                    req = TField.Requirement.OPTIONAL;
                }
            }

            TType type = (TType) visit(ctx.field_type());
            String name = ctx.IDENTIFIER().getText();

            TField field = new TField(type, name, id);
            field.setReq(req);

            if (ctx.const_value() != null) {
                TConstValue value = (TConstValue) visit(ctx.const_value());
                field.setValue(value);
            }

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());
            if (!annotations.isEmpty()) {
                field.setAnnotations(annotations);
            }

            return field;
        }

        @Override
        public Object visitFunction_(ThriftParser.Function_Context ctx) {
            String name = ctx.IDENTIFIER().getText();
            TType returnType;

            if (ctx.function_type().getText().equals("void")) {
                // Create void return type with uppercase name "VOID"
                returnType = new TBaseType("VOID", TBaseType.Base.TYPE_VOID);
            } else {
                returnType = (TType) visit(ctx.function_type().field_type());
            }

            // Create an empty argument struct
            TStruct argStruct = new TStruct(program, name + "_args");

            // Create function with proper constructor
            TFunction function = new TFunction(returnType, name, argStruct);

            if (ctx.oneway() != null) {
                function.setOneWay(true);
            }

            // Process function parameters
            for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
                TField field = (TField) visit(fieldCtx);
                argStruct.append(field);
            }

            // Process throws list
            if (ctx.throws_list() != null) {
                // Create a struct for exceptions
                TStruct xceptStruct = new TStruct(program, name + "_result");
                function.setXceptions(xceptStruct);

                for (ThriftParser.FieldContext fieldCtx : ctx.throws_list().field()) {
                    TField field = (TField) visit(fieldCtx);
                    xceptStruct.append(field);
                }
            }

            Map<String, List<String>> annotations = processTypeAnnotations(ctx.type_annotations());
            if (!annotations.isEmpty()) {
                function.setAnnotations(annotations);
            }

            return function;
        }

        @Override
        public Object visitField_type(ThriftParser.Field_typeContext ctx) {
            if (ctx.base_type() != null) {
                return visit(ctx.base_type());
            } else if (ctx.container_type() != null) {
                return visit(ctx.container_type());
            } else if (ctx.IDENTIFIER() != null) {
                // This is a reference to a custom type (enum, struct, etc.)
                String typeName = ctx.IDENTIFIER().getText();
                // In a real implementation, you would resolve this type from the scope
                // For simplicity, we'll create a reference type
                return new TTypeRef(program, typeName);
            }
            return null;
        }

        @Override
        public Object visitBase_type(ThriftParser.Base_typeContext ctx) {
            String typeName = ctx.real_base_type().getText();
            TBaseType.Base baseType;
            String typeNameUpperCase = typeName.toUpperCase();

            switch (typeName) {
                case "bool":
                    baseType = TBaseType.Base.TYPE_BOOL;
                    break;
                case "byte":
                    baseType = TBaseType.Base.TYPE_I8;
                    break;
                case "i8":
                    baseType = TBaseType.Base.TYPE_I8;
                    break;
                case "i16":
                    baseType = TBaseType.Base.TYPE_I16;
                    break;
                case "i32":
                    baseType = TBaseType.Base.TYPE_I32;
                    break;
                case "i64":
                    baseType = TBaseType.Base.TYPE_I64;
                    break;
                case "double":
                    baseType = TBaseType.Base.TYPE_DOUBLE;
                    break;
                case "string":
                    baseType = TBaseType.Base.TYPE_STRING;
                    break;
                case "binary":
                    TBaseType binaryType = new TBaseType("BINARY", TBaseType.Base.TYPE_STRING);
                    binaryType.setBinary(true);
                    return binaryType;
                case "uuid":
                    baseType = TBaseType.Base.TYPE_UUID;
                    break;
                default:
                    throw new RuntimeException("Unknown base type: " + typeName);
            }

            return new TBaseType(typeNameUpperCase, baseType);
        }

        @Override
        public Object visitContainer_type(ThriftParser.Container_typeContext ctx) {
            if (ctx.map_type() != null) {
                return visit(ctx.map_type());
            } else if (ctx.list_type() != null) {
                return visit(ctx.list_type());
            } else if (ctx.set_type() != null) {
                return visit(ctx.set_type());
            }
            return null;
        }

        @Override
        public Object visitMap_type(ThriftParser.Map_typeContext ctx) {
            TType keyType = (TType) visit(ctx.field_type(0));
            TType valueType = (TType) visit(ctx.field_type(1));
            return new TMap(keyType, valueType);
        }

        @Override
        public Object visitList_type(ThriftParser.List_typeContext ctx) {
            TType elemType = (TType) visit(ctx.field_type());
            return new TList(elemType);
        }

        @Override
        public Object visitSet_type(ThriftParser.Set_typeContext ctx) {
            TType elemType = (TType) visit(ctx.field_type());
            return new TSet(elemType);
        }

        @Override
        public Object visitConst_value(ThriftParser.Const_valueContext ctx) {
            if (ctx.IDENTIFIER() != null) {
                // This is a reference to another constant or enum value
                String identifier = ctx.IDENTIFIER().getText();
                TConstValue constValue = new TConstValue();
                constValue.setIdentifier(identifier);
                return constValue;
            } else if (ctx.LITERAL() != null) {
                // This is a string literal
                String stringValue = getStringLiteral(ctx.LITERAL().getText());
                return new TConstValue(stringValue);
            } else if (ctx.integer() != null) {
                // This is an integer value
                long intValue = Long.parseLong(ctx.integer().getText());
                return new TConstValue(intValue);
            } else if (ctx.DOUBLE() != null) {
                // This is a double value
                double doubleValue = Double.parseDouble(ctx.DOUBLE().getText());
                return new TConstValue(doubleValue);
            } else if (ctx.const_list() != null) {
                // This is a list value
                return visit(ctx.const_list());
            } else if (ctx.const_map() != null) {
                // This is a map value
                return visit(ctx.const_map());
            }
            return null;
        }

        @Override
        public Object visitConst_list(ThriftParser.Const_listContext ctx) {
            List<TConstValue> values = new ArrayList<>();
            for (ThriftParser.Const_valueContext valueCtx : ctx.const_value()) {
                values.add((TConstValue) visit(valueCtx));
            }
            TConstValue listValue = new TConstValue();
            listValue.setList(values);
            return listValue;
        }

        @Override
        public Object visitConst_map(ThriftParser.Const_mapContext ctx) {
            Map<TConstValue, TConstValue> map = new TreeMap<>();
            for (ThriftParser.Const_map_entryContext entryCtx : ctx.const_map_entry()) {
                TConstValue key = (TConstValue) visit(entryCtx.const_value(0));
                TConstValue value = (TConstValue) visit(entryCtx.const_value(1));
                map.put(key, value);
            }
            TConstValue mapValue = new TConstValue();
            mapValue.setMap(map);
            return mapValue;
        }

        @Override
        public Map<String, List<String>> visitType_annotations(ThriftParser.Type_annotationsContext ctx) {
            if (ctx == null) {
                return new HashMap<>();
            }

            Map<String, List<String>> annotations = new HashMap<>();
            for (ThriftParser.Type_annotationContext annotationCtx : ctx.type_annotation()) {
                String key = annotationCtx.IDENTIFIER().getText();
                String value = "";

                if (annotationCtx.annotation_value() != null) {
                    value = annotationCtx.annotation_value().LITERAL() != null ?
                            getStringLiteral(annotationCtx.annotation_value().LITERAL().getText()) :
                            annotationCtx.annotation_value().getText();
                }

                annotations.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
            return annotations;
        }

        /**
         * Process type annotations and return them as a map
         *
         * @param ctx The type annotations context
         * @return A map of annotation names to their values
         */
        private Map<String, List<String>> processTypeAnnotations(ThriftParser.Type_annotationsContext ctx) {
            if (ctx != null) {
                return visitType_annotations(ctx);
            }
            return new HashMap<>();
        }

        /**
         * Convert a string literal (with quotes) to its actual string value
         */
        private String getStringLiteral(String literal) {
            // Remove the quotes and handle escape sequences
            return literal.substring(1, literal.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\")
                    .replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\t", "\t");
        }
    }

    // Helper class for resolving type references
    static class TTypeRef extends TType {
        public TTypeRef(TProgram program, String name) {
            setProgram(program);
            setName(name);
        }
    }
}
