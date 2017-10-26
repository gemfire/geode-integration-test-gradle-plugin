package org.apache.geode
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


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.testing.Test

class GeodeIntegrationPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        GeodeExtension extension = project.extensions.create("geodeIntegration", GeodeExtension)

        project.configurations {
            geodeIntegration {
                defaultDependencies { dependencies ->
                    dependencies.add(project.dependencies.create("org.apache.geode:apache-geode:${extension.version}@zip"))
                }
            }
        }

        Task installGeode = project.task('installGeode', type: Sync) {
            dependsOn project.configurations.geodeIntegration
            from {
                project.zipTree(project.configurations.geodeIntegration.singleFile)
            }

            // Strip version number from base zip name
            eachFile { FileCopyDetails file ->
                file.path = (file.path - ~/apache-geode-[0-9.]+(-SNAPSHOT)?/)
            }
            into "${project.buildDir}/install/apache-geode/"

            doLast{
                println("Using " + project.configurations.geodeIntegration.singleFile.name)
                println("GEODE_HOME is set to '${destinationDir}'")
            }
        }

        project.tasks.withType(Test) {
            environment GEODE_HOME: installGeode.destinationDir
            dependsOn installGeode
        }
    }
}

