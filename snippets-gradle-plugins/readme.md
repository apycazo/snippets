# Gradle plugins

## Usage

Add the plugin:

```groovy
plugins {
  id 'snippets.dependencies' version '1.0-SNAPSHOT'
}
```

And configure dependencies:

```groovy
managedDependencies {
  branch = "master"
  implementations = ["jackson-databind"]
  starters = ['log4j']
}
```

## Maven local

Note that to have plugins working from mavenLocal, the following must be
found in your `settings.gradle` (here found at the root module):

```groovy
pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
  }
}
```