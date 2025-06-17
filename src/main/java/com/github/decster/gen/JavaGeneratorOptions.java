package com.github.decster.gen;

import java.util.HashMap;
import java.util.Map;

/**
 * Options for the Java generator. This class contains all the options that can be passed
 * to the Java generator to control code generation.
 */
public class JavaGeneratorOptions {
  // Core style options
  private boolean beans = false;          // Generate bean-style setters/getters
  private boolean privateMembers = false; // Use private members with getter/setters
  private boolean java5 = false;          // Generate Java 5 features (generics, annotations, etc.)
  private boolean androidStyle = false;   // Generate Android-compatible code (Parcelable)
  private boolean nocamel = false;        // Do not use CamelCase field accessors with beans
  private boolean fullcamel = false;      // Convert underscored names to camelCase

  // Container options
  private boolean sortedContainers = false; // Use TreeSet/TreeMap for ordered containers
  private boolean reuseObjects = false;     // Reuse objects during deserialization

  // Type handling options
  private String optionType = null; // Wrap optional fields in Option class (null=none,
                                    // "java.util.Optional", "org.apache.thrift.Option")

  // Interface options
  private boolean futureIface = false; // Generate Future interface for services

  // Annotation options
  private boolean undatedGeneratedAnnotations = false;  // Skip version in @Generated annotations
  private boolean suppressGeneratedAnnotations = false; // Suppress @Generated annotations entirely
  private boolean jakartaAnnotations = false;           // Use Jakarta annotations instead of javax
  private boolean annotationsAsMetadata = false; // Include Thrift field annotations as metadata

  // Exception handling
  private boolean rethrowUnhandledExceptions = false; // Rethrow unhandled exceptions

  // Misc options
  private boolean unsafeBinaries = false; // Skip checking binary field contents

  // Custom option map for future extensibility
  private final Map<String, String> customOptions = new HashMap<>();

  /**
   * Default constructor with default options
   */
  public JavaGeneratorOptions() {
    // Default values already set in field declarations
  }

  /**
   * Parse generator options from a string array of "key:value" pairs.
   *
   * @param options Array of options in "key:value" format
   * @return Configured JavaGeneratorOptions instance
   */
  public static JavaGeneratorOptions fromStringArray(String[] options) {
    JavaGeneratorOptions result = new JavaGeneratorOptions();

    if (options == null) {
      return result;
    }

    for (String option : options) {
      String[] parts = option.split(":", 2);
      String key = parts[0].trim();
      String value = parts.length > 1 ? parts[1].trim() : "true";

      switch (key) {
      case "beans":
        result.setBeans(Boolean.parseBoolean(value));
        break;
      case "private_members":
        result.setPrivateMembers(Boolean.parseBoolean(value));
        break;
      case "java5":
        result.setJava5(Boolean.parseBoolean(value));
        break;
      case "android":
      case "android_legacy":
        result.setAndroidStyle(Boolean.parseBoolean(value));
        break;
      case "sorted_containers":
        result.setSortedContainers(Boolean.parseBoolean(value));
        break;
      case "reuse_objects":
        result.setReuseObjects(Boolean.parseBoolean(value));
        break;
      case "option_type":
        result.setOptionType(value);
        break;
      case "future_iface":
        result.setFutureIface(Boolean.parseBoolean(value));
        break;
      case "undated_generated_annotations":
        result.setUndatedGeneratedAnnotations(Boolean.parseBoolean(value));
        break;
      case "suppress_generated_annotations":
        result.setSuppressGeneratedAnnotations(Boolean.parseBoolean(value));
        break;
      case "rethrow_unhandled_exceptions":
        result.setRethrowUnhandledExceptions(Boolean.parseBoolean(value));
        break;
      case "unsafe_binaries":
        result.setUnsafeBinaries(Boolean.parseBoolean(value));
        break;
      case "nocamel":
        result.setNocamel(Boolean.parseBoolean(value));
        break;
      case "fullcamel":
        result.setFullcamel(Boolean.parseBoolean(value));
        break;
      case "jakarta_annotations":
        result.setJakartaAnnotations(Boolean.parseBoolean(value));
        break;
      case "annotations_as_metadata":
        result.setAnnotationsAsMetadata(Boolean.parseBoolean(value));
        break;
      default:
        // Store unknown options in custom map for future extensibility
        result.customOptions.put(key, value);
        break;
      }
    }

    return result;
  }

  /**
   * Parse generator options from a map.
   *
   * @param options Map of option key/values
   * @return Configured JavaGeneratorOptions instance
   */
  public static JavaGeneratorOptions fromMap(Map<String, String> options) {
    JavaGeneratorOptions result = new JavaGeneratorOptions();

    if (options == null) {
      return result;
    }

    for (Map.Entry<String, String> entry : options.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      switch (key) {
      case "beans":
        result.setBeans(Boolean.parseBoolean(value));
        break;
      case "private_members":
        result.setPrivateMembers(Boolean.parseBoolean(value));
        break;
      case "java5":
        result.setJava5(Boolean.parseBoolean(value));
        break;
      case "android":
      case "android_legacy":
        result.setAndroidStyle(Boolean.parseBoolean(value));
        break;
      case "sorted_containers":
        result.setSortedContainers(Boolean.parseBoolean(value));
        break;
      case "reuse_objects":
        result.setReuseObjects(Boolean.parseBoolean(value));
        break;
      case "option_type":
        result.setOptionType(value);
        break;
      case "future_iface":
        result.setFutureIface(Boolean.parseBoolean(value));
        break;
      case "undated_generated_annotations":
        result.setUndatedGeneratedAnnotations(Boolean.parseBoolean(value));
        break;
      case "suppress_generated_annotations":
        result.setSuppressGeneratedAnnotations(Boolean.parseBoolean(value));
        break;
      case "rethrow_unhandled_exceptions":
        result.setRethrowUnhandledExceptions(Boolean.parseBoolean(value));
        break;
      case "unsafe_binaries":
        result.setUnsafeBinaries(Boolean.parseBoolean(value));
        break;
      default:
        // Store unknown options in custom map for future extensibility
        result.customOptions.put(key, value);
        break;
      }
    }

    return result;
  }

  // Getters and setters

  public boolean isBeans() { return beans; }

  public void setBeans(boolean beans) { this.beans = beans; }

  public boolean isPrivateMembers() { return privateMembers; }

  public void setPrivateMembers(boolean privateMembers) { this.privateMembers = privateMembers; }

  public boolean isJava5() { return java5; }

  public void setJava5(boolean java5) { this.java5 = java5; }

  public boolean isAndroidStyle() { return androidStyle; }

  public void setAndroidStyle(boolean androidStyle) { this.androidStyle = androidStyle; }

  public boolean isSortedContainers() { return sortedContainers; }

  public void setSortedContainers(boolean sortedContainers) {
    this.sortedContainers = sortedContainers;
  }

  public boolean isReuseObjects() { return reuseObjects; }

  public void setReuseObjects(boolean reuseObjects) { this.reuseObjects = reuseObjects; }

  public String getOptionType() { return optionType; }

  public void setOptionType(String optionType) { this.optionType = optionType; }

  public boolean isFutureIface() { return futureIface; }

  public void setFutureIface(boolean futureIface) { this.futureIface = futureIface; }

  public boolean isUndatedGeneratedAnnotations() { return undatedGeneratedAnnotations; }

  public void setUndatedGeneratedAnnotations(boolean undatedGeneratedAnnotations) {
    this.undatedGeneratedAnnotations = undatedGeneratedAnnotations;
  }

  public boolean isSuppressGeneratedAnnotations() { return suppressGeneratedAnnotations; }

  public void setSuppressGeneratedAnnotations(boolean suppressGeneratedAnnotations) {
    this.suppressGeneratedAnnotations = suppressGeneratedAnnotations;
  }

  public boolean isRethrowUnhandledExceptions() { return rethrowUnhandledExceptions; }

  public void setRethrowUnhandledExceptions(boolean rethrowUnhandledExceptions) {
    this.rethrowUnhandledExceptions = rethrowUnhandledExceptions;
  }

  public boolean isUnsafeBinaries() { return unsafeBinaries; }

  public void setUnsafeBinaries(boolean unsafeBinaries) { this.unsafeBinaries = unsafeBinaries; }

  public Map<String, String> getCustomOptions() {
    return new HashMap<>(customOptions); // Return a defensive copy
  }

  public String getCustomOption(String key) { return customOptions.get(key); }

  public void setCustomOption(String key, String value) { customOptions.put(key, value); }

  public boolean isJakartaAnnotations() { return jakartaAnnotations; }

  public void setJakartaAnnotations(boolean jakartaAnnotations) {
    this.jakartaAnnotations = jakartaAnnotations;
  }

  public boolean isAnnotationsAsMetadata() { return annotationsAsMetadata; }

  public void setAnnotationsAsMetadata(boolean annotationsAsMetadata) {
    this.annotationsAsMetadata = annotationsAsMetadata;
  }

  public boolean isNocamel() { return nocamel; }

  public void setNocamel(boolean nocamel) { this.nocamel = nocamel; }

  public boolean isFullcamel() { return fullcamel; }

  public void setFullcamel(boolean fullcamel) { this.fullcamel = fullcamel; }
}
