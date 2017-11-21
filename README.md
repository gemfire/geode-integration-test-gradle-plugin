# geode-integration-test-gradle-plugin

Gradle plugin to facilitate testing applications that use Apache Geode.

This Gradle plugin adds a task (:installGeode) that downloads a distribution 
of Geode via Maven, unzips it into build/install/apache-geode, and sets the
proper environment variable for any tests that are run through Gradle.

## Usage
To use this plugin in your application, you only need to include the following
line in your `build.gradle` file:
```
apply plugin: 'geode-integration-test-gradle-plugin'
```
This will install the latest version of Apache Geode locally to be used by your
integration tests.

To specify a specific version of Apache Geode, specify the following in your
application's `build.gradle` file:
```
geodeIntegration {
  version = "1.2.0"
}
```

Writing the test itself is described here:
https://cwiki.apache.org/confluence/display/GEODE/Test+your+application
