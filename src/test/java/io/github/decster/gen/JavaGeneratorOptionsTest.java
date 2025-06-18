package io.github.decster.gen;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JavaGeneratorOptionsTest {

    @Test
    public void testDefaultConstructor() {
        JavaGeneratorOptions options = new JavaGeneratorOptions();

        // Verify default values
        assertFalse(options.isBeans());
        assertFalse(options.isPrivateMembers());
        assertFalse(options.isJava5());
        assertFalse(options.isAndroidStyle());
        assertFalse(options.isNocamel());
        assertFalse(options.isFullcamel());
        assertFalse(options.isSortedContainers());
        assertFalse(options.isReuseObjects());
        assertNull(options.getOptionType());
        assertFalse(options.isFutureIface());
        assertFalse(options.isUndatedGeneratedAnnotations());
        assertFalse(options.isSuppressGeneratedAnnotations());
        assertFalse(options.isJakartaAnnotations());
        assertFalse(options.isAnnotationsAsMetadata());
        assertFalse(options.isRethrowUnhandledExceptions());
        assertFalse(options.isUnsafeBinaries());
        assertTrue(options.getCustomOptions().isEmpty());
    }

    @Test
    public void testFromMapWithNullMap() {
        JavaGeneratorOptions options = JavaGeneratorOptions.fromMap(null);

        // Should create default options
        assertFalse(options.isBeans());
        assertTrue(options.getCustomOptions().isEmpty());
    }

    @Test
    public void testFromMapWithEmptyMap() {
        JavaGeneratorOptions options = JavaGeneratorOptions.fromMap(new HashMap<>());

        // Should create default options
        assertFalse(options.isBeans());
        assertTrue(options.getCustomOptions().isEmpty());
    }

    @Test
    public void testFromMapWithAllOptions() {
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("beans", "true");
        optionsMap.put("private_members", "true");
        optionsMap.put("java5", "true");
        optionsMap.put("android", "true");
        optionsMap.put("nocamel", "true");
        optionsMap.put("fullcamel", "true");
        optionsMap.put("sorted_containers", "true");
        optionsMap.put("reuse_objects", "true");
        optionsMap.put("option_type", "java.util.Optional");
        optionsMap.put("future_iface", "true");
        optionsMap.put("undated_generated_annotations", "true");
        optionsMap.put("suppress_generated_annotations", "true");
        optionsMap.put("jakarta_annotations", "true");
        optionsMap.put("annotations_as_metadata", "true");
        optionsMap.put("rethrow_unhandled_exceptions", "true");
        optionsMap.put("unsafe_binaries", "true");

        JavaGeneratorOptions options = JavaGeneratorOptions.fromMap(optionsMap);

        // Verify all options were set correctly
        assertTrue(options.isBeans());
        assertTrue(options.isPrivateMembers());
        assertTrue(options.isJava5());
        assertTrue(options.isAndroidStyle());
        assertTrue(options.isNocamel());
        assertTrue(options.isFullcamel());
        assertTrue(options.isSortedContainers());
        assertTrue(options.isReuseObjects());
        assertEquals("java.util.Optional", options.getOptionType());
        assertTrue(options.isFutureIface());
        assertTrue(options.isUndatedGeneratedAnnotations());
        assertTrue(options.isSuppressGeneratedAnnotations());
        assertTrue(options.isJakartaAnnotations());
        assertTrue(options.isAnnotationsAsMetadata());
        assertTrue(options.isRethrowUnhandledExceptions());
        assertTrue(options.isUnsafeBinaries());
    }

    @Test
    public void testFromMapWithFalseBooleanValues() {
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("beans", "false");
        optionsMap.put("private_members", "false");
        optionsMap.put("java5", "false");
        optionsMap.put("android", "false");
        optionsMap.put("nocamel", "false");
        optionsMap.put("fullcamel", "false");

        JavaGeneratorOptions options = JavaGeneratorOptions.fromMap(optionsMap);

        // Verify options were set to false
        assertFalse(options.isBeans());
        assertFalse(options.isPrivateMembers());
        assertFalse(options.isJava5());
        assertFalse(options.isAndroidStyle());
        assertFalse(options.isNocamel());
        assertFalse(options.isFullcamel());
    }

    @Test
    public void testFromMapWithInvalidBooleanValues() {
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("beans", "not-a-boolean");
        optionsMap.put("java5", "1");

        JavaGeneratorOptions options = JavaGeneratorOptions.fromMap(optionsMap);

        // Invalid boolean values should be interpreted as false
        assertFalse(options.isBeans());
        assertFalse(options.isJava5());
    }

    @Test
    public void testFromMapWithAndroidLegacy() {
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("android_legacy", "true");

        JavaGeneratorOptions options = JavaGeneratorOptions.fromMap(optionsMap);

        // Both "android" and "android_legacy" keys map to the androidStyle property
        assertTrue(options.isAndroidStyle());
    }

    @Test
    public void testFromMapWithCustomOptions() {
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("beans", "true");
        optionsMap.put("custom_option1", "value1");
        optionsMap.put("custom_option2", "value2");

        JavaGeneratorOptions options = JavaGeneratorOptions.fromMap(optionsMap);

        // Verify standard option was set
        assertTrue(options.isBeans());

        // Verify custom options were stored
        Map<String, String> customOptions = options.getCustomOptions();
        assertEquals(2, customOptions.size());
        assertEquals("value1", customOptions.get("custom_option1"));
        assertEquals("value2", customOptions.get("custom_option2"));

        // Also test direct access
        assertEquals("value1", options.getCustomOption("custom_option1"));
        assertEquals("value2", options.getCustomOption("custom_option2"));
        assertNull(options.getCustomOption("non_existent_option"));
    }

    @Test
    public void testSettersAndGetters() {
        JavaGeneratorOptions options = new JavaGeneratorOptions();

        // Test the setters that have 0% coverage
        options.setPrivateMembers(true);
        assertTrue(options.isPrivateMembers());

        options.setJava5(true);
        assertTrue(options.isJava5());

        options.setAndroidStyle(true);
        assertTrue(options.isAndroidStyle());

        options.setSortedContainers(true);
        assertTrue(options.isSortedContainers());

        options.setReuseObjects(true);
        assertTrue(options.isReuseObjects());

        options.setUndatedGeneratedAnnotations(true);
        assertTrue(options.isUndatedGeneratedAnnotations());

        options.setSuppressGeneratedAnnotations(true);
        assertTrue(options.isSuppressGeneratedAnnotations());

        options.setRethrowUnhandledExceptions(true);
        assertTrue(options.isRethrowUnhandledExceptions());

        options.setJakartaAnnotations(true);
        assertTrue(options.isJakartaAnnotations());

        options.setAnnotationsAsMetadata(true);
        assertTrue(options.isAnnotationsAsMetadata());

        options.setNocamel(true);
        assertTrue(options.isNocamel());
    }

    @Test
    public void testCustomOptionsManagement() {
        JavaGeneratorOptions options = new JavaGeneratorOptions();

        // Test setting custom options
        options.setCustomOption("key1", "value1");
        assertEquals("value1", options.getCustomOption("key1"));

        // Test overwriting an option
        options.setCustomOption("key1", "new_value");
        assertEquals("new_value", options.getCustomOption("key1"));

        // Test defensive copy of custom options map
        Map<String, String> customOptions = options.getCustomOptions();
        assertEquals(1, customOptions.size());
        assertEquals("new_value", customOptions.get("key1"));

        // Modify the returned map
        customOptions.put("key2", "value2");

        // The original map should not be affected
        assertNull(options.getCustomOption("key2"));

        // The returned map should be a new copy each time
        Map<String, String> newCustomOptions = options.getCustomOptions();
        assertEquals(1, newCustomOptions.size());
        assertFalse(customOptions == newCustomOptions);
    }
}
