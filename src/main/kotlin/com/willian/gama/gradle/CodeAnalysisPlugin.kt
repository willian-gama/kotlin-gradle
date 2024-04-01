package com.willian.gama.gradle

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask

class CodeAnalysisPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.subprojects {
            setUpKtLint()
            setUpDetekt()
            setUpUnitTest()
            setUpCodeCoverage()
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
            config.setFrom(file("${rootProject.rootDir}/config/detekt/detekt.yml"))

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

    private fun Project.setUpCodeCoverage() {
        pluginManager.apply("jacoco")

        extensions.configure<JacocoPluginExtension> {
            toolVersion = "0.8.10"
        }

        tasks.withType<Test>().configureEach {
            extensions.configure(JacocoTaskExtension::class) {
                isIncludeNoLocationClasses = true // Robolectric support
                excludes = listOf(
                    "jdk.internal.*",
                    "coil.compose.*"
                )
            }
        }

        tasks.register<JacocoReport>("generateCodeCoverage2") {
            group = "Jacoco code coverage report"
            sourceDirectories.from(file("${project.layout.projectDirectory}/src/main/java")) // main source set)
            classDirectories.from(
                fileTree(layout.buildDirectory.get()) { // build directory
                    exclude(
                        "**/BuildConfig.*",
                        "**/*\$*",
                        "**/Hilt_*.class",
                        "hilt_**",
                        "dagger/hilt/**",
                        "**/*JsonAdapter.*"
                    )
                }
            )
            executionData.from(
                fileTree(layout.buildDirectory.get()) {
                    setIncludes(
                        listOf(
                            "**/*.exec", // unit tests
                            "**/*.ec" // ui tests
                        )
                    )
                }
            )
        }
    }
}