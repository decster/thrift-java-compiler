package com.github.decster;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.decster.ast.ProgramNode;
import com.github.decster.parser.ThriftLexer;
import com.github.decster.parser.ThriftParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ThriftCompiler {
  public ProgramNode parse(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      throw new IOException("Input file not found: " + filePath);
    }
    CharStream input = CharStreams.fromPath(path);
    ThriftLexer lexer = new ThriftLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ThriftParser parser = new ThriftParser(tokens);
    ParseTree tree = parser.document();
    SimpleThriftAstBuilder astBuilder = new SimpleThriftAstBuilder();
    return (ProgramNode)astBuilder.visit(tree);
  }
  public static void main(String[] argv) {
    CommandLineArgs cliArgs = new CommandLineArgs();
    JCommander jc = JCommander.newBuilder().addObject(cliArgs).build();
    String fileToParse = null;
    String outputDirectory = ".";
    try {
      jc.parse(argv);
      fileToParse = cliArgs.getInputFile();
      outputDirectory = cliArgs.getOutputDirectory();
      if (fileToParse == null) {
        System.err.println("Input Thrift file must be specified.");
        jc.usage();
        return;
      }
      File inputFile = new File(fileToParse);
      if (!inputFile.exists() || !inputFile.isFile()) {
        System.err.println("Input file does not exist or is not a file: " +
                           fileToParse);
        return;
      }
      System.out.println("Input file: " + fileToParse);
      System.out.println("Output directory: " + outputDirectory);
      if (!cliArgs.getIncludeDirs().isEmpty()) {
        System.out.println("Include directories: " + cliArgs.getIncludeDirs());
      }
    } catch (ParameterException e) {
      System.err.println("Error parsing command-line arguments: " +
                         e.getMessage());
      jc.usage();
      return;
    }
    ThriftCompiler compiler = new ThriftCompiler();
    JavaCodeGenerator generator = new JavaCodeGenerator();
    System.out.println("Attempting to parse: " + fileToParse);
    try {
      ProgramNode astRoot = compiler.parse(fileToParse);
      if (astRoot != null) {
        System.out.println("AST Root: " + astRoot.getClass().getSimpleName());
        System.out.println(astRoot.toString());
        Files.createDirectories(Paths.get(outputDirectory));
        generator.generate(astRoot, outputDirectory);
        System.out.println("Code generation attempted into: " +
                           outputDirectory);
      } else {
        System.err.println("AST construction resulted in null root.");
      }
    } catch (IOException e) {
      System.err.println("IO Error during compilation/generation: " +
                         e.getMessage());
    } catch (Exception e) {
      System.err.println("Unexpected error during compilation/generation: " +
                         e.getMessage());
      e.printStackTrace();
    }
  }
}
