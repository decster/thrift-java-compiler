package com.github.decster.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic representation of a thrift type. These objects are used by the
 * parser module to build up a tree of objects that are all explicitly typed.
 * Corresponds to t_type.h in the C++ implementation.
 */
public class TType extends TDoc {
    private String name;
    private TProgram program;
    private Map<String, List<String>> annotations = new HashMap<>();

    public TType() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProgram(TProgram program) {
        this.program = program;
    }

    public TProgram getProgram() {
        return program;
    }

    public void setAnnotations(Map<String, List<String>> annotations) {
        this.annotations = annotations;
    }

    public Map<String, List<String>> getAnnotations() {
        return annotations;
    }

    public boolean hasAnnotations() {
        return annotations != null && !annotations.isEmpty();
    }

    public boolean isVoid() {
        return false;
    }

    public boolean isBaseType() {
        return false;
    }

    public boolean isUUID() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isBinary() {
        return false;
    }

    public boolean isBool() {
        return false;
    }

    public boolean isContainer() {
        return false;
    }

    public boolean isList() {
        return false;
    }

    public boolean isSet() {
        return false;
    }

    public boolean isMap() {
        return false;
    }

    public boolean isStruct() {
        return false;
    }

    public boolean isEnum() {
        return false;
    }

    public boolean isTypedef() {
        return false;
    }

    public boolean isService() {
        return false;
    }

    public boolean isXception() {
        return false;
    }

    public TType getTrueType() {
        return this;
    }

    @Override
    public String toString() {
        return "type(" + name + ")";
    }
}
