package com.github.decster.ast;

/**
 * A typedef is a mapping from a symbolic name to another type. In dynamically
 * typed languages (i.e. php/python) the code generator can actually usually
 * ignore typedefs and just use the underlying type directly, though in statically
 * typed languages the symbolic naming can be quite useful for code clarity.
 * Corresponds to t_typedef.h in the C++ implementation.
 */
public class TTypedef extends TType {
    private TType type;
    private String symbolic;
    private boolean forward;

    /**
     * Standard constructor
     */
    public TTypedef(TProgram program, TType type, String symbolic) {
        setProgram(program);
        setName(symbolic);
        this.type = type;
        this.symbolic = symbolic;
        this.forward = false;
    }

    /**
     * This constructor is used to refer to a type that is lazily
     * resolved at a later time, like for forward declarations or
     * recursive types.
     */
    public TTypedef(TProgram program, String symbolic, boolean forward) {
        setProgram(program);
        setName(symbolic);
        this.type = null;
        this.symbolic = symbolic;
        this.forward = forward;
    }

    public TType getType() {
        return type;
    }

    public void setType(TType type) {
        this.type = type;
    }

    public String getSymbolic() {
        return symbolic;
    }

    public boolean isForward() {
        return forward;
    }

    @Override
    public TType getTrueType() {
        return type.getTrueType();
    }

    @Override
    public boolean isTypedef() {
        return true;
    }
}
