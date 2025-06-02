package com.github.decster;

import com.github.decster.ast.*;
import com.github.decster.ast.BaseTypeNode;
import com.github.decster.ast.FieldNode;
import com.github.decster.ast.FunctionNode;
import com.github.decster.ast.NamespaceNode;
import com.github.decster.ast.ProgramNode;
import com.github.decster.ast.ServiceNode;
import com.github.decster.ast.StructNode;
import com.github.decster.ast.VoidTypeNode;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.io.File; // <--- ADDED IMPORT

public class JavaCodeGeneratorTest extends TestCase {

    private Path tempOutputDir;

    public JavaCodeGeneratorTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(JavaCodeGeneratorTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tempOutputDir = Paths.get("temp_codegen_output_" + System.currentTimeMillis());
        Files.createDirectories(tempOutputDir);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (tempOutputDir != null && Files.exists(tempOutputDir)) {
            Files.walk(tempOutputDir)
                 .sorted(Comparator.reverseOrder())
                 .map(Path::toFile)
                 .forEach(File::delete);
        }
    }

    public void testGenerateSimpleStruct() throws IOException {
        ProgramNode program = new ProgramNode();
        program.addHeader(new NamespaceNode("java", "com.example.testing"));
        StructNode struct = new StructNode("TestStruct");
        struct.addField(new FieldNode(1, "required", new BaseTypeNode("i32"), "id"));
        program.addDefinition(struct);

        JavaCodeGenerator generator = new JavaCodeGenerator();
        generator.generate(program, tempOutputDir.toString());

        Path expectedFile = tempOutputDir.resolve("com/example/testing/TestStruct.java");
        assertTrue("Generated struct file should exist: " + expectedFile, Files.exists(expectedFile));
        String content = new String(Files.readAllBytes(expectedFile));

        assertTrue("Content should contain package declaration", content.contains("package com.example.testing;"));
        assertTrue("Content should contain class definition", content.contains("public class TestStruct {"));
        assertTrue("Content should contain default constructor", content.contains("public TestStruct() {}"));
    }

    public void testGenerateSimpleService() throws IOException {
        ProgramNode program = new ProgramNode();
        program.addHeader(new NamespaceNode("java", "com.example.testing"));
        ServiceNode service = new ServiceNode("TestService", null);
        FunctionNode func = new FunctionNode(false, new VoidTypeNode(), "ping");
        service.addFunction(func);
        program.addDefinition(service);

        JavaCodeGenerator generator = new JavaCodeGenerator();
        generator.generate(program, tempOutputDir.toString());

        Path expectedFile = tempOutputDir.resolve("com/example/testing/TestService.java");
        assertTrue("Generated service file should exist: " + expectedFile, Files.exists(expectedFile));
        String content = new String(Files.readAllBytes(expectedFile));

        assertTrue("Content should contain package declaration", content.contains("package com.example.testing;"));
        assertTrue("Content should contain interface definition", content.contains("public interface TestService {"));
    }
}
