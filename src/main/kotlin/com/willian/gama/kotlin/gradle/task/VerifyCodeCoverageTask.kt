package com.willian.gama.kotlin.gradle.task

import com.willian.gama.kotlin.gradle.constants.JacocoConstants.JACOCO_EXCLUSION
import com.willian.gama.kotlin.gradle.constants.JacocoConstants.JACOCO_EXECUTION_DATA
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

        violationRules {
            rule {
                limit {
                    minimum = BigDecimal(0.3) // 30%
                }
            }
        }
    }
}