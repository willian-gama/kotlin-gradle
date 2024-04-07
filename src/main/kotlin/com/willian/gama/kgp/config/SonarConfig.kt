package com.willian.gama.kgp.config

import com.willian.gama.kgp.model.CodeAnalysisParams
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarExtension

fun Project.setUpSonar(codeAnalysisParams: CodeAnalysisParams) {
    pluginManager.apply("org.sonarqube")

    extensions.configure<SonarExtension> {
        setAndroidVariant("debug")

        // Sonar properties: https://docs.sonarqube.org/latest/analysis/analysis-parameters/
        properties {
            properties(
                mapOf(
                    "sonar.host.url" to "http://localhost:9000",
                    "sonar.login" to codeAnalysisParams.sonarToken,
                    "sonar.projectName" to codeAnalysisParams.sonarProjectName,
                    "sonar.projectKey" to codeAnalysisParams.sonarProjectKey,
                    "sonar.projectVersion" to codeAnalysisParams.sonarProjectVersion,
                    "sonar.sourceEncoding" to "UTF-8",
                    "sonar.kotlin.ktlint.reportPaths" to getKtLintReports(),
                    "sonar.kotlin.detekt.reportPaths" to "build/reports/detekt/detekt.xml",
                    "sonar.coverage.jacoco.xmlReportPaths" to "**/build/reports/jacoco/jacoco.xml"
                )
            )
        }
    }
}

fun Project.getKtLintReports(): String {
    return subprojects
        .mapNotNull { fileTree(it.layout.buildDirectory.dir("reports/ktlint")) }
        .flatMap { it.matching { include("**/*.json") }.files }
        .joinToString(separator = ",")
}