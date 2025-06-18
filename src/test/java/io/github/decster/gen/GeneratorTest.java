package io.github.decster.gen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for base Generator functionality to improve code coverage.
 */
public class GeneratorTest {

    // Create a test subclass to access protected methods
    private static class TestGenerator extends Generator {
        public String testGetEscapedString(String str) {
            return getEscapedString(str);
        }

        public String testIndent(int level) {
            return indent(level);
        }

        public String testEmitDoubleAsString(double value) {
            return emitDoubleAsString(value);
        }

        public String testTmp(String name) {
            return tmp(name);
        }
    }

    @Test
    void testGetEscapedString() {
        TestGenerator generator = new TestGenerator();

        // Test null input
        assertNull(generator.testGetEscapedString(null));

        // Test empty string
        assertEquals("", generator.testGetEscapedString(""));

        // Test regular characters
        assertEquals("abc123", generator.testGetEscapedString("abc123"));

        // Test special escape characters
        assertEquals("\\\"", generator.testGetEscapedString("\""));
        assertEquals("\\\\", generator.testGetEscapedString("\\"));
        assertEquals("\\b", generator.testGetEscapedString("\b"));
        assertEquals("\\f", generator.testGetEscapedString("\f"));
        assertEquals("\\n", generator.testGetEscapedString("\n"));
        assertEquals("\\r", generator.testGetEscapedString("\r"));
        assertEquals("\\t", generator.testGetEscapedString("\t"));

        // Test control characters
        assertEquals("\\u0000", generator.testGetEscapedString("\u0000"));
        assertEquals("\\u0001", generator.testGetEscapedString("\u0001"));
        assertEquals("\\u001f", generator.testGetEscapedString("\u001f"));

        // Test mixed string with multiple escape sequences
        assertEquals("Hello\\nWorld\\t\\\"Quote\\\"",
                generator.testGetEscapedString("Hello\nWorld\t\"Quote\""));
    }

    @Test
    void testIndentMethod() {
        TestGenerator generator = new TestGenerator();

        // Test zero indentation
        assertEquals("", generator.testIndent(0));

        // Test single indentation
        assertEquals("  ", generator.testIndent(1));

        // Test multiple indentation levels
        assertEquals("    ", generator.testIndent(2));
        assertEquals("      ", generator.testIndent(3));
        assertEquals("        ", generator.testIndent(4));
    }

    @Test
    void testEmitDoubleAsString() {
        TestGenerator generator = new TestGenerator();

        // Test zero
        assertEquals("0", generator.testEmitDoubleAsString(0.0));

        // Test positive integers
        assertEquals("1", generator.testEmitDoubleAsString(1.0));
        assertEquals("123", generator.testEmitDoubleAsString(123.0));

        // Test negative integers
        assertEquals("-1", generator.testEmitDoubleAsString(-1.0));

        // Test decimal values
        assertEquals("1.2345000000000000", generator.testEmitDoubleAsString(1.2345));
        assertEquals("0.0000010000000000", generator.testEmitDoubleAsString(0.000001));

        // Test value with more decimal places than the internal limit (16)
        assertEquals("1.2345678901234567", generator.testEmitDoubleAsString(1.23456789012345678));

        // Test scientific notation input
        assertEquals("1000000", generator.testEmitDoubleAsString(1e6));
        assertEquals("0.0000010000000000", generator.testEmitDoubleAsString(1e-6));
    }

    @Test
    void testTmp() {
        TestGenerator generator = new TestGenerator();

        // First call should return name + 0
        assertEquals("var0", generator.testTmp("var"));

        // Second call should increment counter
        assertEquals("temp1", generator.testTmp("temp"));

        // Third call with the same base name
        assertEquals("var2", generator.testTmp("var"));
    }
}
