package com.example;
import com.example.ast.*;
import com.example.parser.ThriftBaseVisitor;
import com.example.parser.ThriftParser;
import com.example.parser.ThriftLexer;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
public class SimpleThriftAstBuilder extends ThriftBaseVisitor<Node> {
    @Override protected Node defaultResult() { return null; }
    @Override protected Node aggregateResult(Node aggregate, Node nextResult) {
        if (aggregate == null) return nextResult;
        // This is not how one typically builds an AST with a visitor.
        // Usually, the visitXYZ method for a rule creates the parent AST node,
        // then calls visit() on its children and adds the results to the parent.
        // This aggregateResult is often just `return nextResult;`
        // For now, let's assume the AstNode's addChild handles nulls if any.
        // if (aggregate instanceof ProgramNode && nextResult instanceof HeaderNode) ((ProgramNode)aggregate).addHeader((HeaderNode)nextResult);
        // else if (aggregate instanceof ProgramNode && nextResult instanceof DefinitionNode) ((ProgramNode)aggregate).addDefinition((DefinitionNode)nextResult);
        // else if (aggregate instanceof StructNode && nextResult instanceof FieldNode) ((StructNode)aggregate).addField((FieldNode)nextResult);
        // else if (aggregate instanceof ServiceNode && nextResult instanceof FunctionNode) ((ServiceNode)aggregate).addFunction((FunctionNode)nextResult);
        // else if (aggregate instanceof FunctionNode && nextResult instanceof FieldNode) ((FunctionNode)aggregate).addArgument((FieldNode)nextResult);
        // else if (aggregate instanceof FieldNode && nextResult instanceof TypeNode) { /* This would need specific setter */ }
        return aggregate; // Or often, just `return nextResult;` and let visitXYZ handle aggregation.
    }
    private String getRuleName(RuleNode node) { return ThriftParser.ruleNames[node.getRuleContext().getRuleIndex()]; }
    @Override public Node visitDocument(ThriftParser.DocumentContext ctx) {
        ProgramNode program = new ProgramNode();
        for (ThriftParser.HeaderContext hCtx : ctx.header()) { Node hn = visit(hCtx); if(hn instanceof HeaderNode) program.addHeader((HeaderNode)hn); }
        for (ThriftParser.DefinitionContext dCtx : ctx.definition()) { Node dn = visit(dCtx); if(dn instanceof DefinitionNode) program.addDefinition((DefinitionNode)dn); }
        return program;
    }
    @Override public Node visitHeader(ThriftParser.HeaderContext ctx) {
        if (ctx.includeHeader() != null) return visit(ctx.includeHeader());
        if (ctx.namespaceHeader() != null) return visit(ctx.namespaceHeader());
        return null;
    }
    @Override public Node visitIncludeHeader(ThriftParser.IncludeHeaderContext ctx) {
        String path = ctx.LITERAL_STRING().getText(); path = path.substring(1, path.length() - 1); return new IncludeNode(path);
    }
    @Override public Node visitNamespaceHeader(ThriftParser.NamespaceHeaderContext ctx) {
        String scope = ctx.NAMESPACE_SCOPE() != null ? ctx.NAMESPACE_SCOPE().getText() : ctx.ID(0).getText();
        String name = ctx.NAMESPACE_SCOPE() != null ? ctx.ID(0).getText() : ctx.ID(1).getText();
        return new NamespaceNode(scope, name);
    }
    @Override public Node visitDefinition(ThriftParser.DefinitionContext ctx) {
        if (ctx.structDefinition() != null) return visit(ctx.structDefinition());
        if (ctx.serviceDefinition() != null) return visit(ctx.serviceDefinition());
        return null;
    }
    @Override public Node visitTerminal(TerminalNode node) {
        // Terminals are not typically part of a structured AST unless they represent identifiers/literals within a rule
        return null; // Or new TerminalAstNode(symbolicName, text); if desired
    }
    @Override public Node visitStructDefinition(ThriftParser.StructDefinitionContext ctx) {
        StructNode struct = new StructNode(ctx.ID().getText());
        for (ThriftParser.FieldContext fCtx : ctx.field()) { Node fn = visit(fCtx); if(fn instanceof FieldNode) struct.addField((FieldNode)fn); }
        return struct;
    }
    @Override public Node visitField(ThriftParser.FieldContext ctx) {
        Integer fieldId = ctx.INT_CONSTANT() != null ? Integer.parseInt(ctx.INT_CONSTANT().getText()) : null;
        String requiredness = ctx.fieldRequiredness() != null ? ctx.fieldRequiredness().getText() : null;
        TypeNode fieldType = (TypeNode) visit(ctx.fieldType());
        String name = ctx.ID().getText();
        return new FieldNode(fieldId, requiredness, fieldType, name);
    }
    @Override public Node visitFieldType(ThriftParser.FieldTypeContext ctx) {
        if (ctx.ID() != null) return new IdentifierTypeNode(ctx.ID().getText());
        if (ctx.baseType() != null) return visit(ctx.baseType());
        if (ctx.containerType() != null) return new IdentifierTypeNode(ctx.containerType().getText()); // Placeholder
        return null;
    }
    @Override public Node visitBaseType(ThriftParser.BaseTypeContext ctx) { return new BaseTypeNode(ctx.getText()); }
    @Override public Node visitServiceDefinition(ThriftParser.ServiceDefinitionContext ctx) {
        ServiceNode service = new ServiceNode(ctx.ID(0).getText(), ctx.ID(1) != null ? ctx.ID(1).getText() : null);
        for (ThriftParser.FunctionContext funcCtx : ctx.function()) { Node fn = visit(funcCtx); if(fn instanceof FunctionNode) service.addFunction((FunctionNode)fn); }
        return service;
    }
    @Override public Node visitFunction(ThriftParser.FunctionContext ctx) {
        TypeNode returnType = ctx.functionType().KW_VOID() != null ? new VoidTypeNode() : (TypeNode) visit(ctx.functionType().fieldType());
        FunctionNode func = new FunctionNode(ctx.KW_ONEWAY() != null, returnType, ctx.ID().getText());
        for (ThriftParser.FieldContext fieldCtx : ctx.field()) { Node fn = visit(fieldCtx); if(fn instanceof FieldNode) func.addArgument((FieldNode)fn); }
        return func;
    }
    // visitChildren is a fallback, better to implement specific visitXYZ methods
    @Override public Node visitChildren(RuleNode node) { return defaultResult(); }
}
