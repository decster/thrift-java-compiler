package com.github.decster.gen;

import com.github.decster.ast.ServiceNode; // Assuming this AST node exists

public class ServiceGenerator {
    private final ServiceNode serviceNode;
    private final String packageName;
    private final String date;

    public ServiceGenerator(ServiceNode serviceNode, String packageName, String date) {
        this.serviceNode = serviceNode;
        this.packageName = packageName;
        this.date = date;
    }

    public String generate() {
        // TODO: Implement service generation logic
        return "";
    }
}
