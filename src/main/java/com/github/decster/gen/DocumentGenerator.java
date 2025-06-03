package com.github.decster.gen;

import com.github.decster.ast.DocumentNode;

import java.util.HashMap;
import java.util.Map;

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
