package com.willian.gama.kgp.plugin.extension

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_APPLICATION
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_BASELINE_REPORT_PATH
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_LIBRARY
import com.willian.gama.kgp.extension.enableCodeCoverage
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_VARIANT
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("UnstableApiUsage")
class AndroidExtTest {
    @Test
    fun `test abortOnError is disabled in app`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_APPLICATION)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<ApplicationExtension>().run {
            assertEquals(false, lint.abortOnError)
        }
    }

    @Test
    fun `test abortOnError is disabled in library`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_LIBRARY)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<LibraryExtension>().run {
            assertEquals(false, lint.abortOnError)
        }
    }

    @Test
    fun `test android lint xml report is enabled in app`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_APPLICATION)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<ApplicationExtension>().run {
            assertEquals(true, lint.xmlReport)
        }
    }

    @Test
    fun `test android lint xml report is enabled in library`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_LIBRARY)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<LibraryExtension>().run {
            assertEquals(true, lint.xmlReport)
        }
    }

    @Test
    fun `test android lint baseline is set in app`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_APPLICATION)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<ApplicationExtension>().run {
            assertEquals(
                project.layout.projectDirectory.dir(ANDROID_BASELINE_REPORT_PATH).asFile,
                lint.baseline
            )
        }
    }

    @Test
    fun `test android lint baseline is set in library`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_LIBRARY)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<LibraryExtension>().run {
            assertEquals(
                project.layout.projectDirectory.dir(ANDROID_BASELINE_REPORT_PATH).asFile,
                lint.baseline
            )
        }
    }

    @Test
    fun `test android code coverage unit test is enabled in app`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_APPLICATION)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<ApplicationExtension>().run {
            assertEquals(true, buildTypes[TEST_DEBUG_VARIANT].enableUnitTestCoverage)
        }
    }

    @Test
    fun `test android code coverage unit test is enabled in library`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_LIBRARY)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<LibraryExtension>().run {
            assertEquals(true, buildTypes[TEST_DEBUG_VARIANT].enableUnitTestCoverage)
        }
    }

    @Test
    fun `test android code coverage ui test is enabled in app`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_APPLICATION)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<ApplicationExtension>().run {
            assertEquals(true, buildTypes[TEST_DEBUG_VARIANT].enableAndroidTestCoverage)
        }
    }

    @Test
    fun `test android code coverage ui test is enabled in library`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_LIBRARY)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<LibraryExtension>().run {
            assertEquals(true, buildTypes[TEST_DEBUG_VARIANT].enableAndroidTestCoverage)
        }
    }

    @Test
    fun `test android testOptions reportDir is set in app`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_APPLICATION)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<ApplicationExtension>().run {
            assertEquals(TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH, testOptions.reportDir)
        }
    }

    @Test
    fun `test android testOptions reportDir is set in library`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ANDROID_LIBRARY)
        project.enableCodeCoverage(uiTestReportDir = TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH)
        project.extensions.getByType<LibraryExtension>().run {
            assertEquals(TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH, testOptions.reportDir)
        }
    }
}