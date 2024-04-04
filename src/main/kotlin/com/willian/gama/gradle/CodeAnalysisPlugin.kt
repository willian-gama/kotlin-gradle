package com.willian.gama.gradle

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask
import org.sonarqube.gradle.SonarExtension

class CodeAnalysisPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.setUpSonar()
        project.subprojects {
            setUpKtLint()
            setUpDetekt()
            setUpUnitTest()
        }
    }

    private fun Project.setUpDetekt() {
        pluginManager.apply("io.gitlab.arturbosch.detekt")

        tasks.withType<Detekt>().configureEach {
            include("**/*.kt")
            exclude("**/build/**")
            jvmTarget = JavaVersion.VERSION_17.toString()

            parallel = true
            allRules = true
            autoCorrect = true
            buildUponDefaultConfig = true
            setSource(files(projectDir))
            config.setFrom("/Users/android_dev_engineer/IdeaProjects/WillianGamaCI/src/main/resources/linting/detekt/detekt.yml")

            reports {
                txt.required.set(false)
                sarif.required.set(false)
                md.required.set(false)
                html.required.set(true)
                html.outputLocation.set(file("${project.layout.buildDirectory.get()}/reports/detekt/detekt.html"))
                xml.required.set(true) // It's required for Sonar
                xml.outputLocation.set(file("${project.layout.buildDirectory.get()}/reports/detekt/detekt.xml"))
            }
        }
    }

    private fun Project.setUpKtLint() {
        // https://github.com/JLLeitschuh/ktlint-gradle?tab=readme-ov-file#applying-to-subprojects
        pluginManager.apply("org.jlleitschuh.gradle.ktlint")

        // https://github.com/JLLeitschuh/ktlint-gradle?tab=readme-ov-file#configuration
        extensions.configure<KtlintExtension> {
            version.set("1.2.1")
            android.set(true)
            verbose.set(true)

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

    private fun Project.setUpUnitTest() {
        tasks.withType<Test>().configureEach {
            testLogging {
                exceptionFormat = TestExceptionFormat.FULL // Display the full log to identify Paparazzi test failures
                showStackTraces = false
            }
        }
    }

    private fun Project.setUpSonar() {
        pluginManager.apply("org.sonarqube")

        extensions.configure<SonarExtension> {
            description = "Sonar properties task"
            setAndroidVariant("debug")

            // Sonar properties: https://docs.sonarqube.org/latest/analysis/analysis-parameters/
            properties {
                property("sonar.host.url", "http://localhost:9000")
                property("sonar.login", rootProject.extra.get("sonar_login")!!)
                property("sonar.projectName", "KotlinComposeApp")
                property("sonar.projectKey", rootProject.extra.get("sonar_project_key")!!)
                property("sonar.projectVersion", rootProject.extra.get("sonar_project_version")!!)
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
}