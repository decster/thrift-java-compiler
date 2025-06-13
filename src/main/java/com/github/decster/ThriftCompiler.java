package com.github.decster;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.decster.ast.TProgram;
import com.github.decster.gen.JavaGeneratorOptions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ThriftCompiler {

  public static void main(String[] argv) throws IOException {
    CommandLineArgs cliArgs = new CommandLineArgs();
    JCommander jc = JCommander.newBuilder().addObject(cliArgs).build();
    try {
      jc.parse(argv);
      String fileToParse = cliArgs.getInputFile();
      String outputDirectory = cliArgs.getOutputDirectory();

      // Validate input file
      if (fileToParse == null) {
        System.err.println("Input Thrift file must be specified.");
        jc.usage();
        return;
      }

      File inputFile = new File(fileToParse);
      if (!inputFile.exists() || !inputFile.isFile()) {
        System.err.println("Input file does not exist or is not a file: " + fileToParse);
        return;
      }

      // Validate output directory
      File outputDir = new File(outputDirectory);
      if (!outputDir.exists()) {
        System.out.println("Creating output directory: " + outputDirectory);
        if (!outputDir.mkdirs()) {
          System.err.println("Failed to create output directory: " + outputDirectory);
          return;
        }
      } else if (!outputDir.isDirectory()) {
        System.err.println("Output path is not a directory: " + outputDirectory);
        return;
      }

      // Parse generator options
      Map<String, String> genOptions = parseGeneratorOptions(cliArgs.getGeneratorOptions());
      JavaGeneratorOptions javaGenOptions = JavaGeneratorOptions.fromMap(genOptions);

      // Log the compilation settings
      System.out.println("Input file: " + fileToParse);
      System.out.println("Output directory: " + outputDirectory);
      if (!cliArgs.getIncludeDirs().isEmpty()) {
        System.out.println("Include directories: " + cliArgs.getIncludeDirs());
      }
      System.out.println("Generator options: " + genOptions);

      // Parse the Thrift file and build the AST
      TProgram program = ThriftAstBuilder.parseFile(fileToParse);

      // Create a generator and generate code
      compile(program, Paths.get(outputDirectory), javaGenOptions);

      System.out.println("Java code generation completed successfully.");

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
   * Parse generator options string in the format "key1:value1,key2:value2"
   */
  private static Map<String, String> parseGeneratorOptions(String optionsStr) {
    Map<String, String> options = new HashMap<>();
    if (optionsStr == null || optionsStr.isEmpty()) {
      return options;
    }

    String[] optionPairs = optionsStr.split(",");
    for (String pair : optionPairs) {
      String[] keyValue = pair.split(":", 2);
      String key = keyValue[0].trim();
      String value = keyValue.length > 1 ? keyValue[1].trim() : "true";
      options.put(key, value);
    }

    return options;
  }

  /**
   * Compile a Thrift program to Java code
   *
   * @param program The TProgram AST representing the Thrift program
   * @param outputDir The output directory for generated files
   * @param options Java generator options
   * @throws IOException If an I/O error occurs
   */
  public static void compile(TProgram program, Path outputDir, JavaGeneratorOptions options) throws IOException {
    // In Phase 0, we're just setting up the infrastructure
    // The actual JavaGenerator implementation will be added in later phases
    System.out.println("Thrift program parsed successfully.");
    System.out.println("AST contains: " + program.getObjects().size() + " definitions");
    System.out.println("Ready for code generation (to be implemented in future phases)");

    // This is where we would instantiate the JavaGenerator and call its generate() method
    // JavaGenerator generator = new JavaGenerator(program, outputDir, options);
    // generator.generate();
  }
}
