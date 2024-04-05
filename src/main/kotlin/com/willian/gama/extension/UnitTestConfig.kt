package com.willian.gama.extension

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension

fun Project.setUpUnitTest() {
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
}