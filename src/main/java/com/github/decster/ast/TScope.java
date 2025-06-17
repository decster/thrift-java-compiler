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
    if (types.containsKey(name)) {
      throw new TDuplicateDefinitionException("Type", name);
    }
    types.put(name, type);
  }

  public TType getType(String name) { return types.get(name); }

  public void addService(String name, TService service) {
    if (services.containsKey(name)) {
      throw new TDuplicateDefinitionException("Service", name);
    }
    services.put(name, service);
  }

  public TService getService(String name) { return services.get(name); }

  public void addConstant(String name, TConst constant) {
    if (constants.containsKey(name)) {
      throw new TDuplicateDefinitionException("Constant", name);
    }
    constants.put(name, constant);
  }

  public TConst getConstant(String name) { return constants.get(name); }

  public void resolveAllConsts() {
    for (TConst constant : constants.values()) {
      resolveConstValue(constant.getValue(), constant.getType());
    }
  }

  public void resolveConstValue(TConstValue constValue, TType ttype) {
    ttype = ttype.getTrueType();
    if (ttype.isMap()) {
      for (Map.Entry<TConstValue, TConstValue> entry : constValue.getMap().entrySet()) {
        resolveConstValue(entry.getKey(), ((TMap)ttype).getKeyType());
        resolveConstValue(entry.getValue(), ((TMap)ttype).getValType());
      }
    } else if (ttype.isList()) {
      for (TConstValue element : constValue.getList()) {
        resolveConstValue(element, ((TList)ttype).getElemType());
      }
    } else if (ttype.isSet()) {
      for (TConstValue element : constValue.getList()) {
        resolveConstValue(element, ((TSet)ttype).getElemType());
      }
    } else if (ttype.isStruct()) {
      TStruct tstruct = (TStruct)ttype;
      for (Map.Entry<TConstValue, TConstValue> entry : constValue.getMap().entrySet()) {
        String fieldName = entry.getKey().getString();
        TField field = tstruct.getFieldByName(fieldName);
        if (field == null) {
          throw new RuntimeException("No field named \"" + fieldName + "\" was found in struct of type \"" +
                                     tstruct.getName() + "\"");
        }
        resolveConstValue(entry.getValue(), field.getType());
      }
    } else if (constValue.getType() == TConstValue.Type.CV_IDENTIFIER) {
      if (ttype.isEnum()) {
        constValue.setEnum((TEnum)ttype);
      } else {
        TConst constant = getConstant(constValue.getIdentifier());
        if (constant == null) {
          throw new RuntimeException("No enum value or constant found named \"" + constValue.getIdentifier() + "\"!");
        }
        TType constType = constant.getType().getTrueType();
        if (constType.isBaseType()) {
          TBaseType baseType = (TBaseType)constType;
          switch (baseType.getBase()) {
          case TYPE_I8:
          case TYPE_I16:
          case TYPE_I32:
          case TYPE_I64:
          case TYPE_BOOL:
            constValue.setInteger(constant.getValue().getInteger());
            break;
          case TYPE_STRING:
            constValue.setString(constant.getValue().getString());
            break;
          case TYPE_UUID:
            constValue.setUuid(constant.getValue().getUuid());
            break;
          case TYPE_DOUBLE:
            constValue.setDouble(constant.getValue().getDouble());
            break;
          case TYPE_VOID:
            throw new RuntimeException("Constants cannot be of type VOID");
          }
        } else if (constType.isMap()) {
          constValue.setMap();
          for (Map.Entry<TConstValue, TConstValue> entry : constant.getValue().getMap().entrySet()) {
            constValue.addMap(entry.getKey(), entry.getValue());
          }
        } else if (constType.isList()) {
          constValue.setList();
          for (TConstValue element : constant.getValue().getList()) {
            constValue.addList(element);
          }
        }
      }
    } else if (ttype.isEnum()) {
      TEnum tenum = (TEnum)ttype;
      TEnumValue enumValue = tenum.getConstantByValue(constValue.getInteger());
      if (enumValue == null) {
        throw new RuntimeException("Couldn't find a named value in enum " + tenum.getName() + " for value " +
                                   constValue.getInteger());
      }
      constValue.setIdentifier(tenum.getName() + "." + enumValue.getName());
      constValue.setEnum(tenum);
    }
  }

  /**
   * Exception thrown when a duplicate definition is found in a scope.
   */
  public static class TDuplicateDefinitionException extends RuntimeException {
    public TDuplicateDefinitionException(String type, String name) { super(type + " " + name + " already defined"); }
  }
}
