package com.willian.gama.gradle.config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import com.willian.gama.gradle.task.generateCodeCoverageTask
import com.willian.gama.gradle.task.verifyCodeCoverageTask
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension

fun Project.setUpJacoco() {
    pluginManager.apply("jacoco")

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.11"
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf(
                "jdk.internal.*",
                "coil.compose.*"
            )
        }

        testLogging {
            exceptionFormat = TestExceptionFormat.FULL // Display the full log to identify Paparazzi test failures
            showStackTraces = false
        }
    }

    val pluginId = if (pluginManager.hasPlugin("com.android.application")) {
        "com.android.application"
    } else if (pluginManager.hasPlugin("com.android.library")) {
        "com.android.library"
    } else {
        ""
    }

    plugins.withId(pluginId) {
        configure<BaseExtension> {
            when (this) {
                is ApplicationExtension -> {
                    setUpAndroidTestReport(testOptions = testOptions)
                    buildTypes {
                        debug {
                            enableAndroidTestCoverage = true
                            enableUnitTestCoverage = true
                        }
                    }
                }
                is LibraryExtension -> {
                    setUpAndroidTestReport(testOptions = testOptions)
                    buildTypes {
                        debug {
                            enableAndroidTestCoverage = true
                            enableUnitTestCoverage = true
                        }
                    }
                }
            }
        }
    }

    generateCodeCoverageTask()
    verifyCodeCoverageTask()
}