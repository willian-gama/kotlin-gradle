package com.willian.gama.kotlin.gradle.task

import com.willian.gama.kotlin.gradle.constants.JacocoConstants.JACOCO_EXCLUSION
import com.willian.gama.kotlin.gradle.constants.JacocoConstants.JACOCO_EXECUTION_DATA
import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReport

fun Project.generateCodeCoverageTask() {
    tasks.register("generateCodeCoverage", JacocoReport::class.java) {
        sourceDirectories.from(file(layout.projectDirectory.dir("src/main/java")))
        classDirectories.setFrom(
            files(
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(JACOCO_EXCLUSION)
                }
            )
        )
        executionData.setFrom(
            files(
                fileTree(layout.buildDirectory) {
                    include(JACOCO_EXECUTION_DATA)
                }
            )
        )

        // run unit tests and ui tests to generate code coverage report
        reports {
            html.required.set(true)
            html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco").get())
            xml.required.set(true) // It's required for Sonar
            xml.outputLocation.set(file(layout.buildDirectory.dir("reports/jacoco/jacoco.xml")))
        }
    }
}