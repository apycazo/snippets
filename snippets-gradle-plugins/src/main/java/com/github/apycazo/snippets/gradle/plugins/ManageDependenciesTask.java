package com.github.apycazo.snippets.gradle.plugins;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.util.Collections;
import java.util.Properties;

public class ManageDependenciesTask extends DefaultTask {

  private Property<String> branch;
  private ListProperty<String> implementations;
  private Properties properties;

  @Input
  public Property<String> getBranch() {
    return branch;
  }

  public void setBranch(Property<String> branch) {
    this.branch = branch;
  }

  @Input
  public ListProperty<String> getImplementations() {
    return implementations;
  }

  public void setImplementations(ListProperty<String> implementations) {
    this.implementations = implementations;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  @TaskAction
  public void resolveDependencies() {
    System.out.println("branch: " + getBranch().get() + ", implementations: " + getImplementations().get());
    implementations.getOrElse(Collections.emptyList()).forEach(i -> {
      String dependency = properties.getOrDefault(i, "").toString();
      getLogger().info("Configured dependency: " + dependency);
    });

    System.out.println("List of resolved properties");
    properties.keySet().iterator().forEachRemaining(key -> {
      String prop = (String)key;
      String value = properties.getProperty(prop);
      System.out.println("Resolved '" + prop + "' as '" + value + "'");
    });
  }
}
