package com.willian.gama.kgp.extension

import com.android.build.gradle.BaseExtension
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_API_NODE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_ARCHIVE_CLASSIFIER
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_ARTIFACT_ID_NODE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_BUNDLE_FULL_RELEASE_AAR
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_BUNDLE_SNAPSHOT_RELEASE_AAR
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_DEPENDENCIES_NODE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_DEPENDENCY_NODE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_FULL_RELEASE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_GROUP_ID_NODE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_JFROG_PLUGIN_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_LOCAL
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_LOCAL_FULL_RELEASE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_MAIN_SOURCE_SET
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_PUBLISH_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_SNAPSHOT
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_SNAPSHOT_RELEASE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_SOURCE_JAR
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_VERSION_NODE
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_LIBRARY
import com.willian.gama.kgp.model.MavenProperties
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

fun Project.setUpMavenPublish(properties: MavenProperties) {
    plugins.withId(ANDROID_LIBRARY) {
        pluginManager.apply(MAVEN_JFROG_PLUGIN_ID)
        pluginManager.apply(MAVEN_PUBLISH_ID)

        tasks.register<Jar>(MAVEN_SOURCE_JAR) {
            from(project.extensions.getByType<BaseExtension>().sourceSets[MAVEN_MAIN_SOURCE_SET].java.srcDirs)
            archiveClassifier.set(MAVEN_ARCHIVE_CLASSIFIER)
        }

        // https://jfrog.com/help/r/artifactory-how-to-publish-build-info-and-artifacts-from-a-gradle-android-project-into-artifactory/artifactory-how-to-publish-build-info-and-artifacts-from-a-gradle-android-project-into-artifactory
        extensions.configure<PublishingExtension> {
            publications {
                register<MavenPublication>(MAVEN_LOCAL_FULL_RELEASE) {
                    groupId = properties.mavenGroupId
                    artifactId = properties.mavenModule
                    version = "${properties.mavenGroupVersion}-$MAVEN_LOCAL"

                    afterEvaluate {
                        artifact(tasks[MAVEN_SOURCE_JAR])
                        artifact(tasks[MAVEN_BUNDLE_FULL_RELEASE_AAR])
                        configurations[MAVEN_API_NODE].dependencies.toList().let { dependencies ->
                            pom.withXml {
                                val dependenciesNode = asNode().appendNode(MAVEN_DEPENDENCIES_NODE)
                                dependencies.forEach {
                                    dependenciesNode.appendNode(MAVEN_DEPENDENCY_NODE).apply {
                                        appendNode(MAVEN_GROUP_ID_NODE, it.group)
                                        appendNode(MAVEN_ARTIFACT_ID_NODE, it.name)
                                        appendNode(MAVEN_VERSION_NODE, it.version)
                                    }
                                }
                            }
                        }
                    }
                }

                register<MavenPublication>(MAVEN_FULL_RELEASE) {
                    groupId = properties.mavenGroupId
                    artifactId = properties.mavenModule
                    version = properties.mavenGroupVersion

                    afterEvaluate {
                        artifact(tasks[MAVEN_SOURCE_JAR])
                        artifact(tasks[MAVEN_BUNDLE_FULL_RELEASE_AAR])
                        configurations[MAVEN_API_NODE].dependencies.toList().let { dependencies ->
                            pom.withXml {
                                val dependenciesNode = asNode().appendNode(MAVEN_DEPENDENCIES_NODE)
                                dependencies.forEach {
                                    dependenciesNode.appendNode(MAVEN_DEPENDENCY_NODE).apply {
                                        appendNode(MAVEN_GROUP_ID_NODE, it.group)
                                        appendNode(MAVEN_ARTIFACT_ID_NODE, it.name)
                                        appendNode(MAVEN_VERSION_NODE, it.version)
                                    }
                                }
                            }
                        }
                    }
                }

                register<MavenPublication>(MAVEN_SNAPSHOT_RELEASE) {
                    groupId = properties.mavenGroupId
                    artifactId = properties.mavenModule
                    version = "${properties.mavenGroupVersion}-$MAVEN_SNAPSHOT"

                    afterEvaluate {
                        artifact(tasks[MAVEN_SOURCE_JAR])
                        artifact(tasks[MAVEN_BUNDLE_SNAPSHOT_RELEASE_AAR])
                        configurations[MAVEN_API_NODE].dependencies.toList().let { dependencies ->
                            pom.withXml {
                                val dependenciesNode = asNode().appendNode(MAVEN_DEPENDENCIES_NODE)
                                dependencies.forEach {
                                    dependenciesNode.appendNode(MAVEN_DEPENDENCY_NODE).apply {
                                        appendNode(MAVEN_GROUP_ID_NODE, it.group)
                                        appendNode(MAVEN_ARTIFACT_ID_NODE, it.name)
                                        appendNode(MAVEN_VERSION_NODE, it.version)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}