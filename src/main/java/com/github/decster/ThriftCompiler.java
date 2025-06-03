package com.github.decster;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
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
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

public class ThriftCompiler {
  public static ThriftParser.DocumentContext parse(String content)
      throws IOException {
    CharStream input = CharStreams.fromString(content);
    ThriftLexer lexer = new ThriftLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ThriftParser parser = new ThriftParser(tokens);
    ThriftParser.DocumentContext tree = parser.document();
    if (parser.getNumberOfSyntaxErrors() > 0) {
      throw new ParseCancellationException("Thrift parsing failed with " +
                            parser.getNumberOfSyntaxErrors() +
                            " syntax errors");
    }
    return tree;
  }

  public static ThriftParser.DocumentContext parseFile(String filePath)
      throws IOException {
    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      throw new IOException("Input file not found: " + filePath);
    }
    String content = Files.readString(path);
    return parse(content);
  }

  public static void main(String[] argv) throws IOException {
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
    ThriftCompiler.parseFile(fileToParse);
  }
}
