package com.github.decster.gen;

import com.github.decster.ast.ConstNode;
import com.github.decster.ast.DefinitionNode;
import com.github.decster.ast.DocumentNode;
import com.github.decster.ast.EnumNode;
import com.github.decster.ast.HeaderNode;
import com.github.decster.ast.NamespaceNode;
import com.github.decster.ast.ServiceNode;
import com.github.decster.ast.StructLikeNode;
import com.github.decster.ast.StructNode;
// No direct need for ConstNode, etc. here as we only dispatch based on major definition types

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentGenerator {
    private final DocumentNode documentNode;
    private final String date;
    private String resolvedPackageName = "generated"; // Default package name

    public DocumentGenerator(DocumentNode documentNode, String date) {
        this.documentNode = documentNode;
        this.date = date;
    }

    /**
     * Generates a map of generated file content, file path(relative to the output directory) -> file content as string
     * @return Map<String, String> where the key is the file path and the value is the file content
     */
    public Map<String, String> generate() {
        Map<String, String> generatedFiles = new HashMap<>();

        // 1. Resolve Package Name
        List<HeaderNode> headers = documentNode.getHeaders();
        if (headers != null) {
            for (HeaderNode header : headers) {
                if (header instanceof NamespaceNode) {
                    NamespaceNode ns = (NamespaceNode) header;
                    // Use ns.getName() instead of ns.getIdentifier()
                    if ("java".equals(ns.getScope())) {
                        this.resolvedPackageName = ns.getName();
                        break;
                    } else if ("*".equals(ns.getScope())) {
                        // Use wildcard as a fallback if no java specific one is found
                        if (this.resolvedPackageName.equals("generated") || !"java".equals(getScopeOfPackage(this.resolvedPackageName, headers))) {
                             this.resolvedPackageName = ns.getName();
                        }
                    }
                }
            }
        }

        // Refined Warning Logic
        final String currentPkg = this.resolvedPackageName; // effectively final for lambda
        boolean javaNsFound = headers != null && headers.stream()
            .anyMatch(h -> h instanceof NamespaceNode && "java".equals(((NamespaceNode)h).getScope()));
        boolean anyNsFound = headers != null && headers.stream().anyMatch(h -> h instanceof NamespaceNode);

        if (!javaNsFound) {
            if (anyNsFound) {
                 System.err.println("Warning: No 'java' specific namespace found. Using best alternative or default '" + currentPkg + "'.");
            } else {
                 System.err.println("Warning: No namespace definition found in Thrift document. Using default package '" + currentPkg + "'.");
            }
        }


        // 2. Iterate Through Definitions
        List<DefinitionNode> definitions = documentNode.getDefinitions();
        if (definitions == null) {
            return generatedFiles;
        }

        Map<String, Boolean> options = new HashMap<>();
        for (DefinitionNode definition : definitions) {
            String generatedCode = null;
            String fileName = null;
            String entityName = null;

            // 3. Instantiate and Use Sub-Generators
            if (definition instanceof StructLikeNode) {
                StructLikeNode structNode = (StructLikeNode) definition;
                entityName = structNode.getName();
                // Pass documentNode to StructGenerator
                StructLikeGenerator structGenerator = new StructLikeGenerator(structNode, this.documentNode, resolvedPackageName, date, options);
                generatedCode = structGenerator.generate();
            } else if (definition instanceof EnumNode) {
                EnumNode enumNode = (EnumNode) definition;
                entityName = enumNode.getName();
                // EnumGenerator does not currently need documentNode for its own resolution tasks
                EnumGenerator enumGenerator = new EnumGenerator(enumNode, resolvedPackageName, date);
                generatedCode = enumGenerator.generate();
            } else if (definition instanceof ServiceNode) {
                ServiceNode serviceNode = (ServiceNode) definition;
                entityName = serviceNode.getName();
                // Pass documentNode to ServiceGenerator
                ServiceGenerator serviceGenerator = new ServiceGenerator(serviceNode, this.documentNode, resolvedPackageName, date);
                generatedCode = serviceGenerator.generate();
            }
            // Other definition types (TypedefNode, ConstNode) are currently skipped for direct file generation.

            if (generatedCode != null && entityName != null) {
                String packagePath = resolvedPackageName.isEmpty() ? "" : resolvedPackageName.replace('.', '/');
                if (!packagePath.isEmpty()) {
                    fileName = packagePath + "/" + entityName + ".java";
                } else {
                    fileName = entityName + ".java"; // For default (empty) package
                }
                generatedFiles.put(fileName, generatedCode);
            }
        }

        // generate constants
        List<ConstNode> consts = definitions.stream()
            .filter(def -> def instanceof ConstNode)
                .map(t -> (ConstNode) t)
            .collect(Collectors.toList());
        if (!consts.isEmpty()) {
            ConstsGenerator constsGenerator = new ConstsGenerator(documentNode, consts, resolvedPackageName, date);
            String constsCode = constsGenerator.generate();
            if (constsCode != null) {
                String packagePath = resolvedPackageName.isEmpty() ? "" : resolvedPackageName.replace('.', '/') + "/";
                String constsFileName = packagePath + documentNode.getName() + "Constants.java";
                generatedFiles.put(constsFileName, constsCode);
            }
        }
        return generatedFiles;
    }

    // Helper to check scope of a resolved package, in case wildcard was chosen first
    private String getScopeOfPackage(String packageName, List<HeaderNode> headers) {
        if (headers == null) return "";
        for (HeaderNode header : headers) {
            if (header instanceof NamespaceNode) {
                NamespaceNode ns = (NamespaceNode) header;
                if (ns.getName().equals(packageName)) {
                    return ns.getScope();
                }
            }
        }
        return "";
    }
}
