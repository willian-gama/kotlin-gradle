package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.SonarConstants.SONAR_PLUGIN_ID
import com.willian.gama.kgp.test.TestData.TEST_DEBUG_VARIANT
import com.willian.gama.kgp.test.TestData.TEST_MAJOR_VERSION
import com.willian.gama.kgp.test.TestData.TEST_SONAR_ORGANIZATION
import com.willian.gama.kgp.test.TestData.TEST_SONAR_PROJECT_KEY
import com.willian.gama.kgp.test.TestData.TEST_SONAR_PROJECT_NAME
import com.willian.gama.kgp.test.TestData.TEST_SONAR_TOKEN
import com.willian.gama.kgp.test.TestData.createSonarProperties
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sonarqube.gradle.SonarExtension

private val TEST_SONAR_PROPERTIES = createSonarProperties(
    token = TEST_SONAR_TOKEN,
    projectKey = TEST_SONAR_PROJECT_KEY,
    organizationKey = TEST_SONAR_ORGANIZATION,
    projectName = TEST_SONAR_PROJECT_NAME,
    buildType = TEST_DEBUG_VARIANT,
    kotlinVersion = TEST_MAJOR_VERSION
)

class SonarExtTest {
    @Test
    fun `test sonar plugin is applied`() {
        val project = ProjectBuilder.builder().build()
        project.setUpSonar(properties = TEST_SONAR_PROPERTIES)
        assertTrue(project.pluginManager.hasPlugin(SONAR_PLUGIN_ID))
    }

    @Test
    fun `test sonar build variant is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpSonar(properties = TEST_SONAR_PROPERTIES)
        project.extensions.getByType<SonarExtension>().run {
            assertEquals(TEST_DEBUG_VARIANT, androidVariant)
        }
    }
}