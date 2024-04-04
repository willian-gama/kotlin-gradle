package com.willian.gama.gradle

import com.willian.gama.constants.ExtensionConstants.CODE_ANALYSIS_TOOL
import com.willian.gama.extension.CodeAnalysisExtension
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask
import org.sonarqube.gradle.SonarExtension

class CodeAnalysisPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val codeAnalysisExtension = project.extensions.create(
            CODE_ANALYSIS_TOOL,
            CodeAnalysisExtension::class.java
        )

        project.setUpSonar(codeAnalysisExtension = codeAnalysisExtension)
        project.subprojects {
            setUpKtLint()
            setUpDetekt()
            setUpUnitTest()
            setUpJacoco()
        }
    }

    private fun Project.setUpJacoco() {
        pluginManager.apply("jacoco")

        configure<JacocoPluginExtension> {
            toolVersion = "0.8.11"
        }

        tasks.withType(Test::class) {
            configure<JacocoTaskExtension> {
                isIncludeNoLocationClasses = true
                excludes = listOf(
                    "jdk.internal.*",
                    "coil.compose.*"
                )
            }
        }

        tasks.register("generateCodeCoverage", JacocoReport::class.java) {
            sourceDirectories.from(file(layout.projectDirectory.dir("src/main/java")))
            classDirectories.setFrom(
                files(
                    fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                        exclude(
                            listOf(
                                "**/BuildConfig.*",
                                "**/*$*",
                                "**/Hilt_*.class",
                                "hilt_**",
                                "dagger/hilt/**",
                                "**/*JsonAdapter.*"
                            )
                        )
                    }
                )
            )
            executionData.setFrom(files(
                fileTree(layout.buildDirectory) {
                    include(listOf("**/*.exec", "**/*.ec"))
                }
            ))

            // run unit tests and ui tests to generate code coverage report
            reports {
                html.required.set(true)
                html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco").get())
                xml.required.set(true) // It's required for Sonar
                xml.outputLocation.set(file(layout.buildDirectory.dir("reports/jacoco/jacoco.xml")))
            }
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

    private fun Project.setUpSonar(codeAnalysisExtension: CodeAnalysisExtension) {
        pluginManager.apply("org.sonarqube")

        extensions.configure<SonarExtension> {
            description = "Sonar properties task"
            setAndroidVariant("debug")

            // Sonar properties: https://docs.sonarqube.org/latest/analysis/analysis-parameters/
            properties {
                property("sonar.host.url", "http://localhost:9000")
                property("sonar.login", codeAnalysisExtension.sonarToken)
                property("sonar.projectName", "KotlinComposeApp")
                property("sonar.projectKey", codeAnalysisExtension.sonarProjectKey)
                property("sonar.projectVersion", codeAnalysisExtension.sonarProjectVersion)
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