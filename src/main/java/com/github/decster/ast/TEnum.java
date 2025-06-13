package com.github.decster.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * An enumerated type. A list of constant objects with a name for the type.
 * Corresponds to t_enum.h in the C++ implementation.
 */
public class TEnum extends TType {
    private List<TEnumValue> constants;

    public TEnum(TProgram program) {
        setProgram(program);
        this.constants = new ArrayList<>();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public void append(TEnumValue constant) {
        constants.add(constant);
    }

    public List<TEnumValue> getConstants() {
        return constants;
    }

    public TEnumValue getConstantByName(String name) {
        for (TEnumValue value : constants) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public boolean isEnum() {
        return true;
    }
}
