package com.willian.gama.kgp.plugin

import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_LIBRARY
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_GROUP_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_GROUP_VERSION
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_JFROG_PLUGIN_ID
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_GROUP
import com.willian.gama.kgp.test.TestData.TEST_MAVEN_LIB_VERSION
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertTrue
import org.junit.Test

class PublishLibPluginTest {
    @Test
    fun `test jfrog plugin is applied`() {
        val project = ProjectBuilder.builder().build().also {
            it.pluginManager.apply(ANDROID_LIBRARY)
            it.extra.set(MAVEN_GROUP_ID, TEST_MAVEN_LIB_GROUP)
            it.extra.set(MAVEN_GROUP_VERSION, TEST_MAVEN_LIB_VERSION)
        }
        val publishLibPlugin = PublishLibPlugin()
        publishLibPlugin.apply(project = project)
        assertTrue(project.pluginManager.hasPlugin(MAVEN_JFROG_PLUGIN_ID))
    }
}