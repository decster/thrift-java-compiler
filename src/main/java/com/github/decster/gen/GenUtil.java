package com.github.decster.gen;

public class GenUtil {

    public static String clangFormat(String javaCode, String style) {
        try {
            // Create temporary file with the code
            java.nio.file.Path tempInput = java.nio.file.Files.createTempFile("format_input", ".java");
            java.nio.file.Files.writeString(tempInput, javaCode);

            // Build the clang-format command
            ProcessBuilder pb = new ProcessBuilder(
                    "clang-format",
                    "-style=" + style,
                    tempInput.toString());

            // Execute the command and capture output
            Process process = pb.start();
            String formattedCode = new String(process.getInputStream().readAllBytes());

            // Cleanup and wait for process to complete
            java.nio.file.Files.delete(tempInput);
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("clang-format failed with exit code " + exitCode);
            }

            return formattedCode;
        } catch (Exception e) {
            throw new RuntimeException("Failed to format code using clang-format", e);
        }
    }
}
