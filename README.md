# gradle-geode-integration-test-plugin
Gradle plugin to facilitate testing applications that use Apache Geode.

This Gradle plugin adds a task (:installGeode) that downloads a distribution of Geode via Maven, unzips it into build/install/apache-geode, and sets the proper environment variable for any tests that are run through Gradle.
