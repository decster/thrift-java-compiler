package com.github.decster.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * This represents a variable scope used for looking up predefined types and
 * services. Typically, a scope is associated with a TProgram. Scopes are not
 * used to determine code generation, but rather to resolve identifiers at
 * parse time.
 * Corresponds to t_scope.h in the C++ implementation.
 */
public class TScope {
    private Map<String, TType> types;
    private Map<String, TService> services;
    private Map<String, TConst> constants;

    public TScope() {
        types = new HashMap<>();
        services = new HashMap<>();
        constants = new HashMap<>();
    }

    public void addType(String name, TType type) {
        types.put(name, type);
    }

    public TType getType(String name) {
        return types.get(name);
    }

    public void addService(String name, TService service) {
        services.put(name, service);
    }

    public TService getService(String name) {
        return services.get(name);
    }

    public void addConstant(String name, TConst constant) {
        constants.put(name, constant);
    }

    public TConst getConstant(String name) {
        return constants.get(name);
    }
}
