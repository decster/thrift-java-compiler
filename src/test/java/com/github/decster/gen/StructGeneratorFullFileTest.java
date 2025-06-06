package com.github.decster.gen;

import static com.github.decster.gen.GeneratorTestUtil.assertEqualsLineByLine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.decster.ThriftAstBuilder;
import com.github.decster.ast.DefinitionNode;
import com.github.decster.ast.DocumentNode;
import com.github.decster.ast.EnumNode;
import com.github.decster.ast.StructLikeNode;
import com.github.decster.ast.StructNode;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class StructGeneratorFullFileTest {
    @Test
    void testSingleFileCases() throws IOException {
        List<SingleFileCase> cases = loadSingleFileCasesFromResource();
        assertFalse(cases.isEmpty(), "No test cases found in single_file_tests directory");

        for (SingleFileCase testCase : cases) {
            testCase.test();
        }
    }

    static class SingleFileCase {
        String file;
        String idl;
        String expectedOutput;

        SingleFileCase(String file, String idl, String expectedOutput) {
            this.file = file;
            this.idl = idl;
            this.expectedOutput = expectedOutput;
        }

        void test() throws IOException {
            DocumentNode documentNode = ThriftAstBuilder.buildFromString(idl);
            Map<String, Boolean> options = new HashMap<>();
            DefinitionNode definitionNode = documentNode.getDefinitions().get(0);
            Generator gen;
            if (definitionNode instanceof StructLikeNode) {
                gen = new StructLikeGenerator((StructLikeNode) definitionNode, documentNode,
                        documentNode.getPackageName(), "2025-06-06", options);
            } else if (definitionNode instanceof EnumNode) {
                gen = new EnumGenerator((EnumNode) definitionNode, documentNode.getPackageName(), "2025-06-06");
            } else {
                throw new IllegalArgumentException("Unsupported definition type: " + definitionNode.getClass().getSimpleName());
            }
            String generatedCode = gen.generate();
            assertEqualsLineByLine(file, generatedCode, expectedOutput);
        }
    }

    List<SingleFileCase> loadSingleFileCasesFromResource() throws IOException {
        // load file pairs from test/resources/single_file_tests
        List<SingleFileCase> cases = new ArrayList<>();

        URL resourceUrl =
                getClass().getClassLoader().getResource("single_file_tests");
        if (resourceUrl == null) {
            return cases;
        }

        File resourceDir = new File(resourceUrl.getFile());
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            File[] idlFiles =
                    resourceDir.listFiles((dir, name) -> name.endsWith(".thrift"));
            if (idlFiles != null) {
                for (File idlFile : idlFiles) {
                    String baseName = idlFile.getName().replace(".thrift", "");
                    File javaFile = new File(resourceDir, "com/example/thrift/" + baseName + ".java");
                    if (javaFile.exists()) {
                        String idl = Files.readString(idlFile.toPath());
                        String expectedOutput = Files.readString(javaFile.toPath());
                        cases.add(new SingleFileCase(idlFile.getName(), idl, expectedOutput));
                    }
                }
            }
        }
        return cases;
    }
}