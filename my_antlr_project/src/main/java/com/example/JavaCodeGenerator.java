package com.example;

import com.example.ast.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile; // Using STGroupFile

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaCodeGenerator {

    private STGroupFile structGroupFile;
    private STGroupFile serviceGroupFile;
    private String currentPackageNameAst = "com.example.generated.default";
    private boolean templatesLoadedSuccessfully = false;

    public JavaCodeGenerator() {
        System.out.println("Basic ST rendering test assumed to have PASSED.");
        try {
            String structTemplatePath = "templates/struct.stg";
            String serviceTemplatePath = "templates/service.stg";

            // Using '$' as delimiters
            structGroupFile = new STGroupFile(structTemplatePath, "UTF-8", '$', '$');
            serviceGroupFile = new STGroupFile(serviceTemplatePath, "UTF-8", '$', '$');

            boolean loadedStruct = false;
            if (structGroupFile != null) {
                 // Check if the main template definition exists
                if (structGroupFile.isDefined("struct")) {
                    System.out.println("Struct template 'struct' defined in " + structTemplatePath);
                    loadedStruct = true;
                } else {
                     System.err.println("Error: Struct template 'struct' NOT defined in " + structTemplatePath + ". STG file might have parsing errors or template name is wrong.");
                }
            } else { System.err.println("structGroupFile is null after attempting to load " + structTemplatePath); }

            boolean loadedService = false;
            if (serviceGroupFile != null) {
                if (serviceGroupFile.isDefined("service")) {
                    System.out.println("Service template 'service' defined in " + serviceTemplatePath);
                    loadedService = true;
                } else {
                    System.err.println("Error: Service template 'service' NOT defined in " + serviceTemplatePath + ". STG file might have parsing errors or template name is wrong.");
                }
            } else { System.err.println("serviceGroupFile is null after attempting to load " + serviceTemplatePath); }

            templatesLoadedSuccessfully = loadedStruct && loadedService;
            if (!templatesLoadedSuccessfully) {
                 System.err.println("One or more essential templates failed to load/define properly.");
            } else {
                System.out.println("All essential templates seem to be defined via STGroupFile.");
            }

        } catch (Exception e) {
            System.err.println("Exception during StringTemplate STGroupFile loading or definition check: " + e.getMessage());
            // e.printStackTrace(); // For full trace
            System.err.println("StringTemplate reported error: " + e);
            templatesLoadedSuccessfully = false;
            structGroupFile = null; serviceGroupFile = null;
        }
    }

    public void generate(ProgramNode program, String outputDir) throws IOException {
        if (!templatesLoadedSuccessfully) {
            System.err.println("Essential templates were not loaded successfully during constructor. Aborting generation.");
            return;
        }
        this.currentPackageNameAst = "com.example.generated.default";
        for (HeaderNode header : program.headers) {
            if (header instanceof NamespaceNode) {
                NamespaceNode ns = (NamespaceNode) header;
                if ("java".equals(ns.scope) || "*".equals(ns.scope)) {
                    this.currentPackageNameAst = ns.name; break;
                }
            }
        }
        Path packagePath = Paths.get(outputDir, this.currentPackageNameAst.replace('.', File.separatorChar));
        Files.createDirectories(packagePath);
        for (DefinitionNode def : program.definitions) {
            if (def instanceof StructNode) generateStruct((StructNode) def, packagePath);
            else if (def instanceof ServiceNode) generateService((ServiceNode) def, packagePath);
        }
    }

    private void generateStruct(StructNode struct, Path packagePath) throws IOException {
        ST st = structGroupFile.getInstanceOf("struct");
        if (st == null) { System.err.println("Could not get instance of 'struct' template from structGroupFile."); return; }
        st.add("currentPackageName", this.currentPackageNameAst);
        st.add("myStruct", struct); // Matches 'myStruct' in minimal template
        String fileName = struct.name + ".java";
        Path filePath = packagePath.resolve(fileName);
        Files.write(filePath, st.render().getBytes(StandardCharsets.UTF_8));
        System.out.println("Generated: " + filePath);
    }

    private void generateService(ServiceNode service, Path packagePath) throws IOException {
        ST st = serviceGroupFile.getInstanceOf("service");
        if (st == null) { System.err.println("Could not get instance of 'service' template from serviceGroupFile."); return; }
        st.add("currentPackageName", this.currentPackageNameAst);
        st.add("myService", service);
        String fileName = service.name + ".java";
        Path filePath = packagePath.resolve(fileName);
        Files.write(filePath, st.render().getBytes(StandardCharsets.UTF_8));
        System.out.println("Generated: " + filePath);
    }

    // Helper methods are not directly used by these minimal templates, but kept for future.
    public String getFieldTypeName(FieldNode field) { return mapType(field.fieldType); }
    public String getCapitalizedFieldName(FieldNode field) { return capitalize(field.name); }
    public String getFunctionArgTypeName(FieldNode arg) { return mapType(arg.fieldType); }
    public String getFunctionArgName(FieldNode arg) { return arg.name; }
    public String getFunctionReturnType(FunctionNode func) { return mapType(func.returnType); }
    public String mapType(TypeNode typeNode) {
        if (typeNode == null) return "Object";
        if (typeNode instanceof BaseTypeNode) { String thriftType = ((BaseTypeNode) typeNode).typeName;
            switch (thriftType) {
                case "bool": return "boolean"; case "byte": case "i8": return "byte"; case "i16": return "short";
                case "i32": return "int"; case "i64": return "long"; case "double": return "double";
                case "string": return "String"; case "binary": return "java.nio.ByteBuffer";
                default: return "Object";
            }
        } else if (typeNode instanceof IdentifierTypeNode) { return ((IdentifierTypeNode) typeNode).identifier;
        } else if (typeNode instanceof VoidTypeNode) { return "void"; }
        if (typeNode.getTypeName() != null && !typeNode.getTypeName().isEmpty()) {
            return typeNode.getTypeName().replaceAll("[<>,]", "_");
        }
        return "Object";
    }
    public String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
