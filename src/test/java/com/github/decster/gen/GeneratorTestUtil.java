package com.github.decster.gen;

import org.junit.jupiter.api.Assertions;

public class GeneratorTestUtil {

    public static void assertContains(String generatedCode, String expectedSnippet) {
        Assertions.assertTrue(generatedCode.contains(expectedSnippet),
                "Generated code does not contain expected snippet.\n" +
                        "Expected: '" + expectedSnippet + "'\n" +
                        "Actual  : '" + 간단히하기(generatedCode, 200) + "'\n" +
                        "Context : \n" +调试上下文(generatedCode, expectedSnippet, 80)
        );
    }

    public static void assertNotContains(String generatedCode, String unexpectedSnippet) {
        Assertions.assertFalse(generatedCode.contains(unexpectedSnippet),
                "Generated code SHOULD NOT contain: '" + unexpectedSnippet + "'\n" +
                        "Actual  : '" + 간단히하기(generatedCode, 200) + "'\n" +
                        "Context : \n" +调试上下文(generatedCode, unexpectedSnippet, 80)
        );
    }

    public static String 간단히하기(String text, int maxLength) {
        if (text == null) return "null";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    public static String 调试上下文(String text, String snippet, int window) {
        if (text == null || snippet == null) return "N/A (null input)";
        int idx = text.indexOf(snippet);
        if (idx == -1) return "N/A (snippet not found)";

        int start = Math.max(0, idx - window);
        int end = Math.min(text.length(), idx + snippet.length() + window);

        StringBuilder context = new StringBuilder();
        if (start > 0) context.append("...");
        context.append(text, start, end);
        if (end < text.length()) context.append("...");

        // Highlight the snippet
        String contextStr = context.toString();
        int highlightStart = Math.max(0, idx - start) + (start > 0 ? 3 : 0); // adjust for "..."

        // This highlighting part is tricky because contextStr might be shorter than original text window
        // For simplicity, we just show the snippet itself and where it was found.
        // A more robust way would be to re-find snippet in contextStr if necessary.
        return String.format("Context (around index %d, window %d):\n%s\nSnippet to find: '%s'", idx, window, contextStr, snippet);
    }
}
