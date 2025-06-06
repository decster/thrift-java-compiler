package com.github.decster.gen;

import org.junit.jupiter.api.Assertions;

public class GeneratorTestUtil {
    public static void assertEqualsLineByLine(String file, String generatedCode, String expectedCode) {
        String[] generatedLines = generatedCode.split("\n");
        String[] expectedLines = expectedCode.split("\n");
        // compare line by line, if line not equal, fail with surrounding 4 lines as context
        for (int i = 0; i < Math.max(generatedLines.length, expectedLines.length); i++) {
            String generatedLine = i < generatedLines.length ? generatedLines[i] : "";
            String expectedLine = i < expectedLines.length ? expectedLines[i] : "";
            if (!generatedLine.equals(expectedLine)) {
                StringBuilder context = new StringBuilder();
                context.append("File: ").append(file).append("\n");
                context.append("Difference at line ").append(i + 1).append(":\n");
                int start = Math.max(0, i - 4);
                int end = Math.min(Math.max(generatedLines.length, expectedLines.length), i + 5);
                context.append("Generated:\n");
                for (int j = start; j < end; j++) {
                    context.append((j + 1)).append(": ").append(j < generatedLines.length ? generatedLines[j] : "").append("\n");
                }
                context.append("Expected:\n");
                for (int j = start; j < end; j++) {
                    context.append((j + 1)).append(": ").append(j < expectedLines.length ? expectedLines[j] : "").append("\n");
                }
                Assertions.fail(context.toString());
            }
        }
    }

    public static void assertContains(String generatedCode, String expectedSnippet) {
        Assertions.assertTrue(generatedCode.contains(expectedSnippet),
                "Generated code does not contain expected snippet.\n" +
                        "Expected: '" + expectedSnippet + "'\n" +
                        "Actual  : '" + shortenString(generatedCode, 200) + "'\n" +
                        "Context : \n" + debugContext(generatedCode, expectedSnippet, 80)
        );
    }

    public static void assertNotContains(String generatedCode, String unexpectedSnippet) {
        Assertions.assertFalse(generatedCode.contains(unexpectedSnippet),
                "Generated code SHOULD NOT contain: '" + unexpectedSnippet + "'\n" +
                        "Actual  : '" + shortenString(generatedCode, 200) + "'\n" +
                        "Context : \n" + debugContext(generatedCode, unexpectedSnippet, 80)
        );
    }

    public static String shortenString(String text, int maxLength) {
        if (text == null) return "null";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    public static String debugContext(String text, String snippet, int window) {
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
