package com.willian.gama.kgp.plugin.extension

import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_MAIN_SOURCE_SET
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_EXCLUSION
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_GENERATED_EXCLUSION
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_HTML_REPORT_PATH
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_INCLUDE_EXECUTION_DATA
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_PLUGIN_ID
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_TOOLS_VERSION
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_XML_REPORT_PATH
import com.willian.gama.kgp.extension.generateCodeCoverageTask
import com.willian.gama.kgp.extension.setUpJacoco
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_VARIANT
import com.willian.gama.kgp.plugin.test.TestData.TEST_JAVA_PLUGIN
import com.willian.gama.kgp.util.ReportUtil.getGeneratedJavaReportPath
import com.willian.gama.kgp.util.ReportUtil.getGeneratedKotlinReportPath
import org.gradle.api.internal.file.collections.DefaultConfigurableFileCollection
import org.gradle.api.internal.file.collections.DefaultConfigurableFileTree
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.gradle.api.tasks.testing.Test as UnitTest

class JacocoExtTest {
    @Test
    fun `test jacoco plugin is applied`() {
        val project = ProjectBuilder.builder().build()
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        assertTrue(project.pluginManager.hasPlugin(JACOCO_PLUGIN_ID))
    }

    @Test
    fun `test jacoco toolVersion is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        project.extensions.getByType<JacocoPluginExtension>().run {
            assertEquals(JACOCO_TOOLS_VERSION, toolVersion)
        }
    }

    @Test
    fun `test jacoco generateCodeCoverageTask sourceDirectories is set`() {
        val project = ProjectBuilder.builder().build()
        project.generateCodeCoverageTask(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(
                project.layout.projectDirectory.dir(ANDROID_MAIN_SOURCE_SET).asFile,
                sourceDirectories.from.first()
            )
        }
    }

    @Test
    fun `test jacoco executionData path is set`() {
        val project = ProjectBuilder.builder().build()
        project.generateCodeCoverageTask(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(
                project.layout.buildDirectory.get().asFile,
                executionData.from
                    .filterIsInstance<DefaultConfigurableFileTree>()
                    .first().dir.absoluteFile
            )
        }
    }

    @Test
    fun `test jacoco executionData inclusion is set`() {
        val project = ProjectBuilder.builder().build()
        project.generateCodeCoverageTask(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(
                JACOCO_INCLUDE_EXECUTION_DATA.toHashSet(),
                executionData.from
                    .filterIsInstance<DefaultConfigurableFileTree>()
                    .first().includes
            )
        }
    }

    @Test
    fun `test jacoco classDirectories path is set`() {
        val project = ProjectBuilder.builder().build()
        project.generateCodeCoverageTask(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(
                listOf(
                    project.layout.buildDirectory.dir(getGeneratedJavaReportPath(buildType = TEST_DEBUG_VARIANT)).get().asFile,
                    project.layout.buildDirectory.dir(getGeneratedKotlinReportPath(buildType = TEST_DEBUG_VARIANT)).get().asFile
                ),
                classDirectories.from
                    .filterIsInstance<DefaultConfigurableFileCollection>()
                    .flatMap { it.from.filterIsInstance<DefaultConfigurableFileTree>() }
                    .map { it.dir }
            )
        }
    }

    @Test
    fun `test jacoco classDirectories exclusion is set`() {
        val project = ProjectBuilder.builder().build()
        project.generateCodeCoverageTask(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(
                listOf(
                    JACOCO_GENERATED_EXCLUSION.toHashSet(),
                    JACOCO_GENERATED_EXCLUSION.toHashSet()
                ),
                classDirectories.from
                    .filterIsInstance<DefaultConfigurableFileCollection>()
                    .flatMap { it.from.filterIsInstance<DefaultConfigurableFileTree>() }
                    .map { it.excludes }
            )
        }
    }

    @Test
    fun `test jacoco generateCodeCoverageTask html report is required`() {
        val project = ProjectBuilder.builder().build()
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(true, reports.html.required.get())
        }
    }

    @Test
    fun `test jacoco generateCodeCoverageTask html outputLocation is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(
                project.layout.buildDirectory.dir(JACOCO_HTML_REPORT_PATH).get(),
                reports.html.outputLocation.get()
            )
        }
    }

    @Test
    fun `test jacoco generateCodeCoverageTask xml report is required`() {
        val project = ProjectBuilder.builder().build()
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(true, reports.xml.required.get())
        }
    }

    @Test
    fun `test jacoco generateCodeCoverageTask xml outputLocation is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<JacocoReport>().first().run {
            assertEquals(
                project.layout.buildDirectory.dir(JACOCO_XML_REPORT_PATH).get().asFile,
                reports.xml.outputLocation.get().asFile
            )
        }
    }

    @Test
    fun `test jacoco isIncludeNoLocationClasses set`() {
        val project = ProjectBuilder.builder().build().also {
            it.plugins.apply(TEST_JAVA_PLUGIN)
        }
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<UnitTest>()
            .first()
            .extensions.getByType<JacocoTaskExtension>().run {
                assertEquals(true, isIncludeNoLocationClasses)
            }
    }

    @Test
    fun `test jacoco exclusion is set`() {
        val project = ProjectBuilder.builder().build().also {
            it.plugins.apply(TEST_JAVA_PLUGIN)
        }
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        project.tasks.withType<UnitTest>()
            .first()
            .extensions.getByType<JacocoTaskExtension>().run {
                assertEquals(JACOCO_EXCLUSION, excludes)
            }
    }

    @Test
    fun `test showStackTraces is not set`() {
        val project = ProjectBuilder.builder().build().also {
            it.plugins.apply(TEST_JAVA_PLUGIN)
        }
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        assertEquals(
            false,
            project.tasks.withType<UnitTest>().first().testLogging.showStackTraces
        )
    }

    @Test
    fun `test exceptionFormat is set as full`() {
        val project = ProjectBuilder.builder().build().also {
            it.plugins.apply(TEST_JAVA_PLUGIN)
        }
        project.setUpJacoco(buildType = TEST_DEBUG_VARIANT)
        assertEquals(
            TestExceptionFormat.FULL,
            project.tasks.withType<UnitTest>().first().testLogging.exceptionFormat
        )
    }
}