package com.github.decster;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class CommandLineArgs {

    @Parameter(description = "Input Thrift file path", required = true, order = 0)
    private List<String> mainParameters = new ArrayList<>();

    @Parameter(names = {"-o", "--out"}, description = "Output directory for generated files")
    private String outputDirectory = "."; // Default to current directory

    @Parameter(names = {"-I", "--include"}, description = "Directories to search for included Thrift files")
    private List<String> includeDirs = new ArrayList<>();

    @Parameter(names = {"-g", "--gen"}, description = "Generator options in the format key1:value1,key2:value2")
    private String generatorOptions = "";

    // Getter for the main input file path
    public String getInputFile() {
        if (mainParameters.isEmpty()) {
            return null;
        }
        return mainParameters.get(0); // Assuming the first unannotated parameter is the input file
    }

    // It's good practice to return a defensive copy for lists if they are mutable internally
    public List<String> getAdditionalInputFiles() {
        if (mainParameters.size() > 1) {
            return new ArrayList<>(mainParameters.subList(1, mainParameters.size()));
        }
        return new ArrayList<>();
    }


    public String getOutputDirectory() {
        return outputDirectory;
    }

    public List<String> getIncludeDirs() {
        return new ArrayList<>(includeDirs); // Defensive copy
    }

    public String getGeneratorOptions() {
        return generatorOptions;
    }
}
