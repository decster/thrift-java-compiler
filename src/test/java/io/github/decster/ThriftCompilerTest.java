package io.github.decster;

import static org.junit.jupiter.api.Assertions.*;

import io.github.decster.ast.TProgram;
import io.github.decster.gen.GeneratorTestUtil;
import io.github.decster.gen.JavaGenerator;
import io.github.decster.gen.JavaGeneratorOptions;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ThriftCompilerTest {

  @Test
  void testCompileThriftFilesWithNullLogger(@TempDir Path tempDir) throws IOException {
    // Create a simple thrift file
    Path thriftFile = createSimpleThriftFile(tempDir);

    List<String> filesToParse = new ArrayList<>();
    filesToParse.add(thriftFile.toString());

    List<String> includeDirs = new ArrayList<>();
    includeDirs.add(tempDir.toString());

    // Test with null logger
    int result =
        ThriftCompiler.compileThriftFiles(
            filesToParse, tempDir.resolve("output").toString(), includeDirs, "beans", null);

    assertTrue(result > 0, "Should generate at least one file");
    assertTrue(Files.exists(tempDir.resolve("output")), "Output directory should be created");
  }

  @Test
  void testCompileThriftFilesWithCustomLogger(@TempDir Path tempDir) throws IOException {
    // Create a simple thrift file
    Path thriftFile = createSimpleThriftFile(tempDir);

    List<String> filesToParse = new ArrayList<>();
    filesToParse.add(thriftFile.toString());

    List<String> includeDirs = new ArrayList<>();
    includeDirs.add(tempDir.toString());

    List<String> logMessages = new ArrayList<>();
    Function<String, Void> logger =
        s -> {
          logMessages.add(s);
          return null;
        };

    int result =
        ThriftCompiler.compileThriftFiles(
            filesToParse,
            tempDir.resolve("output").toString(),
            includeDirs,
            "beans,private_members",
            logger);

    assertTrue(result > 0, "Should generate at least one file");
    assertFalse(logMessages.isEmpty(), "Logger should have received messages");
    assertTrue(
        logMessages.stream().anyMatch(s -> s.contains("Output directory:")),
        "Should log output directory");
    assertTrue(
        logMessages.stream().anyMatch(s -> s.contains("Include directories:")),
        "Should log include directories");
    assertTrue(
        logMessages.stream().anyMatch(s -> s.contains("Generator options:")),
        "Should log generator options");
    assertTrue(
        logMessages.stream().anyMatch(s -> s.contains("Total generated files:")),
        "Should log total files");
  }

  @Test
  void testCompileThriftFilesWithInvalidInput(@TempDir Path tempDir) {
    // Test with non-existent input file
    List<String> filesToParse = new ArrayList<>();
    filesToParse.add(tempDir.resolve("nonexistent.thrift").toString());

    List<String> includeDirs = new ArrayList<>();
    includeDirs.add(tempDir.toString());

    IOException exception =
        assertThrows(
            IOException.class,
            () -> {
              ThriftCompiler.compileThriftFiles(
                  filesToParse, tempDir.resolve("output").toString(), includeDirs, "", s -> null);
            });

    assertTrue(
        exception.getMessage().contains("Input file does not exist"),
        "Should throw appropriate exception for non-existent file");
  }

  @Test
  void testMainMethod(@TempDir Path tempDir) throws IOException {
    // Create a simple thrift file
    Path thriftFile = createSimpleThriftFile(tempDir);
    Path outputDir = tempDir.resolve("output");

    // Test with valid arguments
    String[] args = {
      "-o",
      outputDir.toString(),
      "-I",
      tempDir.toString(),
      "-g",
      "beans,private_members",
      thriftFile.toString()
    };

    // redirect System.out to avoid cluttering test output
    PrintStream originalOut = System.out;
    try {
      System.setOut(
          new java.io.PrintStream(
              new java.io.OutputStream() {
                @Override
                public void write(int b) {
                  // Do nothing, suppress output
                }
              }));
      ThriftCompiler.main(args);
      assertTrue(Files.exists(outputDir), "Output directory should be created");

      // Test help command
      String[] helpArgs = {"--help"};
      ThriftCompiler.main(helpArgs);
    } finally {
      // reset System.out to default
      System.setOut(originalOut);
    }
  }

  // Helper method to create a simple Thrift file for testing
  private Path createSimpleThriftFile(Path dir) throws IOException {
    Path file = dir.resolve("Simple.thrift");
    String content =
        "namespace java io.github.decster.test\n\n"
            + "struct SimpleStruct {\n"
            + "  1: string name\n"
            + "  2: i32 id\n"
            + "}\n\n"
            + "service SimpleService {\n"
            + "  SimpleStruct getSimple(1: i32 id)\n"
            + "}\n";

    Files.writeString(file, content);
    return file;
  }

  static String getResourcePath(String relPath) {
    String path = ThriftCompilerTest.class.getResource(relPath).getPath();
    // On Windows, fix "/C:/..." -> "C:/..."
    if (path.matches("^/[A-Za-z]:/.*")) {
      path = path.substring(1);
    }
    return path;
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
    assertTrue(firstFound.contains("single_file_tests"), "Should find the file in the first directory where it exists");

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
    includeDirs.add(getResourcePath("/include_tests"));
    String filePath = getResourcePath("/include_tests/Status.thrift");

    // Parse a file that may have an include that doesn't exist (or test warning behavior)
    TProgram program = ThriftCompiler.recursiveParse(filePath, null, new HashSet<>(), includeDirs);

    // Verify the program was parsed successfully despite possible missing includes
    assertNotNull(program, "Program should be parsed successfully even with missing includes");
  }

  @Test
  void testGenerateFullDir() throws IOException {
    String includeDir = getResourcePath("/include_tests");
    List<String> includeDirs = new ArrayList<>();
    includeDirs.add(includeDir);
    // get all thrift files in the include directory
    File dir = new File(includeDir);
    File[] thriftFiles = dir.listFiles((d, name) -> name.endsWith(".thrift"));
    Map<String, String> expectedJavaFilesMap = new TreeMap<>();
    int expectedJavaFilesCount = GeneratorTestUtil.globJavaFilesInDir(dir, expectedJavaFilesMap);
    int generatedJavaFilesCount = 0;
    for (File file : thriftFiles) {
      String filePath = file.getAbsolutePath();
      try {
        TProgram program = ThriftCompiler.recursiveParse(filePath, null, new HashSet<>(), includeDirs);
        assertNotNull(program, "Program should be parsed successfully for " + filePath);
        JavaGenerator generator = new JavaGenerator(program, ".", new JavaGeneratorOptions());
        generator.setTimestamp("2025-06-16");
        List<JavaGenerator.GenResult> results = generator.generate();
        for (JavaGenerator.GenResult result : results) {
          String expectedContent = expectedJavaFilesMap.get(result.filename);
          assertNotNull(expectedContent, "Expected content for " + result.filename + " not found");
          GeneratorTestUtil.assertEqualsLineByLine(result.filename, result.content, expectedContent);
        }
        generatedJavaFilesCount += results.size();
      } catch (IOException e) {
        fail("Failed to parse " + filePath + ": " + e.getMessage());
      }
    }
    assertEquals(expectedJavaFilesCount, generatedJavaFilesCount,
                 "Generated Java files count should match expected count");
  }
}