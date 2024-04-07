package com.willian.gama.kotlin.gradle.config

import com.willian.gama.kotlin.gradle.task.generateCodeCoverageTask
import com.willian.gama.kotlin.gradle.task.verifyCodeCoverageTask
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
        toolVersion = "0.8.12"
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

    plugins.withId("com.android.library") {
        setUpCodeCoverage()
    }
    plugins.withId("com.android.application") {
        setUpCodeCoverage()
    }

    generateCodeCoverageTask()
    verifyCodeCoverageTask()
}