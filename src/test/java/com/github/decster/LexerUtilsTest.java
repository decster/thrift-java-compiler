package com.github.decster;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LexerUtilsTest {

  @Test
  public void testNullCases() {
    // Both null
    assertTrue(LexerUtils.lineEqualsByJavaLexer(null, null));

    // One null, one not
    assertFalse(LexerUtils.lineEqualsByJavaLexer(null, "int x = 5;"));
    assertFalse(LexerUtils.lineEqualsByJavaLexer("int x = 5;", null));
  }

  @Test
  public void testEmptyStrings() {
    assertTrue(LexerUtils.lineEqualsByJavaLexer("", "   "));
    assertFalse(LexerUtils.lineEqualsByJavaLexer("", "int x = 5;"));
  }

  @Test
  public void testIdenticalLines() {
    assertTrue(LexerUtils.lineEqualsByJavaLexer("int x =    5;", "int x = 5;"));
    assertTrue(LexerUtils.lineEqualsByJavaLexer("public static void main(String[] args)    {",
                                                "public static void main(String[] args) {"));
  }

  @Test
  public void testWhitespaceVariations() {
    // Different spacing
    assertTrue(LexerUtils.lineEqualsByJavaLexer("int x=5;    // aaa", "int x = 5; // aaa"));
    assertTrue(LexerUtils.lineEqualsByJavaLexer("for(int i=0;i<10;i++)",
                                                "for (int i = 0; i < 10;    i++)"));

    // Tabs vs spaces
    assertTrue(LexerUtils.lineEqualsByJavaLexer("if(condition)\t{", "if(condition) {"));

    // Leading/trailing whitespace
    assertTrue(LexerUtils.lineEqualsByJavaLexer("  int x = 5;  ", "int x = 5;"));
  }

  @Test
  public void testDifferentLines() {
    assertFalse(LexerUtils.lineEqualsByJavaLexer("int x = 5;", "int x = 6;"));
    assertFalse(LexerUtils.lineEqualsByJavaLexer("int x = 5;", "int y = 5;"));
    assertFalse(LexerUtils.lineEqualsByJavaLexer("int x = 5;", "double x = 5;"));
    assertFalse(LexerUtils.lineEqualsByJavaLexer("x++;", "x--;"));
  }

  @Test
  public void testComplexExpressions() {
    assertTrue(LexerUtils.lineEqualsByJavaLexer("Map<String, List<Integer>> map = new HashMap<>();",
                                                "Map<String,List<Integer>>map=new HashMap<>();"));

    assertTrue(LexerUtils.lineEqualsByJavaLexer(
        "stream.filter(s -> s.length() > 0).map(String::trim).collect(Collectors.toList());",
        "stream.filter(s->s.length()>0).map(String::trim).collect(Collectors.toList());"));
  }

  @Test
  public void testMethodSignatures() {
    assertTrue(LexerUtils.lineEqualsByJavaLexer(
        "public static <T extends Comparable<T>> void sort(List<T> list) {",
        "public static <T extends Comparable<T>> void sort( List<T> list ) {"));
  }

  @Test
  public void testStringLiterals() {
    // Strings with whitespace should be preserved
    assertTrue(LexerUtils.lineEqualsByJavaLexer("String s = \"Hello World\";",
                                                "String s = \"Hello World\";"));

    // These should be different because string content differs
    assertFalse(LexerUtils.lineEqualsByJavaLexer("String s = \"Hello World\";",
                                                 "String s = \"Hello  World\";"));
  }

  @Test
  public void testComments() {
    // Comments should be ignored by lexer on DEFAULT_CHANNEL
    assertTrue(LexerUtils.lineEqualsByJavaLexer("int x = 5; // This is a comment",
                                                "int x = 5; // This is another comment"));

    assertTrue(LexerUtils.lineEqualsByJavaLexer("int x = 5; /* comment */", "int x = 5;"));
  }

  @ParameterizedTest
  @MethodSource("provideEqualPairs")
  public void testParameterizedEquals(String line1, String line2) {
    assertTrue(LexerUtils.lineEqualsByJavaLexer(line1, line2));
  }

  @ParameterizedTest
  @MethodSource("provideUnequalPairs")
  public void testParameterizedNotEquals(String line1, String line2) {
    assertFalse(LexerUtils.lineEqualsByJavaLexer(line1, line2));
  }

  private static Stream<Arguments> provideEqualPairs() {
    return Stream.of(Arguments.of("int x = 5;", "int x=5;"),
                     Arguments.of("if (x > 5) {", "if(x>5){"),
                     Arguments.of("a += b + c;", "a+=b+c;"),
                     Arguments.of("String name = \"John\";", "String name=\"John\";"),
                     Arguments.of("List<String> list = new ArrayList<>();",
                                  "List<String>list=new ArrayList<>();"));
  }

  private static Stream<Arguments> provideUnequalPairs() {
    return Stream.of(Arguments.of("int x = 5;", "int x = 6;"),
                     Arguments.of("int x = 5;", "int y = 5;"),
                     Arguments.of("if (x > 5) {", "if (x >= 5) {"),
                     Arguments.of("List<String> list;", "List<Integer> list;"),
                     Arguments.of("return true;", "return false;"));
  }
}
