package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_BASELINE_REPORT_PATH
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_EDITORCONFIG
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_EDITOR_CONFIG_COMMENT
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_EXCLUDE_PATTERN
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_FILE_PATTERNS
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_INCLUDE_PATTERN
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_PLUGIN_ID
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_REPORT_PATH
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_RULES
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_VERSION
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask
import java.io.File

fun Project.setUpKtLint(isIgnoreFailures: Boolean) {
    // https://github.com/JLLeitschuh/ktlint-gradle?tab=readme-ov-file#applying-to-subprojects
    pluginManager.apply(KTLINT_PLUGIN_ID)

    // https://github.com/JLLeitschuh/ktlint-gradle?tab=readme-ov-file#configuration
    extensions.configure<KtlintExtension> {
        version.set(KTLINT_VERSION)
        android.set(true)
        verbose.set(true)
        ignoreFailures.set(isIgnoreFailures)
        additionalEditorconfig.set(KTLINT_RULES)
        baseline.set(layout.projectDirectory.dir(KTLINT_BASELINE_REPORT_PATH).asFile)

        reporters(
            action = {
                reporter(reporterType = ReporterType.HTML)
                reporter(reporterType = ReporterType.JSON) // it's required for Sonar
            }
        )

        filter(
            filterAction = {
                include(KTLINT_INCLUDE_PATTERN)
                exclude(KTLINT_EXCLUDE_PATTERN)
            }
        )
    }

    // https://github.com/JLLeitschuh/ktlint-gradle#setting-reports-output-directory
    tasks.withType<GenerateReportsTask> {
        reportsOutputDirectory.set(layout.buildDirectory.dir("$KTLINT_REPORT_PATH/$name"))
    }
}

fun Project.generateKtLintEditorConfig() {
    File(projectDir.path, KTLINT_EDITORCONFIG).printWriter().use { writer ->
        writer.println(KTLINT_EDITOR_CONFIG_COMMENT.lines().joinToString("\n") { "# $it" })
        writer.println(KTLINT_FILE_PATTERNS)
        KTLINT_RULES.forEach { (key, value) ->
            writer.println("$key = $value")
        }
    }
}