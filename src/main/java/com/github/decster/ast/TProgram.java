package com.github.decster.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Top level class representing an entire thrift program. A program consists
 * fundamentally of typedefs, enumerations, constants, structs, exceptions, and services.
 * Corresponds to t_program.h in the C++ implementation.
 */
public class TProgram extends TDoc {
  private String path;
  private String name;
  private String outPath;
  private boolean outPathIsAbsolute;
  private TScope scope;
  private boolean recursive;
  private String namespace;

  private List<TTypedef> typedefs;
  private List<TEnum> enums;
  private List<TConst> consts;
  private List<TStruct> structs;
  private List<TStruct> xceptions;
  private List<TStruct> objects; // Combined list of structs and exceptions
  private List<TService> services;
  private Map<String, String> namespaces;
  private List<TProgram> includes;
  private List<String> includeFiles;

  public TProgram(String path, String name) {
    this.path = path;
    this.name = name;
    this.outPath = "./";
    this.outPathIsAbsolute = false;
    this.scope = new TScope();
    this.recursive = false;

    this.typedefs = new ArrayList<>();
    this.enums = new ArrayList<>();
    this.consts = new ArrayList<>();
    this.structs = new ArrayList<>();
    this.xceptions = new ArrayList<>();
    this.objects = new ArrayList<>();
    this.services = new ArrayList<>();
    this.namespaces = new HashMap<>();
    this.includes = new ArrayList<>();
    this.includeFiles = new ArrayList<>();
  }

  public TProgram(String path) { this(path, programName(path)); }

  private static String programName(String path) {
    // Extract program name from path
    int slash = path.lastIndexOf('/');
    if (slash >= 0) {
      path = path.substring(slash + 1);
    }
    int dot = path.indexOf('.');
    if (dot >= 0) {
      path = path.substring(0, dot);
    }
    return path;
  }

  // Path accessor
  public String getPath() { return path; }

  // Output path accessor
  public String getOutPath() { return outPath; }

  public void setOutPath(String outPath) { this.outPath = outPath; }

  // Create gen-* dir accessor
  public boolean isOutPathAbsolute() { return outPathIsAbsolute; }

  public void setOutPathAbsolute(boolean outPathIsAbsolute) {
    this.outPathIsAbsolute = outPathIsAbsolute;
  }

  // Name accessor
  public String getName() { return name; }

  // Namespace
  public String getNamespace() { return namespace; }

  public void setNamespace(String namespace) { this.namespace = namespace; }

  // Accessors for program elements
  public List<TTypedef> getTypedefs() { return typedefs; }

  public List<TEnum> getEnums() { return enums; }

  public List<TConst> getConsts() { return consts; }

  public List<TStruct> getStructs() { return structs; }

  public List<TStruct> getXceptions() { return xceptions; }

  public List<TStruct> getObjects() { return objects; }

  public List<TService> getServices() { return services; }

  public Map<String, String> getNamespaces() { return namespaces; }

  public List<TProgram> getIncludes() { return includes; }

  public List<String> getIncludeFiles() { return includeFiles; }

  public void addIncludeFile(String includeFile) {
    if (!includeFiles.contains(includeFile)) {
      includeFiles.add(includeFile);
    } else {
      throw new RuntimeException("Include file '" + includeFile +
                                 "' already exists in the program.");
    }
  }

  public TScope getScope() { return scope; }

  // Methods to add program elements
  public void addTypedef(TTypedef td) { typedefs.add(td); }

  public void addEnum(TEnum tenum) { enums.add(tenum); }

  public void addConst(TConst tconst) { consts.add(tconst); }

  public void addStruct(TStruct tstruct) {
    structs.add(tstruct);
    objects.add(tstruct);
  }

  public void addXception(TStruct txception) {
    xceptions.add(txception);
    objects.add(txception);
  }

  public void addService(TService tservice) { services.add(tservice); }

  public void addInclude(TProgram program) { includes.add(program); }

  public void setNamespace(String language, String namespace) {
    namespaces.put(language, namespace);
  }

  public String getNamespace(String language) {
    String ret = namespaces.get(language);
    if (ret == null) {
      ret = namespaces.get("*");
    }
    if (ret == null) {
      ret = "";
    }
    return ret;
  }

  public Map<String, String> getAllNamespaces() { return namespaces; }

  public void resolveTypeRefsAndConsts() {
    resolveTypeRefs();
    resolveConstValues();
  }

  public void resolveTypeRefs() {
    // walk through all typedefs, structs, exceptions, constants and services
    // resolving TTypeRef instances to their actual types
    // do not consider includes currently, handling of includes is done in the future
    // Resolve typedefs
    for (TTypedef typedef : typedefs) {
      typedef.setType(resolveTypeRef(typedef.getType()));
    }

    // Resolve structs
    for (TStruct struct : structs) {
      resolveStructTypeRefs(struct);
    }

    // Resolve exceptions
    for (TStruct exception : xceptions) {
      resolveStructTypeRefs(exception);
    }

    // Resolve constants
    for (TConst constant : consts) {
      resolveConstTypeRefs(constant);
    }

    // Resolve services
    for (TService service : services) {
      resolveServiceTypeRefs(service);
    }
  }

  public void resolveConstValues() {
    for (TStruct struct : structs) {
      for (TField field : struct.getMembers()) {
        if (field.hasValue()) {
          scope.resolveConstValue(field.getValue(), field.getType().getTrueType());
        }
      }
    }

    // Resolve exceptions
    for (TStruct exception : xceptions) {
      for (TField field : exception.getMembers()) {
        if (field.hasValue()) {
          scope.resolveConstValue(field.getValue(), field.getType().getTrueType());
        }
      }
    }

    scope.resolveAllConsts();
  }

  private void resolveStructTypeRefs(TStruct struct) {
    for (TField field : struct.getMembers()) {
      field.setType(resolveTypeRef(field.getType()));
      // If field has a default value, resolve any type references in it
      if (field.hasValue()) {
        resolveConstValueTypeRefs(field.getValue());
      }
    }
  }

  private void resolveConstTypeRefs(TConst constant) {
    constant.setType(resolveTypeRef(constant.getType()));
    resolveConstValueTypeRefs(constant.getValue());
  }

  private void resolveServiceTypeRefs(TService service) {
    for (TFunction function : service.getFunctions()) {
      // Resolve return type
      function.setReturnType(resolveTypeRef(function.getReturnType()));

      // Resolve argument types
      for (TField argument : function.getArglist().getMembers()) {
        argument.setType(resolveTypeRef(argument.getType()));
      }

      // Resolve exception types
      for (TField exception : function.getXceptions().getMembers()) {
        exception.setType(resolveTypeRef(exception.getType()));
      }
    }
  }

  private void resolveConstValueTypeRefs(TConstValue constValue) {
    if (constValue.getType() == TConstValue.Type.CV_MAP) {
      for (Map.Entry<TConstValue, TConstValue> entry : constValue.getMap().entrySet()) {
        resolveConstValueTypeRefs(entry.getKey());
        resolveConstValueTypeRefs(entry.getValue());
      }
    } else if (constValue.getType() == TConstValue.Type.CV_LIST) {
      for (TConstValue element : constValue.getList()) {
        resolveConstValueTypeRefs(element);
      }
    }
  }

  private TType resolveTypeRef(TType type) {
    if (type instanceof TTypeRef) {
      String typeName = type.getName();
      TType resolvedType = scope.getType(typeName);
      if (resolvedType == null) {
        throw new RuntimeException("Type '" + typeName + "' not found in scope");
      }
      return resolvedType; //.getTrueType();
    } else if (type instanceof TContainer) {
      resolveContainerTypeRefs((TContainer)type);
    }
    return type;
  }

  private void resolveContainerTypeRefs(TContainer container) {
    if (container instanceof TList) {
      TList list = (TList)container;
      list.setElemType(resolveTypeRef(list.getElemType()));
    } else if (container instanceof TSet) {
      TSet set = (TSet)container;
      set.setElemType(resolveTypeRef(set.getElemType()));
    } else if (container instanceof TMap) {
      TMap map = (TMap)container;
      map.setKeyType(resolveTypeRef(map.getKeyType()));
      map.setValType(resolveTypeRef(map.getValType()));
    }
  }
}
