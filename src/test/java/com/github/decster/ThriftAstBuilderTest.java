package com.github.decster;

import com.github.decster.ast.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ThriftAstBuilderTest {

    /**
     * Tests the `buildFromString` method of the `ThriftAstBuilder` class.
     * <p>
     * This method generates a `Document` object by parsing the provided Thrift
     * content string.
     */

    @Test
    void testBuildFromString_ValidInput() throws IOException {
        // Arrange
        String thriftCode = """
                namespace java com.example
                struct User {
                  1: required string name;
                  2: optional i32 age;
                }""";

        // Act
        DocumentNode documentNode = ThriftAstBuilder.buildFromString(thriftCode);

        // Assert
        assertNotNull(documentNode);
        assertEquals(1, documentNode.getHeaders().size());
        assertEquals(1, documentNode.getDefinitions().size());
    }

    @Test
    void testBuildFromString_EmptyInput() throws IOException {
        // Arrange
        String content = "";

        // Act
        DocumentNode documentNode = ThriftAstBuilder.buildFromString(content);

        // Assert
        assertNotNull(documentNode);
        assertTrue(documentNode.getHeaders().isEmpty());
        assertTrue(documentNode.getDefinitions().isEmpty());
    }

    @Test
    void testBuildFromString_InvalidSyntax() {
        // Arrange
        // Use clearly invalid syntax that will force an IOException
        // Changed to reduce ANTLR console noise while still being invalid.
        String invalidContent = "struct Test { 1: required string name"; // Missing closing brace

        // Act and Assert
        // We expect an IOException (or a subclass like ThriftParseException if defined)
        // due to the ANTLR parser encountering a syntax error and the builder wrapping it.
        assertThrows(IOException.class, () -> ThriftAstBuilder.buildFromString(invalidContent));
    }

    @Test
    void testBuildFromString_MultipleNamespaces() throws IOException {
        // Arrange
        String thriftCode = """
                namespace java com.example
                namespace py com.example_py""";

        // Act
        DocumentNode documentNode = ThriftAstBuilder.buildFromString(thriftCode);

        // Assert
        assertNotNull(documentNode);
        assertEquals(2, documentNode.getHeaders().size());
    }

    @Test
    void testBuildFromString_MultipleDefinitions() throws IOException {
        // Arrange
        String thriftCode = """
                struct User {
                  1: required string name;
                  2: optional i32 age;
                }
                struct Product {
                  1: required string id;
                  2: optional string description;
                }""";

        // Act
        DocumentNode documentNode = ThriftAstBuilder.buildFromString(thriftCode);

        // Assert
        assertNotNull(documentNode);
        assertEquals(2, documentNode.getDefinitions().size());
    }

    @Test
    void testParsingThriftTestFile() throws IOException {
        // Load the complex1.thrift file from resources
        URL url = getClass().getClassLoader().getResource("multi_file_tests/complex1.thrift");
        assertNotNull(url, "Test file not found in resources");

        Path filePath = Paths.get(url.getPath());
        String content = Files.readString(filePath);

        // Parse the file using ThriftAstBuilder
        DocumentNode documentNode = ThriftAstBuilder.buildFromString(content);

        // Verify document is not null
        assertNotNull(documentNode, "Document should not be null");

        // Verify namespaces
        List<HeaderNode> namespaces = documentNode.getHeaders().stream()
                .filter(h -> h instanceof NamespaceNode)
                .toList();
        assertFalse(namespaces.isEmpty(), "Namespaces should be present");

        // Verify specific namespaces
        Optional<NamespaceNode> javaNs = namespaces.stream()
                .filter(h -> h instanceof NamespaceNode)
                .map(h -> (NamespaceNode) h)
                .filter(n -> "java".equals(n.getScope()))
                .findFirst();
        assertTrue(javaNs.isPresent(), "Java namespace should be present");
        assertEquals("thrift.test", javaNs.get().getName(), "Java namespace should match");

        // Verify enum definition
        Map<String, DefinitionNode> definitions = documentNode.getDefinitions().stream()
                .collect(Collectors.toMap(DefinitionNode::getName, d -> d));

        // Verify Numberz enum
        assertTrue(definitions.containsKey("Numberz"), "Enum Numberz should exist");
        assertInstanceOf(EnumNode.class, definitions.get("Numberz"), "Numberz should be an enum");
        EnumNode numberz = (EnumNode) definitions.get("Numberz");
        assertEquals(6, numberz.getValues().size(), "Numberz should have 6 items");
        assertEquals(1, numberz.getValues().get(0).getValue(), "ONE should have value 1");
        assertEquals("ONE", numberz.getValues().get(0).getName(), "First enum value should be ONE");

        // Verify constant
        Optional<ConstNode> myNumberz = documentNode.getDefinitions().stream()
                .filter(d -> d instanceof ConstNode)
                .map(d -> (ConstNode) d)
                .filter(c -> "myNumberz".equals(c.getName()))
                .findFirst();
        assertTrue(myNumberz.isPresent(), "Constant myNumberz should be present");

        // Verify typedefs
        Optional<TypedefNode> userId = documentNode.getDefinitions().stream()
                .filter(d -> d instanceof TypedefNode)
                .map(d -> (TypedefNode) d)
                .filter(t -> "UserId".equals(t.getName()))
                .findFirst();
        assertTrue(userId.isPresent(), "Typedef UserId should be present");
        // Use a Type reference method that exists in the API
        TypeNode userIdType = userId.get().getType();
        assertEquals("i64", ((BaseTypeNode) userIdType).getName(), "UserId should be typedef of i64");

        // Verify structs
        assertTrue(definitions.containsKey("Bonk"), "Struct Bonk should exist");
        assertInstanceOf(StructNode.class, definitions.get("Bonk"), "Bonk should be a struct");
        StructNode bonk = (StructNode) definitions.get("Bonk");
        assertEquals(2, bonk.getFields().size(), "Bonk should have 2 fields");

        // Verify complex struct (CrazyNesting)
        assertTrue(definitions.containsKey("CrazyNesting"), "Struct CrazyNesting should exist");
        StructNode crazyNesting = (StructNode) definitions.get("CrazyNesting");
        assertEquals(5, crazyNesting.getFields().size(), "CrazyNesting should have 5 fields");
        // Verify the uuid field is present
        Optional<FieldNode> uuidField = crazyNesting.getFields().stream()
                .filter(f -> f.getId() == 5)
                .findFirst();
        assertTrue(uuidField.isPresent(), "CrazyNesting should have a uuid field");
        assertEquals("uuid", ((IdentifierTypeNode)uuidField.get().getType()).getName(), "Field 5 should be uuid type");

        // Verify union
        assertTrue(definitions.containsKey("SomeUnion"), "Union SomeUnion should exist");
        assertInstanceOf(UnionNode.class, definitions.get("SomeUnion"), "SomeUnion should be a union");
        UnionNode someUnion = (UnionNode) definitions.get("SomeUnion");
        assertEquals(5, someUnion.getFields().size(), "SomeUnion should have 5 fields");

        // Verify exceptions
        assertTrue(definitions.containsKey("Xception"), "Exception Xception should exist");
        assertInstanceOf(ExceptionNode.class, definitions.get("Xception"), "Xception should be an exception");
        ExceptionNode xception = (ExceptionNode) definitions.get("Xception");
        assertEquals(2, xception.getFields().size(), "Xception should have 2 fields");

        // Verify services
        assertTrue(definitions.containsKey("ThriftTest"), "Service ThriftTest should exist");
        assertInstanceOf(ServiceNode.class, definitions.get("ThriftTest"), "ThriftTest should be a service");
        ServiceNode thriftTest = (ServiceNode) definitions.get("ThriftTest");
        assertFalse(thriftTest.getFunctions().isEmpty(), "ThriftTest should have functions");

        // Verify SecondService exists
        assertTrue(definitions.containsKey("SecondService"), "Service SecondService should exist");
        assertInstanceOf(ServiceNode.class, definitions.get("SecondService"), "SecondService should be a service");
        ServiceNode secondService = (ServiceNode) definitions.get("SecondService");
        // Assuming SecondService extends ThriftTest, a common pattern in test files
        assertEquals("ThriftTest", secondService.getExtendsService(), "SecondService should extend ThriftTest");
        assertTrue(secondService.getFunctions().stream().anyMatch(f -> "secondtestString".equals(f.getName())),
                "SecondService should have function secondtestString");

        // Detailed checks for some functions in ThriftTest service
        // testVoid()
        Optional<FunctionNode> testVoidFuncOpt = thriftTest.getFunctions().stream()
                .filter(f -> "testVoid".equals(f.getName())).findFirst();
        assertTrue(testVoidFuncOpt.isPresent(), "Function testVoid should exist in ThriftTest");
        FunctionNode testVoidFunc = testVoidFuncOpt.get();
        assertNull(testVoidFunc.getReturnType(), "testVoid return type should be void (represented as null)");
        assertTrue(testVoidFunc.getParameters().isEmpty(), "testVoid should have no parameters");
        assertEquals(FunctionNode.Oneway.SYNC, testVoidFunc.getOneway(), "testVoid should be SYNC by default");

        // testString(1: string thing)
        Optional<FunctionNode> testStringFuncOpt = thriftTest.getFunctions().stream()
                .filter(f -> "testString".equals(f.getName())).findFirst();
        assertTrue(testStringFuncOpt.isPresent(), "Function testString should exist in ThriftTest");
        FunctionNode testStringFunc = testStringFuncOpt.get();
        assertNotNull(testStringFunc.getReturnType(), "testString return type should not be null");
        assertEquals("string", testStringFunc.getReturnType().getName(), "testString return type should be string");
        assertEquals(1, testStringFunc.getParameters().size(), "testString should have 1 parameter");
        FieldNode tsParam = testStringFunc.getParameters().get(0);
        assertEquals("thing", tsParam.getName(), "testString parameter name should be 'thing'");
        assertEquals(1, tsParam.getId(), "testString parameter ID should be 1");
        assertEquals("string", tsParam.getType().getName(), "testString parameter type should be string");

        // testOneway(1: i32 secondsToSleep)
        Optional<FunctionNode> testOnewayFuncOpt = thriftTest.getFunctions().stream()
                .filter(f -> "testOneway".equals(f.getName())).findFirst();
        assertTrue(testOnewayFuncOpt.isPresent(), "Function testOneway should exist in ThriftTest");
        FunctionNode testOnewayFunc = testOnewayFuncOpt.get();
        assertNull(testOnewayFunc.getReturnType(), "testOneway return type should be void (null)");
        assertEquals(FunctionNode.Oneway.ONEWAY, testOnewayFunc.getOneway(), "testOneway should be ONEWAY");
        assertEquals(1, testOnewayFunc.getParameters().size(), "testOneway should have 1 parameter");
        assertEquals("i32", testOnewayFunc.getParameters().get(0).getType().getName(),
                "testOneway parameter type should be i32");

        // testMap(1: map<i32,i32> thing)
        Optional<FunctionNode> testMapFuncOpt = thriftTest.getFunctions().stream()
                .filter(f -> "testMap".equals(f.getName())).findFirst();
        assertTrue(testMapFuncOpt.isPresent(), "Function testMap should exist in ThriftTest");
        FunctionNode testMapFunc = testMapFuncOpt.get();
        assertNotNull(testMapFunc.getReturnType());
        assertEquals("map<i32,i32>", testMapFunc.getReturnType().getName(), "testMap return type should be map<i32,i32>");
        assertEquals(1, testMapFunc.getParameters().size());
        assertEquals("map<i32,i32>", testMapFunc.getParameters().get(0).getType().getName(),
                "testMap parameter type should be map<i32,i32>");

        // testStruct(1: Xtruct thing) - Assuming Xtruct is a defined struct
        Optional<FunctionNode> testStructFuncOpt = thriftTest.getFunctions().stream()
                .filter(f -> "testStruct".equals(f.getName())).findFirst();
        assertTrue(testStructFuncOpt.isPresent(), "Function testStruct should exist in ThriftTest");
        FunctionNode testStructFunc = testStructFuncOpt.get();
        assertNotNull(testStructFunc.getReturnType());
        assertEquals("Xtruct", testStructFunc.getReturnType().getName(), "testStruct return type should be Xtruct");
        assertEquals(1, testStructFunc.getParameters().size());
        assertEquals("Xtruct", testStructFunc.getParameters().get(0).getType().getName(),
                "testStruct parameter type should be Xtruct");

        // Verify fields with various types in a struct (e.g., Xtruct)
        // struct Xtruct { 1: string string_thing, 2: byte byte_thing, 3: i32 i32_thing, 4: i64 i64_thing }
        assertTrue(definitions.containsKey("Xtruct"), "Struct Xtruct should exist");
        assertInstanceOf(StructNode.class, definitions.get("Xtruct"), "Xtruct should be a struct");
        StructNode xtruct = (StructNode) definitions.get("Xtruct");

        Optional<FieldNode> stringField = xtruct.getFields().stream().filter(f -> "string_thing".equals(f.getName())).findFirst();
        assertTrue(stringField.isPresent(), "Xtruct.string_thing field should exist");
        assertEquals("string", stringField.get().getType().getName(), "Xtruct.string_thing type should be string");

        Optional<FieldNode> byteField = xtruct.getFields().stream().filter(f -> "byte_thing".equals(f.getName())).findFirst();
        assertTrue(byteField.isPresent(), "Xtruct.byte_thing field should exist");
        assertEquals("i8", byteField.get().getType().getName(), "Xtruct.byte_thing type should be byte");
    }
}
