package com.github.decster.gen;

import com.github.decster.ast.DefinitionNode;
import com.github.decster.ast.DocumentNode;
import com.github.decster.ast.EnumNode;
import com.github.decster.ast.HeaderNode;
import com.github.decster.ast.NamespaceNode;
import com.github.decster.ast.ServiceNode;
import com.github.decster.ast.StructNode;


import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class DocumentGenerator {
    DocumentNode documentNode;
    String date;

    public DocumentGenerator(DocumentNode documentNode, String date) {
        this.documentNode = documentNode;
        this.date = date;
    }

    /**
     * Generates a map of generated file content, file path(relative to the output directory) -> file content as string
     * @return Map<String, String> where the key is the file path and the value is the file content
     */
    public Map<String, String> generate() {
        // TODO:
        return new HashMap<String, String>();
    }
}
