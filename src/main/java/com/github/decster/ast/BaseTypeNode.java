package com.github.decster.ast;

/**
 * Represents a base type in a Thrift document (bool, byte, i16, i32, i64, double, string, binary).
 */
public class BaseTypeNode extends TypeNode {
    public enum BaseTypeEnum {
        BOOL, BYTE, I16, I32, I64, DOUBLE, STRING, BINARY
    }

    private BaseTypeEnum type;

    public BaseTypeNode(BaseTypeEnum type) {
        this.type = type;
    }

    public BaseTypeEnum getType() {
        return type;
    }

    public void setType(BaseTypeEnum type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.name().toLowerCase();
    }
}
