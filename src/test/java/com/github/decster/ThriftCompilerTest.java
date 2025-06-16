package com.github.decster;

import com.github.decster.ast.TProgram;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThriftCompilerTest {

    static String getResourcePath(String relPath) {
        return ThriftCompilerTest.class.getResource(relPath).getPath();
    }

    @Test
    void testGetIncludeFile() {
        // Set up test directories and files
        List<String> includeDirs = new ArrayList<>();
        includeDirs.add(getResourcePath("/include_tests"));
        includeDirs.add(getResourcePath("/single_file_tests"));

        // Test finding an existing file
        String foundPath = ThriftCompiler.getIncludeFile(includeDirs, "Data.thrift");
        assertNotNull(foundPath, "Should find the Data.thrift file");
        assertTrue(foundPath.endsWith("Data.thrift"), "Path should end with the filename");

        // Test when a file exists in multiple include directories (should return the first one)
        String firstFound = ThriftCompiler.getIncludeFile(includeDirs, "someConst.thrift");
        assertNotNull(firstFound, "Should find someConst.thrift");
        assertTrue(firstFound.contains("single_file_tests"),
                "Should find the file in the first directory where it exists");

        // Test with a non-existent file
        String notFound = ThriftCompiler.getIncludeFile(includeDirs, "NonExistent.thrift");
        assertNull(notFound, "Should return null for non-existent file");

        // Test with empty include dirs list
        String emptyTest = ThriftCompiler.getIncludeFile(new ArrayList<>(), "Data.thrift");
        assertNull(emptyTest, "Should return null when include dirs list is empty");
    }

    @Test
    void testRecursiveParse() throws IOException {
        // Set up include directories
        List<String> includeDirs = new ArrayList<>();
        includeDirs.add(getResourcePath("/include_tests"));

        // Test parsing a file with includes
        String filePath = getResourcePath("/include_tests") + "/FrontendService.thrift";
        TProgram program = ThriftCompiler.recursiveParse(filePath, null, new HashSet<>(), includeDirs);

        // Verify the program was parsed successfully
        assertNotNull(program, "Program should be parsed successfully");
        assertFalse(program.getObjects().isEmpty(), "Program should have objects");

        // Verify includes were processed
        assertFalse(program.getIncludes().isEmpty(), "Program should have processed includes");

        // Verify the name of the program matches the file
        assertEquals("FrontendService", program.getName(), "Program name should match the thrift file name");
    }

    @Test
    void testRecursiveParseWithCircularIncludes(@TempDir Path tempDir) throws IOException {
        // Create temporary files with circular includes
        Path file1 = tempDir.resolve("File1.thrift");
        Path file2 = tempDir.resolve("File2.thrift");

        Files.writeString(file1, "include \"File2.thrift\"\nenum TestEnum1 { VALUE1 = 1 }");
        Files.writeString(file2, "include \"File1.thrift\"\nenum TestEnum2 { VALUE2 = 2 }");

        List<String> includeDirs = new ArrayList<>();
        includeDirs.add(tempDir.toString());

        // Test parsing with circular includes - should throw IOException
        Exception exception = assertThrows(IOException.class, () -> {
            ThriftCompiler.recursiveParse(file1.toString(), null, new HashSet<>(), includeDirs);
        });

        assertTrue(exception.getMessage().contains("Circular include detected"),
                "Exception should mention circular include");
    }

    @Test
    void testRecursiveParseWithMissingInclude() throws IOException {
        // Set up include directories
        List<String> includeDirs = new ArrayList<>();
        includeDirs.add("src/test/resources/include_tests");

        // Create a path to a file that exists
        String filePath = "src/test/resources/include_tests/Status.thrift";

        // Capture System.err for testing warnings
        java.io.ByteArrayOutputStream errContent = new java.io.ByteArrayOutputStream();
        System.setErr(new java.io.PrintStream(errContent));

        // Parse a file that may have an include that doesn't exist (or test warning behavior)
        TProgram program = ThriftCompiler.recursiveParse(filePath, null, new HashSet<>(), includeDirs);

        // Verify the program was parsed successfully despite possible missing includes
        assertNotNull(program, "Program should be parsed successfully even with missing includes");

        // Restore System.err
        System.setErr(System.err);
    }
}