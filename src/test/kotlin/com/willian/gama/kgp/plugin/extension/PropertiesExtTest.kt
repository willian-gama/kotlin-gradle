package com.willian.gama.kgp.plugin.extension

import com.willian.gama.kgp.constants.SonarConstants.SONAR_LOCALHOST_URL_VALUE
import com.willian.gama.kgp.constants.SonarConstants.getMissingPropertyErrorMessage
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_ACCESS_TOKEN
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_URL
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_USERNAME
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_ORGANIZATION_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_PROJECT_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_PROJECT_NAME_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_TOKEN_KEY
import com.willian.gama.kgp.extension.getPropertySafely
import com.willian.gama.kgp.extension.toJfrogProperties
import com.willian.gama.kgp.extension.toSonarProperties
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_VARIANT
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAJOR_VERSION
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_JFROG_REPO_KEY
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_REPO_ACCESS_TOKEN
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_REPO_URL
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAVEN_REPO_USERNAME
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_ORGANIZATION
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_PROJECT_KEY
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_PROJECT_NAME
import com.willian.gama.kgp.plugin.test.TestData.TEST_SONAR_TOKEN
import com.willian.gama.kgp.plugin.test.TestData.createCodeAnalysis
import com.willian.gama.kgp.plugin.test.TestData.createJfrogProperties
import com.willian.gama.kgp.plugin.test.TestData.createSonarProperties
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Properties

private val TEST_CODE_ANALYSIS = createCodeAnalysis(
    buildType = TEST_DEBUG_VARIANT,
    kotlinVersion = TEST_MAJOR_VERSION,
    jfrogRepoKey = TEST_MAVEN_JFROG_REPO_KEY
)

class PropertiesExtTest {
    @Test
    fun `test properties to sonar properties parsing successfully`() {
        val expectedSonarProperties = createSonarProperties(
            url = SONAR_LOCALHOST_URL_VALUE,
            token = TEST_SONAR_TOKEN,
            projectKey = TEST_SONAR_PROJECT_KEY,
            organizationKey = TEST_SONAR_ORGANIZATION,
            projectName = TEST_SONAR_PROJECT_NAME,
            buildType = TEST_DEBUG_VARIANT,
            kotlinVersion = TEST_MAJOR_VERSION
        )
        val sonarProperties = Properties().apply {
            setProperty(USER_PROPERTY_SONAR_TOKEN_KEY, TEST_SONAR_TOKEN)
            setProperty(USER_PROPERTY_SONAR_PROJECT_KEY, TEST_SONAR_PROJECT_KEY)
            setProperty(USER_PROPERTY_SONAR_ORGANIZATION_KEY, TEST_SONAR_ORGANIZATION)
            setProperty(USER_PROPERTY_SONAR_PROJECT_NAME_KEY, TEST_SONAR_PROJECT_NAME)
        }.toSonarProperties(
            codeAnalysis = TEST_CODE_ANALYSIS,
            isCiEnvironment = false
        )

        assertEquals(expectedSonarProperties, sonarProperties)
    }

    @Test
    fun `test properties to sonar properties parsing with missing token key`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            Properties().apply {
                setProperty(USER_PROPERTY_SONAR_PROJECT_KEY, TEST_SONAR_PROJECT_KEY)
                setProperty(USER_PROPERTY_SONAR_ORGANIZATION_KEY, TEST_SONAR_ORGANIZATION)
                setProperty(USER_PROPERTY_SONAR_PROJECT_NAME_KEY, TEST_SONAR_PROJECT_NAME)
            }.toSonarProperties(
                codeAnalysis = TEST_CODE_ANALYSIS,
                isCiEnvironment = false
            )
        }
        assertEquals(
            getMissingPropertyErrorMessage(USER_PROPERTY_SONAR_TOKEN_KEY),
            exception.message
        )
    }

    @Test
    fun `test properties to sonar properties parsing with missing project key`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            Properties().apply {
                setProperty(USER_PROPERTY_SONAR_TOKEN_KEY, TEST_SONAR_TOKEN)
                setProperty(USER_PROPERTY_SONAR_ORGANIZATION_KEY, TEST_SONAR_ORGANIZATION)
                setProperty(USER_PROPERTY_SONAR_PROJECT_NAME_KEY, TEST_SONAR_PROJECT_NAME)
            }.toSonarProperties(
                codeAnalysis = TEST_CODE_ANALYSIS,
                isCiEnvironment = false
            )
        }
        assertEquals(
            getMissingPropertyErrorMessage(USER_PROPERTY_SONAR_PROJECT_KEY),
            exception.message
        )
    }

    @Test
    fun `test properties to sonar properties parsing with missing organization key`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            Properties().apply {
                setProperty(USER_PROPERTY_SONAR_TOKEN_KEY, TEST_SONAR_TOKEN)
                setProperty(USER_PROPERTY_SONAR_PROJECT_KEY, TEST_SONAR_PROJECT_KEY)
                setProperty(USER_PROPERTY_SONAR_PROJECT_NAME_KEY, TEST_SONAR_PROJECT_NAME)
            }.toSonarProperties(
                codeAnalysis = TEST_CODE_ANALYSIS,
                isCiEnvironment = false
            )
        }
        assertEquals(
            getMissingPropertyErrorMessage(USER_PROPERTY_SONAR_ORGANIZATION_KEY),
            exception.message
        )
    }

    @Test
    fun `test properties to sonar properties parsing with missing project name key`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            Properties().apply {
                setProperty(USER_PROPERTY_SONAR_TOKEN_KEY, TEST_SONAR_TOKEN)
                setProperty(USER_PROPERTY_SONAR_PROJECT_KEY, TEST_SONAR_PROJECT_KEY)
                setProperty(USER_PROPERTY_SONAR_ORGANIZATION_KEY, TEST_SONAR_ORGANIZATION)
            }.toSonarProperties(
                codeAnalysis = TEST_CODE_ANALYSIS,
                isCiEnvironment = false
            )
        }
        assertEquals(
            getMissingPropertyErrorMessage(USER_PROPERTY_SONAR_PROJECT_NAME_KEY),
            exception.message
        )
    }

    @Test
    fun `test get property value safely successfully`() {
        val properties = Properties().apply {
            setProperty(USER_MAVEN_REPO_URL, TEST_MAVEN_REPO_URL)
        }
        assertEquals(
            TEST_MAVEN_REPO_URL,
            properties.getPropertySafely(param = USER_MAVEN_REPO_URL)
        )
    }

    @Test
    fun `test properties to jfrog properties parsing parsing successfully`() {
        val expectedKtLintProperties = createJfrogProperties(
            repoUrl = TEST_MAVEN_REPO_URL,
            repoKey = TEST_MAVEN_JFROG_REPO_KEY,
            username = TEST_MAVEN_REPO_USERNAME,
            password = TEST_MAVEN_REPO_ACCESS_TOKEN
        )
        val jfrogProperties = Properties().apply {
            setProperty(USER_MAVEN_REPO_URL, TEST_MAVEN_REPO_URL)
            setProperty(USER_MAVEN_REPO_USERNAME, TEST_MAVEN_REPO_USERNAME)
            setProperty(USER_MAVEN_REPO_ACCESS_TOKEN, TEST_MAVEN_REPO_ACCESS_TOKEN)
        }.toJfrogProperties(codeAnalysis = TEST_CODE_ANALYSIS)

        assertEquals(expectedKtLintProperties, jfrogProperties)
    }

    @Test
    fun `test properties to jfrog properties parsing with missing maven repo url`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            Properties().apply {
                setProperty(USER_MAVEN_REPO_USERNAME, TEST_MAVEN_REPO_USERNAME)
                setProperty(USER_MAVEN_REPO_ACCESS_TOKEN, TEST_MAVEN_REPO_ACCESS_TOKEN)
            }.toJfrogProperties(codeAnalysis = TEST_CODE_ANALYSIS)
        }
        assertEquals(
            getMissingPropertyErrorMessage(USER_MAVEN_REPO_URL),
            exception.message
        )
    }

    @Test
    fun `test properties to jfrog properties parsing with missing maven username`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            Properties().apply {
                setProperty(USER_MAVEN_REPO_URL, TEST_MAVEN_REPO_URL)
                setProperty(USER_MAVEN_REPO_ACCESS_TOKEN, TEST_MAVEN_REPO_ACCESS_TOKEN)
            }.toJfrogProperties(codeAnalysis = TEST_CODE_ANALYSIS)
        }
        assertEquals(
            getMissingPropertyErrorMessage(USER_MAVEN_REPO_USERNAME),
            exception.message
        )
    }

    @Test
    fun `test properties to jfrog properties parsing with missing maven access token`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            Properties().apply {
                setProperty(USER_MAVEN_REPO_URL, TEST_MAVEN_REPO_URL)
                setProperty(USER_MAVEN_REPO_USERNAME, TEST_MAVEN_REPO_USERNAME)
            }.toJfrogProperties(codeAnalysis = TEST_CODE_ANALYSIS)
        }
        assertEquals(
            getMissingPropertyErrorMessage(USER_MAVEN_REPO_ACCESS_TOKEN),
            exception.message
        )
    }

    @Test(expected = IllegalStateException::class)
    fun `test get property value safely with exception`() {
        Properties().getPropertySafely(USER_MAVEN_REPO_URL)
    }
}