package com.willian.gama.gradle.extension

import com.willian.gama.gradle.model.CodeAnalysisParams
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarExtension

fun Project.setUpSonar(codeAnalysisParams: CodeAnalysisParams) {
    pluginManager.apply("org.sonarqube")

    extensions.configure<SonarExtension> {
        description = "Sonar properties task"
        setAndroidVariant("debug")

        // Sonar properties: https://docs.sonarqube.org/latest/analysis/analysis-parameters/
        properties {
            property("sonar.host.url", "http://localhost:9000")
            property("sonar.login", codeAnalysisParams.sonarToken)
            property("sonar.projectName", "KotlinComposeApp")
            property("sonar.projectKey", codeAnalysisParams.sonarProjectKey)
            property("sonar.projectVersion", codeAnalysisParams.sonarProjectVersion)
            property("sonar.sourceEncoding", "UTF-8")
//                property("sonar.coverage.jacoco.xmlReportPaths", "**/build/reports/jacoco/jacoco.xml")
//                property("sonar.kotlin.detekt.reportPaths", "build/reports/detekt/detekt.xml")
            property(
                "sonar.kotlin.ktlint.reportPaths",
                subprojects
                    .mapNotNull { fileTree(it.layout.buildDirectory.dir("reports/ktlint")) }
                    .flatMap { it.matching { include("**/*.json") }.files }
                    .joinToString(separator = ",")
            )
        }
    }
}