package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.MavenConstants.MAVEN_JFROG_PLUGIN_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_PUBLISH_ID
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_JFROG_REPO_KEY
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_REPO_ACCESS_TOKEN
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_REPO_URL
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_REPO_USERNAME
import com.willian.gama.kgp.test.TestData.createJfrogProperties
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private val TEST_JFROG_PROPERTIES = createJfrogProperties(
    repoUrl = TEST_MAVEN_REPO_URL,
    repoKey = TEST_MAVEN_JFROG_REPO_KEY,
    username = TEST_MAVEN_REPO_USERNAME,
    password = TEST_MAVEN_REPO_ACCESS_TOKEN
)

class JfrogExtTest {
    private lateinit var project: Project

    @Before
    fun setUp() {
        project = ProjectBuilder.builder().build()
    }

    @Test
    fun `test jfrog plugin is applied`() {
        project.setUpFrog(properties = TEST_JFROG_PROPERTIES)
        assertTrue(project.pluginManager.hasPlugin(MAVEN_JFROG_PLUGIN_ID))
    }

    @Test
    fun `test maven publish is applied`() {
        project.setUpFrog(properties = TEST_JFROG_PROPERTIES)
        assertTrue(project.pluginManager.hasPlugin(MAVEN_PUBLISH_ID))
    }

    @Test
    fun `test context url is set`() {
        project.setUpFrog(properties = TEST_JFROG_PROPERTIES)
        project.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(
                TEST_JFROG_PROPERTIES.repoUrl,
                publisherConfig.contextUrl
            )
        }
    }

    @Test
    fun `test repo key is set`() {
        project.setUpFrog(properties = TEST_JFROG_PROPERTIES)
        project.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(
                TEST_JFROG_PROPERTIES.repoKey,
                publisherConfig.repository.repoKey
            )
        }
    }

    @Test
    fun `test repo user name is set`() {
        project.setUpFrog(properties = TEST_JFROG_PROPERTIES)
        project.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(
                TEST_JFROG_PROPERTIES.username,
                publisherConfig.repository.username
            )
        }
    }

    @Test
    fun `test repo password is set`() {
        project.setUpFrog(properties = TEST_JFROG_PROPERTIES)
        project.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(
                TEST_JFROG_PROPERTIES.password,
                publisherConfig.repository.password
            )
        }
    }
}