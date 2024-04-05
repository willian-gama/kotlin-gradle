package com.willian.gama.extension

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

fun Project.setUpJacoco() {
    pluginManager.apply("jacoco")

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.11"
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