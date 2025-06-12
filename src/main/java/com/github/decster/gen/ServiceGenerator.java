package com.github.decster.gen;

import com.github.decster.ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.protocol.TType;


public class ServiceGenerator implements Generator {
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

    private String getPackageFromQualifiedName(String qName) {
        if (qName == null || !qName.contains(".")) return null;
        return qName.substring(0, qName.lastIndexOf('.'));
    }

    private String getWrapperTypeIfPrimitive(String javaType) {
        if (javaType == null) return "Void";
        switch (javaType) {
            case "boolean": return "java.lang.Boolean";
            case "byte":    return "java.lang.Byte";
            case "short":   return "java.lang.Short";
            case "int":     return "java.lang.Integer";
            case "long":    return "java.lang.Long";
            case "double":  return "java.lang.Double";
            case "void":    return "Void";
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
                case BOOL:   return new JavaTypeResolution("boolean", TType.BOOL, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)");
                case BYTE:   return new JavaTypeResolution("byte", TType.BYTE, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)");
                case I16:    return new JavaTypeResolution("short", TType.I16, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)");
                case I32:    return new JavaTypeResolution("int", TType.I32, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)");
                case I64:    return new JavaTypeResolution("long", TType.I64, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)");
                case DOUBLE: return new JavaTypeResolution("double", TType.DOUBLE, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)");
                case STRING: return new JavaTypeResolution("java.lang.String", TType.STRING, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)");
                case BINARY:
                    JavaTypeResolution binRes = new JavaTypeResolution("java.nio.ByteBuffer", TType.STRING, "new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING, true)");
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
            String elementJavaType = getWrapperTypeIfPrimitive(elementTypeRes.javaType);
            JavaTypeResolution listRes = new JavaTypeResolution("java.util.List<" + elementJavaType + ">", TType.LIST, "new ListMetaData(org.apache.thrift.protocol.TType.LIST, " + elementTypeRes.fieldMetaDataType + ")");
            listRes.addImport("java.util.List"); return listRes;
        } else if (typeNode instanceof SetTypeNode) {
            SetTypeNode setType = (SetTypeNode) typeNode;
            JavaTypeResolution elementTypeRes = resolveType(setType.getElementType());
            String elementJavaType = getWrapperTypeIfPrimitive(elementTypeRes.javaType);
            JavaTypeResolution setRes =  new JavaTypeResolution("java.util.Set<" + elementJavaType + ">", TType.SET, "new SetMetaData(org.apache.thrift.protocol.TType.SET, " + elementTypeRes.fieldMetaDataType + ")");
            setRes.addImport("java.util.Set"); return setRes;
        } else if (typeNode instanceof MapTypeNode) {
            MapTypeNode mapType = (MapTypeNode) typeNode;
            JavaTypeResolution keyTypeRes = resolveType(mapType.getKeyType());
            JavaTypeResolution valueTypeRes = resolveType(mapType.getValueType());
            String keyJavaType = getWrapperTypeIfPrimitive(keyTypeRes.javaType);
            String valueJavaType = getWrapperTypeIfPrimitive(valueTypeRes.javaType);
            JavaTypeResolution mapRes = new JavaTypeResolution("java.util.Map<" + keyJavaType + "," + valueJavaType + ">", TType.MAP, "new MapMetaData(org.apache.thrift.protocol.TType.MAP, " + keyTypeRes.fieldMetaDataType + ", " + valueTypeRes.fieldMetaDataType + ")");
            mapRes.addImport("java.util.Map"); return mapRes;
        }
        throw new IllegalArgumentException("Unsupported TypeNode: " + typeNode.getClass().getName() + (typeNode.getName() != null ? " for type name " + typeNode.getName() : ""));
    }

    public String generate() {
        String serviceName = serviceNode.getName();

        sb.append("/**\n");
        sb.append(" * Autogenerated by Thrift Compiler (0.20.0)\n");
        sb.append(" *\n");
        sb.append(" * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING\n");
        sb.append(" *  @generated\n");
        sb.append(" */\n");
        sb.append("package ").append(packageName).append(";\n\n");

        sb.append("@javax.annotation.Generated(value = \"Autogenerated by Thrift Compiler (0.20.0)\", date = \"").append(date).append("\")\n");
        sb.append("@SuppressWarnings({\"cast\", \"rawtypes\", \"serial\", \"unchecked\", \"unused\"})\n");
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

        return sb.toString();
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
        sb.append("    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {\n");
        sb.append("      public Factory() {}\n");
        sb.append("      @Override\n");
        sb.append("      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {\n");
        sb.append("        return new Client(prot);\n");
        sb.append("      }\n");
        sb.append("      @Override\n");
        sb.append("      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {\n");
        sb.append("        return new Client(iprot, oprot);\n");
        sb.append("      }\n");
        sb.append("    }\n\n");
        sb.append("    public Client(org.apache.thrift.protocol.TProtocol prot)\n");
        sb.append("    {\n");
        sb.append("      super(prot, prot);\n");
        sb.append("    }\n\n");
        sb.append("    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {\n");
        sb.append("      super(iprot, oprot);\n");
        sb.append("    }\n\n");

        for (FunctionNode fn : serviceNode.getFunctions()) {
            JavaTypeResolution returnType = resolveType(fn.getReturnType());
            sb.append("    @Override\n");
            sb.append("    public ").append(returnType.javaType).append(" ").append(fn.getName()).append("(");
            appendFunctionParameters(fn.getParameters(), false);
            sb.append(")").append(getThrowsClause(fn, false)).append("\n");
            sb.append("    {\n");
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
            sb.append(") throws org.apache.thrift.TException\n");
            sb.append("    {\n");
            sb.append("      ").append(fn.getName()).append("_args args = new ").append(fn.getName()).append("_args();\n");
            for (FieldNode arg : fn.getParameters()) {
                sb.append("      args.set").append(capitalize(arg.getName())).append("(").append(arg.getName()).append(");\n");
            }
            sb.append("      sendBase(\"").append(fn.getName()).append("\", args);\n");
            sb.append("    }\n\n");

            if (fn.getOneway() != FunctionNode.Oneway.ONEWAY) {
                sb.append("    public ").append(returnType.javaType).append(" recv_").append(fn.getName()).append("()").append(getThrowsClause(fn, false)).append("\n    {\n");
                sb.append("      ").append(fn.getName()).append("_result result = new ").append(fn.getName()).append("_result();\n");
                sb.append("      receiveBase(result, \"").append(fn.getName()).append("\");\n");
                if (!returnType.javaType.equals("void")) {
                    sb.append("      if (result.isSetSuccess()) {\n");
                    sb.append("        return result.success;\n");
                    sb.append("      }\n");
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
        sb.append("  }\n");
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
        sb.append("        this.clientManager = clientManager;\n");
        sb.append("        this.protocolFactory = protocolFactory;\n      }\n");
        sb.append("    @Override\n");
        sb.append("      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {\n");
        sb.append("        return new AsyncClient(protocolFactory, clientManager, transport);\n");
        sb.append("      }\n    }\n\n");
        sb.append("    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {\n");
        sb.append("      super(protocolFactory, clientManager, transport);\n    }\n\n");

        for (FunctionNode fn : serviceNode.getFunctions()) {
            JavaTypeResolution returnTypeRes = resolveType(fn.getReturnType());
            String resultWrapperClass = getWrapperTypeIfPrimitive(returnTypeRes.javaType);

            sb.append("    @Override\n");
            sb.append("    public void ").append(fn.getName()).append("(");
            appendFunctionParameters(fn.getParameters(), true);
            sb.append("org.apache.thrift.async.AsyncMethodCallback<").append(resultWrapperClass).append("> resultHandler");
            sb.append(") throws org.apache.thrift.TException {\n");
            sb.append("      checkReady();\n");
            sb.append("      ").append(fn.getName()).append("_call method_call = new ").append(fn.getName()).append("_call(");
            appendFunctionCallArguments(fn.getParameters());
            if (fn.getParameters().size() > 0) sb.append(", ");
            sb.append("resultHandler, this, ___protocolFactory, ___transport);\n");
            sb.append("      this.___currentMethod = method_call;\n");
            sb.append("      ___manager.call(method_call);\n    }\n\n");

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
            sb.append("      @Override\n");
            sb.append("      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {\n");
            sb.append("        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage(\"").append(fn.getName()).append("\", org.apache.thrift.protocol.TMessageType.CALL, 0));\n");
            sb.append("        ").append(fn.getName()).append("_args args = new ").append(fn.getName()).append("_args();\n");
            for(FieldNode arg : fn.getParameters()){ sb.append("        args.set").append(capitalize(arg.getName())).append("(").append(arg.getName()).append(");\n"); }
            sb.append("        args.write(prot);\n        prot.writeMessageEnd();\n      }\n\n");
            sb.append("      @Override\n");
            sb.append("      public ").append(resultWrapperClass).append(" getResult()").append(getThrowsClause(fn, true)).append(" {\n");
            sb.append("        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {\n");
            sb.append("          throw new java.lang.IllegalStateException(\"Method call not finished!\");\n");
            sb.append("        }\n");
            sb.append("        org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());\n");
            sb.append("        org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);\n");
            sb.append("        ");
            if (!returnTypeRes.javaType.equals("void")) {
                sb.append("return ");
            }
            sb.append("(new Client(prot)).recv_").append(fn.getName()).append("();\n");
            if(returnTypeRes.javaType.equals("void")) { sb.append("        return null;\n"); }
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
        sb.append("    private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(Processor.class.getName());\n");
        sb.append("    public Processor(I iface) {\n      super(iface, getProcessMap(new java.util.HashMap<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));\n    }\n\n");
        sb.append("    protected Processor(I iface, java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {\n      super(iface, getProcessMap(processMap));\n    }\n\n");
        sb.append("    private static <I extends Iface> java.util.Map<java.lang.String,  org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {\n");
        for (FunctionNode fn : serviceNode.getFunctions()) {
            sb.append("      processMap.put(\"").append(fn.getName()).append("\", new ").append(fn.getName()).append("());\n");
        }
        sb.append("      return processMap;\n    }\n\n");

        for (FunctionNode fn : serviceNode.getFunctions()) {
            String argsStructName = fn.getName() + "_args"; String resultStructName = fn.getName() + "_result";
            JavaTypeResolution returnType = resolveType(fn.getReturnType());

            sb.append("    public static class ").append(fn.getName()).append("<I extends Iface> extends org.apache.thrift.ProcessFunction<I, ").append(argsStructName).append("> {\n");
            sb.append("      public ").append(fn.getName()).append("() {\n");
            sb.append("        super(\"").append(fn.getName()).append("\");\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n");
            sb.append("      public ").append(argsStructName).append(" getEmptyArgsInstance() {\n");
            sb.append("        return new ").append(argsStructName).append("();\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n");
            sb.append("      protected boolean isOneway() {\n");
            sb.append("        return ").append(fn.getOneway() == FunctionNode.Oneway.ONEWAY).append(";\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n");
            sb.append("      protected boolean rethrowUnhandledExceptions() {\n");
            sb.append("        return false;\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n      public ").append(resultStructName).append(" getResult(I iface, ").append(argsStructName).append(" args) throws org.apache.thrift.TException {\n");
            sb.append("        ").append(resultStructName).append(" result = new ").append(resultStructName).append("();\n");
            StringBuilder argPassingSb = new StringBuilder();
            for(int i=0; i<fn.getParameters().size(); i++) {
                argPassingSb.append("args.").append(fn.getParameters().get(i).getName()).append(i < fn.getParameters().size() -1 ? ", " : "");
            }
            if (returnType.javaType.equals("void")) {
                sb.append("        iface.").append(fn.getName()).append("(").append(argPassingSb.toString()).append(");\n");
            } else {
                sb.append("        result.success = iface.").append(fn.getName()).append("(").append(argPassingSb.toString()).append(");\n");
                if (isPrimitive(returnType.javaType)) {
                    sb.append("        result.setSuccessIsSet(true);\n");
                }
            }
            sb.append("        return result;\n      }\n    }\n\n");
        }
        sb.append("  }\n\n");

        // Generate AsyncProcessor
        sb.append("  public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {\n");
        sb.append("    private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(AsyncProcessor.class.getName());\n");
        sb.append("    public AsyncProcessor(I iface) {\n");
        sb.append("      super(iface, getProcessMap(new java.util.HashMap<java.lang.String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));\n");
        sb.append("    }\n\n");
        sb.append("    protected AsyncProcessor(I iface, java.util.Map<java.lang.String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {\n");
        sb.append("      super(iface, getProcessMap(processMap));\n");
        sb.append("    }\n\n");
        sb.append("    private static <I extends AsyncIface> java.util.Map<java.lang.String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase,?>> getProcessMap(java.util.Map<java.lang.String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {\n");
        for (FunctionNode fn : serviceNode.getFunctions()) {
            sb.append("      processMap.put(\"").append(fn.getName()).append("\", new ").append(fn.getName()).append("());\n");
        }
        sb.append("      return processMap;\n");
        sb.append("    }\n\n");

        for (FunctionNode fn : serviceNode.getFunctions()) {
            String argsStructName = fn.getName() + "_args";
            String resultStructName = fn.getName() + "_result";
            JavaTypeResolution returnType = resolveType(fn.getReturnType());
            String resultWrapperClass = getWrapperTypeIfPrimitive(returnType.javaType);

            sb.append("    public static class ").append(fn.getName()).append("<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, ").append(argsStructName).append(", ").append(resultWrapperClass).append("> {\n");
            sb.append("      public ").append(fn.getName()).append("() {\n");
            sb.append("        super(\"").append(fn.getName()).append("\");\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n");
            sb.append("      public ").append(argsStructName).append(" getEmptyArgsInstance() {\n");
            sb.append("        return new ").append(argsStructName).append("();\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n");
            sb.append("      public org.apache.thrift.async.AsyncMethodCallback<").append(resultWrapperClass).append("> getResultHandler(final org.apache.thrift.server.AbstractNonblockingServer.AsyncFrameBuffer fb, final int seqid) {\n");
            sb.append("        final org.apache.thrift.AsyncProcessFunction fcall = this;\n");
            sb.append("        return new org.apache.thrift.async.AsyncMethodCallback<").append(resultWrapperClass).append(">() { \n");
            sb.append("          @Override\n");
            sb.append("          public void onComplete(").append(resultWrapperClass).append(" o) {\n");
            sb.append("            ").append(fn.getName()).append("_result result = new ").append(fn.getName()).append("_result();\n");
            if (!resultWrapperClass.equals("Void")) {
                sb.append("            result.success = o;\n");
                JavaTypeResolution fnReturnType = resolveType(fn.getReturnType());
                if (isPrimitive(fnReturnType.javaType)) {
                    sb.append("            result.setSuccessIsSet(true);\n");
                }
            }
            sb.append("            try {\n");
            sb.append("              fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY,seqid);\n");
            sb.append("            } catch (org.apache.thrift.transport.TTransportException e) {\n");
            sb.append("              _LOGGER.error(\"TTransportException writing to internal frame buffer\", e);\n");
            sb.append("              fb.close();\n");
            sb.append("            } catch (java.lang.Exception e) {\n");
            sb.append("              _LOGGER.error(\"Exception writing to internal frame buffer\", e);\n");
            sb.append("              onError(e);\n");
            sb.append("            }\n");
            sb.append("          }\n");
            sb.append("          @Override\n");
            sb.append("          public void onError(java.lang.Exception e) {\n");
            sb.append("            byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;\n");
            sb.append("            org.apache.thrift.TSerializable msg;\n");
            sb.append("            ").append(fn.getName()).append("_result result = new ").append(fn.getName()).append("_result();\n");
            sb.append("            if (e instanceof org.apache.thrift.transport.TTransportException) {\n");
            sb.append("              _LOGGER.error(\"TTransportException inside handler\", e);\n");
            sb.append("              fb.close();\n");
            sb.append("              return;\n");
            sb.append("            } else if (e instanceof org.apache.thrift.TApplicationException) {\n");
            sb.append("              _LOGGER.error(\"TApplicationException inside handler\", e);\n");
            sb.append("              msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;\n");
            sb.append("              msg = (org.apache.thrift.TApplicationException)e;\n");
            sb.append("            } else {\n");
            sb.append("              _LOGGER.error(\"Exception inside handler\", e);\n");
            sb.append("              msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;\n");
            sb.append("              msg = new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());\n");
            sb.append("            }\n");
            sb.append("            try {\n");
            sb.append("              fcall.sendResponse(fb,msg,msgType,seqid);\n");
            sb.append("            } catch (java.lang.Exception ex) {\n");
            sb.append("              _LOGGER.error(\"Exception writing to internal frame buffer\", ex);\n");
            sb.append("              fb.close();\n");
            sb.append("            }\n");
            sb.append("          }\n");
            sb.append("        };\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n");
            sb.append("      protected boolean isOneway() {\n");
            sb.append("        return ").append(fn.getOneway() == FunctionNode.Oneway.ONEWAY).append(";\n");
            sb.append("      }\n\n");
            sb.append("      @Override\n");
            sb.append("      public void start(I iface, ").append(argsStructName).append(" args, org.apache.thrift.async.AsyncMethodCallback<").append(resultWrapperClass).append("> resultHandler) throws org.apache.thrift.TException {\n");
            StringBuilder argPassingSb = new StringBuilder();
            for(int i=0; i<fn.getParameters().size(); i++) {
                argPassingSb.append("args.").append(fn.getParameters().get(i).getName()).append(i < fn.getParameters().size() -1 ? ", " : "");
            }
            if (fn.getParameters().size() > 0 || !argPassingSb.toString().isEmpty()) {
                argPassingSb.append(",");
            }
            sb.append("        iface.").append(fn.getName()).append("(").append(argPassingSb.toString()).append("resultHandler);\n");
            sb.append("      }\n");
            sb.append("    }\n\n");
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
        sb.append("  @SuppressWarnings({\"cast\", \"rawtypes\", \"serial\", \"unchecked\", \"unused\"})\n");
        sb.append("  public static class ").append(structName).append(" implements org.apache.thrift.TBase<").append(structName).append(", ").append(structName).append("._Fields>, java.io.Serializable, Cloneable, Comparable<").append(structName).append(">   {\n");
        sb.append("    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(\"").append(structName).append("\");\n\n");

        Map<FieldNode, JavaTypeResolution> fieldResolutions = new HashMap<>();
        int bitFieldIndex = 0;
        List<String> issetConstants = new ArrayList<>();
        boolean hasIssetBitfield = false;

        for(FieldNode field : fields) {
            JavaTypeResolution typeRes = resolveType(field.getType());
            fieldResolutions.put(field, typeRes);

            Integer fieldIdInt = field.getId();
            short fieldIdVal = (fieldIdInt != null) ? fieldIdInt.shortValue() : (isArgs ? (short)(fields.indexOf(field) + 1) : (short)0 );
             if (!isArgs && !"success".equals(field.getName()) && field instanceof InternalFieldNode) {
                 fieldIdVal = field.getId().shortValue();
             }

            sb.append("    private static final org.apache.thrift.protocol.TField ").append(toAllCapsUnderscore(field.getName())).append("_FIELD_DESC = new org.apache.thrift.protocol.TField(\"").append(field.getName()).append("\", org.apache.thrift.protocol.TType.").append(getTTypeConstant(typeRes.thriftType)).append(", (short)").append(fieldIdVal).append(");\n");
            if (isPrimitive(typeRes.javaType)) {
                 issetConstants.add("    private static final int __" + field.getName().toUpperCase(Locale.ROOT) + "_ISSET_ID = " + bitFieldIndex++ + ";\n");
                 hasIssetBitfield = true;
            }
        }

        sb.append("\n");
        sb.append("    private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new ").append(structName).append("StandardSchemeFactory();\n");
        sb.append("    private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new ").append(structName).append("TupleSchemeFactory();\n");
        sb.append("\n");

        for(FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            if (!isPrimitive(typeRes.javaType)) {
                sb.append("    public @org.apache.thrift.annotation.Nullable ").append(typeRes.javaType).append(" ").append(field.getName()).append("; // required\n");
            } else {
                sb.append("    public ").append(typeRes.javaType).append(" ").append(field.getName()).append("; // required\n");
            }
        }
        sb.append("\n");

        sb.append("    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */\n");
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
        sb.append("\n");
        sb.append("      private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();\n");
        sb.append("\n");
        sb.append("      static {\n");
        sb.append("        for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {\n");
        sb.append("          byName.put(field.getFieldName(), field);\n");
        sb.append("        }\n");
        sb.append("      }\n");
        sb.append("\n");
        sb.append("      /**\n");
        sb.append("       * Find the _Fields constant that matches fieldId, or null if its not found.\n");
        sb.append("       */\n");
        sb.append("      @org.apache.thrift.annotation.Nullable\n");
        sb.append("      public static _Fields findByThriftId(int fieldId) {\n");
        sb.append("        switch(fieldId) {\n");
        for(int i=0; i<fields.size(); i++) {
            FieldNode field = fields.get(i);
            Integer fieldIdInt = field.getId();
            short fieldIdVal = (fieldIdInt != null) ? fieldIdInt.shortValue() : (isArgs ? (short)(i + 1) : (short)0);
            if (!isArgs && !"success".equals(field.getName()) && field instanceof InternalFieldNode) {
                 fieldIdVal = field.getId().shortValue();
            }
            sb.append("          case ").append(fieldIdVal).append(": // ").append(toAllCapsUnderscore(field.getName())).append("\n");
            sb.append("            return ").append(toAllCapsUnderscore(field.getName())).append(";\n");
        }
        sb.append("          default:\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("      }\n");
        sb.append("\n");
        sb.append("      /**\n");
        sb.append("       * Find the _Fields constant that matches fieldId, throwing an exception\n");
        sb.append("       * if it is not found.\n");
        sb.append("       */\n");
        sb.append("      public static _Fields findByThriftIdOrThrow(int fieldId) {\n");
        sb.append("        _Fields fields = findByThriftId(fieldId);\n");
        sb.append("        if (fields == null) throw new java.lang.IllegalArgumentException(\"Field \" + fieldId + \" doesn't exist!\");\n");
        sb.append("        return fields;\n");
        sb.append("      }\n");
        sb.append("\n");
        sb.append("      /**\n");
        sb.append("       * Find the _Fields constant that matches name, or null if its not found.\n");
        sb.append("       */\n");
        sb.append("      @org.apache.thrift.annotation.Nullable\n");
        sb.append("      public static _Fields findByName(java.lang.String name) {\n");
        sb.append("        return byName.get(name);\n");
        sb.append("      }\n");
        sb.append("\n");
        sb.append("      private final short _thriftId;\n");
        sb.append("      private final java.lang.String _fieldName;\n");
        sb.append("\n");
        sb.append("      _Fields(short thriftId, java.lang.String fieldName) {\n");
        sb.append("        _thriftId = thriftId;\n");
        sb.append("        _fieldName = fieldName;\n");
        sb.append("      }\n");
        sb.append("\n");
        sb.append("      @Override\n");
        sb.append("      public short getThriftFieldId() {\n");
        sb.append("        return _thriftId;\n");
        sb.append("      }\n");
        sb.append("\n");
        sb.append("      @Override\n");
        sb.append("      public java.lang.String getFieldName() {\n");
        sb.append("        return _fieldName;\n");
        sb.append("      }\n");
        sb.append("    }\n\n");

        // Add isset declarations after _Fields enum
        if (hasIssetBitfield) {
            sb.append("    // isset id assignments\n");
            for(String s : issetConstants) { sb.append(s); }
            sb.append("    private byte __isset_bitfield = 0;\n");
        }
        sb.append("    public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;\n");
        sb.append("    static {\n      java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);\n");
        for(FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            com.github.decster.ast.FieldNode.Requirement reqEnum = field.getRequirement();
            String requirementStr;
            if (isArgs) {
                requirementStr = (reqEnum == null) ? "DEFAULT" : reqEnum.name();
            } else {
                requirementStr = "OPTIONAL";
            }
            // Use fully qualified names for types
            sb.append("      tmpMap.put(_Fields.").append(toAllCapsUnderscore(field.getName())).append(", new org.apache.thrift.meta_data.FieldMetaData(\"").append(field.getName()).append("\", org.apache.thrift.TFieldRequirementType.").append(requirementStr).append(", \n");
            sb.append("          ").append(typeRes.fieldMetaDataType).append("));\n");
        }
        sb.append("      metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);\n");
        sb.append("      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(").append(structName).append(".class, metaDataMap);\n    }\n\n");

        sb.append("    public ").append(structName).append("() {\n    }\n\n");

        // Add constructor with all fields
        if (!fields.isEmpty()) {
            sb.append("    public ").append(structName).append("(\n");
            for (int i = 0; i < fields.size(); i++) {
                FieldNode field = fields.get(i);
                JavaTypeResolution typeRes = fieldResolutions.get(field);
                sb.append("      ");
                if (!isPrimitive(typeRes.javaType)) {
                    sb.append("java.lang.").append(typeRes.javaType.substring(typeRes.javaType.lastIndexOf('.') + 1));
                } else {
                    sb.append(typeRes.javaType);
                }
                sb.append(" ").append(field.getName());
                if (i < fields.size() - 1) {
                    sb.append(",\n");
                } else {
                    sb.append(")\n");
                }
            }
            sb.append("    {\n");
            sb.append("      this();\n");
            for (FieldNode field : fields) {
                sb.append("      this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n");
                JavaTypeResolution typeRes = fieldResolutions.get(field);
                if (isPrimitive(typeRes.javaType)) {
                    sb.append("      set").append(capitalize(field.getName())).append("IsSet(true);\n");
                }
            }
            sb.append("    }\n\n");

            // Add copy constructor
            sb.append("    /**\n");
            sb.append("     * Performs a deep copy on <i>other</i>.\n");
            sb.append("     */\n");
            sb.append("    public ").append(structName).append("(").append(structName).append(" other) {\n");
            if (hasIssetBitfield) {
                sb.append("      __isset_bitfield = other.__isset_bitfield;\n");
            }
            for (FieldNode field : fields) {
                JavaTypeResolution typeRes = fieldResolutions.get(field);
                if (isPrimitive(typeRes.javaType)) {
                    sb.append("      this.").append(field.getName()).append(" = other.").append(field.getName()).append(";\n");
                } else {
                    sb.append("      if (other.isSet").append(capitalize(field.getName())).append("()) {\n");
                    sb.append("        this.").append(field.getName()).append(" = other.").append(field.getName()).append(";\n");
                    sb.append("      }\n");
                }
            }
            sb.append("    }\n\n");
            sb.append("    @Override\n");
            sb.append("    public ").append(structName).append(" deepCopy() {\n");
            sb.append("      return new ").append(structName).append("(this);\n");
            sb.append("    }\n\n");
            sb.append("    @Override\n");
            sb.append("    public void clear() {\n");
            for (FieldNode field : fields) {
                JavaTypeResolution typeRes = fieldResolutions.get(field);
                if (isPrimitive(typeRes.javaType)) {
                    sb.append("      set").append(capitalize(field.getName())).append("IsSet(false);\n");
                    if (typeRes.javaType.equals("boolean")) {
                        sb.append("      this.").append(field.getName()).append(" = false;\n");
                    } else if (typeRes.javaType.equals("byte") || typeRes.javaType.equals("short") || 
                              typeRes.javaType.equals("int") || typeRes.javaType.equals("long")) {
                        sb.append("      this.").append(field.getName()).append(" = 0;\n");
                    } else if (typeRes.javaType.equals("double")) {
                        sb.append("      this.").append(field.getName()).append(" = 0.0;\n");
                    }
                } else {
                    sb.append("      this.").append(field.getName()).append(" = null;\n");
                }
            }
            sb.append("    }\n\n");
        }
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field); String capFieldName = capitalize(field.getName());
            if (!isPrimitive(typeRes.javaType)) {
                sb.append("    @org.apache.thrift.annotation.Nullable\n");
            }
            sb.append("    public ").append(typeRes.javaType).append(" get").append(capFieldName).append("() {\n");
            sb.append("      return this.").append(field.getName()).append(";\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public ").append(structName).append(" set").append(capFieldName).append("(");
            if (!isPrimitive(typeRes.javaType)) {
                sb.append("@org.apache.thrift.annotation.Nullable ");
            }
            sb.append(typeRes.javaType).append(" ").append(field.getName()).append(") {\n");
            sb.append("      this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n");
            if (isPrimitive(typeRes.javaType)) sb.append("      set").append(capFieldName).append("IsSet(true);\n");
            sb.append("      return this;\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public void unset").append(capFieldName).append("() {\n");
            if (isPrimitive(typeRes.javaType)) {
                sb.append("      __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __").append(field.getName().toUpperCase(Locale.ROOT)).append("_ISSET_ID);\n");
            } else {
                sb.append("      this.").append(field.getName()).append(" = null;\n");
            }
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    /** Returns true if field ").append(field.getName()).append(" is set (has been assigned a value) and false otherwise */\n");
            sb.append("    public boolean isSet").append(capFieldName).append("() {\n");
            if (isPrimitive(typeRes.javaType)) {
                sb.append("      return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __").append(field.getName().toUpperCase(Locale.ROOT)).append("_ISSET_ID);\n");
            } else {
                sb.append("      return this.").append(field.getName()).append(" != null;\n");
            }
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public void set").append(capFieldName).append("IsSet(boolean value) {\n");
            if (isPrimitive(typeRes.javaType)) {
                sb.append("      __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __").append(field.getName().toUpperCase(Locale.ROOT)).append("_ISSET_ID, value);\n");
            } else {
                sb.append("      if (!value) {\n");
                sb.append("        this.").append(field.getName()).append(" = null;\n");
                sb.append("      }\n");
            }
            sb.append("    }\n\n");
        }

        sb.append("");
        sb.append("    @Override\n");
        sb.append("    public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {\n");
        sb.append("      switch (field) {\n");
        for (FieldNode field : fields) {
            JavaTypeResolution typeRes = fieldResolutions.get(field);
            String capFieldName = capitalize(field.getName());
            sb.append("      case ").append(toAllCapsUnderscore(field.getName())).append(":\n");
            sb.append("        if (value == null) {\n");
            sb.append("          unset").append(capFieldName).append("();\n");
            sb.append("        } else {\n");
            if (isPrimitive(typeRes.javaType)) {
                if (typeRes.javaType.equals("boolean")) {
                    sb.append("          set").append(capFieldName).append("((java.lang.Boolean)value);\n");
                } else if (typeRes.javaType.equals("byte")) {
                    sb.append("          set").append(capFieldName).append("((java.lang.Byte)value);\n");
                } else if (typeRes.javaType.equals("short")) {
                    sb.append("          set").append(capFieldName).append("((java.lang.Short)value);\n");
                } else if (typeRes.javaType.equals("int")) {
                    sb.append("          set").append(capFieldName).append("((java.lang.Integer)value);\n");
                } else if (typeRes.javaType.equals("long")) {
                    sb.append("          set").append(capFieldName).append("((java.lang.Long)value);\n");
                } else if (typeRes.javaType.equals("double")) {
                    sb.append("          set").append(capFieldName).append("((java.lang.Double)value);\n");
                }
            } else {
                sb.append("          set").append(capFieldName).append("((").append(typeRes.javaType).append(")value);\n");
            }
            sb.append("        }\n");
            sb.append("        break;\n");
            sb.append("\n");
        }
        sb.append("      }\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @org.apache.thrift.annotation.Nullable\n");
        sb.append("    @Override\n");
        sb.append("    public java.lang.Object getFieldValue(_Fields field) {\n");
        sb.append("      switch (field) {\n");
        for (FieldNode field : fields) {
            String capFieldName = capitalize(field.getName());
            sb.append("      case ").append(toAllCapsUnderscore(field.getName())).append(":\n");
            sb.append("        return get").append(capFieldName).append("();\n");
            sb.append("\n");
        }
        sb.append("      }\n");
        sb.append("      throw new java.lang.IllegalStateException();\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */\n");
        sb.append("    @Override\n");
        sb.append("    public boolean isSet(_Fields field) {\n");
        sb.append("      if (field == null) {\n");
        sb.append("        throw new java.lang.IllegalArgumentException();\n");
        sb.append("      }\n");
        sb.append("\n");
        sb.append("      switch (field) {\n");
        for (FieldNode field : fields) {
            String capFieldName = capitalize(field.getName());
            sb.append("      case ").append(toAllCapsUnderscore(field.getName())).append(":\n");
            sb.append("        return isSet").append(capFieldName).append("();\n");
        }
        sb.append("      }\n");
        sb.append("      throw new java.lang.IllegalStateException();\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public boolean equals(java.lang.Object that) {\n");
        sb.append("      if (that instanceof ").append(structName).append(")\n");
        sb.append("        return this.equals((").append(structName).append(")that);\n");
        sb.append("      return false;\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    public boolean equals(").append(structName).append(" that) {\n");
        sb.append("      if (that == null)\n");
        sb.append("        return false;\n");
        sb.append("      if (this == that)\n");
        sb.append("        return true;\n");
        sb.append("      /* TODO */\n");
        sb.append("      return true;\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public int hashCode() {\n");
        sb.append("      /* TODO */\n");
        sb.append("      return 0;\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @org.apache.thrift.annotation.Nullable\n");
        sb.append("    @Override\n");
        sb.append("    public _Fields fieldForId(int fieldId) {\n");
        sb.append("      return _Fields.findByThriftId(fieldId);\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {\n");
        sb.append("      TProtocolUtil.skip(iprot, TType.STRUCT); /* TODO: Implement full read for ").append(structName).append(" */\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {\n");
        sb.append("      oprot.writeStructBegin(STRUCT_DESC);\n");
        sb.append("      /* TODO: Implement full write for ").append(structName).append(" */\n");
        sb.append("      oprot.writeFieldStop();\n");
        sb.append("      oprot.writeStructEnd();\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public String toString() {\n");
        sb.append("      return \"").append(structName).append("(...)\";\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    public void validate() throws TException {\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public int compareTo(").append(structName).append(" other) {\n");
        sb.append("      if (!getClass().equals(other.getClass())) {\n");
        sb.append("        return getClass().getName().compareTo(other.getClass().getName());\n");
        sb.append("      }\n");
        sb.append("      /* TODO */\n");
        sb.append("      return 0;\n");
        sb.append("    }\n");
        sb.append("\n");

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

    private String getTTypeConstant(byte thriftType) {
        switch (thriftType) {
            case TType.BOOL: return "BOOL";
            case TType.BYTE: return "BYTE";
            case TType.I16: return "I16";
            case TType.I32: return "I32";
            case TType.I64: return "I64";
            case TType.DOUBLE: return "DOUBLE";
            case TType.STRING: return "STRING";
            case TType.STRUCT: return "STRUCT";
            case TType.MAP: return "MAP";
            case TType.SET: return "SET";
            case TType.LIST: return "LIST";
            case TType.ENUM: return "ENUM";
            case TType.VOID: return "VOID";
            default: return String.valueOf(thriftType);
        }
    }
}
