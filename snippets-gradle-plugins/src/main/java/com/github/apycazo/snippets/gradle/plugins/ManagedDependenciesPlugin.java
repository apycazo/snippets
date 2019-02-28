package com.github.apycazo.snippets.gradle.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ApplicationPlugin;
import org.gradle.api.plugins.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.gradle.api.plugins.JavaPlugin.*;

/**
 * TODO:
 * - Submodule dependency management
 */
public class ManagedDependenciesPlugin implements Plugin<Project> {

  private Properties properties = new Properties();
  private Properties versions = new Properties();
  private Properties starters = new Properties();
  private Pattern versionPattern = Pattern.compile("[\\w.:-]+\\$\\{(\\w+)}");

  @Override
  public void apply(Project target) {
    // --- create the extensions
    target.getExtensions().create("managedDependencies", ManagedDependenciesExtension.class, target);
    // --- register what to do with java plugin tasks
    target.getPlugins().withType(JavaPlugin.class, plugin -> {
      target.afterEvaluate(y -> addDependencies(target));
      configureTasks(target);
    });
    // --- application plugin requires the dependencies too, to run from IDE
    target.getPlugins().withType(ApplicationPlugin.class, plugin -> {
      target.afterEvaluate(y -> addDependencies(target));
    });
  }

  private void addDependencies (Project target) {
    // --- creates the gradle configuration extension
    ManagedDependenciesExtension extension = target.getExtensions().findByType(ManagedDependenciesExtension.class);
    // --- read versions
    String versionsFile = "versions-" + extension.getBranch().getOrElse("master") + ".properties";
    versions = readProperties(versionsFile);
    // --- read dependencies
    String dependenciesFile = "dependencies-" + extension.getBranch().getOrElse("master") + ".properties";
    properties = readProperties(dependenciesFile);
    // --- read starters
    String startersFile = "starters-" + extension.getBranch().getOrElse("master") + ".properties";
    starters = readProperties(startersFile);
    // --- resolve property versions where required
    properties.keySet().iterator().forEachRemaining(key -> {
      String value = properties.getProperty((String)key);
      Matcher matcher = versionPattern.matcher(value);
      if (matcher.matches()) {
        String placeholder = matcher.group(1);
        if (placeholder == null) System.err.println("Failed to get version for '" + value + "'");
        String version = versions.getProperty(placeholder);
        value = value.replace("${" + placeholder + "}", version);
        properties.setProperty((String)key, value);
      }
    });
    // --- add starter dependencies
    extension.getStarters().getOrElse(Collections.emptyList()).forEach(starter -> {
      // implementations
      String deps = starters.getProperty(starter + ".implementation");
      if (deps != null && deps.length() > 0) {
        Arrays.asList(deps.split("\\s*,\\s*")).forEach(dep -> extension.getImplementations().add(dep));
      }
      deps = starters.getProperty(starter + ".test");
      if (deps != null && deps.length() > 0) {
        Arrays.asList(deps.split("\\s*,\\s*")).forEach(dep -> extension.getTestImplementations().add(dep));
      }
    });
    // --- resolve implementation dependencies
    extension.getImplementations().getOrElse(Collections.emptyList()).forEach(dep -> {
      String resolvedDependency = properties.getProperty(dep);
      target.getDependencies().add(IMPLEMENTATION_CONFIGURATION_NAME, resolvedDependency);
    });
    // --- resolve test implementation dependencies
    extension.getTestImplementations().getOrElse(Collections.emptyList()).forEach(dep -> {
      String resolvedDependency = properties.getProperty(dep);
      target.getDependencies().add(TEST_IMPLEMENTATION_CONFIGURATION_NAME, resolvedDependency);
    });
    // --- resolve compile only dependencies
    extension.getCompileOnly().getOrElse(Collections.emptyList()).forEach(dep -> {
      String resolvedDependency = properties.getProperty(dep);
      target.getDependencies().add(COMPILE_ONLY_CONFIGURATION_NAME, resolvedDependency);
    });
  }

  private void configureTasks(Project target) {
    ManagedDependenciesExtension extension = target.getExtensions().findByType(ManagedDependenciesExtension.class);
    target.getTasks().register("logManagedDependencies", ManageDependenciesTask.class,
      task -> {
        task.setBranch(extension.getBranch());
        task.setImplementations(extension.getImplementations());
        task.setProperties(properties);
      });
  }

  private Properties readProperties(String resourceName) {
    InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourceName);
    Properties properties = new Properties();
    try {
      properties.load(resourceAsStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

}
