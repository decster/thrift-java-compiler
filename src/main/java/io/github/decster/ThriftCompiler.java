package io.github.decster;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import io.github.decster.ast.TProgram;
import io.github.decster.gen.JavaGenerator;
import io.github.decster.gen.JavaGeneratorOptions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ThriftCompiler {
  public static String getIncludeFile(List<String> includeDirs, String fileName) {
    for (String dir : includeDirs) {
      File file = new File(dir, fileName);
      if (file.exists() && file.isFile()) {
        return file.getPath();
      }
    }
    return null;
  }

  private static String getFileName(String filePath) {
    File file = new File(filePath);
    return file.getName();
  }

  public static TProgram recursiveParse(String filePath, TProgram parent, Set<String> knownIncludes,
                                        List<String> includeDirs) throws IOException {
    String fileName = getFileName(filePath);
    knownIncludes.add(fileName);
    TProgram program =
        ThriftAstBuilder.parseFile(filePath, parent != null ? parent.getScope() : null);
    for (String includeFile : program.getIncludeFiles()) {
      if (knownIncludes.contains(getFileName(includeFile))) {
        // Avoid circular includes
        throw new IOException("Circular include detected: " + includeFile);
      }
      String resolvedPath = getIncludeFile(includeDirs, includeFile);
      if (resolvedPath != null) {
        TProgram includedProgram =
            recursiveParse(resolvedPath, program, knownIncludes, includeDirs);
        program.addInclude(includedProgram);
      } else {
        throw new IOException(
            "Include file not found: " + includeFile + " in directories: " + includeDirs);
      }
    }
    program.resolveTypeRefsAndConsts();
    knownIncludes.remove(fileName);
    return program;
  }

  public static int compileThriftFiles(
          List<String> filesToParse,
          String outputDirectory,
          List<String> includeDirs,
          String genOptionsStr,
          Function<String, Void> logger)
          throws IOException {
    return compileThriftFiles(filesToParse, outputDirectory, includeDirs, genOptionsStr, logger, false);
  }

  /**
   * @return total number of files generated
   */
  public static int compileThriftFiles(
      List<String> filesToParse,
      String outputDirectory,
      List<String> includeDirs,
      String genOptionsStr,
      Function<String, Void> logger,
      boolean incrementalCompilation)
      throws IOException {
    if (logger==null) {
      logger = s -> {
        return null;
      };
    }

    // Validate input files
    List<File> inputFiles = new ArrayList<>();
    for (String filePath : filesToParse) {
      File inputFile = new File(filePath);
      if (!inputFile.exists() || !inputFile.isFile()) {
        throw new IOException("Input file does not exist or is not a file: " + filePath);
      }
      inputFiles.add(inputFile);
    }

    // Collect all parent directories of input files and add them to include directories if not
    // already present
    List<String> allIncludeDirs = new ArrayList<>(includeDirs);
    for (File inputFile : inputFiles) {
      String inputFileDir = inputFile.getParent();
      if (inputFileDir != null && !allIncludeDirs.contains(inputFileDir)) {
        allIncludeDirs.add(inputFileDir);
      }
    }

    // Parse generator options
    Map<String, String> genOptions = parseGeneratorOptions(genOptionsStr);
    JavaGeneratorOptions javaGenOptions = JavaGeneratorOptions.fromMap(genOptions);

    logger.apply(" Output directory: " + outputDirectory);
    if (!allIncludeDirs.isEmpty()) {
      logger.apply("Include directories: " + allIncludeDirs);
    }
    if (!genOptionsStr.isEmpty()) {
      logger.apply("Generator options: " + genOptionsStr);
    }

    // Validate output directory
    File outputDir = new File(outputDirectory);
    if (!outputDir.exists()) {
      if (!outputDir.mkdirs()) {
        throw new IOException("Failed to create output directory: " + outputDirectory);
      }
    } else if (!outputDir.isDirectory()) {
      throw new IOException("Output path is not a directory: " + outputDirectory);
    }

    // Process each input file
    int totalFilesGenerated = 0;
    for (String filePath : filesToParse) {
      // Parse the Thrift file and build the AST
      TProgram program = recursiveParse(filePath, null, new HashSet<>(), allIncludeDirs);

      // Generate code for this file
      JavaGenerator generator = new JavaGenerator(program, outputDirectory, javaGenOptions, incrementalCompilation);
      int filesGenerated = generator.generateAndWriteToFile();

      // Output a single line per file with the number of files generated
      logger.apply(
          "File: "
              + new File(filePath).getName()
              + " - Generated "
              + filesGenerated
              + " Java file"
              + (filesGenerated != 1 ? "s" : ""));

      totalFilesGenerated += filesGenerated;
    }
    logger.apply("Total generated files: " + totalFilesGenerated);
    return totalFilesGenerated;
  }

  public static void main(String[] argv) throws IOException {
    CommandLineArgs cliArgs = new CommandLineArgs();
    JCommander jc =
        JCommander.newBuilder().addObject(cliArgs).programName("ThriftCompiler").build();

    try {
      jc.parse(argv);

      // Check if help was requested
      if (cliArgs.isHelp()) {
        jc.usage();
        printGeneratorOptionsHelp();
        return;
      }

      compileThriftFiles(
          cliArgs.getInputFiles(),
          cliArgs.getOutputDirectory(),
          cliArgs.getIncludeDirs(),
          cliArgs.getGeneratorOptions(),
          s -> {
            System.out.println(s);
            return null;
          },
          false); // Pass false for incrementalCompilation in main, or add CLI arg

    } catch (ParameterException e) {
      System.err.println("Error parsing command-line arguments: " + e.getMessage());
      jc.usage();
      return;
    } catch (Exception e) {
      System.err.println("Error during compilation: " + e.getMessage());
      e.printStackTrace();
      return;
    }
  }

  /**
   * Print detailed help for generator options
   */
  private static void printGeneratorOptionsHelp() {
    System.out.println("\nDetailed Generator Options (-g, --gen):");
    System.out.println("  java (Java):");
    System.out.println("    beans:           Members will be private, and setter methods will return void.");
    System.out.println(
        "    private_members: Members will be private, but setter methods will "
            + "return 'this' like usual.");
    System.out.println("    nocamel:         Do not use CamelCase field accessors with beans.");
    System.out.println("    fullcamel:       Convert underscored_accessor_or_service_names to camelCase.");
    System.out.println("    android:         Generated structures are Parcelable (unsupported).");
    System.out.println(
        "    android_legacy:  Do not use java.io.IOException(throwable) "
            + "(available for Android 2.3 and above).");
    System.out.println("    option_type=[thrift|jdk8]:");
    System.out.println("                     thrift: wrap optional fields in thrift Option type.");
    System.out.println("                     jdk8: Wrap optional fields in JDK8+ Option type.");
    System.out.println("                     If the Option type is not specified, 'thrift' is used.");
    System.out.println("    rethrow_unhandled_exceptions:");
    System.out.println(
        "                     Enable rethrow of unhandled exceptions and let them "
            + "propagate further. (Default behavior is to catch and log it.)");
    System.out.println("    java5:           Generate Java 1.5 compliant code (includes android_legacy flag).");
    System.out.println("    future_iface:    Generate CompletableFuture based iface based on async client.");
    System.out.println(
        "    reuse_objects:   Data objects will not be allocated, but existing "
            + "instances will be used (read and write).");
    System.out.println("    reuse-objects:   Same as 'reuse_objects' (deprecated).");
    System.out.println("    sorted_containers:");
    System.out.println(
        "                     Use TreeSet/TreeMap instead of HashSet/HashMap as a "
            + "implementation of set/map.");
    System.out.println("    generated_annotations=[undated|suppress]:");
    System.out.println("                     undated: suppress the date at @Generated annotations");
    System.out.println("                     suppress: suppress @Generated annotations entirely");
    System.out.println("    unsafe_binaries: Do not copy ByteBuffers in constructors, getters, and setters.");
    System.out.println("    jakarta_annotations: generate jakarta annotations (javax by default)");
    System.out.println("    annotations_as_metadata:");
    System.out.println("                     Include Thrift field annotations as metadata in the generated code.");
  }

  /**
   * Parse generator options string in the format "key1=value1,key2=value2"
   */
  private static Map<String, String> parseGeneratorOptions(String optionsStr) {
    Map<String, String> options = new HashMap<>();
    if (optionsStr == null || optionsStr.isEmpty()) {
      return options;
    }

    String[] optionPairs = optionsStr.split(",");
    for (String pair : optionPairs) {
      String[] keyValue = pair.split("=", 2);
      String key = keyValue[0].trim();
      String value = keyValue.length > 1 ? keyValue[1].trim() : "true";
      options.put(key, value);
    }

    return options;
  }
}
