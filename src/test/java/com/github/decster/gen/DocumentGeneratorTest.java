package com.github.decster.gen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.decster.ThriftAstBuilder;
import com.github.decster.ast.DocumentNode;
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
  public void testGenerateMatchesJavagen() throws IOException {
    // Parse the thrift file to build an AST
    URL url = getClass().getClassLoader().getResource(
        "parser_tests/thrift_test.thrift");
    assertNotNull(url, "Test file not found in resources");

    Path filePath = Paths.get(url.getPath());
    String content = Files.readString(filePath);
    DocumentNode documentNode = ThriftAstBuilder.buildFromString(content);

    assertNotNull(documentNode, "Failed to build AST from thrift file");

    // Generate code using DocumentGenerator
    DocumentGenerator generator =
        new DocumentGenerator(documentNode, "2025-06-03");
    Map<String, String> generatedFiles = generator.generate();

    assertNotNull(generatedFiles, "Generated files map should not be null");

    // Compare with files in javagen directory
    URL javagenUrl = getClass().getClassLoader().getResource("javagen");
    assertNotNull(javagenUrl, "Javagen directory not found in resources");
    Map<String, String> expectedFiles= loadExpectedFiles(javagenUrl);
    // TODO: load expected files from src/test/resources/javagen directory
    verifyGeneratedFilesMatchExpected(generatedFiles, expectedFiles);
  }

  private Map<String, String> loadExpectedFiles(URL directoryUrl) throws IOException {
    Path directoryPath = Paths.get(directoryUrl.getPath());
    return Files.walk(directoryPath)
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
  }

  private void
  verifyGeneratedFilesMatchExpected(Map<String, String> generatedFiles,
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

      assertEquals(expectedContent, generatedContent,
                   "Generated content doesn't match expected for: " +
                       relativePath);
    }

    // Check that no extra files are generated
    assertEquals(expectedFiles.size(), generatedFiles.size(),
                 "Number of generated files should match expected files");
  }

  private String normalizeLineEndings(String content) {
    return content.replaceAll("\r\n", "\n");
  }
}
