package com.github.decster.gen;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StructGeneratorFullFileTest {
    @Test
    void testSingleFileCases() throws IOException {
        List<SingleFileCase> cases = loadSingleFileCasesFromResource("single_file_tests");
        assertFalse(cases.isEmpty(), "No test cases found");
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
//            DocumentNode documentNode = ThriftAstBuilder.buildFromString(idl, file);
//            Map<String, Boolean> options = new HashMap<>();
//            String date = "2025-06-06"; // Use a fixed date for consistency in tests
//            Generator gen;
//            if (documentNode.getDefinitions().size() == 1) {
//                DefinitionNode definitionNode = documentNode.getDefinitions().get(0);
//                if (definitionNode instanceof StructLikeNode) {
//                    gen = new StructLikeGenerator((StructLikeNode) definitionNode, documentNode,
//                            documentNode.getPackageName(), date, options);
//                } else if (definitionNode instanceof EnumNode) {
//                    gen = new EnumGenerator((EnumNode) definitionNode, documentNode.getPackageName(), date);
//                } else if (definitionNode instanceof ServiceNode) {
//                    gen = new ServiceGenerator((ServiceNode) definitionNode, documentNode, documentNode.getPackageName(), date);
//                } else {
//                    throw new IllegalArgumentException("Unsupported definition type: " + definitionNode.getClass().getSimpleName());
//                }
//            } else {
//                List<ConstNode> consts = documentNode.getDefinitions().stream()
//                        .filter(def -> def instanceof ConstNode)
//                        .map(t -> (ConstNode) t)
//                        .collect(Collectors.toList());
//                if (!consts.isEmpty()) {
//                    gen = new ConstsGenerator(documentNode, consts, documentNode.getPackageName(), date);
//                } else {
//                    throw new IllegalArgumentException("No valid definition/consts found in document");
//                }
//            }
//            String generatedCode = gen.generate();
//            assertEqualsLineByLine(file, generatedCode, expectedOutput);
        }
    }

    static List<SingleFileCase> loadSingleFileCasesFromResource(String caseGroup) throws IOException {
        // load file pairs from test/resources/single_file_tests
        List<SingleFileCase> cases = new ArrayList<>();

        URL resourceUrl = StructGeneratorFullFileTest.class.getClassLoader().getResource(caseGroup);
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
                    String suffix = ".java";
                    if (caseGroup.equals("constants")) {
                        suffix = "Constants.java";
                    }
                    File javaFile = new File(resourceDir, "com/example/thrift/" + baseName + suffix);
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