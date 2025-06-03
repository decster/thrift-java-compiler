package com.github.decster;

import com.github.decster.ast.*;
import com.github.decster.parser.*;
import java.io.IOException;
import org.antlr.v4.runtime.Token;

/**
 * Builder class to convert Thrift parse trees into AST Document objects.
 * This class uses the visitor pattern to build the AST.
 */
public class ThriftAstBuilder {

  /**
   * Parse a string containing Thrift IDL and build an AST Document.
   *
   * @param content The Thrift IDL content as a string
   * @return A Document object representing the AST
   * @throws IOException If parsing fails
   */
  public static DocumentNode buildFromString(String content) throws IOException {
    try {
      ThriftParser.DocumentContext parseTree = ThriftCompiler.parse(content);
      return (DocumentNode) new AstVisitor().visit(parseTree);
    } catch (RuntimeException e) {
      throw new IOException("Failed to parse Thrift content", e);
    }
  }

  /**
   * Parse a Thrift IDL file and build an AST Document.
   *
   * @param filePath The path to the Thrift IDL file
   * @return A Document object representing the AST
   * @throws IOException If the file cannot be read or parsing fails
   */
  public static DocumentNode buildFromFile(String filePath) throws IOException {
    try {
      ThriftParser.DocumentContext parseTree =
          ThriftCompiler.parseFile(filePath);
      return (DocumentNode) new AstVisitor().visit(parseTree);
    } catch (RuntimeException e) {
      throw new IOException("Failed to parse Thrift file: " + filePath, e);
    }
  }

  /**
   * Visitor implementation that builds an AST from a Thrift parse tree.
   */
  private static class AstVisitor extends ThriftBaseVisitor<Node> {

    @Override
    public DocumentNode visitDocument(ThriftParser.DocumentContext ctx) {
      DocumentNode documentNode = new DocumentNode();

      // Process headers
      for (ThriftParser.HeaderContext headerCtx : ctx.header()) {
        Node header = visitHeader(headerCtx);
        if (header instanceof HeaderNode) {
          documentNode.addHeader((HeaderNode)header);
        }
      }

      // Process definitions
      for (ThriftParser.DefinitionContext defCtx : ctx.definition()) {
        Node definition = visitDefinition(defCtx);
        if (definition instanceof DefinitionNode) {
          documentNode.addDefinition((DefinitionNode)definition);
        }
      }

      return documentNode;
    }

    @Override
    public Node visitHeader(ThriftParser.HeaderContext ctx) {
      if (ctx.include_() != null) {
        return visitInclude_(ctx.include_());
      } else if (ctx.namespace_() != null) {
        return visitNamespace_(ctx.namespace_());
      } else if (ctx.cpp_include() != null) {
        return visitCpp_include(ctx.cpp_include());
      }
      return null;
    }

    @Override
    public Node visitInclude_(ThriftParser.Include_Context ctx) {
      String path = unquoteString(ctx.LITERAL().getText());
      Include include = new Include(path);
      setNodeLocation(include, ctx.start);
      return include;
    }

    @Override
    public Node visitNamespace_(ThriftParser.Namespace_Context ctx) {
        String scope;
        String name;

        // Handle 'namespace * identifier' form
        if (ctx.getText().startsWith("namespace*")) {
            scope = "*";
            if (ctx.IDENTIFIER() != null && !ctx.IDENTIFIER().isEmpty()) {
                name = ctx.IDENTIFIER(0).getText();
            } else if (ctx.LITERAL() != null && !ctx.LITERAL().getText().isEmpty()) {
                name = unquoteString(ctx.LITERAL().getText());
            } else {
                // Handle error case
                throw new RuntimeException("Invalid namespace declaration: missing name");
            }
        }
        // Handle 'cpp_namespace identifier' or 'php_namespace identifier'
        else if (ctx.getText().startsWith("cpp_namespace") || ctx.getText().startsWith("php_namespace")) {
            scope = ctx.getText().startsWith("cpp_namespace") ? "cpp" : "php";
            if (ctx.IDENTIFIER() != null && !ctx.IDENTIFIER().isEmpty()) {
                name = ctx.IDENTIFIER(0).getText();
            } else {
                throw new RuntimeException("Invalid namespace declaration: missing name");
            }
        }
        // Handle regular 'namespace identifier (identifier|literal)' form
        else {
            if (ctx.IDENTIFIER().size() < 1) {
                throw new RuntimeException("Invalid namespace declaration: missing scope");
            }

            scope = ctx.IDENTIFIER(0).getText();

            if (ctx.IDENTIFIER().size() >= 2) {
                name = ctx.IDENTIFIER(1).getText();
            } else if (ctx.LITERAL() != null && !ctx.LITERAL().getText().isEmpty()) {
                name = unquoteString(ctx.LITERAL().getText());
            } else {
                throw new RuntimeException("Invalid namespace declaration: missing name");
            }
        }

        NamespaceNode namespace = new NamespaceNode(scope, name);
        setNodeLocation(namespace, ctx.start);
        return namespace;
    }

    @Override
    public Node visitCpp_include(ThriftParser.Cpp_includeContext ctx) {
      String path = unquoteString(ctx.LITERAL().getText());
      Include include = new Include(path); // Remove boolean argument
      setNodeLocation(include, ctx.start);
      return include;
    }

    @Override
    public Node visitDefinition(ThriftParser.DefinitionContext ctx) {
      if (ctx.const_rule() != null) {
        return visitConst_rule(ctx.const_rule());
      } else if (ctx.typedef_() != null) {
        return visitTypedef_(ctx.typedef_());
      } else if (ctx.enum_rule() != null) {
        return visitEnum_rule(ctx.enum_rule());
      } else if (ctx.struct_() != null) {
        return visitStruct_(ctx.struct_());
      } else if (ctx.union_() != null) {
        return visitUnion_(ctx.union_());
      } else if (ctx.exception() != null) {
        return visitException(ctx.exception());
      } else if (ctx.service() != null) {
        return visitService(ctx.service());
      }
      return null;
    }

    @Override
    public Node visitConst_rule(ThriftParser.Const_ruleContext ctx) {
      String name = ctx.IDENTIFIER().getText();
      TypeNode type = (TypeNode)visitField_type(ctx.field_type());

      // Default value is null, will be set below if available
      Object value = null;

      // Handle constant value if present
      if (ctx.const_value() != null) {
        // For now, just store the text representation of the value
        // In a complete implementation, we would parse this into the appropriate type
        value = ctx.const_value().getText();
      }

      // Create the constant node with all required parameters
      ConstNode constNode = new ConstNode(name, type, value);

      setNodeLocation(constNode, ctx.start);
      return constNode;
    }

    @Override
    public Node visitEnum_rule(ThriftParser.Enum_ruleContext ctx) {
      String name = ctx.IDENTIFIER().getText();
      EnumNode enumDef = new EnumNode(name);
      setNodeLocation(enumDef, ctx.start);

      // Process enum values
      if (ctx.enum_field() != null) {
        for (ThriftParser.Enum_fieldContext fieldCtx : ctx.enum_field()) {
          String valueName = fieldCtx.IDENTIFIER().getText();
          EnumValueNode enumValueNode = new EnumValueNode(valueName);

          // Set value if provided
          if (fieldCtx.integer() != null) {
            int value = Integer.parseInt(fieldCtx.integer().getText());
            enumValueNode.setValue(value);
          }

          setNodeLocation(enumValueNode, fieldCtx.start);
          enumDef.addValue(enumValueNode);
        }
      }

      return enumDef;
    }

    @Override
    public Node visitStruct_(ThriftParser.Struct_Context ctx) {
      String name = ctx.IDENTIFIER().getText();
      StructNode struct = new StructNode(name);
      setNodeLocation(struct, ctx.start);

      // Process fields
      if (ctx.field() != null) {
        for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
          FieldNode fieldNode = (FieldNode)visitField(fieldCtx);
          struct.addField(fieldNode);
        }
      }

      return struct;
    }

    @Override
    public Node visitUnion_(ThriftParser.Union_Context ctx) {
      String name = ctx.IDENTIFIER().getText();
      UnionNode union = new UnionNode(name);
      setNodeLocation(union, ctx.start);

      // Process fields
      if (ctx.field() != null) {
        for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
          FieldNode fieldNode = (FieldNode)visitField(fieldCtx);
          union.addField(fieldNode);
        }
      }

      return union;
    }

    @Override
    public Node visitException(ThriftParser.ExceptionContext ctx) {
      String name = ctx.IDENTIFIER().getText();
      ExceptionNode exceptionNode =
          new ExceptionNode(name);
      setNodeLocation(exceptionNode, ctx.start);

      // Process fields
      if (ctx.field() != null) {
        for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
          FieldNode fieldNode = (FieldNode)visitField(fieldCtx);
          exceptionNode.addField(fieldNode);
        }
      }

      return exceptionNode;
    }

    @Override
    public Node visitService(ThriftParser.ServiceContext ctx) {
      String name = ctx.IDENTIFIER(0).getText();
      ServiceNode serviceNode = new ServiceNode(name);
      setNodeLocation(serviceNode, ctx.start);

      // Handle service inheritance (extends)
      if (ctx.IDENTIFIER().size() > 1) {
        String parentName = ctx.IDENTIFIER(1).getText();
        serviceNode.setExtendsService(parentName);
      }

      // Process functions
      if (ctx.function_() != null) {
        for (ThriftParser.Function_Context funcCtx : ctx.function_()) {
          FunctionNode functionNode = (FunctionNode)visitFunction_(funcCtx);
          serviceNode.addFunction(functionNode);
        }
      }

      return serviceNode;
    }

    @Override
    public Node visitField(ThriftParser.FieldContext ctx) {
      // Prepare type and name before constructing Field
      TypeNode type = null;
      String name = null;

      // Set field type
      if (ctx.field_type() != null) {
        type = (TypeNode)visitField_type(ctx.field_type());
      }

      // Set field name
      if (ctx.IDENTIFIER() != null) {
        name = ctx.IDENTIFIER().getText();
      }

      FieldNode fieldNode = new FieldNode(type, name);

      // Set field ID if provided
      if (ctx.field_id() != null) {
        int id = Integer.parseInt(ctx.field_id().integer().getText());
        fieldNode.setId(id);
      }

      // Set field requirement
      if (ctx.field_req() != null) {
        String reqText = ctx.field_req().getText().toUpperCase();
        FieldNode.Requirement req = FieldNode.Requirement.valueOf(reqText);
        fieldNode.setRequirement(req);
      }

      // Set default value if provided
      if (ctx.const_value() != null) {
        // Implementation for const_value would go here
        // This is a placeholder
      }

      setNodeLocation(fieldNode, ctx.start);
      return fieldNode;
    }

    @Override
    public Node visitField_type(ThriftParser.Field_typeContext ctx) {
      if (ctx.base_type() != null) {
        return visitBase_type(ctx.base_type());
      } else if (ctx.container_type() != null) {
        return visitContainer_type(ctx.container_type());
      } else if (ctx.IDENTIFIER() != null) {
        // Handle identifier (custom types)
        String typeName = ctx.IDENTIFIER().getText();
        IdentifierTypeNode identifierType = new IdentifierTypeNode(typeName);
        setNodeLocation(identifierType, ctx.start);
        return identifierType;
      }
      return null;
    }

    @Override
    public Node visitContainer_type(ThriftParser.Container_typeContext ctx) {
      if (ctx.set_type() != null) {
        return visitSet_type(ctx.set_type());
      } else if (ctx.list_type() != null) {
        return visitList_type(ctx.list_type());
      } else if (ctx.map_type() != null) {
        return visitMap_type(ctx.map_type());
      }
      return null;
    }

    @Override
    public Node visitSet_type(ThriftParser.Set_typeContext ctx) {
      TypeNode elementType = null;
      if (ctx.field_type() != null) {
        elementType = (TypeNode)visitField_type(ctx.field_type());
      }

      SetTypeNode setType = new SetTypeNode(elementType);
      setNodeLocation(setType, ctx.start);
      return setType;
    }

    @Override
    public Node visitList_type(ThriftParser.List_typeContext ctx) {
      TypeNode elementType = null;
      if (ctx.field_type() != null) {
        elementType = (TypeNode)visitField_type(ctx.field_type());
      }

      ListTypeNode listType = new ListTypeNode(elementType);
      setNodeLocation(listType, ctx.start);
      return listType;
    }

    @Override
    public Node visitMap_type(ThriftParser.Map_typeContext ctx) {
      TypeNode keyType = null;
      TypeNode valueType = null;

      if (ctx.field_type().size() >= 2) {
        keyType = (TypeNode)visitField_type(ctx.field_type(0));
        valueType = (TypeNode)visitField_type(ctx.field_type(1));
      }

      MapTypeNode mapType = new MapTypeNode(keyType, valueType);
      setNodeLocation(mapType, ctx.start);
      return mapType;
    }

    @Override
    public Node visitBase_type(ThriftParser.Base_typeContext ctx) {
      BaseTypeNode.BaseTypeEnum typeEnum;

      if (ctx.real_base_type().TYPE_BOOL() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.BOOL;
      } else if (ctx.real_base_type().TYPE_BYTE() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.BYTE;
      } else if (ctx.real_base_type().TYPE_I16() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.I16;
      } else if (ctx.real_base_type().TYPE_I32() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.I32;
      } else if (ctx.real_base_type().TYPE_I64() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.I64;
      } else if (ctx.real_base_type().TYPE_DOUBLE() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.DOUBLE;
      } else if (ctx.real_base_type().TYPE_STRING() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.STRING;
      } else if (ctx.real_base_type().TYPE_BINARY() != null) {
        typeEnum = BaseTypeNode.BaseTypeEnum.BINARY;
      } else {
        throw new IllegalArgumentException("Unknown base type");
      }

      BaseTypeNode baseTypeNode = new BaseTypeNode(typeEnum);
      setNodeLocation(baseTypeNode, ctx.start);
      return baseTypeNode;
    }

    @Override
    public Node visitFunction_(ThriftParser.Function_Context ctx) {
      // Get return type
      TypeNode returnType = null;
      if (ctx.function_type() != null) {
        if (ctx.function_type().field_type() != null) {
          returnType = (TypeNode)visitField_type(ctx.function_type().field_type());
        }
      }

      // Get function name
      String name = ctx.IDENTIFIER().getText();

      // Create the function
      FunctionNode functionNode = new FunctionNode(name, returnType);
      setNodeLocation(functionNode, ctx.start);

      // Process function arguments
      if (ctx.field() != null) {
        for (ThriftParser.FieldContext fieldCtx : ctx.field()) {
          FieldNode fieldNode = (FieldNode)visitField(fieldCtx);
          functionNode.addParameter(fieldNode);
        }
      }

      // Process exceptions
      if (ctx.throws_list() != null && ctx.throws_list().field() != null) {
        for (ThriftParser.FieldContext fieldCtx : ctx.throws_list().field()) {
          FieldNode fieldNode = (FieldNode)visitField(fieldCtx);
          functionNode.addException(fieldNode);
        }
      }

      // Process oneway modifier
      if (ctx.oneway() != null) {
        if (ctx.oneway().getText().equals("oneway")) {
          functionNode.setOneway(FunctionNode.Oneway.ONEWAY);
        } else if (ctx.oneway().getText().equals("async")) {
          functionNode.setOneway(FunctionNode.Oneway.ASYNC);
        } else {
          functionNode.setOneway(FunctionNode.Oneway.SYNC);
        }
      }

      return functionNode;
    }

    @Override
    public Node visitTypedef_(ThriftParser.Typedef_Context ctx) {
      TypeNode type = (TypeNode)visitField_type(ctx.field_type());
      String name = ctx.IDENTIFIER().getText();

      TypedefNode typedefNode = new TypedefNode(name, type);
      setNodeLocation(typedefNode, ctx.start);
      return typedefNode;
    }

    // Additional visitor methods would be implemented here
    // This is a partial implementation

    /**
     * Helper method to set line and column information on a node.
     *
     * @param node The AST node
     * @param token The token with location information
     */
    private void setNodeLocation(Node node, Token token) {
      if (token != null) {
        node.setLine(token.getLine());
        node.setColumn(token.getCharPositionInLine());
      }
    }

    /**
     * Helper method to remove quotes from a quoted string.
     *
     * @param quoted The quoted string
     * @return The string without quotes
     */
    private String unquoteString(String quoted) {
      if (quoted.startsWith("\"") && quoted.endsWith("\"")) {
        return quoted.substring(1, quoted.length() - 1);
      }
      return quoted;
    }
  }
}
