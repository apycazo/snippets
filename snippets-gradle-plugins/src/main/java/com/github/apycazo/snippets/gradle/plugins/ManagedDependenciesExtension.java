package com.github.apycazo.snippets.gradle.plugins;

import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public class ManagedDependenciesExtension {

  private final Property<String> branch;
  private final ListProperty<String> implementations;
  private final ListProperty<String> testImplementations;
  private final ListProperty<String> compileOnly;
  private final ListProperty<String> starters;

  public ManagedDependenciesExtension(Project project) {
    branch = project.getObjects().property(String.class);
    implementations = project.getObjects().listProperty(String.class);
    testImplementations = project.getObjects().listProperty(String.class);
    compileOnly = project.getObjects().listProperty(String.class);
    starters = project.getObjects().listProperty(String.class);
  }

  public Property<String> getBranch() {
    return branch;
  }

  public ListProperty<String> getImplementations() {
    return implementations;
  }

  public ListProperty<String> getTestImplementations() {
    return testImplementations;
  }

  public ListProperty<String> getCompileOnly() {
    return compileOnly;
  }

  public ListProperty<String> getStarters() {
    return starters;
  }

}
