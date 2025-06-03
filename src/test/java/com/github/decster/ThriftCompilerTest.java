package com.github.decster;

import com.github.decster.parser.ThriftParser;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ThriftCompilerTest {

    /**
     * Tests for the ThriftCompiler class, specifically the parse method.
     * <p>
     * The parse method takes a String content as input, processes it using the
     * ANTLR-generated ThriftLexer and ThriftParser, and returns a ThriftParser.DocumentContext
     * representing the parsed syntax tree.
     */

    @Test
    void testParseValidThriftContent() throws IOException {
        // Arrange
        String validThriftContent = "namespace java example\nservice ExampleService { string ping(); }";

        // Act
        ThriftParser.DocumentContext result = ThriftCompiler.parse(validThriftContent);

        // Assert
        assertNotNull(result, "Parsed DocumentContext should not be null for valid input.");
        assertEquals("example", result.header(0).namespace_().IDENTIFIER(1).getText(),
                "The namespace should parse correctly.");
    }

    @Test
    void testParseEmptyContent() throws IOException {
        // Arrange
        String emptyContent = "";

        // Act
        ThriftParser.DocumentContext result = ThriftCompiler.parse(emptyContent);

        // Assert
        assertNotNull(result, "Parsed DocumentContext should not be null even for empty input.");
        assertTrue(result.getChildCount() == 1,
                "Parsed Tree should only contain EOF for empty content.");
    }

    @Test
    void testParseInvalidThriftContent() {
        // Arrange
        String invalidContent = "service MissingBracketService { string ping(); ";

        // Act and Assert
        Exception exception = assertThrows(ParseCancellationException.class, () -> {
            ThriftCompiler.parse(invalidContent);
        });
    }

    @Test
    void testParseOnlyComments() throws IOException {
        // Arrange
        String commentOnlyContent = "// This is a comment";

        // Act
        ThriftParser.DocumentContext result = ThriftCompiler.parse(commentOnlyContent);

        // Assert
        assertNotNull(result, "Parsed DocumentContext should not be null for comments.");
        assertEquals(1, result.children.size(),
                "Tree should only contain EOF for comment-only content.");
    }

    @Test
    void testParseComplexThriftContent() throws IOException {
        // Arrange
        String complexThriftContent = """
                namespace java com.example
                include "shared.thrift"
                
                service ComplexService {
                    void exampleMethod1(1: string param1, 2: i32 param2);
                    i32 exampleMethod2();
                }
                """;

        // Act
        ThriftParser.DocumentContext result = ThriftCompiler.parse(complexThriftContent);

        // Assert
        assertNotNull(result, "Parsed DocumentContext should not be null for complex input.");
        assertEquals("ComplexService",
                result.definition(0).service().IDENTIFIER(0).getText(),
                "The service name should parse correctly.");
    }
}