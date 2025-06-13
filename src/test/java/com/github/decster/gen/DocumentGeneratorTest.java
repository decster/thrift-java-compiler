package com.github.decster.gen;

import static com.github.decster.gen.GeneratorTestUtil.assertEqualsLineByLine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.decster.ThriftAstBuilder;
//import com.github.decster.ast.DocumentNode;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class DocumentGeneratorTest {

  @Test
  public void testMultiFile() throws IOException {
    // Parse the thrift file to build an AST
    URL url = getClass().getClassLoader().getResource(
            "multi_file_tests/complex1.thrift");
    assertNotNull(url, "Test file not found in resources");

    Path filePath = Paths.get(url.getPath());
    String content = Files.readString(filePath);
//    DocumentNode documentNode = ThriftAstBuilder.buildFromFile(url.getPath());
//
//    assertNotNull(documentNode, "Failed to build AST from thrift file");
//
//    // Generate code using DocumentGenerator
//    DocumentGenerator generator =
//        new DocumentGenerator(documentNode, "2025-06-06");
//    Map<String, String> generatedFiles = generator.generate();

//    assertNotNull(generatedFiles, "Generated files map should not be null");

    // Compare with files in javagen directory
    URL javagenUrl = getClass().getClassLoader().getResource("multi_file_tests/thrift/test");
    assertNotNull(javagenUrl, "Javagen directory not found in resources");
    Path directoryPath = Paths.get(javagenUrl.getPath());
    Map<String, String> expectedFiles= Files.walk(directoryPath)
            .filter(Files::isRegularFile)
            .collect(Collectors.toMap(
                    path -> directoryPath.relativize(path).toString(),
                    path -> {
                      try {
                        return Files.readString(path);
                      } catch (IOException e) {
                        throw new RuntimeException("Failed to read file: " + path, e);
                      }
                    }
            ));
    // TODO: load expected files from src/test/resources/javagen directory
//    verifyGeneratedFilesMatchExpected(generatedFiles, expectedFiles);
  }

  private void dumpFiles(Map<String, String> files, String path) {
    files.forEach((filePath, content) -> {
      try {
        Path outputPath = Paths.get(path, filePath);
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, content);
      } catch (IOException e) {
        throw new RuntimeException("Failed to write file: " + filePath, e);
      }
    });
  }

  private void verifyGeneratedFilesMatchExpected(Map<String, String> generatedFiles,
                                    Map<String, String> expectedFiles) throws IOException {
    // Check that all expected files are generated
    for (Map.Entry<String, String> expectedEntry : expectedFiles.entrySet()) {
      String relativePath = expectedEntry.getKey();
      String expectedContent = expectedEntry.getValue();

      String generatedContent = generatedFiles.get(relativePath);
      assertNotNull(generatedContent,
                    "Generated content missing for: " + relativePath);

      // Normalize line endings for comparison
      expectedContent = normalizeLineEndings(expectedContent);
      generatedContent = normalizeLineEndings(generatedContent);

      assertEqualsLineByLine(relativePath, expectedContent, generatedContent);
    }

    // Check that no extra files are generated
    assertEquals(expectedFiles.size(), generatedFiles.size(),
                 "Number of generated files should match expected files");
  }

  private String normalizeLineEndings(String content) {
    return content.replaceAll("\r\n", "\n");
  }
}
