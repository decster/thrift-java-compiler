package com.github.decster.gen;

import com.github.decster.ThriftAstBuilder;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import com.github.decster.ast.TProgram;
import org.junit.jupiter.api.Test;

public class JavaGeneratorTest {
    private static String loadResourceFile(String filePath) throws Exception {
        URL resourceUrl = JavaGeneratorTest.class.getResource("/"+filePath);
        return Files.readString(new File(resourceUrl.getPath()).toPath());
    }

    @Test
    void testGenEnum() throws Exception {
        String idl = loadResourceFile("single_file_tests/DemoEnum.thrift");
        String genJava = loadResourceFile("single_file_tests/com/example/thrift/DemoEnum.java");
        TProgram program = ThriftAstBuilder.parseString(idl, "DemoEnum.thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateEnum(program.getEnums().get(0));
        GeneratorTestUtil.assertEqualsLineByLine("DemoEnum.thrift", result.content, genJava);
    }

    @Test
    void testGenConsts() throws Exception {
        String idl = loadResourceFile("single_file_tests/someConst.thrift");
        String genJava = loadResourceFile("single_file_tests/com/example/thrift/someConstConstants.java");
        TProgram program = ThriftAstBuilder.parseString(idl, "someConst.thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateConstants();
        GeneratorTestUtil.assertEqualsLineByLine("someConst.thrift", result.content, genJava);
    }

    @Test
    void testGenStruct() throws Exception {
        singleTestGenStruct("ManyFields");
        singleTestGenStruct("ManyFields2");
        singleTestGenStruct("ManyFields3");
        singleTestGenStruct("Point");
        singleTestGenStruct("Xtruct");
    }

    @Test
    void testGenXception() throws Exception {
        singleTestGenXception("Xception");
    }

    @Test
    void testGenService() throws Exception {
        singleTestGenService("DemoService");
    }

    void singleTestGenStruct(String structName) throws Exception {
        String idl = loadResourceFile("single_file_tests/" + structName + ".thrift");
        String genJava = loadResourceFile("single_file_tests/com/example/thrift/" + structName + ".java");
        TProgram program = ThriftAstBuilder.parseString(idl, structName+".thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateStruct(program.getStructs().get(0), false);
        GeneratorTestUtil.assertEqualsLineByLine(structName, result.content, genJava);
    }

    void singleTestGenXception(String xceptionName) throws Exception {
        String idl = loadResourceFile("single_file_tests/" + xceptionName + ".thrift");
        String genJava = loadResourceFile("single_file_tests/com/example/thrift/" + xceptionName + ".java");
        TProgram program = ThriftAstBuilder.parseString(idl, xceptionName+".thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateStruct(program.getXceptions().get(0), true);
        GeneratorTestUtil.assertEqualsLineByLine(xceptionName, result.content, genJava);
    }

    void singleTestGenService(String serviceName) throws Exception {
        String idl = loadResourceFile("single_file_tests" + "/" + serviceName + ".thrift");
        String genJava = loadResourceFile("single_file_tests/com/example/thrift/" + serviceName + ".java");
        TProgram program = ThriftAstBuilder.parseString(idl, serviceName+".thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateService(program.getServices().get(0));
        GeneratorTestUtil.assertEqualsLineByLine(serviceName, result.content, genJava);
    }

    @Test
    void testMultiFileGen() throws Exception {
        String file = "complex1.thrift";
        String idl = loadResourceFile("multi_file_tests/" + file);
        TProgram program = ThriftAstBuilder.parseString(idl, file);
        program.resolveTypeRefsAndConsts();
        JavaGenerator generator = new JavaGenerator(program, "." , null);
        generator.setTimestamp("2025-06-06");
        List<JavaGenerator.GenResult> results = generator.generate();
        for (JavaGenerator.GenResult result : results) {
            String genJava = loadResourceFile("multi_file_tests/thrift/test/" + result.filename);
            GeneratorTestUtil.assertEqualsLineByLine(file, result.content, genJava);
        }
    }
}
