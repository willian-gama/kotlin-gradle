package com.willian.gama.gradle.task

import com.willian.gama.gradle.constants.JacocoConstants
import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import java.math.BigDecimal

fun Project.verifyCodeCoverageTask() {
    // Jacoco - Verify violations: https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:jacoco_report_violation_rules
    tasks.register("verifyCodeCoverage", JacocoCoverageVerification::class.java) {
        sourceDirectories.from(file(layout.projectDirectory.dir("src/main/java")))
        classDirectories.setFrom(
            files(
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(JacocoConstants.KOTLIN_CLASSES)
                }
            )
        )
        executionData.setFrom(files(
            fileTree(layout.buildDirectory) {
                include(JacocoConstants.EXECUTION_DATA)
            }
        ))

        violationRules {
            rule {
                limit {
                    minimum = BigDecimal(0.3) // 30%
                }
            }
        }
    }
}