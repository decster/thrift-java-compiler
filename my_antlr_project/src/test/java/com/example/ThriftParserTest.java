package com.example;

import com.example.ast.Node;
import com.example.ast.ProgramNode;
import com.example.ast.StructNode;
import com.example.ast.FieldNode;
import com.example.ast.NamespaceNode;
import com.example.ast.BaseTypeNode;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import java.util.ArrayList;
import java.util.List;

import com.example.parser.ThriftLexer;
import com.example.parser.ThriftParser;


public class ThriftParserTest extends TestCase {

    public ThriftParserTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ThriftParserTest.class);
    }

    private ProgramNode parseString(String thriftContent) throws IOException {
        ThriftLexer lexer = new ThriftLexer(CharStreams.fromReader(new StringReader(thriftContent)));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ThriftParser parser = new ThriftParser(tokens);

        // Optional: Add error listener to capture syntax errors if needed for specific tests
        // parser.removeErrorListeners(); // Remove default console error listener
        // TestErrorListener errorListener = new TestErrorListener();
        // parser.addErrorListener(errorListener);

        ThriftParser.DocumentContext tree = parser.document();
        // if (errorListener.getSyntaxErrors().size() > 0) {
        //    throw new RuntimeException("Syntax errors found: " + errorListener.getSyntaxErrors());
        // }

        SimpleThriftAstBuilder astBuilder = new SimpleThriftAstBuilder();
        return (ProgramNode) astBuilder.visit(tree);
    }

    private ProgramNode parseFile(String filePath) throws IOException {
        ThriftLexer lexer = new ThriftLexer(CharStreams.fromPath(Paths.get(filePath)));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ThriftParser parser = new ThriftParser(tokens);
        ThriftParser.DocumentContext tree = parser.document();
        SimpleThriftAstBuilder astBuilder = new SimpleThriftAstBuilder();
        return (ProgramNode) astBuilder.visit(tree);
    }


    public void testSimpleStruct() throws IOException {
        String thriftContent =
            "namespace java com.example.testing\n" +
            "struct Point {\n" +
            "  1: required double x,\n" +  // Comma is optional for last element by grammar
            "  2: optional double y\n" +
            "}";
        ProgramNode program = parseString(thriftContent);
        assertNotNull("ProgramNode should not be null", program);
        assertEquals("Should have 1 header", 1, program.headers.size());
        assertTrue("Header should be NamespaceNode", program.headers.get(0) instanceof NamespaceNode);
        NamespaceNode ns = (NamespaceNode) program.headers.get(0);
        assertEquals("Namespace scope should be 'java'", "java", ns.scope);
        assertEquals("Namespace name should be 'com.example.testing'", "com.example.testing", ns.name);

        assertEquals("Should have 1 definition", 1, program.definitions.size());
        assertTrue("Definition should be StructNode", program.definitions.get(0) instanceof StructNode);
        StructNode struct = (StructNode) program.definitions.get(0);
        assertEquals("Struct name should be 'Point'", "Point", struct.name);
        assertEquals("Struct should have 2 fields", 2, struct.fields.size());

        FieldNode fieldX = struct.fields.get(0);
        assertEquals("Field 1 name should be 'x'", "x", fieldX.name);
        assertEquals("Field 1 ID should be 1", Integer.valueOf(1), fieldX.fieldId);
        assertEquals("Field 1 requiredness should be 'required'", "required", fieldX.requiredness);
        assertTrue("Field 1 type should be BaseTypeNode", fieldX.fieldType instanceof BaseTypeNode);
        assertEquals("Field 1 type should be 'double'", "double", ((BaseTypeNode)fieldX.fieldType).typeName);

        FieldNode fieldY = struct.fields.get(1);
        assertEquals("Field 2 name should be 'y'", "y", fieldY.name);
        assertEquals("Field 2 ID should be 2", Integer.valueOf(2), fieldY.fieldId);
        assertEquals("Field 2 requiredness should be 'optional'", "optional", fieldY.requiredness);
        assertTrue("Field 2 type should be BaseTypeNode", fieldY.fieldType instanceof BaseTypeNode);
        assertEquals("Field 2 type should be 'double'", "double", ((BaseTypeNode)fieldY.fieldType).typeName);
    }
}
