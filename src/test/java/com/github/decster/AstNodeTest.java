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

public class AstNodeTest extends TestCase {

    public AstNodeTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AstNodeTest.class);
    }

    public void testProgramNode() {
        ProgramNode program = new ProgramNode();
        NamespaceNode ns = new NamespaceNode("java", "com.test");
        program.addHeader(ns);
        StructNode sn = new StructNode("MyStruct");
        program.addDefinition(sn);

        assertEquals(1, program.headers.size());
        assertEquals(ns, program.headers.get(0));
        assertEquals(1, program.definitions.size());
        assertEquals(sn, program.definitions.get(0));
    }

    public void testStructAndFieldNodes() {
        StructNode struct = new StructNode("TestStruct");
        assertEquals("TestStruct", struct.name);
        assertTrue(struct.fields.isEmpty());

        BaseTypeNode typeInt = new BaseTypeNode("i32");
        FieldNode field1 = new FieldNode(1, "required", typeInt, "id");
        struct.addField(field1);

        assertEquals(1, struct.fields.size());
        assertEquals(field1, struct.fields.get(0));
        assertEquals(Integer.valueOf(1), field1.fieldId);
        assertEquals("required", field1.requiredness);
        assertEquals(typeInt, field1.fieldType);
        assertEquals("id", field1.name);
        assertEquals("i32", field1.fieldType.getTypeName());
    }

    public void testServiceAndFunctionNodes() {
        ServiceNode service = new ServiceNode("MyService", "OtherService.Iface");
        assertEquals("MyService", service.name);
        assertEquals("OtherService.Iface", service.extendsService);

        VoidTypeNode voidType = new VoidTypeNode();
        FunctionNode func1 = new FunctionNode(true, voidType, "ping");
        service.addFunction(func1);

        assertEquals(1, service.functions.size());
        assertEquals(func1, service.functions.get(0));
        assertTrue(func1.oneway);
        assertEquals("void", func1.returnType.getTypeName());
        assertEquals("ping", func1.name);
        assertTrue(func1.arguments.isEmpty());
    }
}
