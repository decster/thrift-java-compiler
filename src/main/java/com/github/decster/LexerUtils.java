package com.github.decster;

import com.github.decster.parser.JavaLexer;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class LexerUtils {

  /**
   * Compares two lines of Java code by lexing them and checking if the tokens are equal.
   * This ignores whitespace differences between the lines.
   *
   * @param line1 First line of Java code
   * @param line2 Second line of Java code
   * @return true if the lines produce the same token sequence, false otherwise
   */
  public static boolean lineEqualsByJavaLexer(String line1, String line2) {
    // Handle null cases
    if (line1 == null && line2 == null) {
      return true;
    }
    if (line1 == null || line2 == null) {
      return false;
    }

    // Get tokens for line1
    JavaLexer lexer1 = new JavaLexer(CharStreams.fromString(line1));
    List<Token> tokens1 = getAllTokens(lexer1);

    // Get tokens for line2
    JavaLexer lexer2 = new JavaLexer(CharStreams.fromString(line2));
    List<Token> tokens2 = getAllTokens(lexer2);

    // Compare token counts
    if (tokens1.size() != tokens2.size()) {
      return false;
    }

    // Compare each token
    for (int i = 0; i < tokens1.size(); i++) {
      Token t1 = tokens1.get(i);
      Token t2 = tokens2.get(i);

      // Compare token types and text
      if (t1.getType() != t2.getType() || !t1.getText().equals(t2.getText())) {
        return false;
      }
    }

    return true;
  }

  /**
   * Gets all tokens from a lexer and resets the lexer.
   *
   * @param lexer The lexer to get tokens from
   * @return A list of all tokens
   */
  private static List<Token> getAllTokens(JavaLexer lexer) {
    List<Token> tokens = new ArrayList<>();
    Token token;

    // Skip whitespace and comments
    lexer.removeErrorListeners();

    // Collect all tokens except for EOF
    do {
      token = lexer.nextToken();
      if (token.getChannel() == Token.DEFAULT_CHANNEL) {
        tokens.add(token);
      }
    } while (token.getType() != Token.EOF);

    // Reset lexer for potential reuse
    lexer.reset();

    return tokens;
  }
}
