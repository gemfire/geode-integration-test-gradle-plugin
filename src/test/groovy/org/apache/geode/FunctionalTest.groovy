package org.apache.geode

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

import static org.assertj.core.api.Assertions.*

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

class FunctionalTest  extends Specification {
    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'GeodeIntegration'
            }
        """
    }

    def "can configure release version"() {
        buildFile << """
            repositories {
                maven { url 'https://repository.apache.org/content/repositories/releases' }
            }
            geodeIntegration {
                version = '1.2.0'
            }
        """

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('installGeode')
                .withPluginClasspath()
                .build()

        then:
        result.task(":installGeode").outcome == SUCCESS
        verifyVersion(result, "1.2.0")
        verifyInstallation()
    }

    def "can configure snapshot version"() {
        buildFile << """
            repositories {
                maven { url 'https://repository.apache.org/content/repositories/snapshots' }
            }
            geodeIntegration {
                version = '1.3.0-SNAPSHOT'
            }
        """

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('installGeode')
                .withPluginClasspath()
                .build()

        then:
        result.task(":installGeode").outcome == SUCCESS

        verifyVersion(result, "1.3.0-SNAPSHOT")
        verifyInstallation()
    }

    private  void verifyVersion(BuildResult result, String version){
        assertThat(result.output).contains("Using apache-geode-${version}.zip");
    }

    private  void verifyInstallation() {
        assertThat(new File("${testProjectDir.root}/build/install/apache-geode/bin/gfsh")).exists()
    }
}
