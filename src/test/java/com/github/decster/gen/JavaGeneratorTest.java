package com.github.decster.gen;

import com.github.decster.ThriftAstBuilder;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import com.github.decster.ast.TProgram;
import org.junit.jupiter.api.Test;

public class JavaGeneratorTest {

    @Test
    void testGenEnum() throws Exception {
        URL resourceUrl = JavaGeneratorTest.class.getResource("/single_file_tests");
        String resourcePath = resourceUrl.getPath();
        String idl = Files.readString(new File(resourcePath, "DemoEnum.thrift").toPath());
        String genJava = Files.readString(new File(resourcePath, "com/example/thrift/DemoEnum.java").toPath());
        TProgram program = ThriftAstBuilder.parseString(idl, "DemoEnum.thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateEnum(program.getEnums().get(0));
        GeneratorTestUtil.assertEqualsLineByLine("DemoEnum.thrift", result.content, genJava);
    }

    @Test
    void testGenConsts() throws Exception {
        URL resourceUrl = JavaGeneratorTest.class.getResource("/single_file_tests");
        String resourcePath = resourceUrl.getPath();
        String idl = Files.readString(new File(resourcePath, "someConst.thrift").toPath());
        String genJava = Files.readString(new File(resourcePath, "com/example/thrift/someConstConstants.java").toPath());
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
        URL resourceUrl = JavaGeneratorTest.class.getResource("/single_file_tests");
        String resourcePath = resourceUrl.getPath();
        String idl = Files.readString(new File(resourcePath, structName+".thrift").toPath());
        String genJava = Files.readString(new File(resourcePath, "com/example/thrift/"+structName+".java").toPath());
        TProgram program = ThriftAstBuilder.parseString(idl, structName+".thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateStruct(program.getStructs().get(0), false);
        GeneratorTestUtil.assertEqualsLineByLine(structName, result.content, genJava);
    }

    void singleTestGenXception(String xceptionName) throws Exception {
        URL resourceUrl = JavaGeneratorTest.class.getResource("/single_file_tests");
        String resourcePath = resourceUrl.getPath();
        String idl = Files.readString(new File(resourcePath, xceptionName+".thrift").toPath());
        String genJava = Files.readString(new File(resourcePath, "com/example/thrift/"+xceptionName+".java").toPath());
        TProgram program = ThriftAstBuilder.parseString(idl, xceptionName+".thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateStruct(program.getXceptions().get(0), true);
        GeneratorTestUtil.assertEqualsLineByLine(xceptionName, result.content, genJava);
    }

    void singleTestGenService(String serviceName) throws Exception {
        URL resourceUrl = JavaGeneratorTest.class.getResource("/single_file_tests");
        String resourcePath = resourceUrl.getPath();
        String idl = Files.readString(new File(resourcePath, serviceName+".thrift").toPath());
        String genJava = Files.readString(new File(resourcePath, "com/example/thrift/"+serviceName+".java").toPath());
        TProgram program = ThriftAstBuilder.parseString(idl, serviceName+".thrift");
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        JavaGenerator.GenResult result = generator.generateService(program.getServices().get(0));
        GeneratorTestUtil.assertEqualsLineByLine(serviceName, result.content, genJava);
    }

    @Test
    void testMultiFileGen() throws Exception {
        URL resourceUrl = JavaGeneratorTest.class.getResource("/multi_file_tests");
        String resourcePath = resourceUrl.getPath();
        String file = "complex1.thrift";
        String idl = Files.readString(new File(resourcePath, file).toPath());
        TProgram program = ThriftAstBuilder.parseString(idl, file);
        JavaGenerator generator = new JavaGenerator(program, "" , null);
        generator.setTimestamp("2025-06-06");
        List<JavaGenerator.GenResult> results = generator.generate();
        for (JavaGenerator.GenResult result : results) {
            String genJava = Files.readString(new File(resourcePath+"/thrift.test", result.filename).toPath());
            GeneratorTestUtil.assertEqualsLineByLine(file, result.content, genJava);
        }
    }
}
