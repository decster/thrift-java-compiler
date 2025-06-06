package com.github.decster.gen;

import com.github.decster.ast.ConstNode;
import com.github.decster.ast.DocumentNode;

import java.util.List;

public class ConstsGenerator implements Generator {
    DocumentNode documentNode;
    List<ConstNode> consts;
    String packageName;
    String date;

    ConstsGenerator(DocumentNode documentNode, List<ConstNode> consts, String packageName, String date) {
        this.documentNode = documentNode;
        this.consts = consts;
        this.packageName = packageName;
        this.date = date;
    }

    @Override
    public String generate() {
        return "\n\n";
    }
}
