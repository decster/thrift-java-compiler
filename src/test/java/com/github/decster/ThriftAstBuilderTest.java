package com.github.decster;

import static org.junit.jupiter.api.Assertions.*;

import com.github.decster.ast.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThriftAstBuilderTest {

  private static final String TEST_FILE_PATH = "src/test/resources/multi_file_tests/complex1.thrift";

  @Test
  @DisplayName("Test parsing complex1.thrift file")
  void testParseComplex1File() throws IOException {
    // Get the absolute path to the test file
    String absolutePath = Paths.get(TEST_FILE_PATH).toAbsolutePath().toString();

    // Parse the file
    TProgram program = ThriftAstBuilder.parseFile(absolutePath);

    // Verify program basics
    assertNotNull(program, "Parsed program should not be null");
    assertEquals("complex1", program.getName(), "Program name should be 'complex1'");

    // Verify namespaces
    Map<String, String> namespaces = program.getNamespaces();
    assertNotNull(namespaces, "Namespaces should not be null");
    assertEquals("thrift.test", namespaces.get("cpp"), "CPP namespace should be 'thrift.test'");
    assertEquals("thrift.test", namespaces.get("java"), "Java namespace should be 'thrift.test'");
    assertEquals("ThriftTest", namespaces.get("js"), "JS namespace should be 'ThriftTest'");

    // Verify enum definition
    TEnum numberz = findEnum(program, "Numberz");
    assertNotNull(numberz, "Enum 'Numberz' should exist");
    assertEquals(6, numberz.getConstants().size(), "Enum 'Numberz' should have 6 values");
    assertEquals(1, numberz.getConstantByName("ONE").getValue(), "Enum value 'ONE' should be 1");
    assertEquals(2, numberz.getConstantByName("TWO").getValue(), "Enum value 'TWO' should be 2");
    assertEquals(8, numberz.getConstantByName("EIGHT").getValue(), "Enum value 'EIGHT' should be 8");

    // Verify constants
    TConst myNumberz = findConst(program, "myNumberz");
    assertNotNull(myNumberz, "Constant 'myNumberz' should exist");
    assertEquals("Numberz.ONE", myNumberz.getValue().getIdentifier(), "myNumberz should be Numberz.ONE");

    TConst constMyString = findConst(program, "constMyString");
    assertNotNull(constMyString, "Constant 'constMyString' should exist");
    assertEquals("Hello, world!", constMyString.getValue().getString(), "constMyString value mismatch");

    // Verify struct definitions
    TStruct xtruct = findStruct(program, "Xtruct");
    assertNotNull(xtruct, "Struct 'Xtruct' should exist");
    assertEquals(4, xtruct.getMembers().size(), "Struct 'Xtruct' should have 4 fields");

    TField stringThing = findField(xtruct, "string_thing");
    assertNotNull(stringThing, "Field 'string_thing' should exist in Xtruct");
    assertEquals(1, stringThing.getKey(), "Field 'string_thing' should have ID 1");
    assertEquals("STRING", stringThing.getType().getName(), "Field 'string_thing' should be of type STRING");

    // Verify service definitions
    TService thriftTest = findService(program, "ThriftTest");
    assertNotNull(thriftTest, "Service 'ThriftTest' should exist");
    assertTrue(thriftTest.getFunctions().size() > 0, "Service 'ThriftTest' should have functions");

    TFunction testVoid = findFunction(thriftTest, "testVoid");
    assertNotNull(testVoid, "Function 'testVoid' should exist");
    assertEquals("VOID", testVoid.getReturnType().getName(), "testVoid should return void");
    assertEquals(0, testVoid.getArglist().getMembers().size(), "testVoid should have no arguments");

    TFunction testString = findFunction(thriftTest, "testString");
    assertNotNull(testString, "Function 'testString' should exist");
    assertEquals("STRING", testString.getReturnType().getName(), "testString should return string");
    assertEquals(1, testString.getArglist().getMembers().size(), "testString should have 1 argument");

    // Verify exception definitions
    TStruct xception = findException(program, "Xception");
    assertNotNull(xception, "Exception 'Xception' should exist");
    assertTrue(xception.isXception(), "Xception should be an exception");
    assertEquals(2, xception.getMembers().size(), "Exception 'Xception' should have 2 fields");

    // Verify union definitions
    TStruct someUnion = findUnion(program, "SomeUnion");
    assertNotNull(someUnion, "Union 'SomeUnion' should exist");
    assertTrue(someUnion.isUnion(), "SomeUnion should be a union");
    assertEquals(6, someUnion.getMembers().size(), "Union 'SomeUnion' should have 5 fields");
  }

  @Test
  @DisplayName("Test parsing complex1.thrift content as string")
  void testParseComplex1String() throws IOException {
    // Read the file content
    String content = new String(java.nio.file.Files.readAllBytes(Paths.get(TEST_FILE_PATH)));

    // Parse the content
    TProgram program = ThriftAstBuilder.parseString(content, "complex1.thrift");

    // Basic verification
    assertNotNull(program, "Parsed program should not be null");
    assertEquals("complex1", program.getName(), "Program name should be 'complex1'");

    // Verify one struct to ensure the content was properly parsed
    TStruct xtruct = findStruct(program, "Xtruct");
    assertNotNull(xtruct, "Struct 'Xtruct' should exist");
    assertEquals(4, xtruct.getMembers().size(), "Struct 'Xtruct' should have 4 fields");
  }

  @Test
  @DisplayName("Test resolveTypeRefs method")
  void testResolveTypeRefs() throws IOException {
    // Create a test thrift file that contains type references
    String thriftContent = "namespace java test.resolve\n\n"
                           + "typedef i32 MyInt\n"
                           + "typedef string MyString\n\n"
                           + "struct TestStruct {\n"
                           + "  1: MyInt id,\n"
                           + "  2: MyString name,\n"
                           + "  3: list<MyInt> numbers,\n"
                           + "  4: map<MyString, MyInt> mapping\n"
                           + "}\n\n"
                           + "exception TestException {\n"
                           + "  1: MyInt errorCode,\n"
                           + "  2: MyString message\n"
                           + "}\n\n"
                           + "const MyInt DEFAULT_ID = 100\n\n"
                           + "service TestService {\n"
                           + "  MyString testMethod(1: MyInt param1, 2: TestStruct param2) "
                           + "throws (1: TestException ex),\n"
                           + "  list<MyInt> getList()\n"
                           + "}";

    // Parse the string
    TProgram program = ThriftAstBuilder.parseString(thriftContent, "test_resolve.thrift");

    // Initial verification - these should still be TTypeRef before resolution
    TTypedef myIntTypedef = findTypedef(program, "MyInt");
    assertNotNull(myIntTypedef, "Typedef 'MyInt' should exist");

    TStruct testStruct = findStruct(program, "TestStruct");
    assertNotNull(testStruct, "Struct 'TestStruct' should exist");
    TField idField = findField(testStruct, "id");
    assertNotNull(idField, "Field 'id' should exist");
    assertTrue(idField.getType() instanceof TTypeRef, "Field type should be a TTypeRef before resolution");
    assertEquals("MyInt", idField.getType().getName(), "Field type name should be 'MyInt'");

    // Verify container types before resolution
    TField numbersField = findField(testStruct, "numbers");
    assertNotNull(numbersField, "Field 'numbers' should exist");
    assertTrue(numbersField.getType() instanceof TList, "Field type should be a TList");
    TList numbersList = (TList)numbersField.getType();
    assertTrue(numbersList.getElemType() instanceof TTypeRef,
               "List element type should be a TTypeRef before resolution");
    assertEquals("MyInt", numbersList.getElemType().getName(), "List element type name should be 'MyInt'");

    // Verify map types before resolution
    TField mappingField = findField(testStruct, "mapping");
    assertNotNull(mappingField, "Field 'mapping' should exist");
    assertTrue(mappingField.getType() instanceof TMap, "Field type should be a TMap");
    TMap mappingMap = (TMap)mappingField.getType();
    assertTrue(mappingMap.getKeyType() instanceof TTypeRef, "Map key type should be a TTypeRef before resolution");
    assertTrue(mappingMap.getValType() instanceof TTypeRef, "Map value type should be a TTypeRef before resolution");

    // Now resolve the type references
    program.resolveTypeRefsAndConsts();

    // Verify that types are resolved correctly
    assertFalse(idField.getType() instanceof TTypeRef, "Field type should not be a TTypeRef after resolution");
    assertEquals("I32", idField.getType().getTrueType().getName(), "Field type should be resolved to I32");

    // Verify container types after resolution
    assertFalse(numbersList.getElemType() instanceof TTypeRef,
                "List element type should not be a TTypeRef after resolution");
    assertEquals("I32", numbersList.getElemType().getTrueType().getName(),
                 "List element type should be resolved to I32");

    // Verify map types after resolution
    assertFalse(mappingMap.getKeyType() instanceof TTypeRef, "Map key type should not be a TTypeRef after resolution");
    assertFalse(mappingMap.getValType() instanceof TTypeRef,
                "Map value type should not be a TTypeRef after resolution");
    assertEquals("STRING", mappingMap.getKeyType().getTrueType().getName(),
                 "Map key type should be resolved to STRING");
    assertEquals("I32", mappingMap.getValType().getTrueType().getName(), "Map value type should be resolved to I32");

    // Verify constant resolution
    TConst defaultId = findConst(program, "DEFAULT_ID");
    assertNotNull(defaultId, "Constant 'DEFAULT_ID' should exist");
    assertFalse(defaultId.getType() instanceof TTypeRef, "Constant type should not be a TTypeRef after resolution");
    assertEquals("I32", defaultId.getType().getTrueType().getName(), "Constant type should be resolved to I32");

    // Verify service method resolution
    TService testService = findService(program, "TestService");
    assertNotNull(testService, "Service 'TestService' should exist");

    TFunction testMethod = findFunction(testService, "testMethod");
    assertNotNull(testMethod, "Function 'testMethod' should exist");
    assertFalse(testMethod.getReturnType() instanceof TTypeRef,
                "Return type should not be a TTypeRef after resolution");
    assertEquals("STRING", testMethod.getReturnType().getTrueType().getName(),
                 "Return type should be resolved to STRING");

    // Verify function parameters
    TField param1 = findField(testMethod.getArglist(), "param1");
    assertNotNull(param1, "Parameter 'param1' should exist");
    assertFalse(param1.getType() instanceof TTypeRef, "Parameter type should not be a TTypeRef after resolution");
    assertEquals("I32", param1.getType().getTrueType().getName(), "Parameter type should be resolved to I32");

    // Verify exception resolution
    TField exceptionField = findField(testMethod.getXceptions(), "ex");
    assertNotNull(exceptionField, "Exception 'ex' should exist");
    assertFalse(exceptionField.getType() instanceof TTypeRef,
                "Exception type should not be a TTypeRef after resolution");
    assertEquals("TestException", exceptionField.getType().getName(), "Exception type name should be 'TestException'");
  }

  // Helper methods to find elements by name
  private TEnum findEnum(TProgram program, String name) {
    for (TEnum tEnum : program.getEnums()) {
      if (tEnum.getName().equals(name)) {
        return tEnum;
      }
    }
    return null;
  }

  private TConst findConst(TProgram program, String name) {
    for (TConst tConst : program.getConsts()) {
      if (tConst.getName().equals(name)) {
        return tConst;
      }
    }
    return null;
  }

  private TStruct findStruct(TProgram program, String name) {
    for (TStruct tStruct : program.getStructs()) {
      if (tStruct.getName().equals(name)) {
        return tStruct;
      }
    }
    return null;
  }

  private TStruct findException(TProgram program, String name) {
    for (TStruct tStruct : program.getXceptions()) {
      if (tStruct.getName().equals(name)) {
        return tStruct;
      }
    }
    return null;
  }

  private TStruct findUnion(TProgram program, String name) {
    for (TStruct tStruct : program.getObjects()) {
      if (tStruct.getName().equals(name) && tStruct.isUnion()) {
        return tStruct;
      }
    }
    return null;
  }

  private TService findService(TProgram program, String name) {
    for (TService tService : program.getServices()) {
      if (tService.getName().equals(name)) {
        return tService;
      }
    }
    return null;
  }

  private TFunction findFunction(TService service, String name) {
    for (TFunction tFunction : service.getFunctions()) {
      if (tFunction.getName().equals(name)) {
        return tFunction;
      }
    }
    return null;
  }

  private TField findField(TStruct struct, String name) {
    for (TField tField : struct.getMembers()) {
      if (tField.getName().equals(name)) {
        return tField;
      }
    }
    return null;
  }

  private TTypedef findTypedef(TProgram program, String name) {
    for (TTypedef typedef : program.getTypedefs()) {
      if (typedef.getName().equals(name)) {
        return typedef;
      }
    }
    return null;
  }
}
