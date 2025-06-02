package com.github.decster.ast;

import java.util.List;
import java.util.ArrayList;

public class ProgramNode implements Node {
    public final List<HeaderNode> headers = new ArrayList<>();
    public final List<DefinitionNode> definitions = new ArrayList<>();

    public void addHeader(HeaderNode header) {
        this.headers.add(header);
    }

    public void addDefinition(DefinitionNode definition) {
        this.definitions.add(definition);
    }

    @Override
    public String toString() {
        return "Program(headers=" + headers.size() + ", definitions=" + definitions.size() + ")";
    }
}
