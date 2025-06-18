package io.github.decster.ast;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TConstValueTest {

    @Test
    @DisplayName("Test compareTo method")
    void testCompareTo() {
        // Test comparing different types
        TConstValue intValue = new TConstValue(42);
        TConstValue doubleValue = new TConstValue(42.0);
        TConstValue stringValue = new TConstValue("42");

        // Different types should be compared by their enum ordinal
        assertTrue(intValue.compareTo(doubleValue) < 0);
        assertTrue(doubleValue.compareTo(stringValue) < 0);
        assertTrue(stringValue.compareTo(intValue) > 0);

        // Same type comparisons
        TConstValue intValue2 = new TConstValue(24);
        assertTrue(intValue.compareTo(intValue2) > 0);
        assertEquals(0, intValue.compareTo(new TConstValue(42)));

        TConstValue doubleValue2 = new TConstValue(24.0);
        assertTrue(doubleValue.compareTo(doubleValue2) > 0);
        assertEquals(0, doubleValue.compareTo(new TConstValue(42.0)));

        TConstValue stringValue2 = new TConstValue("24");
        assertTrue(stringValue.compareTo(stringValue2) > 0);
        assertEquals(0, stringValue.compareTo(new TConstValue("42")));

        // Test identifier comparison
        TConstValue idValue1 = new TConstValue();
        idValue1.setIdentifier("abc");
        TConstValue idValue2 = new TConstValue();
        idValue2.setIdentifier("xyz");
        assertTrue(idValue1.compareTo(idValue2) < 0);
        assertEquals(0, idValue1.compareTo(idValue1));

        // Test map comparison
        TConstValue mapValue1 = new TConstValue();
        mapValue1.setMap();
        mapValue1.addMap(new TConstValue(1), new TConstValue("one"));
        mapValue1.addMap(new TConstValue(2), new TConstValue("two"));

        TConstValue mapValue2 = new TConstValue();
        mapValue2.setMap();
        mapValue2.addMap(new TConstValue(1), new TConstValue("one"));

        // Different size maps
        assertTrue(mapValue1.compareTo(mapValue2) > 0);

        // Same maps
        TConstValue mapValue3 = new TConstValue();
        mapValue3.setMap();
        mapValue3.addMap(new TConstValue(1), new TConstValue("one"));
        mapValue3.addMap(new TConstValue(2), new TConstValue("two"));
        assertEquals(0, mapValue1.compareTo(mapValue3));

        // Different values for same key
        TConstValue mapValue4 = new TConstValue();
        mapValue4.setMap();
        mapValue4.addMap(new TConstValue(1), new TConstValue("one"));
        mapValue4.addMap(new TConstValue(2), new TConstValue("tvo"));
        assertTrue(mapValue1.compareTo(mapValue4) > 0); // "two" > "tvo"

        // Test list comparison
        TConstValue listValue1 = new TConstValue();
        listValue1.setList();
        listValue1.addList(new TConstValue(1));
        listValue1.addList(new TConstValue(2));

        TConstValue listValue2 = new TConstValue();
        listValue2.setList();
        listValue2.addList(new TConstValue(1));

        // Different size lists
        assertTrue(listValue1.compareTo(listValue2) > 0);

        // Same lists
        TConstValue listValue3 = new TConstValue();
        listValue3.setList();
        listValue3.addList(new TConstValue(1));
        listValue3.addList(new TConstValue(2));
        assertEquals(0, listValue1.compareTo(listValue3));

        // Different values at same position
        TConstValue listValue4 = new TConstValue();
        listValue4.setList();
        listValue4.addList(new TConstValue(1));
        listValue4.addList(new TConstValue(3));
        assertTrue(listValue1.compareTo(listValue4) < 0);

        // Test unknown type
        TConstValue unknownValue1 = new TConstValue();
        TConstValue unknownValue2 = new TConstValue();
        assertEquals(0, unknownValue1.compareTo(unknownValue2));
    }

    @Test
    @DisplayName("Test validateUuid method")
    void testValidateUuid() {
        TConstValue value = new TConstValue();

        // Valid UUIDs
        assertDoesNotThrow(() -> value.setUuid("01234567-9012-4567-9012-456789012345"));
        assertDoesNotThrow(() -> value.setUuid("{01234567-9012-4567-9012-456789012345}"));

        // Invalid UUIDs
        assertThrows(RuntimeException.class, () -> value.setUuid("not-a-uuid"));
        assertThrows(RuntimeException.class, () -> value.setUuid("01234567-9012-4567-9012-45678901234")); // Too short
        assertThrows(RuntimeException.class, () -> value.setUuid("01234567-9012-4567-9012-4567890123456")); // Too long
        assertThrows(RuntimeException.class, () -> value.setUuid("01234567X9012-4567-9012-456789012345")); // Wrong separator
        assertThrows(RuntimeException.class, () -> value.setUuid("01234567-9012-4567-9012-45678901234G")); // Invalid char
    }

    @Test
    @DisplayName("Test getInteger method")
    void testGetInteger() {
        // Direct integer value
        TConstValue intValue = new TConstValue(42);
        assertEquals(42, intValue.getInteger());

        // Integer from enum
        TConstValue enumIdValue = new TConstValue();
        enumIdValue.setIdentifier("TestEnum.VALUE");

        TEnum testEnum = new TEnum(null);
        TEnumValue enumValue = new TEnumValue("VALUE", 123);
        testEnum.append(enumValue);

        enumIdValue.setEnum(testEnum);
        assertEquals(123, enumIdValue.getInteger());

        // Test enum identifier with module
        TConstValue enumIdValueWithModule = new TConstValue();
        enumIdValueWithModule.setIdentifier("module.TestEnum.VALUE");
        enumIdValueWithModule.setEnum(testEnum);
        assertEquals(123, enumIdValueWithModule.getInteger());

        // Test error cases
        TConstValue noEnumValue = new TConstValue();
        noEnumValue.setIdentifier("TestEnum.MISSING");
        assertThrows(RuntimeException.class, () -> noEnumValue.getInteger());

        TConstValue withEnumButMissingValue = new TConstValue();
        withEnumButMissingValue.setIdentifier("TestEnum.MISSING");
        withEnumButMissingValue.setEnum(testEnum);
        assertThrows(RuntimeException.class, () -> withEnumButMissingValue.getInteger());
    }

    @Test
    @DisplayName("Test getIdentifierName method")
    void testGetIdentifierName() {
        TConstValue value = new TConstValue();

        // Test unqualified identifier (should throw exception)
        value.setIdentifier("unqualified");
        assertThrows(RuntimeException.class, () -> value.getIdentifierName());

        // Test qualified identifier with one dot
        value.setIdentifier("module.name");
        assertEquals("name", value.getIdentifierName());

        // Test qualified identifier with multiple dots
        value.setIdentifier("module.submodule.name");
        assertEquals("name", value.getIdentifierName());
    }

    @Test
    @DisplayName("Test toString method")
    void testToString() {
        // Test integer
        TConstValue intValue = new TConstValue(42);
        assertEquals("42", intValue.toString());

        // Test double
        TConstValue doubleValue = new TConstValue(42.5);
        assertEquals("42.5", doubleValue.toString());

        // Test string
        TConstValue stringValue = new TConstValue("hello");
        assertEquals("\"hello\"", stringValue.toString());

        // Test map
        TConstValue mapValue = new TConstValue();
        mapValue.setMap();
        mapValue.addMap(new TConstValue(1), new TConstValue("one"));
        assertEquals("map[1]", mapValue.toString());

        // Test list
        TConstValue listValue = new TConstValue();
        listValue.setList();
        listValue.addList(new TConstValue(1));
        listValue.addList(new TConstValue(2));
        assertEquals("list[2]", listValue.toString());

        // Test identifier
        TConstValue idValue = new TConstValue();
        idValue.setIdentifier("module.name");
        assertEquals("module.name", idValue.toString());

        // Test unknown
        TConstValue unknownValue = new TConstValue();
        assertEquals("unknown", unknownValue.toString());
    }

    @Test
    @DisplayName("Test UUID methods")
    void testUuidMethods() {
        TConstValue value = new TConstValue();
        String validUuid = "01234567-9012-4567-9012-456789012345";

        value.setUuid(validUuid);
        assertEquals(validUuid, value.getUuid());
        assertEquals(TConstValue.Type.CV_STRING, value.getType());
    }

    @Test
    @DisplayName("Test map methods")
    void testMapMethods() {
        TConstValue value = new TConstValue();

        // Test setMap()
        value.setMap();
        assertEquals(TConstValue.Type.CV_MAP, value.getType());
        assertTrue(value.getMap().isEmpty());

        // Test addMap
        TConstValue key = new TConstValue(1);
        TConstValue val = new TConstValue("one");
        value.addMap(key, val);
        assertEquals(1, value.getMap().size());
        assertEquals(val, value.getMap().get(key));

        // Test setMap(Map)
        Map<TConstValue, TConstValue> newMap = new HashMap<>();
        newMap.put(new TConstValue(2), new TConstValue("two"));
        value.setMap(newMap);
        assertEquals(newMap, value.getMap());
    }

    @Test
    @DisplayName("Test list methods")
    void testListMethods() {
        TConstValue value = new TConstValue();

        // Test setList()
        value.setList();
        assertEquals(TConstValue.Type.CV_LIST, value.getType());
        assertTrue(value.getList().isEmpty());

        // Test addList
        TConstValue item = new TConstValue(1);
        value.addList(item);
        assertEquals(1, value.getList().size());
        assertEquals(item, value.getList().get(0));

        // Test setList(List)
        List<TConstValue> newList = Arrays.asList(new TConstValue(2), new TConstValue(3));
        value.setList(newList);
        assertEquals(newList, value.getList());
    }

    @Test
    @DisplayName("Test getIdentifierWithParent method")
    void testGetIdentifierWithParent() {
        TConstValue value = new TConstValue();

        // Test unqualified identifier (should throw exception)
        value.setIdentifier("unqualified");
        assertThrows(RuntimeException.class, () -> value.getIdentifierWithParent());

        // Test qualified identifier with one dot
        value.setIdentifier("module.name");
        assertEquals("module.name", value.getIdentifierWithParent());

        // Test qualified identifier with multiple dots
        value.setIdentifier("module.submodule.name");
        assertEquals("submodule.name", value.getIdentifierWithParent());
    }

    @Test
    @DisplayName("Test getEnum and setEnum methods")
    void testEnumMethods() {
        TConstValue value = new TConstValue();
        TEnum testEnum = new TEnum(null);

        value.setEnum(testEnum);
        assertEquals(testEnum, value.getEnum());
    }
}
