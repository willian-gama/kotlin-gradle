package com.willian.gama.kgp.config

import com.willian.gama.kgp.rule.KTLINT_RULES
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask

fun Project.setUpKtLint() {
    // https://github.com/JLLeitschuh/ktlint-gradle?tab=readme-ov-file#applying-to-subprojects
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")

    // https://github.com/JLLeitschuh/ktlint-gradle?tab=readme-ov-file#configuration
    extensions.configure<KtlintExtension> {
        version.set("1.2.1")
        android.set(true)
        verbose.set(true)
        additionalEditorconfig.set(KTLINT_RULES)

        reporters {
            reporter(ReporterType.HTML)
            reporter(ReporterType.JSON) // it's required for Sonar
        }

        filter {
            include("**/*.kt", "**/*.kts")
            exclude("**/build/**")
        }
    }

    // https://github.com/JLLeitschuh/ktlint-gradle#setting-reports-output-directory
    tasks.withType<GenerateReportsTask> {
        reportsOutputDirectory.set(project.layout.buildDirectory.dir("reports/ktlint/$name"))
    }
}