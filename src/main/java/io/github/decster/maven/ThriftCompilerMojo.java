package io.github.decster.maven;

import io.github.decster.ThriftCompiler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Maven plugin to compile Thrift files into Java classes.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ThriftCompilerMojo extends AbstractMojo {

    /**
     * The directory where thrift files are located.
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/thrift", required = true)
    private File sourceDirectory;

    /**
     * The directory where generated Java files will be placed.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/thrift", required = true)
    private File outputDirectory;

    /**
     * List of directories to search for included Thrift files.
     */
    @Parameter
    private List<File> includeDirectories = new ArrayList<>();

    /**
     * Generator options in format "key1=value1,key2=value2".
     */
    @Parameter
    private String generatorOptions = "";

    /**
     * Specifies file extensions for Thrift files. Default is ".thrift".
     */
    @Parameter(defaultValue = ".thrift")
    private String fileExtension;

    /**
     * Flag to skip the execution of the plugin.
     */
    @Parameter(defaultValue = "false", property = "thrift.skip")
    private boolean skip;

    /**
     * Flag to enable incremental compilation.
     */
    @Parameter(defaultValue = "false", property = "thrift.incrementalCompilation")
    private boolean incrementalCompilation;

    /**
     * Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skipping Thrift compilation as requested.");
            return;
        }

        // Ensure output directory exists
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        // Find all thrift files in the source directory
        List<String> thriftFiles = findThriftFiles(sourceDirectory);
        if (thriftFiles.isEmpty()) {
            getLog().info("No Thrift files found in " + sourceDirectory);
            return;
        }

        // Convert include directories to string paths
        List<String> includeDirPaths = new ArrayList<>();
        for (File includeDir : includeDirectories) {
            includeDirPaths.add(includeDir.getAbsolutePath());
        }

        // Add the source directory to include dirs if not already present
        if (!includeDirPaths.contains(sourceDirectory.getAbsolutePath())) {
            includeDirPaths.add(sourceDirectory.getAbsolutePath());
        }

        try {
            getLog().info("Compiling " + thriftFiles.size() + " Thrift files to " + outputDirectory);
            int numFiles = ThriftCompiler.compileThriftFiles(thriftFiles, outputDirectory.getAbsolutePath(), includeDirPaths, generatorOptions, null, incrementalCompilation);
            getLog().info("Thrift compilation completed, incremental: "+ incrementalCompilation+ " generatedFiles: " + numFiles);

            // Add the output directory to the project's sources
            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
        } catch (IOException e) {
            throw new MojoExecutionException("Error compiling Thrift files: " + e.getMessage(), e);
        }
    }

    /**
     * Recursively find all Thrift files in the given directory.
     */
    private List<String> findThriftFiles(File directory) {
        List<String> files = new ArrayList<>();
        if (!directory.exists() || !directory.isDirectory()) {
            return files;
        }

        File[] directoryFiles = directory.listFiles();
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                if (file.isDirectory()) {
                    files.addAll(findThriftFiles(file));
                } else if (file.getName().endsWith(fileExtension)) {
                    files.add(file.getAbsolutePath());
                }
            }
        }
        return files;
    }
}
