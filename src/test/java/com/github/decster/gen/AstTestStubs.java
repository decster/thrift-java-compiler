package com.github.decster.gen;

import com.github.decster.ast.*; // Import all from the actual AST package
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// Common Stub classes for AST nodes used across generator tests.
// These stubs extend the actual AST classes to ensure type compatibility
// and proper inheritance of methods.

// Base Node Stub (if Node itself is abstract and needs concrete instantiation for tests)
class StubNode extends Node { // Assuming Node is abstract
    public StubNode() { super(); }
    public StubNode(int line, int column) { super(line, column); }
}

// TypeNode Stubs
abstract class StubTypeNodeBase extends TypeNode { // Make it abstract if TypeNode is, to be extended by concrete types
    public StubTypeNodeBase() {super();}
    // getName() is abstract in TypeNode, so concrete stubs must implement it
}

class StubBaseTypeNode extends BaseTypeNode {
    public StubBaseTypeNode(BaseTypeEnum type) {
        super(type);
    }
    // getName() is inherited
}

class StubIdentifierTypeNode extends IdentifierTypeNode {
    private boolean isEnumForTest = false; // Test-specific flag

    public StubIdentifierTypeNode(String name) {
        super(name);
    }
    public StubIdentifierTypeNode(String name, boolean isEnumForTest) {
        super(name);
        this.isEnumForTest = isEnumForTest;
    }
    // This is a mock-like method used by generator's resolveType logic if it relies on it.
    // The actual resolveType should use DocumentNode to check DefinitionNode type.
    public boolean isEnum() { return isEnumForTest; }
}

class StubListTypeNode extends ListTypeNode {
    public StubListTypeNode(TypeNode elementType) {
        super(elementType);
    }
}

class StubSetTypeNode extends SetTypeNode {
    public StubSetTypeNode(TypeNode elementType) {
        super(elementType);
    }
}

class StubMapTypeNode extends MapTypeNode {
    public StubMapTypeNode(TypeNode keyType, TypeNode valueType) {
        super(keyType, valueType);
    }
}


// FieldNode Stub
class StubFieldNode extends FieldNode {
    public StubFieldNode(TypeNode type, String name) {
        super(type, name);
    }
    public StubFieldNode(short id, TypeNode type, String name) { // Convenience for tests
        super(type, name);
        this.setId((int)id);
    }
    // Methods like setId, setName, setType, setRequirement, setDefaultValue are inherited
}

// EnumValueNode Stub
class StubEnumValueNode extends EnumValueNode {
    public StubEnumValueNode(String name) {
        super(name);
    }
    public StubEnumValueNode(String name, Integer value) {
        super(name, value);
    }
}

// DefinitionNode Stubs
abstract class StubDefinitionNode extends DefinitionNode {
    public StubDefinitionNode(String name) { super(name); }
}

class StubStructNode extends StructNode {
    public StubStructNode(String name) {
        super(name);
    }
    // addField is inherited from StructLikeNode
}

class StubEnumNode extends EnumNode {
    private String docString; // For testing Javadoc on Enum if it were supported by AST
    public StubEnumNode(String name) {
        super(name);
    }
    // addValue is inherited
    // public String getDocString() { return docString; } // Not in actual EnumNode
    // public void setDocString(String doc) { this.docString = doc; }
}

class StubFunctionNode extends FunctionNode {
     private String docString;
    public StubFunctionNode(String name, TypeNode returnType, boolean isOneway) {
        super(name, returnType); // Assuming FunctionNode constructor
        if(isOneway) this.setOneway(Oneway.ONEWAY); else this.setOneway(Oneway.SYNC);
    }
    // addParameter, addException are inherited
    // public String getDocString() { return docString; } // Not in actual FunctionNode
    // public void setDocString(String doc) { this.docString = doc; }
}

class StubServiceNode extends ServiceNode {
    private String docString;
    public StubServiceNode(String name) {
        super(name);
    }
    // addFunction, setExtendsService are inherited
    // public String getDocString() { return docString; } // Not in actual ServiceNode
    // public void setDocString(String doc) { this.docString = doc; }
}

// HeaderNode Stubs
abstract class StubHeaderNode extends HeaderNode {
    public StubHeaderNode() {super();}
}

class StubNamespaceNode extends NamespaceNode {
    public StubNamespaceNode(String scope, String name) {
        super(scope, name); // AST uses name for identifier
    }
}

// DocumentNode Stub
class StubDocumentNode extends DocumentNode {
    public StubDocumentNode() {
        super();
    }
    // addHeader, addDefinition are inherited
}
