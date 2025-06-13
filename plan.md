Okay, this is an ambitious but very doable project! Migrating the Thrift Java generator to pure Java will provide better maintainability, easier debugging for Java developers, and potentially better integration with Java build tools.

Here's a plan:

## Overall Goal

To create a pure Java library that takes a Thrift IDL file (parsed by the provided ANTLR grammar into a Java AST) and generates Java code equivalent to what the official C++ Thrift compiler's Java generator produces.

## Key Challenges

1.  **AST Feature Parity:** Ensuring the Java AST nodes (`com.github.decster.ast.*`) can capture all the nuances and metadata present in the C++ `t_*` AST (`parse/t_*.h`). This includes annotations, default values, requiredness, etc.
2.  **Logic Replication:** Accurately translating the C++ generation logic (loops, conditionals, string manipulations, type mappings) into Java.
3.  **Options Handling:** Replicating the behavior of various generator options (e.g., `beans`, `java5`, `android`, `option_type`, `sorted_containers`).
4.  **Testing:** Ensuring the generated Java code is identical or functionally equivalent to the C++ generator's output for a wide range of Thrift files.

## Java Class Map (Key Classes)

This map outlines the core Java classes needed for the generator.

1.  **`ThriftCompiler.java` (Main Entry Point)**
    *   `public static void main(String[] args)`: Parses command-line arguments (input Thrift file, output directory, generator options).
    *   `public void compile(Path thriftFile, Path outputDir, JavaGeneratorOptions options)`: Orchestrates parsing and generation.
        *   Calls ANTLR parser to get ANTLR ParseTree.
        *   Calls `AstBuilder` to convert ParseTree to our custom `DocumentNode`.
        *   Instantiates `JavaGenerator` and calls its main generation method.

2.  **`AstBuilder.java` (ANTLR Visitor/Listener)**
    *   Extends ANTLR-generated base visitor or listener.
    *   `public DocumentNode build(ThriftParser.DocumentContext ctx)`: Root method to build the AST.
    *   `visitXYZ(XYZContext ctx)` methods for each ANTLR rule to populate `com.github.decster.ast.*` nodes.
        *   *Critical*: Ensure all information like field IDs, requiredness, default values, annotations, docstrings are captured from the ANTLR context and stored in the AST nodes.

3.  **`JavaGeneratorOptions.java`**
    *   Fields to store all supported Java generator options (e.g., `boolean beansStyle`, `boolean androidStyle`, `String optionType`, etc.).
    *   Constructor or static factory method to parse options from a string array or map.

4.  **`JavaGenerator.java` (Core Logic - mirrors `t_java_generator.cc`)**
    *   `private final DocumentNode document;`
    *   `private final JavaGeneratorOptions options;`
    *   `private final Path outputBaseDir;`
    *   `private String currentPackageName;`
    *   `private Path currentPackageDir;`
    *   `private StringBuilder sb; // For building current file content`
    *   `private int indentLevel;`

    *   **Core Generation Methods:**
        *   `public void generate()`: Main entry point. Iterates through definitions in `DocumentNode`.
            *   Calls `prepareOutputDirectoryStructure()`.
            *   Iterates `document.getDefinitions()`:
                *   `generateEnum(EnumNode)`
                *   `generateStruct(StructNode)` // or `generateStructLike(StructLikeNode)`
                *   `generateException(ExceptionNode)`
                *   `generateUnion(UnionNode)`
                *   `generateConsts(List<ConstNode>)`
                *   `generateService(ServiceNode)`
                *   `generateTypedef(TypedefNode)` (likely a no-op or just for comments in Java)
        *   `private void generateEnum(EnumNode enumNode)`: Generates `enum Xxx { ... }`.
            *   Writes package, imports, Javadoc, class signature.
            *   `generateEnumValues(List<EnumValueNode>)`.
            *   `generateEnumGetValueMethod()`.
            *   `generateEnumFindByValueMethod()`.
        *   `private void generateStructLike(StructLikeNode structNode, boolean isException, boolean isUnion)`: Unified method for structs, exceptions, unions.
            *   `generateClassHeader(StructLikeNode, String baseClass, List<String> interfaces)`.
            *   `generateFieldsDeclarations(List<FieldNode>)`.
            *   `generateIsSetBitDeclarations(List<FieldNode>)`. // if needed
            *   `generateStaticMetadataMap(StructLikeNode)`.
            *   `generateConstructors(StructLikeNode)`.
            *   `generateAccessors(FieldNode)`. // getters, setters, isSetters, unsetters
            *   `generateDeepCopy(StructLikeNode)`.
            *   `generateClear(StructLikeNode)`.
            *   `generateEqualsHashCode(StructLikeNode)`.
            *   `generateCompareTo(StructLikeNode)`.
            *   `generateToString(StructLikeNode)`.
            *   `generateRead(StructLikeNode)`. // calls standardScheme.read or tupleScheme.read
            *   `generateWrite(StructLikeNode)`. // calls standardScheme.write or tupleScheme.write
            *   `generateValidate(StructLikeNode)`.
            *   `generateStandardScheme(StructLikeNode)`.
            *   `generateTupleScheme(StructLikeNode)`.
        *   `private void generateService(ServiceNode serviceNode)`: Generates `Xxx.java` containing Iface, Client, Processor, etc.
            *   `generateServiceInterface(ServiceNode)`.
            *   `generateServiceAsyncInterface(ServiceNode)`.
            *   `generateServiceFutureInterface(ServiceNode)`. // if option enabled
            *   `generateServiceClient(ServiceNode)`.
            *   `generateServiceAsyncClient(ServiceNode)`.
            *   `generateServiceFutureClient(ServiceNode)`. // if option enabled
            *   `generateServiceProcessor(ServiceNode)`.
            *   `generateServiceAsyncProcessor(ServiceNode)`.
            *   `generateFunctionArgsStruct(FunctionNode)`.
            *   `generateFunctionResultStruct(FunctionNode)`.
        *   `private void generateConsts(List<ConstNode> constNodes)`: Generates `Constants.java`.
            *   `renderConstValue(TypeNode, Object constValue)`.

    *   **Helper/Utility Methods (many private):**
        *   `private String getJavaTypeName(TypeNode typeNode, boolean inContainer, boolean forInit)`: Maps Thrift types to Java types.
        *   `private String getBaseTypeName(BaseTypeNode baseTypeNode, boolean inContainer)`
        *   `private String getJavaPackage()`
        *   `private String getAutogenComment()`
        *   `private void indent()` / `private void unindent()` / `private String getIndent()`
        *   `private void append(String s)` / `private void appendLine(String s)`
        *   `private void writeToFile(String className, String content)`
        *   `private String escapeJavaString(String thriftString)`
        *   `private String toCamelCase(String underscoreName, boolean capitalizeFirst)`
        *   `private String toUpperCaseIdentifier(String name)` // For _Fields enum
        *   `private boolean typeCanBeNull(TypeNode typeNode)`
        *   `private String generateIsSetCheck(FieldNode fieldNode)`
        *   `private void generateFieldSerialization(FieldNode fieldNode, String structInstanceName)`
        *   `private void generateFieldDeserialization(FieldNode fieldNode, String structInstanceName)`
        *   `private void generateContainerSerialization(FieldNode fieldNode, String structInstanceName)`
        *   `private void generateContainerDeserialization(FieldNode fieldNode, String structInstanceName)`

5.  **`JavaCodeFormatter.java` (Optional but Recommended)**
    *   `public String format(String rawJavaCode)`: Uses a library like Google Java Format or Eclipse JDT formatter to beautify generated code.

## Detailed Implementation Steps

### Phase 0: Setup & AST Enhancement

1.  **Class & Methods:**
    *   `com.github.decster.ast.*`: Review all AST nodes.
    *   `AstBuilder.java`
2.  **Dependencies:** ANTLR runtime, `Thrift.g4`.
3.  **Tasks:**
    *   Ensure all AST nodes (`FieldNode`, `StructNode`, `EnumNode`, `EnumValueNode`, `FunctionNode`, `TypeNode` and its children) can store necessary metadata:
        *   `FieldNode`: `id`, `requirement` (enum: REQUIRED, OPTIONAL, DEFAULT), `defaultValue` (Object), `annotations`, `docString`.
        *   `StructLikeNode` (Struct, Union, Exception): `annotations`, `docString`.
        *   `EnumValueNode`: `value` (Integer), `annotations`, `docString`.
        *   `FunctionNode`: `oneway` (enum), `returnType`, `parameters`, `exceptions`, `annotations`, `docString`.
        *   `TypeNode`: `annotations`.
    *   Enhance `AstBuilder.java` to fully populate these new fields in the AST nodes from the ANTLR parse tree.
    *   Implement `ThriftCompiler.main` for basic argument parsing (input file, output dir).
    *   Implement `JavaGeneratorOptions` with a few basic options for now.
4.  **Unit Tests:**
    *   For `AstBuilder`:
        *   Parse a Thrift file with fields having IDs, default values, `required`/`optional` keywords, and annotations. Verify the AST `FieldNode`s contain this info.
        *   Test docstrings capture for structs, fields, enums, services, functions.
        *   Test annotations capture at various levels.

### Phase 1: Basic Generator Structure & Enums

1.  **Class & Methods:**
    *   `JavaGenerator.java`: Constructor, `generate()`, `generateEnum()`, `getJavaPackage()`, `getAutogenComment()`, `indent()`, `appendLine()`, `writeToFile()`, `getJavaTypeName()` (for basic types and enums), `toUpperCaseIdentifier()`.
2.  **Dependencies:** `DocumentNode`, `EnumNode`, `EnumValueNode`, `JavaGeneratorOptions`.
3.  **Tasks:**
    *   Implement `JavaGenerator.generate()` to iterate definitions.
    *   Implement file and directory creation logic (`prepareOutputDirectoryStructure`, `writeToFile`).
    *   Implement `JavaGenerator.generateEnum()`:
        *   Package and import statements.
        *   Class Javadoc (from `EnumNode.docString`).
        *   `@Generated` annotation.
        *   Enum class signature.
        *   Enum constants (from `EnumValueNode`), including their Javadoc and annotations.
        *   `value` field, constructor.
        *   `getValue()` method.
        *   `findByValue()` method.
4.  **Unit Tests:**
    *   `JavaGenerator.getJavaTypeName()`: Test for primitive types, enums.
    *   `JavaGenerator.generateEnum()`:
        *   Input: Simple enum without explicit values. Output: Correct Java enum.
        *   Input: Enum with explicit values. Output: Correct Java enum.
        *   Input: Enum with Javadoc and annotations. Output: Java enum with Javadoc and `@Deprecated` (if applicable).
        *   Verify generated code compiles and runs basic checks (e.g., `MyEnum.findByValue(0) == MyEnum.VALUE1`).

### Phase 2: Constants

1.  **Class & Methods:**
    *   `JavaGenerator.java`: `generateConsts()`, `renderConstValue()`.
    *   `JavaGenerator.getJavaTypeName()`: Extend for lists, sets, maps of primitives.
2.  **Dependencies:** `ConstNode`, `TypeNode` (and its subtypes for const values).
3.  **Tasks:**
    *   Implement `JavaGenerator.generateConsts()`:
        *   Creates `Constants.java`.
        *   Iterates `ConstNode`s.
        *   For each const, declares `public static final <type> NAME = <value>;`.
    *   Implement `JavaGenerator.renderConstValue()`:
        *   Handles rendering of integers, doubles, strings (with escaping), booleans.
        *   Handles lists, sets, maps of primitive constants (e.g., `new ArrayList<>(Arrays.asList(1, 2))`, `new HashMap<>() {{ put("k", "v"); }}`).
        *   Handles references to other enum values.
4.  **Unit Tests:**
    *   `JavaGenerator.renderConstValue()`: Test all primitive types, simple lists/maps.
    *   `JavaGenerator.generateConsts()`:
        *   Input: Thrift with various const types. Output: `Constants.java`.
        *   Verify generated code compiles.

### Phase 3: Structs & Exceptions (Core Data Types) - Iteration 1 (Basic Structure)

1.  **Class & Methods:**
    *   `JavaGenerator.java`: `generateStructLike()`, `generateClassHeader()`, `generateFieldsDeclarations()`, `generateIsSetBitDeclarations()`, `generateStaticMetadataMap()`, `generateConstructors()` (default, all-args, copy), `generateAccessors()` (simple getters/setters, isSet, unset), `typeCanBeNull()`, `generateIsSetCheck()`, `getJavaTypeName()` (for structs).
2.  **Dependencies:** `StructLikeNode`, `FieldNode`, `TypeNode`.
3.  **Tasks:**
    *   Implement `generateStructLike()`:
        *   Handle `public class Xxx implements org.apache.thrift.TBase<...>, ...`
        *   Call sub-methods for fields, constructors, basic accessors.
    *   `generateFieldsDeclarations()`: `private <type> fieldName;`. Consider `final` for non-bean style if applicable.
    *   `generateIsSetBitDeclarations()`: Logic for `__isset_bitfield` or `__isset_bit_vector`.
    *   `generateStaticMetadataMap()`: (Initial, simple version). `_Fields` enum, `metaDataMap`.
    *   `generateConstructors()`: Default, all-args (for required/default fields), copy constructor.
    *   `generateAccessors()`:
        *   Basic getters (`public type getField()`).
        *   Basic setters (`public void setField(type val)` or `public Xxx setField(type val)`).
        *   `isSetField()`, `unsetField()`.
    *   Handle `beans` and `private_members` options for accessor style and field visibility.
4.  **Unit Tests:**
    *   For a simple struct with primitive fields:
        *   Verify class signature, field declarations.
        *   Verify constructors.
        *   Verify getters, setters, isSet, unset methods.
        *   Verify `_Fields` enum and basic `metaDataMap`.
        *   Test with `beans` option.
        *   Generated code compiles.

### Phase 4: Structs & Exceptions - Iteration 2 (Complex Logic)

1.  **Class & Methods:**
    *   `JavaGenerator.java`: Enhance `generateStructLike()` with:
        *   `generateDeepCopy()`, `generateClear()`.
        *   `generateEqualsHashCode()`.
        *   `generateCompareTo()`.
        *   `generateToString()`.
        *   `generateValidate()`.
        *   `generateRead()`, `generateWrite()` (delegating to schemes).
        *   `generateStandardScheme()`, `generateTupleScheme()`.
        *   `generateFieldSerialization()`, `generateFieldDeserialization()`.
        *   `generateContainerSerialization()`, `generateContainerDeserialization()`.
2.  **Dependencies:** Existing struct generation logic.
3.  **Tasks:**
    *   Implement methods for `equals`, `hashCode`, `compareTo`, `toString`.
    *   Implement `deepCopy`, `clear`.
    *   Implement `validate` (checking required fields).
    *   Implement `StandardScheme`'s `read` and `write` methods. This is complex and involves iterating fields, checking types, and calling `TProtocol` methods.
        *   `generateFieldDeserialization` will use `iprot.readX()`.
        *   `generateFieldSerialization` will use `oprot.writeX()`.
        *   Handle nested structs/containers recursively.
    *   Implement `TupleScheme`'s `read` and `write` (if supporting tuples).
    *   Handle `reuse_objects` option in (de)serialization.
    *   Handle `sorted_containers` option for map/set initializations.
    *   Handle `android` option (`Parcelable` implementation).
4.  **Unit Tests:**
    *   Struct with various field types (primitives, list, map, set, nested struct).
    *   Verify `equals`, `hashCode`, `compareTo`, `toString` correctness.
    *   Verify `deepCopy` creates a true deep copy.
    *   Test `validate()` throws exception for missing required fields.
    *   Test serialization/deserialization: write a struct, read it back, verify it's equal to the original.
    *   Test with `reuse_objects`, `sorted_containers`, `android` options.

### Phase 5: Unions

1.  **Class & Methods:**
    *   `JavaGenerator.java`: Adapt `generateStructLike()` or create `generateUnion()` specific logic.
        *   Unions extend `TUnion`.
        *   Specific constructor logic for unions.
        *   Static factory methods for each field (`public static MyUnion field(fieldType val)`).
        *   Override `checkType`, `standardSchemeReadValue`, `standardSchemeWriteValue`, etc. from `TUnion`.
2.  **Dependencies:** `UnionNode`, general struct generation logic.
3.  **Tasks:**
    *   Implement union-specific `TUnion` overrides.
    *   Ensure only one field can be set.
    *   Correct getter/setter logic for union fields (throwing exceptions if wrong field is accessed).
4.  **Unit Tests:**
    *   Simple union with a few field types.
    *   Verify static factory methods.
    *   Verify getters/setters behavior (correct access, exception on wrong access).
    *   Verify serialization/deserialization.

### Phase 6: Services

1.  **Class & Methods:**
    *   `JavaGenerator.java`: `generateService()`, `generateServiceInterface()`, `generateServiceAsyncInterface()`, `generateServiceFutureInterface()`, `generateServiceClient()`, `generateServiceAsyncClient()`, `generateServiceFutureClient()`, `generateServiceProcessor()`, `generateServiceAsyncProcessor()`, `generateFunctionArgsStruct()`, `generateFunctionResultStruct()`, `getJavaRpcMethodName()`, `getFunctionSignature()`.
2.  **Dependencies:** `ServiceNode`, `FunctionNode`, struct generation logic (for args/result structs).
3.  **Tasks:**
    *   `generateFunctionArgsStruct()` and `generateFunctionResultStruct()`: Use existing struct generation logic.
    *   `generateServiceInterface()`: Define `Iface` with all service methods.
    *   `generateServiceClient()`:
        *   `send_xxx()` and `recv_xxx()` methods.
        *   Actual public `xxx()` method calling send/recv.
    *   `generateServiceProcessor()`:
        *   `ProcessFunction` inner class for each method.
        *   Dispatch logic in `process()`.
    *   Implement async versions (`AsyncIface`, `AsyncClient`, `AsyncProcessor`).
    *   Implement future versions if `future_iface` option is set.
    *   Handle `oneway` functions correctly.
    *   Handle service inheritance (`extends`).
4.  **Unit Tests:**
    *   Service with a few methods (void, primitive return, struct return, throws exception, oneway).
    *   Verify Iface, Client, Processor are generated correctly.
    *   Verify args/result structs are correct.
    *   Generated code compiles.
    *   (Advanced) Test client-server interaction with mock TProtocol.

### Phase 7: Refinements, Options, and Finalization

1.  **Class & Methods:** All previously implemented classes.
2.  **Dependencies:** Full AST, all generator logic.
3.  **Tasks:**
    *   Implement all remaining generator options (`java5`, `option_type`, `undated_generated_annotations`, `rethrow_unhandled_exceptions`, `unsafe_binaries`, etc.). This will involve adding conditional logic in many places.
    *   `option_type`: Modify field declarations, getters, setters, (de)serialization to wrap optional fields in `org.apache.thrift.Option` or `java.util.Optional`.
    *   Thorough code review and refactoring.
    *   Add comprehensive Javadoc.
    *   Ensure consistent error handling and logging.
    *   (Optional) Implement `JavaCodeFormatter`.
4.  **Unit Tests:**
    *   Add tests for each generator option, verifying the output changes as expected.
    *   Test complex Thrift files with many features combined.
    *   Compare generated output against the C++ compiler's output for a suite of Thrift files.

## General Advice

*   **Incremental Development:** Follow the phases. Get one part working and tested before moving to the next.
*   **Test-Driven Development (TDD):** Write tests *before* or *alongside* implementation. This is crucial for a complex project like a compiler.
*   **Refer to `t_java_generator.cc` Constantly:** It's your ground truth for logic and output.
*   **Helper Methods:** Don't repeat code. Create helper methods for common tasks (e.g., generating Javadoc, checking options, formatting names).
*   **AST is Key:** A well-populated and accurate AST from `AstBuilder` will make the `JavaGenerator` much easier to write.
*   **Keep it Simple Initially:** Don't worry about all options at once. Get the core generation working, then add options.
*   **Output Comparison:** Have a set of diverse Thrift files. Generate Java code using the existing C++ compiler. Your new Java compiler should aim to produce identical or functionally equivalent output. Use diff tools.

This plan provides a structured approach. Each phase can be broken down further. Good luck!
