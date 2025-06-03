package com.github.decster.gen;

import com.github.decster.ast.*; // Assuming these interfaces/classes exist
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

// --- Stub AST Node Implementations (copied/adapted from previous tests for self-containment) ---

interface Node { String getDocString(); }
interface TypeNode extends Node {}

enum BaseType { BOOL, BYTE, I16, I32, I64, DOUBLE, STRING, BINARY, VOID }

class StubBaseTypeNode implements BaseTypeNode {
    private BaseType type;
    public StubBaseTypeNode(BaseType type) { this.type = type; }
    public BaseType getType() { return type; }
    @Override public String getDocString() { return null; }
}

class StubIdentifierTypeNode implements IdentifierTypeNode {
    private String name;
    private boolean isEnum;
    public StubIdentifierTypeNode(String name) { this.name = name; this.isEnum = false; }
    public StubIdentifierTypeNode(String name, boolean isEnum) { this.name = name; this.isEnum = isEnum; }
    @Override public String getName() { return name; }
    @Override public boolean isEnum() { return isEnum; } // Assumed by ServiceGenerator
    @Override public String getDocString() { return null; }
}

class StubListTypeNode implements ListTypeNode {
    private TypeNode elementType;
    public StubListTypeNode(TypeNode elementType) { this.elementType = elementType; }
    @Override public TypeNode getElementType() { return elementType; }
    @Override public String getDocString() { return null; }
}

class StubSetTypeNode implements SetTypeNode {
    private TypeNode elementType;
    public StubSetTypeNode(TypeNode elementType) { this.elementType = elementType; }
    @Override public TypeNode getElementType() { return elementType; }
    @Override public String getDocString() { return null; }
}

class StubMapTypeNode implements MapTypeNode {
    private TypeNode keyType;
    private TypeNode valueType;
    public StubMapTypeNode(TypeNode keyType, TypeNode valueType) { this.keyType = keyType; this.valueType = valueType; }
    @Override public TypeNode getKeyType() { return keyType; }
    @Override public TypeNode getValueType() { return valueType; }
    @Override public String getDocString() { return null; }
}

class StubFieldNode implements FieldNode {
    private short id;
    private String name;
    private TypeNode type;
    private Requirement requirement = Requirement.DEFAULT;
    private String docString;

    public StubFieldNode(short id, String name, TypeNode type) { this.id = id; this.name = name; this.type = type; }
    @Override public short getId() { return id; }
    @Override public String getName() { return name; }
    @Override public TypeNode getType() { return type; }
    @Override public Requirement getRequirement() { return requirement; }
    public void setRequirement(Requirement req) { this.requirement = req; }
    @Override public String getDocString() { return docString; }
    public void setDocString(String doc) { this.docString = doc; }
}

class StubFunctionNode implements FunctionNode {
    private String name;
    private TypeNode returnType;
    private boolean isOneway;
    private List<FieldNode> arguments = new ArrayList<>();
    private List<FieldNode> exceptions = new ArrayList<>();
    private String docString;

    public StubFunctionNode(String name, TypeNode returnType, boolean isOneway) {
        this.name = name;
        this.returnType = returnType;
        this.isOneway = isOneway;
    }
    @Override public String getName() { return name; }
    @Override public TypeNode getReturnType() { return returnType; }
    @Override public boolean isOneway() { return isOneway; }
    @Override public List<FieldNode> getArguments() { return arguments; }
    public void addArgument(FieldNode arg) { this.arguments.add(arg); }
    @Override public List<FieldNode> getExceptions() { return exceptions; }
    public void addException(FieldNode ex) { this.exceptions.add(ex); }
    @Override public String getDocString() { return docString; }
    public void setDocString(String doc) { this.docString = doc; }
}

class StubServiceNode implements ServiceNode {
    private String name;
    private IdentifierTypeNode extendsService;
    private List<FunctionNode> functions = new ArrayList<>();
    private String docString;

    public StubServiceNode(String name) { this.name = name; }
    @Override public String getName() { return name; }
    @Override public IdentifierTypeNode getExtendsService() { return extendsService; }
    public void setExtendsService(IdentifierTypeNode ext) { this.extendsService = ext; }
    @Override public List<FunctionNode> getFunctions() { return functions; }
    public void addFunction(FunctionNode func) { this.functions.add(func); }
    @Override public String getDocString() { return docString; }
    public void setDocString(String doc) { this.docString = doc; }
}


public class ServiceGeneratorTest {

    private ServiceGenerator generator;
    private StubServiceNode serviceNode;
    private static final String TEST_PACKAGE = "com.test.services";
    private static final String TEST_DATE = "2023-10-27";

    private void assertContains(String generatedCode, String expectedSnippet) {
        assertTrue(generatedCode.contains(expectedSnippet),
                "Generated code missing: '" + expectedSnippet + "'\n------ Code Snippet (approx 200 chars around missing part) -----\n" +调试上下文(generatedCode, expectedSnippet, 200) + "\n------ Full Code -----\n" + 간단히하기(generatedCode, 2000) + "\n--------------------");
    }

    private String 간단히하기(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength/2) + "\n...\n[TRUNCATED]\n...\n" + text.substring(text.length() - maxLength/2);
    }

    private String 调试上下文(String text, String snippet, int window) {
        int idx = text.indexOf(snippet);
        if (idx == -1) return "[Snippet not found for context]";
        int start = Math.max(0, idx - window/2);
        int end = Math.min(text.length(), idx + snippet.length() + window/2);
        return text.substring(start, end);
    }


    @BeforeEach
    void setUp() {
        // Default setup, can be overridden by tests
    }

    @Test
    void testSimpleService() {
        serviceNode = new StubServiceNode("Calculator");

        StubFunctionNode pingFunc = new StubFunctionNode("ping", new StubBaseTypeNode(BaseType.VOID), false);
        pingFunc.setDocString("A simple ping function.");
        serviceNode.addFunction(pingFunc);

        StubFunctionNode addFunc = new StubFunctionNode("add", new StubBaseTypeNode(BaseType.I32), false);
        addFunc.addArgument(new StubFieldNode((short)1, "num1", new StubBaseTypeNode(BaseType.I32)));
        addFunc.addArgument(new StubFieldNode((short)2, "num2", new StubBaseTypeNode(BaseType.I32)));
        serviceNode.addFunction(addFunc);

        generator = new ServiceGenerator(serviceNode, TEST_PACKAGE, TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public class Calculator {");
        assertContains(code, "package " + TEST_PACKAGE + ";");
        assertContains(code, "@javax.annotation.Generated(value = \"Autogenerated by Thrift Compiler (0.20.0)\", date = \"" + TEST_DATE + "\")");

        // Iface
        assertContains(code, "public interface Iface {");
        assertContains(code, "    /**\n     * A simple ping function.\n     */");
        assertContains(code, "public void ping() throws org.apache.thrift.TException;");
        assertContains(code, "public int add(int num1, int num2) throws org.apache.thrift.TException;");

        // AsyncIface
        assertContains(code, "public interface AsyncIface {");
        assertContains(code, "public void ping(org.apache.thrift.async.AsyncMethodCallback<java.lang.Void> resultHandler) throws org.apache.thrift.TException;");
        assertContains(code, "public void add(int num1, int num2, org.apache.thrift.async.AsyncMethodCallback<java.lang.Integer> resultHandler) throws org.apache.thrift.TException;");

        // Client
        assertContains(code, "public static class Client extends org.apache.thrift.TServiceClient implements Iface {");
        assertContains(code, "public void send_ping() throws org.apache.thrift.TException {");
        assertContains(code, "sendBase(\"ping\", args, TMessageType.CALL);");
        assertContains(code, "public void recv_ping() throws org.apache.thrift.TException {");
        assertContains(code, "public int add(int num1, int num2) throws org.apache.thrift.TException {");
        assertContains(code, "send_add(num1, num2);");
        assertContains(code, "return recv_add();");

        // AsyncClient
        assertContains(code, "public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {");
        assertContains(code, "public static class ping_call extends org.apache.thrift.async.TAsyncMethodCall<java.lang.Void> {");
        assertContains(code, "public static class add_call extends org.apache.thrift.async.TAsyncMethodCall<java.lang.Integer> {");

        // Processor
        assertContains(code, "public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor implements org.apache.thrift.TProcessor {");
        assertContains(code, "processMap.put(\"ping\", new ping());");
        assertContains(code, "processMap.put(\"add\", new add());");
        assertContains(code, "public static class ping<I extends Iface> extends org.apache.thrift.ProcessFunction<I, ping_args> {");
        assertContains(code, "public static class add<I extends Iface> extends org.apache.thrift.ProcessFunction<I, add_args> {");

        // Args and Results
        assertContains(code, "public static class ping_args implements TBase<ping_args, ping_args._Fields>");
        assertContains(code, "public static class ping_result implements TBase<ping_result, ping_result._Fields>");
        assertContains(code, "public static class add_args implements TBase<add_args, add_args._Fields>");
        assertContains(code, "public int num1;");
        assertContains(code, "public int num2;");
        assertContains(code, "public static class add_result implements TBase<add_result, add_result._Fields>");
        assertContains(code, "public int success;");

        // Imports
        assertContains(code, "import java.util.List;");
        assertContains(code, "import org.apache.thrift.async.AsyncMethodCallback;");
    }

    @Test
    void testServiceWithExceptions() {
        serviceNode = new StubServiceNode("ErrService");
        StubFunctionNode errFunc = new StubFunctionNode("doOrThrow", new StubBaseTypeNode(BaseType.STRING), false);
        errFunc.addArgument(new StubFieldNode((short)1, "input", new StubBaseTypeNode(BaseType.STRING)));
        // Assume MyException is a struct defined elsewhere
        errFunc.addException(new StubFieldNode((short)1, "ex1", new StubIdentifierTypeNode("com.test.exceptions.MyException")));
        serviceNode.addFunction(errFunc);

        generator = new ServiceGenerator(serviceNode, "com.test.svc", "2023-01-01");
        String code = generator.generate();

        assertContains(code, "public interface Iface {");
        assertContains(code, "public java.lang.String doOrThrow(java.lang.String input) throws com.test.exceptions.MyException, org.apache.thrift.TException;");

        assertContains(code, "public interface AsyncIface {");
        assertContains(code, "public void doOrThrow(java.lang.String input, org.apache.thrift.async.AsyncMethodCallback<java.lang.String> resultHandler) throws org.apache.thrift.TException;");

        assertContains(code, "public java.lang.String recv_doOrThrow() throws com.test.exceptions.MyException, org.apache.thrift.TException {");
        assertContains(code, "if (result.ex1 != null) {");
        assertContains(code, "throw result.ex1;");

        assertContains(code, "public static class doOrThrow_result implements TBase<doOrThrow_result, doOrThrow_result._Fields>");
        assertContains(code, "public java.lang.String success;");
        assertContains(code, "public com.test.exceptions.MyException ex1;");
        assertContains(code, "tmpMap.put(_Fields.EX1, new FieldMetaData(\"ex1\", TFieldRequirementType.OPTIONAL, new StructMetaData(TType.STRUCT, com.test.exceptions.MyException.class)));");
        assertContains(code, "import com.test.exceptions.MyException;");
    }

    @Test
    void testServiceExtends() {
        serviceNode = new StubServiceNode("ChildService");
        serviceNode.setExtendsService(new StubIdentifierTypeNode("com.test.parent.ParentService"));
        StubFunctionNode childFunc = new StubFunctionNode("childAction", new StubBaseTypeNode(BaseType.BOOL), false);
        serviceNode.addFunction(childFunc);

        generator = new ServiceGenerator(serviceNode, TEST_PACKAGE, TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public interface Iface extends com.test.parent.ParentService.Iface {");
        assertContains(code, "public interface AsyncIface extends com.test.parent.ParentService.AsyncIface {");
        assertContains(code, "public static class Client extends com.test.parent.ParentService.Client implements Iface {");
        assertContains(code, "public static class AsyncClient extends com.test.parent.ParentService.AsyncClient implements AsyncIface {");
        assertContains(code, "public static class Processor<I extends Iface> extends com.test.parent.ParentService.Processor implements org.apache.thrift.TProcessor {");
        assertContains(code, "super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends TBase>>()));"); // Processor constructor
        assertContains(code, "import com.test.parent.ParentService;");
    }

    @Test
    void testServiceWithVariousTypes() {
        serviceNode = new StubServiceNode("ComplexTypeService");
        StubFunctionNode func = new StubFunctionNode("processComplex", new StubMapTypeNode(new StubBaseTypeNode(BaseType.STRING), new StubIdentifierTypeNode("com.custom.MyStruct")), false);
        func.addArgument(new StubFieldNode((short)1, "pListOfInts", new StubListTypeNode(new StubBaseTypeNode(BaseType.I32))));
        func.addArgument(new StubFieldNode((short)2, "pSetOfStrings", new StubSetTypeNode(new StubBaseTypeNode(BaseType.STRING))));
        func.addArgument(new StubFieldNode((short)3, "pMyEnum", new StubIdentifierTypeNode("com.custom.MyEnum", true)));
        serviceNode.addFunction(func);

        generator = new ServiceGenerator(serviceNode, TEST_PACKAGE, TEST_DATE);
        String code = generator.generate();

        // Iface
        assertContains(code, "public java.util.Map<java.lang.String, com.custom.MyStruct> processComplex(java.util.List<java.lang.Integer> pListOfInts, java.util.Set<java.lang.String> pSetOfStrings, com.custom.MyEnum pMyEnum) throws org.apache.thrift.TException;");
        // AsyncIface
        assertContains(code, "public void processComplex(java.util.List<java.lang.Integer> pListOfInts, java.util.Set<java.lang.String> pSetOfStrings, com.custom.MyEnum pMyEnum, org.apache.thrift.async.AsyncMethodCallback<java.util.Map<java.lang.String, com.custom.MyStruct>> resultHandler) throws org.apache.thrift.TException;");

        // Args struct fields
        assertContains(code, "public static class processComplex_args implements TBase<processComplex_args, processComplex_args._Fields>");
        assertContains(code, "public java.util.List<java.lang.Integer> pListOfInts;");
        assertContains(code, "public java.util.Set<java.lang.String> pSetOfStrings;");
        assertContains(code, "public com.custom.MyEnum pMyEnum;");

        // Args MetaData
        assertContains(code, "new ListMetaData(TType.LIST, new FieldValueMetaData(TType.I32))");
        assertContains(code, "new SetMetaData(TType.SET, new FieldValueMetaData(TType.STRING))");
        assertContains(code, "new EnumMetaData(TType.ENUM, com.custom.MyEnum.class)");

        // Result struct field
        assertContains(code, "public static class processComplex_result implements TBase<processComplex_result, processComplex_result._Fields>");
        assertContains(code, "public java.util.Map<java.lang.String, com.custom.MyStruct> success;");
        // Result MetaData
        assertContains(code, "new MapMetaData(TType.MAP, new FieldValueMetaData(TType.STRING), new StructMetaData(TType.STRUCT, com.custom.MyStruct.class))");

        assertContains(code, "import com.custom.MyStruct;");
        assertContains(code, "import com.custom.MyEnum;");
        assertContains(code, "import java.util.Set;"); // From pSetOfStrings
    }

    @Test
    void testOnewayFunction() {
        serviceNode = new StubServiceNode("OnewayTestService");
        StubFunctionNode onewayFunc = new StubFunctionNode("logMessage", new StubBaseTypeNode(BaseType.VOID), true); // oneway = true
        onewayFunc.addArgument(new StubFieldNode((short)1, "message", new StubBaseTypeNode(BaseType.STRING)));
        serviceNode.addFunction(onewayFunc);

        generator = new ServiceGenerator(serviceNode, TEST_PACKAGE, TEST_DATE);
        String code = generator.generate();

        // Iface: oneway still has TException because client send can fail
        assertContains(code, "public void logMessage(java.lang.String message) throws org.apache.thrift.TException;");
        // AsyncIface: still has TException
        assertContains(code, "public void logMessage(java.lang.String message, org.apache.thrift.async.AsyncMethodCallback<java.lang.Void> resultHandler) throws org.apache.thrift.TException;");

        // Client: No recv_logMessage() call
        assertContains(code, "public void logMessage(java.lang.String message) throws org.apache.thrift.TException {\n" +
                                 "      send_logMessage(message);\n" +
                                 "    }");
        assertNotContains(code, "recv_logMessage();");

        // AsyncClient _call class for oneway
        assertContains(code, "public static class logMessage_call extends org.apache.thrift.async.TAsyncMethodCall<java.lang.Void> {");
        assertContains(code, "super(client, protocolFactory, transport, resultHandler, true);"); // last arg isOneway = true

        // Processor: isOneway() should be true
        assertContains(code, "public static class logMessage<I extends Iface> extends org.apache.thrift.ProcessFunction<I, logMessage_args> {");
        assertContains(code, "protected boolean isOneway() {\n        return true;\n      }");
        // Processor: getResult should not attempt to write for oneway if there was an application error (though this part is tricky)
        // For now, we primarily check the isOneway flag.
    }
}
