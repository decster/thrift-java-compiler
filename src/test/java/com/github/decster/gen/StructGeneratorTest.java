package com.github.decster.gen;

import com.github.decster.ast.*;
// Import common stubs
import com.github.decster.gen.AstTestStubs.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach; // Added for setUp

import java.util.Arrays; // Keep if used by any test logic later
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


// Local stubs removed, using AstTestStubs.*

public class StructGeneratorTest {

    private StructGenerator generator;
    private AstTestStubs.StubStructNode structNode; // Use common stub
    private AstTestStubs.StubDocumentNode docNode;    // Use common stub for DocumentNode
    private static final String TEST_PACKAGE = "com.test.gen";
    private static final String TEST_DATE = "2023-01-01";


    private void assertContains(String generatedCode, String expectedSnippet) {
        assertTrue(generatedCode.contains(expectedSnippet),
                "Generated code missing: '" + expectedSnippet + "'");
    }

    private void assertNotContains(String generatedCode, String unexpectedSnippet) {
        assertFalse(generatedCode.contains(unexpectedSnippet),
                "Generated code should not contain: '" + unexpectedSnippet + "'");
    }

    @BeforeEach
    void setUp() {
        docNode = new AstTestStubs.StubDocumentNode();
    }


    @Test
    void testBasicStruct() {
        structNode = new AstTestStubs.StubStructNode("TestBasicStruct");
        // Use stubs from AstTestStubs
        structNode.addField(new AstTestStubs.StubFieldNode((short) 1, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32), "myInt"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 2, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING), "myString"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 3, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BOOL), "myBool"));

        generator = new StructGenerator(structNode, docNode, TEST_PACKAGE, TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public class TestBasicStruct implements org.apache.thrift.TBase<TestBasicStruct, TestBasicStruct._Fields>");
        assertContains(code, "public int myInt;");
        assertContains(code, "public java.lang.String myString;");
        assertContains(code, "public boolean myBool;");

        assertContains(code, "public enum _Fields implements org.apache.thrift.TFieldIdEnum {");
        assertContains(code, "MY_INT((short)1, \"myInt\")");
        // ... rest of assertions for basic struct
    }

    @Test
    void testAllPrimitiveTypes() {
        structNode = new AstTestStubs.StubStructNode("AllPrimitives");
        structNode.addField(new AstTestStubs.StubFieldNode((short) 1, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BOOL), "aBool"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 2, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BYTE), "aByte"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 3, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I16), "aI16"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 4, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32), "aI32"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 5, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I64), "aI64"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 6, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.DOUBLE), "aDouble"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 7, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING), "aString"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 8, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BINARY), "aBinary"));

        generator = new StructGenerator(structNode, docNode, "com.test.primitives", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public class AllPrimitives");
        assertContains(code, "public boolean aBool;");
        assertContains(code, "public byte aByte;");
        // ... other assertions
    }

    @Test
    void testListI32() {
        structNode = new AstTestStubs.StubStructNode("StructWithList");
        structNode.addField(new AstTestStubs.StubFieldNode((short)1, new AstTestStubs.StubListTypeNode(new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32)), "intList"));
        generator = new StructGenerator(structNode, docNode, "com.test.coll", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.List<java.lang.Integer> intList;");
        assertContains(code, "new org.apache.thrift.meta_data.ListMetaData(TType.LIST, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))");
    }

    @Test
    void testSetString() {
        structNode = new AstTestStubs.StubStructNode("StructWithSet");
        structNode.addField(new AstTestStubs.StubFieldNode((short)1, new AstTestStubs.StubSetTypeNode(new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING)), "stringSet"));
        generator = new StructGenerator(structNode, docNode, "com.test.coll", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.Set<java.lang.String> stringSet;");
    }

    @Test
    void testMapI16String() {
        structNode = new AstTestStubs.StubStructNode("StructWithMap");
        structNode.addField(new AstTestStubs.StubFieldNode((short)1,
            new AstTestStubs.StubMapTypeNode(
                new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I16),
                new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING)
            ), "shortToStringMap"));
        generator = new StructGenerator(structNode, docNode, "com.test.coll", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.Map<java.lang.Short, java.lang.String> shortToStringMap;");
    }

    @Test
    void testEnumField() {
        structNode = new AstTestStubs.StubStructNode("StructWithEnum");
        String enumName = "com.test.enums.DayOfWeek";
        // Define DayOfWeek Enum in the DocumentNode so resolveType can find it
        AstTestStubs.StubEnumNode enumDef = new AstTestStubs.StubEnumNode(enumName);
        docNode.addDefinition(enumDef); // Use inherited addDefinition from DocumentNode

        structNode.addField(new AstTestStubs.StubFieldNode((short)1, new AstTestStubs.StubIdentifierTypeNode(enumName), "myDay"));
        generator = new StructGenerator(structNode, docNode, "com.test.enums", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public com.test.enums.DayOfWeek myDay;");
        assertContains(code, "new org.apache.thrift.meta_data.EnumMetaData(TType.ENUM, com.test.enums.DayOfWeek.class)");
    }

    @Test
    void testNestedStructField() {
        structNode = new AstTestStubs.StubStructNode("OuterStruct");
        String innerStructName = "com.test.other.InnerStruct";
        AstTestStubs.StubStructNode innerStructDef = new AstTestStubs.StubStructNode(innerStructName);
        docNode.addDefinition(innerStructDef);

        structNode.addField(new AstTestStubs.StubFieldNode((short)1, new AstTestStubs.StubIdentifierTypeNode(innerStructName), "inner"));
        generator = new StructGenerator(structNode, docNode, "com.test.outer", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public com.test.other.InnerStruct inner;");
    }

    @Test
    void testListNestedStruct() {
        structNode = new AstTestStubs.StubStructNode("ListWithStruct");
        String nestedItemName = "com.test.other.NestedItem";
        AstTestStubs.StubStructNode nestedItemDef = new AstTestStubs.StubStructNode(nestedItemName);
        docNode.addDefinition(nestedItemDef);

        com.github.decster.ast.TypeNode innerStructType = new AstTestStubs.StubIdentifierTypeNode(nestedItemName);
        structNode.addField(new AstTestStubs.StubFieldNode((short)1, new AstTestStubs.StubListTypeNode(innerStructType), "listOfItems"));
        generator = new StructGenerator(structNode, docNode, "com.test.main", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.List<com.test.other.NestedItem> listOfItems;");
    }


    @Test
    void testFieldRequirements() {
        structNode = new AstTestStubs.StubStructNode("RequiredFieldsStruct");
        AstTestStubs.StubFieldNode reqField = new AstTestStubs.StubFieldNode((short)1, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32), "reqInt");
        reqField.setRequirement(com.github.decster.ast.FieldNode.Requirement.REQUIRED); // Use actual enum
        structNode.addField(reqField);

        AstTestStubs.StubFieldNode optField = new AstTestStubs.StubFieldNode((short)2, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING), "optString");
        optField.setRequirement(com.github.decster.ast.FieldNode.Requirement.OPTIONAL);
        structNode.addField(optField);

        generator = new StructGenerator(structNode, docNode, "com.test.req", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "FieldMetaData(\"reqInt\", TFieldRequirementType.REQUIRED");
        assertContains(code, "FieldMetaData(\"optString\", TFieldRequirementType.OPTIONAL");
    }

    @Test
    void testDefaultValues() {
        structNode = new AstTestStubs.StubStructNode("DefaultsStruct");

        AstTestStubs.StubFieldNode intField = new AstTestStubs.StubFieldNode((short)1, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32), "myIntWithDefault");
        intField.setDefaultValue(100); // Use inherited setDefaultValue(Object)
        structNode.addField(intField);

        AstTestStubs.StubFieldNode stringField = new AstTestStubs.StubFieldNode((short)2, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING), "myStringWithDefault");
        stringField.setDefaultValue("Hello Thrift");
        structNode.addField(stringField);

        String enumName = "com.test.enums.MyColor";
        AstTestStubs.StubEnumNode enumDef = new AstTestStubs.StubEnumNode(enumName);
        enumDef.addValue(new AstTestStubs.StubEnumValueNode("BLUE", 0));
        docNode.addDefinition(enumDef);

        AstTestStubs.StubFieldNode enumField = new AstTestStubs.StubFieldNode((short)3, new AstTestStubs.StubIdentifierTypeNode(enumName), "myEnumWithDefault");
        enumField.setDefaultValue("BLUE"); // Pass string name of enum value for generator to resolve
        structNode.addField(enumField);

        generator = new StructGenerator(structNode, docNode, "com.test.defaults", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "this.myIntWithDefault = 100;");
        assertContains(code, "this.myStringWithDefault = \"Hello Thrift\";");
        assertContains(code, "this.myEnumWithDefault = com.test.enums.MyColor.BLUE;");
    }

    @Test
    void testHashCodeAndEqualsPrimitives() {
        structNode = new AstTestStubs.StubStructNode("HashEqualsPrimitives");
        structNode.addField(new AstTestStubs.StubFieldNode((short) 1, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32), "id"));
        structNode.addField(new AstTestStubs.StubFieldNode((short) 2, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING), "name"));
        generator = new StructGenerator(structNode, docNode, "com.test", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public boolean equals(HashEqualsPrimitives that) {");
        assertContains(code, "if (this.id != that.id) return false;");
        assertContains(code, "if (!this.name.equals(that.name)) return false;");
        assertContains(code, "public int hashCode() {");
    }

    @Test
    void testToStringGen() {
        structNode = new AstTestStubs.StubStructNode("ToStringStruct");
        structNode.addField(new AstTestStubs.StubFieldNode((short) 1, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING), "reqField"));
        AstTestStubs.StubFieldNode optField = new AstTestStubs.StubFieldNode((short)2, new AstTestStubs.StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32), "optField");
        optField.setRequirement(com.github.decster.ast.FieldNode.Requirement.OPTIONAL);
        structNode.addField(optField);

        generator = new StructGenerator(structNode, docNode, "com.test", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public String toString() {");
        assertContains(code, "sb.append(\"reqField:\");");
        assertContains(code, "if (isSetOptField()) {");
    }
}
