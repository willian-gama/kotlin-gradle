package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_BASELINE_REPORT_PATH
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_EDITORCONFIG
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_PLUGIN_ID
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_RULES
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_VERSION
import com.willian.gama.kgp.test.TestData.TEST_KTLINT_CHECK_REPORT
import com.willian.gama.kgp.test.TestData.TEST_KTLINT_FORMAT_REPORT
import com.willian.gama.kgp.test.TestData.TEST_KTLINT_RULES_CONTENT
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class KtLintExtTest {
    @Test
    fun `test ktlint plugin is applied`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = false)
        assertTrue(project.pluginManager.hasPlugin(KTLINT_PLUGIN_ID))
    }

    @Test
    fun `test ktlint version is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = false)
        project.extensions.getByType<KtlintExtension>().run {
            assertEquals(KTLINT_VERSION, version.get())
        }
    }

    @Test
    fun `test ktlint android environment is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = false)
        project.extensions.getByType<KtlintExtension>().run {
            assertEquals(true, android.get())
        }
    }

    @Test
    fun `test ktlint verbose is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = false)
        project.extensions.getByType<KtlintExtension>().run {
            assertEquals(true, verbose.get())
        }
    }

    @Test
    fun `test ktlint ignore failure is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = true)
        project.extensions.getByType<KtlintExtension>().run {
            assertEquals(true, ignoreFailures.get())
        }
    }

    @Test
    fun `test ktlint additionalEditorconfig is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = false)
        project.extensions.getByType<KtlintExtension>().run {
            assertEquals(KTLINT_RULES, additionalEditorconfig.get())
        }
    }

    @Test
    fun `test ktlint baseline is set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = false)
        project.extensions.getByType<KtlintExtension>().run {
            assertEquals(
                project.layout.projectDirectory.dir(KTLINT_BASELINE_REPORT_PATH).asFile,
                baseline.get().asFile
            )
        }
    }

    @Test
    fun `test ktlint generated reports are set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint(isIgnoreFailures = false)

        val expectedReports = listOf(TEST_KTLINT_CHECK_REPORT, TEST_KTLINT_FORMAT_REPORT)
        val reports = project.tasks.withType<GenerateReportsTask>().map {
            it.reportsOutputDirectory.get()
                .asFile
                .absolutePath.substringAfter(delimiter = "projectDir/")
        }
        assertEquals(reports, expectedReports)
    }

    @Test
    fun `test ktlint editorconfig is generated`() {
        val project = ProjectBuilder.builder().build()
        project.generateKtLintEditorConfig()

        val editorConfigContent = File(project.projectDir.path, KTLINT_EDITORCONFIG)
            .readText()
            .trimEnd('\n')
            .lines()

        assertEquals(TEST_KTLINT_RULES_CONTENT, editorConfigContent)
    }
}