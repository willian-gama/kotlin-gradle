package com.willian.gama.kgp.plugin.plugin

import com.android.build.gradle.LibraryExtension
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_LIBRARY
import com.willian.gama.kgp.constants.CodeAnalysisConstants.DETEKT_CONFIG_FILE_PATH
import com.willian.gama.kgp.constants.CodeAnalysisConstants.LOCAL_PROPERTIES
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_PLUGIN_IN
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_PLUGIN_ID
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_PLUGIN_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_JFROG_PLUGIN_ID
import com.willian.gama.kgp.constants.SonarConstants.SONAR_PLUGIN_ID
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_ACCESS_TOKEN
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_URL
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_USERNAME
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_ORGANIZATION_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_PROJECT_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_PROJECT_NAME_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_TOKEN_KEY
import com.willian.gama.kgp.model.CodeAnalysis
import com.willian.gama.kgp.plugin.CodeAnalysisPlugin
import com.willian.gama.kgp.plugin.test.TestData.TEST_ANDROID_COMPILE_SDK
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_JFROG_REPO_KEY
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_REPO_ACCESS_TOKEN
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_REPO_URL
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_REPO_USERNAME
import com.willian.gama.kgp.plugin.test.TestData.TEST_NAMESPACE
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_ORGANIZATION
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_PROJECT_KEY
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_PROJECT_NAME
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_TOKEN
import com.willian.gama.kgp.util.FileUtil.getFileFromResource
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.internal.project.DefaultProject
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.Properties

private val TEST_PROPERTIES = Properties().apply {
    setProperty(USER_PROPERTY_SONAR_TOKEN_KEY, TEST_SONAR_TOKEN)
    setProperty(USER_PROPERTY_SONAR_PROJECT_KEY, TEST_SONAR_PROJECT_KEY)
    setProperty(USER_PROPERTY_SONAR_ORGANIZATION_KEY, TEST_SONAR_ORGANIZATION)
    setProperty(USER_PROPERTY_SONAR_PROJECT_NAME_KEY, TEST_SONAR_PROJECT_NAME)
    setProperty(USER_MAVEN_REPO_URL, TEST_MAVEN_REPO_URL)
    setProperty(USER_MAVEN_REPO_USERNAME, TEST_MAVEN_REPO_USERNAME)
    setProperty(USER_MAVEN_REPO_ACCESS_TOKEN, TEST_MAVEN_REPO_ACCESS_TOKEN)
}

class CodeAnalysisPluginTest {
    private lateinit var rootProject: DefaultProject
    private lateinit var codeAnalysisPlugin: CodeAnalysisPlugin

    @Before
    fun setUp() {
        rootProject = ProjectBuilder.builder().build().also {
            it.pluginManager.apply(ANDROID_LIBRARY)
            it.extensions.configure<LibraryExtension> {
                compileSdk = TEST_ANDROID_COMPILE_SDK
                namespace = TEST_NAMESPACE
            }
        } as DefaultProject

        File(rootProject.projectDir, LOCAL_PROPERTIES).apply {
            outputStream().use { TEST_PROPERTIES.store(it, null) }
            deleteOnExit()
        }

        codeAnalysisPlugin = CodeAnalysisPlugin()
    }

    @Test
    fun `test sonar plugin is applied in rootProject`() {
        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.evaluate()

        assertTrue(rootProject.plugins.hasPlugin(SONAR_PLUGIN_ID))
    }

    @Test
    fun `test jfrog plugin is applied in rootProject`() {
        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.extensions.getByType<CodeAnalysis>().jfrogRepoKey = TEST_MAVEN_JFROG_REPO_KEY
        rootProject.evaluate()

        assertTrue(rootProject.plugins.hasPlugin(MAVEN_JFROG_PLUGIN_ID))
    }

    @Test
    fun `test jfrog plugin is not applied in rootProject`() {
        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.extensions.getByType<CodeAnalysis>().jfrogRepoKey = ""

        assertFalse(rootProject.plugins.hasPlugin(MAVEN_JFROG_PLUGIN_ID))
    }

    @Test
    fun `test ktlint plugin is applied in subproject`() {
        val subProject = ProjectBuilder.builder().withParent(rootProject).build()

        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.evaluate()

        assertTrue(subProject.plugins.hasPlugin(KTLINT_PLUGIN_ID))
    }

    @Test
    fun `test detekt plugin is applied in subproject`() {
        val subProject = ProjectBuilder.builder().withParent(rootProject).build()

        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.evaluate()

        assertTrue(subProject.plugins.hasPlugin(DETEKT_PLUGIN_IN))
    }

    @Test
    fun `test jacoco plugin is applied in subproject`() {
        val subProject = ProjectBuilder.builder().withParent(rootProject).build()

        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.evaluate()

        assertTrue(subProject.plugins.hasPlugin(JACOCO_PLUGIN_ID))
    }

    @Test
    fun `test detekt config rules file is set in subproject`() {
        val subProject = ProjectBuilder.builder().withParent(rootProject).build()

        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.evaluate()

        subProject.tasks.withType<Detekt>().first().run {
            assertEquals(
                getFileFromResource(fileName = DETEKT_CONFIG_FILE_PATH),
                config.from.first()
            )
        }
    }

    @Test
    fun `test jfrog context url is set in rootProject`() {
        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.extensions.getByType<CodeAnalysis>().jfrogRepoKey = TEST_MAVEN_JFROG_REPO_KEY
        rootProject.evaluate()

        rootProject.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(TEST_MAVEN_REPO_URL, publisherConfig.contextUrl)
        }
    }

    @Test
    fun `test jfrog repo key is set in rootProject`() {
        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.extensions.getByType<CodeAnalysis>().jfrogRepoKey = TEST_MAVEN_JFROG_REPO_KEY
        rootProject.evaluate()

        rootProject.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(TEST_MAVEN_JFROG_REPO_KEY, publisherConfig.repository.repoKey)
        }
    }

    @Test
    fun `test jfrog repo user name is set in rootProject`() {
        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.extensions.getByType<CodeAnalysis>().jfrogRepoKey = TEST_MAVEN_JFROG_REPO_KEY
        rootProject.evaluate()

        rootProject.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(TEST_MAVEN_REPO_USERNAME, publisherConfig.repository.username)
        }
    }

    @Test
    fun `test jfrog repo password is set in rootProject`() {
        codeAnalysisPlugin.apply(project = rootProject)
        rootProject.extensions.getByType<CodeAnalysis>().jfrogRepoKey = TEST_MAVEN_JFROG_REPO_KEY
        rootProject.evaluate()

        rootProject.extensions.getByType(ArtifactoryPluginConvention::class.java).run {
            assertEquals(TEST_MAVEN_REPO_ACCESS_TOKEN, publisherConfig.repository.password)
        }
    }
}