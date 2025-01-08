package com.willian.gama.kgp.extension

import com.android.build.gradle.BaseExtension
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_LIBRARY
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_MAIN_SOURCE_SET
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_ARCHIVE_CLASSIFIER
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_FULL_RELEASE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_JFROG_PLUGIN_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_LOCAL_FULL_RELEASE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_MAIN_SOURCE_SET
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_PUBLISH_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_SNAPSHOT_RELEASE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_SOURCE_JAR
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_ARTIFACT_ID
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_GROUP
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_VERSION
import com.willian.gama.kgp.test.TestData.createMavenProperties
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private val TEST_MAVEN_PROPERTIES = createMavenProperties(
    mavenGroupId = TEST_MAVEN_LIB_GROUP,
    mavenGroupVersion = TEST_MAVEN_LIB_VERSION,
    mavenArtifactId = TEST_MAVEN_LIB_ARTIFACT_ID
)

class MavenExtTest {
    private lateinit var project: Project

    @Before
    fun setUp() {
        project = ProjectBuilder.builder().build().also {
            it.pluginManager.apply(ANDROID_LIBRARY)
        }
    }

    @Test
    fun `test maven jfrog plugin is applied`() {
        project.setUpMavenPublish(properties = TEST_MAVEN_PROPERTIES)
        assertTrue(project.pluginManager.hasPlugin(MAVEN_JFROG_PLUGIN_ID))
    }

    @Test
    fun `test maven publish plugin is applied`() {
        project.setUpMavenPublish(properties = TEST_MAVEN_PROPERTIES)
        assertTrue(project.pluginManager.hasPlugin(MAVEN_PUBLISH_ID))
    }

    @Test
    fun `test all publications are found`() {
        project.setUpMavenPublish(properties = TEST_MAVEN_PROPERTIES)
        project.extensions.getByType<PublishingExtension>().run {
            assertEquals(
                listOf(
                    MAVEN_FULL_RELEASE,
                    MAVEN_LOCAL_FULL_RELEASE,
                    MAVEN_SNAPSHOT_RELEASE
                ),
                publications.names.toList()
            )
        }
    }
    
    @Test
    fun `test sourceJar name is set`() {
        project.setUpMavenPublish(properties = TEST_MAVEN_PROPERTIES)
        project.tasks.withType<Jar>().first().run {
            assertEquals(MAVEN_SOURCE_JAR, name)
        }
    }

    @Test
    fun `test sourceJar archiveClassifier is set`() {
        project.setUpMavenPublish(properties = TEST_MAVEN_PROPERTIES)
        project.tasks.withType<Jar>().first().run {
            assertEquals(MAVEN_ARCHIVE_CLASSIFIER, archiveClassifier.get())
        }
    }

    @Test
    fun `test sourceJar from is set`() {
        project.setUpMavenPublish(properties = TEST_MAVEN_PROPERTIES)
        project.tasks.withType<Jar>().first().run {
            assertEquals(
                ANDROID_MAIN_SOURCE_SET,
                from().project.extensions.getByType<BaseExtension>()
                    .sourceSets.getByName(MAVEN_MAIN_SOURCE_SET)
                    .java.srcDirs.first().toString()
                    .substringAfter(delimiter = "projectDir/")
            )
        }
    }
}