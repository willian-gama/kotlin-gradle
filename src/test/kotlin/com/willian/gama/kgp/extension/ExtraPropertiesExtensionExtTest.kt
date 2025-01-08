package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.MavenConstants.MAVEN_GROUP_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_GROUP_VERSION
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_ARTIFACT_ID
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_GROUP
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_VERSION
import com.willian.gama.kgp.test.TestData.createMavenProperties
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtraPropertiesExtensionExtTest {
    @Test
    fun `test extra properties to maven properties parsing`() {
        val project = ProjectBuilder.builder().build().also {
            it.extra.set(MAVEN_GROUP_ID, TEST_MAVEN_LIB_GROUP)
            it.extra.set(MAVEN_GROUP_VERSION, TEST_MAVEN_LIB_VERSION)
        }
        val expectedMavenProperties = createMavenProperties(
            mavenGroupId = TEST_MAVEN_LIB_GROUP,
            mavenGroupVersion = TEST_MAVEN_LIB_VERSION,
            mavenArtifactId = TEST_MAVEN_LIB_ARTIFACT_ID
        )

        assertEquals(
            expectedMavenProperties,
            project.extra.toMavenProperties(moduleName = TEST_MAVEN_LIB_ARTIFACT_ID)
        )
    }
}