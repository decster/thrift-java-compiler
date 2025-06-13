package com.github.decster;

import com.github.decster.ast.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(5, someUnion.getMembers().size(), "Union 'SomeUnion' should have 5 fields");
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
}
