package com.github.decster.gen;

import com.github.decster.ast.*;
// Import common stubs
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
    private StubStructNode structNode; // Use common stub
    private StubDocumentNode docNode;    // Use common stub for DocumentNode
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
        docNode = new StubDocumentNode();
    }


    @Test
    void testBasicStruct() {
        structNode = new StubStructNode("TestBasicStruct");
        // Use stubs from AstTestStubs
        structNode.addField(new StubFieldNode((short) 1, "myInt", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32)));
        structNode.addField(new StubFieldNode((short) 2, "myString", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING)));
        structNode.addField(new StubFieldNode((short) 3, "myBool", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BOOL)));

        generator = new StructGenerator(structNode, docNode, TEST_PACKAGE, TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public class TestBasicStruct implements org.apache.thrift.TBase<TestBasicStruct, TestBasicStruct._Fields>");
        assertContains(code, "public int myInt;");
        assertContains(code, "public @org.apache.thrift.annotation.Nullable java.lang.String myString;");
        assertContains(code, "public boolean myBool;");

        assertContains(code, "public enum _Fields implements org.apache.thrift.TFieldIdEnum {");
        assertContains(code, "MY_INT((short)1, \"myInt\")");
        // ... rest of assertions for basic struct
    }

    @Test
    void testAllPrimitiveTypes() {
        structNode = new StubStructNode("AllPrimitives");
        structNode.addField(new StubFieldNode((short) 1, "aBool", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BOOL)));
        structNode.addField(new StubFieldNode((short) 2, "aByte", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BYTE)));
        structNode.addField(new StubFieldNode((short) 3, "aI16", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I16)));
        structNode.addField(new StubFieldNode((short) 4, "aI32", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32)));
        structNode.addField(new StubFieldNode((short) 5, "aI64", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I64)));
        structNode.addField(new StubFieldNode((short) 6, "aDouble", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.DOUBLE)));
        structNode.addField(new StubFieldNode((short) 7, "aString", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING)));
        structNode.addField(new StubFieldNode((short) 8, "aBinary", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.BINARY)));

        generator = new StructGenerator(structNode, docNode, "com.test.primitives", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public class AllPrimitives");
        assertContains(code, "public boolean aBool;");
        assertContains(code, "public byte aByte;");
        // ... other assertions
    }

    @Test
    void testListI32() {
        structNode = new StubStructNode("StructWithList");
        structNode.addField(new StubFieldNode((short)1, "intList", new StubListTypeNode(new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32))));
        generator = new StructGenerator(structNode, docNode, "com.test.coll", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.List<java.lang.Integer> intList;");
        assertContains(code, "new org.apache.thrift.meta_data.ListMetaData(TType.LIST, new org.apache.thrift.meta_data.FieldValueMetaData(TType.I32))");
    }

    @Test
    void testSetString() {
        structNode = new StubStructNode("StructWithSet");
        structNode.addField(new StubFieldNode((short)1, "stringSet", new StubSetTypeNode(new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING))));
        generator = new StructGenerator(structNode, docNode, "com.test.coll", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.Set<java.lang.String> stringSet;");
    }

    @Test
    void testMapI16String() {
        structNode = new StubStructNode("StructWithMap");
        structNode.addField(new StubFieldNode((short)1, "shortToStringMap",
            new StubMapTypeNode(
                new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I16),
                new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING)
            )));
        generator = new StructGenerator(structNode, docNode, "com.test.coll", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.Map<java.lang.Short, java.lang.String> shortToStringMap;");
    }

    @Test
    void testEnumField() {
        structNode = new StubStructNode("StructWithEnum");
        String enumName = "com.test.enums.DayOfWeek";
        // Define DayOfWeek Enum in the DocumentNode so resolveType can find it
        StubEnumNode enumDef = new StubEnumNode(enumName);
        docNode.addDefinition(enumDef); // Use inherited addDefinition from DocumentNode

        structNode.addField(new StubFieldNode((short)1, "myDay", new StubIdentifierTypeNode(enumName)));
        generator = new StructGenerator(structNode, docNode, "com.test.enums", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public com.test.enums.DayOfWeek myDay;");
        assertContains(code, "new org.apache.thrift.meta_data.EnumMetaData(TType.ENUM, com.test.enums.DayOfWeek.class)");
    }

    @Test
    void testNestedStructField() {
        structNode = new StubStructNode("OuterStruct");
        String innerStructName = "com.test.other.InnerStruct";
        StubStructNode innerStructDef = new StubStructNode(innerStructName);
        docNode.addDefinition(innerStructDef);

        structNode.addField(new StubFieldNode((short)1, "inner", new StubIdentifierTypeNode(innerStructName)));
        generator = new StructGenerator(structNode, docNode, "com.test.outer", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public com.test.other.InnerStruct inner;");
    }

    @Test
    void testListNestedStruct() {
        structNode = new StubStructNode("ListWithStruct");
        String nestedItemName = "com.test.other.NestedItem";
        StubStructNode nestedItemDef = new StubStructNode(nestedItemName);
        docNode.addDefinition(nestedItemDef);

        com.github.decster.ast.TypeNode innerStructType = new StubIdentifierTypeNode(nestedItemName);
        structNode.addField(new StubFieldNode((short)1, "listOfItems", new StubListTypeNode(innerStructType)));
        generator = new StructGenerator(structNode, docNode, "com.test.main", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public java.util.List<com.test.other.NestedItem> listOfItems;");
    }


    @Test
    void testFieldRequirements() {
        structNode = new StubStructNode("RequiredFieldsStruct");
        StubFieldNode reqField = new StubFieldNode((short)1, "reqInt", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32));
        reqField.setRequirement(com.github.decster.ast.FieldNode.Requirement.REQUIRED); // Use actual enum
        structNode.addField(reqField);

        StubFieldNode optField = new StubFieldNode((short)2, "optString", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING));
        optField.setRequirement(com.github.decster.ast.FieldNode.Requirement.OPTIONAL);
        structNode.addField(optField);

        generator = new StructGenerator(structNode, docNode, "com.test.req", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "FieldMetaData(\"reqInt\", TFieldRequirementType.REQUIRED");
        assertContains(code, "FieldMetaData(\"optString\", TFieldRequirementType.OPTIONAL");
    }

    @Test
    void testDefaultValues() {
        structNode = new StubStructNode("DefaultsStruct");

        StubFieldNode intField = new StubFieldNode((short)1, "myIntWithDefault", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32));
        intField.setDefaultValue(100); // Use inherited setDefaultValue(Object)
        structNode.addField(intField);

        StubFieldNode stringField = new StubFieldNode((short)2, "myStringWithDefault", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING));
        stringField.setDefaultValue("Hello Thrift");
        structNode.addField(stringField);

        String enumName = "com.test.enums.MyColor";
        StubEnumNode enumDef = new StubEnumNode(enumName);
        enumDef.addValue(new StubEnumValueNode("BLUE", 0));
        docNode.addDefinition(enumDef);

        StubFieldNode enumField = new StubFieldNode((short)3, "myEnumWithDefault", new StubIdentifierTypeNode(enumName));
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
        structNode = new StubStructNode("HashEqualsPrimitives");
        structNode.addField(new StubFieldNode((short) 1, "id", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32)));
        structNode.addField(new StubFieldNode((short) 2, "name", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING)));
        generator = new StructGenerator(structNode, docNode, "com.test", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public boolean equals(HashEqualsPrimitives that) {");
        assertContains(code, "if (this.id != that.id) return false;");
        assertContains(code, "if (!this.name.equals(that.name)) return false;");
        assertContains(code, "public int hashCode() {");
    }

    @Test
    void testToStringGen() {
        structNode = new StubStructNode("ToStringStruct");
        structNode.addField(new StubFieldNode((short) 1, "reqField", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.STRING)));
        StubFieldNode optField = new StubFieldNode((short)2, "optField", new StubBaseTypeNode(BaseTypeNode.BaseTypeEnum.I32));
        optField.setRequirement(com.github.decster.ast.FieldNode.Requirement.OPTIONAL);
        structNode.addField(optField);

        generator = new StructGenerator(structNode, docNode, "com.test", TEST_DATE);
        String code = generator.generate();

        assertContains(code, "public String toString() {");
        assertContains(code, "sb.append(\"reqField:\");");
        assertContains(code, "if (isSetOptField()) {");
    }
}
