package com.willian.gama.kgp.plugin

import com.willian.gama.kgp.config.setUpKtLint
import com.willian.gama.kgp.rule.KTLINT_RULES
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask
import org.junit.Assert.assertEquals
import org.junit.Test

class KtLintConfigExtTest {
    @Test
    fun `test ktlint plugin is applied`() {
        val project = ProjectBuilder.builder().build()
        project.setUpKtLint()
        project.pluginManager.hasPlugin("org.jlleitschuh.gradle.ktlint")
    }

    @Test
    fun `test ktlint config params`() {
        ProjectBuilder.builder().build().run {
            setUpKtLint()
            with(extensions.findByType<KtlintExtension>()!!) {
                assertEquals("1.2.1", version.get())
                assertEquals(true, android.get())
                assertEquals(true, verbose.get())
                assertEquals(KTLINT_RULES, additionalEditorconfig.get())
            }
        }

    }

    @Test
    fun `test ktlint generated reports path`() {
        ProjectBuilder.builder().build().run {
            setUpKtLint()
            val files = tasks.withType<GenerateReportsTask>().map {
                it.reportsOutputDirectory.get()
                    .asFile
                    .absolutePath.substringAfter(delimiter = "projectDir/")
            }
            val expectedFiles = listOf(
                "build/reports/ktlint/ktlintKotlinScriptCheck",
                "build/reports/ktlint/ktlintKotlinScriptFormat"
            )
            assertEquals(files, expectedFiles)
        }
    }
}