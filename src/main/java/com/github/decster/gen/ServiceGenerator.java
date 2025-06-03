package com.github.decster.gen;

import com.github.decster.ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.EnumMap;
import java.util.BitSet;

import org.apache.thrift.protocol.TType;
import org.apache.thrift.TFieldRequirementType;


public class ServiceGenerator {
    private final ServiceNode serviceNode;
    private final DocumentNode documentNode;
    private final String packageName;
    private final String date;
    private final Set<String> imports = new HashSet<>();
    private final StringBuilder sb = new StringBuilder();

    private static class JavaTypeResolution {
        String javaType;
        byte thriftType;
        String fieldMetaDataType;
        Set<String> imports = new HashSet<>();

        JavaTypeResolution(String javaType, byte thriftType, String fieldMetaDataType) {
            this.javaType = javaType;
            this.thriftType = thriftType;
            this.fieldMetaDataType = fieldMetaDataType;
        }
        void addImport(String imp) { if (imp != null && !imp.isEmpty() && !imp.startsWith("java.lang.")) imports.add(imp); }
    }

    public ServiceGenerator(ServiceNode serviceNode, DocumentNode documentNode, String packageName, String date) {
        this.serviceNode = serviceNode;
        this.documentNode = documentNode;
        this.packageName = packageName;
        this.date = date;
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
         if (importName == null || importName.isEmpty() || isPrimitive(importName) || importName.startsWith("java.lang.")) {
            return;
        }
        imports.add(importName);
    }

    private String getPackageFromQualifiedName(String qName) {
        if (qName == null || !qName.contains(".")) return null;
        return qName.substring(0, qName.lastIndexOf('.'));
    }

    private String getWrapperTypeIfPrimitive(String javaType) {
        if (javaType == null) return "java.lang.Void";
        switch (javaType) {
            case "boolean": return "java.lang.Boolean";
            case "byte":    return "java.lang.Byte";
            case "short":   return "java.lang.Short";
            case "int":     return "java.lang.Integer";
            case "long":    return "java.lang.Long";
            case "double":  return "java.lang.Double";
            case "void":    return "java.lang.Void";
            default:        return javaType;
        }
    }

    private JavaTypeResolution resolveType(TypeNode typeNode) {
        if (typeNode == null) {
            return new JavaTypeResolution("void", TType.VOID, null);
        }
        if (typeNode instanceof BaseTypeNode) {
            BaseTypeNode baseTypeNode = (BaseTypeNode) typeNode;
            switch (baseTypeNode.getType()) {
                case BOOL:   return new JavaTypeResolution("boolean", TType.BOOL, "new FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)");
                case BYTE:   return new JavaTypeResolution("byte", TType.BYTE, "new FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)");
                case I16:    return new JavaTypeResolution("short", TType.I16, "new FieldValueMetaData(org.apache.thrift.protocol.TType.I16)");
                case I32:    return new JavaTypeResolution("int", TType.I32, "new FieldValueMetaData(org.apache.thrift.protocol.TType.I32)");
                case I64:    return new JavaTypeResolution("long", TType.I64, "new FieldValueMetaData(org.apache.thrift.protocol.TType.I64)");
                case DOUBLE: return new JavaTypeResolution("double", TType.DOUBLE, "new FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)");
                case STRING: return new JavaTypeResolution("java.lang.String", TType.STRING, "new FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)");
                case BINARY:
                    JavaTypeResolution binRes = new JavaTypeResolution("java.nio.ByteBuffer", TType.STRING, "new FieldValueMetaData(org.apache.thrift.protocol.TType.STRING, true)");
                    binRes.addImport("java.nio.ByteBuffer"); return binRes;
                case VOID: // Added case for VOID
                    return new JavaTypeResolution("void", TType.VOID, null);
                default: throw new IllegalArgumentException("Unsupported base type from BaseTypeNode: " + baseTypeNode.getType());
            }
        } else if (typeNode instanceof IdentifierTypeNode) {
            IdentifierTypeNode idType = (IdentifierTypeNode) typeNode;
            String typeName = idType.getName();
            DefinitionNode resolvedDef = null;
            String resolvedTypeName = typeName;

            if (this.documentNode != null && this.documentNode.getDefinitions() != null) {
                for (DefinitionNode def : this.documentNode.getDefinitions()) {
                    if (def.getName().equals(typeName)) { resolvedDef = def; resolvedTypeName = def.getName(); break; }
                    if (!typeName.contains(".") && def.getName().equals(this.packageName + "." + typeName)) {
                         resolvedDef = def; resolvedTypeName = typeName; break;
                    }
                }
            }

            JavaTypeResolution idRes;
            String metaDataTypeName = resolvedTypeName;
            String javaTypeName = resolvedTypeName;
            String typePackage = getPackageFromQualifiedName(resolvedTypeName);

            if (typePackage != null && typePackage.equals(this.packageName)) {
                javaTypeName = resolvedTypeName.substring(resolvedTypeName.lastIndexOf('.') + 1);
            } else if (typePackage != null) {
                 addImport(resolvedTypeName);
            }

            if (resolvedDef instanceof EnumNode) {
                idRes = new JavaTypeResolution(javaTypeName, TType.ENUM, "new EnumMetaData(org.apache.thrift.protocol.TType.ENUM, " + metaDataTypeName + ".class)");
            } else {
                idRes = new JavaTypeResolution(javaTypeName, TType.STRUCT, "new StructMetaData(org.apache.thrift.protocol.TType.STRUCT, " + metaDataTypeName + ".class)");
            }
            return idRes;
        } else if (typeNode instanceof ListTypeNode) {
            ListTypeNode listType = (ListTypeNode) typeNode;
            JavaTypeResolution elementTypeRes = resolveType(listType.getElementType());
            elementTypeRes.imports.forEach(this::addImport);
            String elementJavaType = getWrapperTypeIfPrimitive(elementTypeRes.javaType);
            addImport(getPackageFromQualifiedName(elementJavaType));
            JavaTypeResolution listRes = new JavaTypeResolution("java.util.List<" + elementJavaType + ">", TType.LIST, "new ListMetaData(org.apache.thrift.protocol.TType.LIST, " + elementTypeRes.fieldMetaDataType + ")");
            listRes.addImport("java.util.List"); return listRes;
        } else if (typeNode instanceof SetTypeNode) {
            SetTypeNode setType = (SetTypeNode) typeNode;
            JavaTypeResolution elementTypeRes = resolveType(setType.getElementType());
            elementTypeRes.imports.forEach(this::addImport);
            String elementJavaType = getWrapperTypeIfPrimitive(elementTypeRes.javaType);
            addImport(getPackageFromQualifiedName(elementJavaType));
            JavaTypeResolution setRes =  new JavaTypeResolution("java.util.Set<" + elementJavaType + ">", TType.SET, "new SetMetaData(org.apache.thrift.protocol.TType.SET, " + elementTypeRes.fieldMetaDataType + ")");
            setRes.addImport("java.util.Set"); return setRes;
        } else if (typeNode instanceof MapTypeNode) {
            MapTypeNode mapType = (MapTypeNode) typeNode;
            JavaTypeResolution keyTypeRes = resolveType(mapType.getKeyType());
            JavaTypeResolution valueTypeRes = resolveType(mapType.getValueType());
            keyTypeRes.imports.forEach(this::addImport); valueTypeRes.imports.forEach(this::addImport);
            String keyJavaType = getWrapperTypeIfPrimitive(keyTypeRes.javaType);
            String valueJavaType = getWrapperTypeIfPrimitive(valueTypeRes.javaType);
            addImport(getPackageFromQualifiedName(keyJavaType)); addImport(getPackageFromQualifiedName(valueJavaType));
            JavaTypeResolution mapRes = new JavaTypeResolution("java.util.Map<" + keyJavaType + ", " + valueJavaType + ">", TType.MAP, "new MapMetaData(org.apache.thrift.protocol.TType.MAP, " + keyTypeRes.fieldMetaDataType + ", " + valueTypeRes.fieldMetaDataType + ")");
            mapRes.addImport("java.util.Map"); return mapRes;
        }
        throw new IllegalArgumentException("Unsupported TypeNode: " + typeNode.getClass().getName() + (typeNode.getName() != null ? " for type name " + typeNode.getName() : ""));
    }

    public String generate() {
        String serviceName = serviceNode.getName();
        this.imports.clear();
        this.sb.setLength(0);
        addDefaultImports();

        sb.append("package ").append(packageName).append(";\n\n");
        String importPlaceholder = "// IMPORTS_PLACEHOLDER\n";
        sb.append(importPlaceholder);

        sb.append("@javax.annotation.Generated(value = \"Autogenerated by Thrift Compiler (0.20.0)\", date = \"").append(date).append("\")\n");
        sb.append("public class ").append(serviceName).append(" {\n\n");

        generateIface(serviceName);
        generateAsyncIface(serviceName);
        generateClient(serviceName);
        generateAsyncClient(serviceName);
        generateProcessor(serviceName);

        for (FunctionNode fn : serviceNode.getFunctions()) {
            generateArgsStruct(fn);
            generateResultStruct(fn);
        }

        sb.append("}\n");

        StringBuilder importSb = new StringBuilder();
        List<String> sortedImports = new ArrayList<>(this.imports);
        Collections.sort(sortedImports);
        for (String imp : sortedImports) { importSb.append("import ").append(imp).append(";\n"); }
        if (!sortedImports.isEmpty()) importSb.append("\n");
        return sb.toString().replace(importPlaceholder, importSb.toString());
    }

    private void addDefaultImports(){
        addImport("org.apache.thrift.scheme.IScheme"); addImport("org.apache.thrift.scheme.SchemeFactory");
        addImport("org.apache.thrift.scheme.StandardScheme"); addImport("org.apache.thrift.scheme.TupleScheme");
        addImport("org.apache.thrift.protocol.TTupleProtocol"); addImport("org.apache.thrift.protocol.TProtocolUtil");
        addImport("org.apache.thrift.TException"); addImport("org.apache.thrift.async.AsyncMethodCallback");
        addImport("org.apache.thrift.server.AbstractNonblockingServer.*;");
        addImport("org.apache.thrift.transport.TNonblockingTransport"); addImport("org.apache.thrift.protocol.TProtocolFactory");
        addImport("org.apache.thrift.async.TAsyncClientManager"); addImport("org.apache.thrift.async.TAsyncClient");
        addImport("org.apache.thrift.async.TAsyncMethodCall"); addImport("org.apache.thrift.TBase");
        addImport("org.apache.thrift.TProcessor"); addImport("org.apache.thrift.TApplicationException");
        addImport("org.apache.thrift.TServiceClient"); addImport("org.apache.thrift.protocol.TMessage");
        addImport("org.apache.thrift.protocol.TMessageType"); addImport("org.apache.thrift.protocol.TField");
        addImport("org.apache.thrift.protocol.TStruct"); addImport("org.apache.thrift.meta_data.FieldMetaData");
        addImport("org.apache.thrift.meta_data.FieldValueMetaData"); addImport("org.apache.thrift.meta_data.StructMetaData");
        addImport("org.apache.thrift.meta_data.ListMetaData"); addImport("org.apache.thrift.meta_data.SetMetaData");
        addImport("org.apache.thrift.meta_data.MapMetaData"); addImport("org.apache.thrift.meta_data.EnumMetaData");
        addImport("org.apache.thrift.EncodingUtils"); addImport("org.apache.thrift.TBaseHelper");
        addImport("org.apache.thrift.TFieldRequirementType");
        addImport("java.util.List"); addImport("java.util.ArrayList"); addImport("java.util.Map");
        addImport("java.util.HashMap"); addImport("java.util.EnumMap"); addImport("java.util.Set");
        addImport("java.util.HashSet"); addImport("java.util.EnumSet"); addImport("java.util.Collections");
        addImport("java.util.BitSet"); addImport("java.nio.ByteBuffer");
        addImport("org.slf4j.Logger"); addImport("org.slf4j.LoggerFactory");
    }

    private String getThrowsClause(FunctionNode fn, boolean isAsyncInterfaceMethod) {
        StringBuilder throwsSb = new StringBuilder();
        boolean hasDeclaredExceptions = fn.getExceptions() != null && !fn.getExceptions().isEmpty();

        if (hasDeclaredExceptions) {
            throwsSb.append(" throws ");
            for (int i = 0; i < fn.getExceptions().size(); i++) {
                FieldNode exField = fn.getExceptions().get(i);
                JavaTypeResolution exRes = resolveType(exField.getType());
                throwsSb.append(exRes.javaType);
                if (i < fn.getExceptions().size() - 1) throwsSb.append(", ");
            }
        }

        // For synchronous Iface, always add TException as send can fail.
        // For AsyncIface, only add TException if not a oneway call (as there's no reply).
        if (!isAsyncInterfaceMethod) {
            if (throwsSb.length() > 0 && hasDeclaredExceptions) throwsSb.append(", ");
            else if (throwsSb.length() == 0) throwsSb.append(" throws ");
            throwsSb.append("org.apache.thrift.TException");
        } else { // AsyncIface
            if (fn.getOneway() != FunctionNode.Oneway.ONEWAY) {
                if (throwsSb.length() > 0 && hasDeclaredExceptions) throwsSb.append(", ");
                else if (throwsSb.length() == 0) throwsSb.append(" throws ");
                throwsSb.append("org.apache.thrift.TException");
            }
        }
        return throwsSb.toString();
    }

    private void generateIface(String serviceName) {
        sb.append("  public interface Iface");
        String extendsServiceName = serviceNode.getExtendsService();
        if (extendsServiceName != null && !extendsServiceName.isEmpty()) {
            JavaTypeResolution parentService = resolveType(new SynthesizedIdentifierTypeNode(extendsServiceName));
            sb.append(" extends ").append(parentService.javaType).append(".Iface");
        }
        sb.append(" {\n\n");
        for (FunctionNode fn : serviceNode.getFunctions()) {
            JavaTypeResolution returnType = resolveType(fn.getReturnType());
            String docString = fn.getDocString();
            if (docString != null && !docString.isEmpty()) {
                sb.append("    /**\n");
                for (String line : docString.split("\n")) {
                    sb.append("     * ").append(line.replace("*/", "* /")).append("\n"); // Avoid premature comment end
                }
                sb.append("     */\n");
            }
            sb.append("    public ").append(returnType.javaType).append(" ").append(fn.getName()).append("(");
            appendFunctionParameters(fn.getParameters(), false);
            sb.append(")").append(getThrowsClause(fn, false)).append(";\n\n");
        }
        sb.append("  }\n\n");
    }

    private void generateAsyncIface(String serviceName) {
        sb.append("  public interface AsyncIface");
        String extendsServiceName = serviceNode.getExtendsService();
         if (extendsServiceName != null && !extendsServiceName.isEmpty()) {
            JavaTypeResolution parentService = resolveType(new SynthesizedIdentifierTypeNode(extendsServiceName));
            sb.append(" extends ").append(parentService.javaType).append(".AsyncIface");
        }
        sb.append(" {\n\n");
        for (FunctionNode fn : serviceNode.getFunctions()) {
            JavaTypeResolution returnType = resolveType(fn.getReturnType());
            String docString = fn.getDocString();
            if (docString != null && !docString.isEmpty()) {
                sb.append("    /**\n");
                for (String line : docString.split("\n")) {
                    sb.append("     * ").append(line.replace("*/", "* /")).append("\n"); // Avoid premature comment end
                }
                sb.append("     */\n");
            }
            sb.append("    public void ").append(fn.getName()).append("(");
            appendFunctionParameters(fn.getParameters(), true);
            sb.append("org.apache.thrift.async.AsyncMethodCallback<").append(getWrapperTypeIfPrimitive(returnType.javaType)).append("> resultHandler");
            sb.append(") throws org.apache.thrift.TException;\n\n");
        }
        sb.append("  }\n\n");
    }

    private void appendFunctionParameters(List<FieldNode> params, boolean trailingCommaForCallback) {
        for (int i = 0; i < params.size(); i++) {
            FieldNode arg = params.get(i);
            JavaTypeResolution argType = resolveType(arg.getType());
            sb.append(argType.javaType).append(" ").append(arg.getName());
            if (i < params.size() - 1 || trailingCommaForCallback) {
                sb.append(", ");
            }
        }
    }

    private void appendFunctionCallArguments(List<FieldNode> params) {
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            if (i < params.size() - 1) sb.append(", ");
        }
    }

    private void generateClient(String serviceName) {
        sb.append("  public static class Client extends ");
        String extendsServiceName = serviceNode.getExtendsService();
        if (extendsServiceName != null && !extendsServiceName.isEmpty()) {
            JavaTypeResolution parentService = resolveType(new SynthesizedIdentifierTypeNode(extendsServiceName));
            sb.append(parentService.javaType).append(".Client");
        } else {
            sb.append("org.apache.thrift.TServiceClient");
        }
        sb.append(" implements Iface {\n");
        sb.append("    public Client(org.apache.thrift.protocol.TProtocol prot) { super(prot, prot); }\n");
        sb.append("    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) { super(iprot, oprot); }\n\n");

        for (FunctionNode fn : serviceNode.getFunctions()) {
            JavaTypeResolution returnType = resolveType(fn.getReturnType());
            sb.append("    public ").append(returnType.javaType).append(" ").append(fn.getName()).append("(");
            appendFunctionParameters(fn.getParameters(), false);
            sb.append(")").append(getThrowsClause(fn, false)).append(" {\n");
            sb.append("      send_").append(fn.getName()).append("(");
            appendFunctionCallArguments(fn.getParameters());
            sb.append(");\n");
            if (fn.getOneway() != FunctionNode.Oneway.ONEWAY) {
                sb.append(returnType.javaType.equals("void") ? "      recv_" : "      return recv_")
                  .append(fn.getName()).append("();\n");
            }
            sb.append("    }\n\n");

            sb.append("    public void send_").append(fn.getName()).append("(");
            appendFunctionParameters(fn.getParameters(), false);
            sb.append(") throws org.apache.thrift.TException {\n");
            sb.append("      ").append(fn.getName()).append("_args args = new ").append(fn.getName()).append("_args();\n");
            for (FieldNode arg : fn.getParameters()) {
                sb.append("      args.set").append(capitalize(arg.getName())).append("(").append(arg.getName()).append(");\n");
            }
            sb.append("      sendBase(\"").append(fn.getName()).append("\", args, TMessageType.CALL);\n"); // Use simple name
            sb.append("    }\n\n");

            if (fn.getOneway() != FunctionNode.Oneway.ONEWAY) {
                sb.append("    public ").append(returnType.javaType).append(" recv_").append(fn.getName()).append("()").append(getThrowsClause(fn, false)).append(" {\n");
                sb.append("      ").append(fn.getName()).append("_result result = new ").append(fn.getName()).append("_result();\n");
                sb.append("      receiveBase(result, \"").append(fn.getName()).append("\");\n");
                if (!returnType.javaType.equals("void")) {
                    sb.append("      if (result.isSetSuccess()) { return result.success; }\n");
                }
                for (FieldNode exField : fn.getExceptions()) {
                    // Changed to direct null check to match test expectation for testServiceWithExceptions
                    sb.append("      if (result.").append(exField.getName()).append(" != null) { throw result.").append(exField.getName()).append("; }\n");
                }
                if (returnType.javaType.equals("void")) { sb.append("      return;\n"); }
                else { sb.append("      throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, \"").append(fn.getName()).append(" failed: unknown result\");\n"); }
                sb.append("    }\n\n");
            }
        }
        sb.append("  }\n\n");
    }

    private void generateAsyncClient(String serviceName) {
        sb.append("  public static class AsyncClient extends ");
        String extendsServiceName = serviceNode.getExtendsService();
         if (extendsServiceName != null && !extendsServiceName.isEmpty()) {
            JavaTypeResolution parentService = resolveType(new SynthesizedIdentifierTypeNode(extendsServiceName));
            sb.append(parentService.javaType).append(".AsyncClient");
        } else {
            sb.append("org.apache.thrift.async.TAsyncClient");
        }
        sb.append(" implements AsyncIface {\n");
        sb.append("    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {\n");
        sb.append("      private org.apache.thrift.async.TAsyncClientManager clientManager;\n      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;\n");
        sb.append("      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {\n");
        sb.append("        this.clientManager = clientManager; this.protocolFactory = protocolFactory;\n      }\n");
        sb.append("      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {\n");
        sb.append("        return new AsyncClient(protocolFactory, clientManager, transport);\n");
        sb.append("      }\n    }\n\n");
        sb.append("    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {\n");
        sb.append("      super(protocolFactory, clientManager, transport);\n    }\n\n");

        for (FunctionNode fn : serviceNode.getFunctions()) {
            JavaTypeResolution returnTypeRes = resolveType(fn.getReturnType());
            String resultWrapperClass = getWrapperTypeIfPrimitive(returnTypeRes.javaType);

            sb.append("    public void ").append(fn.getName()).append("(");
            appendFunctionParameters(fn.getParameters(), true);
            sb.append("org.apache.thrift.async.AsyncMethodCallback<").append(resultWrapperClass).append("> resultHandler");
            sb.append(") throws org.apache.thrift.TException {\n");
            sb.append("      checkReady();\n");
            sb.append("      ").append(fn.getName()).append("_call method_call = new ").append(fn.getName()).append("_call(");
            appendFunctionCallArguments(fn.getParameters());
            if (fn.getParameters().size() > 0) sb.append(", ");
            sb.append("resultHandler, this, ___protocolFactory, ___transport);\n");
            sb.append("      this.___manager.call(method_call);\n    }\n\n");

            sb.append("    public static class ").append(fn.getName()).append("_call extends org.apache.thrift.async.TAsyncMethodCall<").append(resultWrapperClass).append("> {\n");
            for(FieldNode arg : fn.getParameters()){
                 JavaTypeResolution argTypeRes = resolveType(arg.getType());
                 sb.append("      private ").append(argTypeRes.javaType).append(" ").append(arg.getName()).append(";\n");
            }
            sb.append("      public ").append(fn.getName()).append("_call(");
            appendFunctionParameters(fn.getParameters(), true);
            sb.append("org.apache.thrift.async.AsyncMethodCallback<").append(resultWrapperClass).append("> resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {\n");
            sb.append("        super(client, protocolFactory, transport, resultHandler, ").append(fn.getOneway() == FunctionNode.Oneway.ONEWAY).append(");\n");
            for(FieldNode arg : fn.getParameters()){ sb.append("        this.").append(arg.getName()).append(" = ").append(arg.getName()).append(";\n"); }
            sb.append("      }\n\n");
            sb.append("      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {\n");
            sb.append("        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage(\"").append(fn.getName()).append("\", org.apache.thrift.protocol.TMessageType.CALL, 0));\n");
            sb.append("        ").append(fn.getName()).append("_args args = new ").append(fn.getName()).append("_args();\n");
            for(FieldNode arg : fn.getParameters()){ sb.append("        args.set").append(capitalize(arg.getName())).append("(this.").append(arg.getName()).append(");\n"); }
            sb.append("        args.write(prot);\n        prot.writeMessageEnd();\n      }\n\n");
            sb.append("      public ").append(resultWrapperClass).append(" getResult()").append(getThrowsClause(fn, true)).append(" {\n");
            sb.append("        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.SUCCESS) throw getError();\n");
            sb.append("        if (getFrameBuffer() == null) throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, \"").append(fn.getName()).append(" failed: unknown result\");\n");
            sb.append("        ").append(fn.getName()).append("_result result = new ").append(fn.getName()).append("_result();\n");
            sb.append("        try { result.read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array()))); } catch (TException e) { throw new org.apache.thrift.TApplicationException(TApplicationException.INTERNAL_ERROR, \"Internal error reading result: \" + e.getMessage()); }\n");
            if(!returnTypeRes.javaType.equals("void")){
                 sb.append("        if (result.isSetSuccess()) return result.success;\n");
            }
            for (FieldNode exField : fn.getExceptions()) {
                // Apply same change for AsyncClient's getResult exception handling
                sb.append("        if (result.").append(exField.getName()).append(" != null) throw result.").append(exField.getName()).append(";\n");
            }
            if(returnTypeRes.javaType.equals("void")) { sb.append("        return null;\n"); }
            else { sb.append("        throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, \"").append(fn.getName()).append(" failed: unknown result\");\n"); }
            sb.append("      }\n    }\n\n");
        }
        sb.append("  }\n\n");
    }

    private void generateProcessor(String serviceName) {
        sb.append("  public static class Processor<I extends Iface> extends ");
        String extendsServiceName = serviceNode.getExtendsService();
         if (extendsServiceName != null && !extendsServiceName.isEmpty()) {
            JavaTypeResolution parentService = resolveType(new SynthesizedIdentifierTypeNode(extendsServiceName));
            sb.append(parentService.javaType).append(".Processor<I>");
        } else {
            sb.append("org.apache.thrift.TBaseProcessor<I>");
        }
        sb.append(" implements org.apache.thrift.TProcessor {\n");
        sb.append("    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Processor.class.getName());\n");
        sb.append("    public Processor(I iface) {\n      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends TBase>>()));\n    }\n\n");
        sb.append("    protected Processor(I iface, Map<String, org.apache.thrift.ProcessFunction<I, ? extends TBase>> processMap) {\n      super(iface, getProcessMap(processMap));\n    }\n\n");
        sb.append("    private static <I extends Iface> Map<String,  org.apache.thrift.ProcessFunction<I, ? extends TBase>> getProcessMap(Map<String, org.apache.thrift.ProcessFunction<I, ? extends TBase>> processMap) {\n");
        for (FunctionNode fn : serviceNode.getFunctions()) {
            sb.append("      processMap.put(\"").append(fn.getName()).append("\", new ").append(fn.getName()).append("<I>());\n");
        }
        sb.append("      return processMap;\n    }\n\n");

        for (FunctionNode fn : serviceNode.getFunctions()) {
            String argsStructName = fn.getName() + "_args"; String resultStructName = fn.getName() + "_result";
            JavaTypeResolution returnType = resolveType(fn.getReturnType());

            sb.append("    public static class ").append(fn.getName()).append("<I extends Iface> extends org.apache.thrift.ProcessFunction<I, ").append(argsStructName).append("> {\n");
            sb.append("      public ").append(fn.getName()).append("() { super(\"").append(fn.getName()).append("\"); }\n\n");
            sb.append("      public ").append(argsStructName).append(" getEmptyArgsInstance() { return new ").append(argsStructName).append("(); }\n\n");
            sb.append("      protected boolean isOneway() {\n");
            sb.append("        return ").append(fn.getOneway() == FunctionNode.Oneway.ONEWAY).append(";\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n      protected ").append(resultStructName).append(" getResult(I iface, ").append(argsStructName).append(" args) throws org.apache.thrift.TException {\n");
            sb.append("        ").append(resultStructName).append(" result = new ").append(resultStructName).append("();\n");
            sb.append("        try {\n          ");
            StringBuilder argPassingSb = new StringBuilder();
            for(int i=0; i<fn.getParameters().size(); i++) {
                argPassingSb.append("args.").append(fn.getParameters().get(i).getName()).append(i < fn.getParameters().size() -1 ? ", " : "");
            }
            if (returnType.javaType.equals("void")) {
                sb.append("iface.").append(fn.getName()).append("(").append(argPassingSb.toString()).append(");\n");
            } else {
                sb.append("result.success = iface.").append(fn.getName()).append("(").append(argPassingSb.toString()).append(");\n");
                if (isPrimitive(returnType.javaType)) {
                     sb.append("          result.setSuccessIsSet(true);\n");
                }
            }
            sb.append("        }");
            for (FieldNode exField : fn.getExceptions()) {
                JavaTypeResolution exRes = resolveType(exField.getType());
                sb.append(" catch (").append(exRes.javaType).append(" ").append(exField.getName()).append(") {\n");
                sb.append("          result.set").append(capitalize(exField.getName())).append("(").append(exField.getName()).append(");\n        }");
            }
            sb.append(" catch (Throwable th) {\n          LOGGER.error(\"Internal error processing ").append(fn.getName()).append("\", th);\n");
            sb.append("          if (!isOneway()) { throw new TApplicationException(TApplicationException.INTERNAL_ERROR, \"Internal error processing ").append(fn.getName()).append(": \" + th.getMessage()); }\n");
            sb.append("        }\n");
            sb.append("        return result;\n      }\n    }\n\n");
        }
        sb.append("  }\n\n");
    }

    private void generateArgsStruct(FunctionNode fn) {
        generateFnStruct(fn.getName() + "_args", fn.getParameters(), true);
    }

    private void generateResultStruct(FunctionNode fn) {
        List<FieldNode> resultFields = new ArrayList<>();
        JavaTypeResolution returnType = resolveType(fn.getReturnType());
        if (!returnType.javaType.equals("void")) {
            resultFields.add(new InternalFieldNode((short) 0, "success", fn.getReturnType()));
        }
        short exId = 1;
        for (FieldNode ex : fn.getExceptions()) {
            resultFields.add(new InternalFieldNode(exId++, ex.getName(), ex.getType()));
        }
        generateFnStruct(fn.getName() + "_result", resultFields, false);
    }

    private void generateFnStruct(String structName, List<FieldNode> fields, boolean isArgs) {
        sb.append("  public static class ").append(structName).append(" implements TBase<").append(structName).append(", ").append(structName).append("._Fields>, java.io.Serializable, Cloneable, Comparable<").append(structName).append("> {\n");
        sb.append("    private static final TStruct STRUCT_DESC = new TStruct(\"").append(structName).append("\");\n");

        Map<FieldNode, JavaTypeResolution> fieldResolutions = new HashMap<>();
        int bitFieldIndex = 0;
        List<String> issetConstants = new ArrayList<>();

        for(FieldNode field : fields) {
            JavaTypeResolution typeRes = resolveType(field.getType());
            fieldResolutions.put(field, typeRes);

            Integer fieldIdInt = field.getId();
            short fieldIdVal = (fieldIdInt != null) ? fieldIdInt.shortValue() : (isArgs ? (short)(fields.indexOf(field) + 1) : (short)0 );
             if (!isArgs && !"success".equals(field.getName()) && field instanceof InternalFieldNode) {
                 fieldIdVal = field.getId().shortValue();
             }

            sb.append("    private static final TField ").append(toAllCapsUnderscore(field.getName())).append("_FIELD_DESC = new TField(\"").append(field.getName()).append("\", ").append(typeRes.thriftType).append(", (short)").append(fieldIdVal).append(");\n");
            sb.append("    public ").append(typeRes.javaType).append(" ").append(field.getName()).append(";\n");
            if (isPrimitive(typeRes.javaType)) {
                 issetConstants.add("    private static final int __" + field.getName().toUpperCase(Locale.ROOT) + "_ISSET_ID = " + bitFieldIndex++ + ";\n");
            }
        }
        if (!issetConstants.isEmpty()) {
            sb.append("    private byte __isset_bitfield = 0;\n");
            for(String s : issetConstants) { sb.append(s); }
            sb.append("\n");
        } else if (!isArgs && fields.stream().anyMatch(f -> "success".equals(f.getName()) && isPrimitive(fieldResolutions.get(f).javaType))) {
             sb.append("    private byte __isset_bitfield = 0;\n\n");
        }

        sb.append("    public enum _Fields implements org.apache.thrift.TFieldIdEnum {\n");
        for(int i=0; i<fields.size(); i++) {
            FieldNode field = fields.get(i);
            Integer fieldIdInt = field.getId();
            short fieldIdVal = (fieldIdInt != null) ? fieldIdInt.shortValue() : (isArgs ? (short)(i + 1) : (short)0);
            if (!isArgs && !"success".equals(field.getName()) && field instanceof InternalFieldNode) {
                 fieldIdVal = field.getId().shortValue();
            }
            sb.append("      ").append(toAllCapsUnderscore(field.getName())).append("((short)").append(fieldIdVal).append(", \"").append(field.getName()).append("\")").append(i == fields.size() -1 ? ";\n" : ",\n");
        }
        sb.append("      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();\n");
        sb.append("      static { for (_Fields field : EnumSet.allOf(_Fields.class)) { byName.put(field.getFieldName(), field); } }\n");
        sb.append("      @org.apache.thrift.annotation.Nullable public static _Fields findByThriftId(int fieldId) { /* TODO */ return null; }\n");
        sb.append("      public static _Fields findByThriftIdOrThrow(int fieldId) { _Fields fields = findByThriftId(fieldId); if (fields == null) throw new IllegalArgumentException(\"Field \" + fieldId + \" doesn't exist!\"); return fields; }\n");
        sb.append("      @org.apache.thrift.annotation.Nullable public static _Fields findByName(String name) { return byName.get(name); }\n");
        sb.append("      private final short _thriftId; private final String _fieldName;\n");
        sb.append("      _Fields(short thriftId, String fieldName) { this._thriftId = thriftId; this._fieldName = fieldName; }\n");
        sb.append("      @Override public short getThriftFieldId() { return _thriftId; }\n      @Override public String getFieldName() { return _fieldName; }\n    }\n\n");

        sb.append("    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;\n");
        sb.append("    static {\n      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);\n");
        for(FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            com.github.decster.ast.FieldNode.Requirement reqEnum = field.getRequirement();
            String requirementStr;
            if (isArgs) {
                requirementStr = (reqEnum == null) ? "DEFAULT" : reqEnum.name();
            } else {
                requirementStr = "OPTIONAL";
            }
            // Use simple name for FieldMetaData, relying on import
            sb.append("      tmpMap.put(_Fields.").append(toAllCapsUnderscore(field.getName())).append(", new FieldMetaData(\"").append(field.getName()).append("\", TFieldRequirementType.").append(requirementStr).append(", ").append(typeRes.fieldMetaDataType).append("));\n");
        }
        sb.append("      metaDataMap = Collections.unmodifiableMap(tmpMap);\n");
        sb.append("      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(").append(structName).append(".class, metaDataMap);\n    }\n\n");

        sb.append("    public ").append(structName).append("() {}\n\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String capFieldName = capitalize(field.getName());
            sb.append("    public ").append(typeRes.javaType).append(" get").append(capFieldName).append("() { return this.").append(field.getName()).append("; }\n");
            sb.append("    public ").append(structName).append(" set").append(capFieldName).append("(@org.apache.thrift.annotation.Nullable ").append(typeRes.javaType).append(" ").append(field.getName()).append(") {\n");
            sb.append("      this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n");
            if (isPrimitive(typeRes.javaType)) sb.append("      set").append(capFieldName).append("IsSet(true);\n");
            sb.append("      return this;\n    }\n");
            sb.append("    public void unset").append(capFieldName).append("() { ");
            if (isPrimitive(typeRes.javaType)) sb.append("__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __").append(field.getName().toUpperCase(Locale.ROOT)).append("_ISSET_ID);\n");
            else sb.append("this.").append(field.getName()).append(" = null;\n");
            sb.append("    }\n");
            sb.append("    public boolean isSet").append(capFieldName).append("() {\n");
            if (isPrimitive(typeRes.javaType)) sb.append("      return EncodingUtils.testBit(__isset_bitfield, __").append(field.getName().toUpperCase(Locale.ROOT)).append("_ISSET_ID);\n");
            else sb.append("      return this.").append(field.getName()).append(" != null;\n");
            sb.append("    }\n");
            if (isPrimitive(typeRes.javaType)) sb.append("    public void set").append(capFieldName).append("IsSet(boolean value) { __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __").append(field.getName().toUpperCase(Locale.ROOT)).append("_ISSET_ID, value);}\n");
        }

        sb.append("    @Override public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException { TProtocolUtil.skip(iprot, TType.STRUCT); /* TODO: Implement full read for ").append(structName).append(" */ }\n");
        sb.append("    @Override public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException { oprot.writeStructBegin(STRUCT_DESC); /* TODO: Implement full write for ").append(structName).append(" */ oprot.writeFieldStop(); oprot.writeStructEnd(); }\n");
        sb.append("    @Override public String toString() { return \"").append(structName).append("(...)\"; }\n");
        sb.append("    public void validate() throws TException {} \n");
        sb.append("    @Override public int compareTo(").append(structName).append(" other){ if (!getClass().equals(other.getClass())) { return getClass().getName().compareTo(other.getClass().getName()); } /* TODO */ return 0; } \n");
        sb.append("    @Override public boolean equals(Object o){ if (o == this) return true; if (!(o instanceof ").append(structName).append(")) return false; return this.equals((").append(structName).append(")o); } \n");
        sb.append("    public boolean equals(").append(structName).append(" o){ if (o == null) return false; if (this == o) return true; /* TODO */ return true; } \n");
        sb.append("    @Override public int hashCode(){ /* TODO */ return 0; } \n");
        sb.append("    @org.apache.thrift.annotation.Nullable @Override public _Fields fieldForId(int fieldId) { return _Fields.findByThriftId(fieldId); }\n");
        sb.append("    @Override public void clear() {/* TODO */}\n");
        sb.append("    @org.apache.thrift.annotation.Nullable @Override public Object getFieldValue(_Fields field) {/* TODO */return null;}\n");
        sb.append("    @Override public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable Object value) {/* TODO */}\n");
        sb.append("    @Override public boolean isSet(_Fields field) {/* TODO */return false;}\n");
        sb.append("    @SuppressWarnings(\"unchecked\")\n    public ").append(structName).append(" deepCopy() { /* TODO */ return new ").append(structName).append("(this); } \n");

        sb.append("  }\n\n");
    }

    private static class SynthesizedIdentifierTypeNode extends com.github.decster.ast.IdentifierTypeNode {
        public SynthesizedIdentifierTypeNode(String name) {
            super(name);
        }
        // All methods are inherited.
    }

    private static class InternalFieldNode extends com.github.decster.ast.FieldNode {
        public InternalFieldNode(short id, String name, TypeNode type) {
            super(type, name);
            this.setId((int)id);
        }
        // All methods are inherited.
    }
}
