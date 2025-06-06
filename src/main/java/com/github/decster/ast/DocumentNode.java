package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Thrift document, which is the root of the AST.
 */
public class DocumentNode extends Node {
    private List<HeaderNode> headerNodes;
    private List<DefinitionNode> definitions;

    public DocumentNode() {
        this.headerNodes = new ArrayList<>();
        this.definitions = new ArrayList<>();
    }

    public List<HeaderNode> getHeaders() {
        return headerNodes;
    }

    public void addHeader(HeaderNode headerNode) {
        this.headerNodes.add(headerNode);
    }

    public List<DefinitionNode> getDefinitions() {
        return definitions;
    }

    public void addDefinition(DefinitionNode definition) {
        this.definitions.add(definition);
    }

    public String getPackageName() {
        for (HeaderNode header : headerNodes) {
            if (header instanceof NamespaceNode) {
                NamespaceNode namespace = (NamespaceNode) header;
                if (namespace.getScope().equals("java")) {
                    return namespace.getName();
                }
            }
        }
        return null;
    }
}
