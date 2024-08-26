package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_MAIN_SOURCE_SET
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_EXCLUSION
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_GENERATED_EXCLUSION
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_GENERATE_CODE_COVERAGE
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_HTML_REPORT_PATH
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_INCLUDE_EXECUTION_DATA
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_PLUGIN_ID
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_TOOLS_VERSION
import com.willian.gama.kgp.constants.JacocoConstants.JACOCO_XML_REPORT_PATH
import com.willian.gama.kgp.util.ReportUtil.getAndroidTestReportPath
import com.willian.gama.kgp.util.ReportUtil.getGeneratedJavaReportPath
import com.willian.gama.kgp.util.ReportUtil.getGeneratedKotlinReportPath
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

fun Project.setUpJacoco(buildType: String) {
    // https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:jacoco_getting_started
    pluginManager.apply(JACOCO_PLUGIN_ID)

    // https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:configuring_the_jacoco_plugin
    extensions.configure<JacocoPluginExtension> {
        toolVersion = JACOCO_TOOLS_VERSION
    }

    // https://docs.gradle.org/current/userguide/jacoco_plugin.html#default_values_of_the_jacoco_task_extension
    tasks.withType<Test> {
        extensions.configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = JACOCO_EXCLUSION
        }

        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            showStackTraces = false
        }
    }

    enableCodeCoverage(
        uiTestReportDir = layout.buildDirectory
            .dir(getAndroidTestReportPath(buildType = buildType))
            .get().asFile.absolutePath
    )
    generateCodeCoverageTask(buildType = buildType)
}

fun Project.generateCodeCoverageTask(buildType: String) {
    tasks.register(JACOCO_GENERATE_CODE_COVERAGE, JacocoReport::class.java) {
        sourceDirectories.from(layout.projectDirectory.dir(ANDROID_MAIN_SOURCE_SET).asFile)
        classDirectories.setFrom(
            files(
                fileTree(layout.buildDirectory.dir(getGeneratedJavaReportPath(buildType = buildType))) {
                    exclude(JACOCO_GENERATED_EXCLUSION)
                },
                fileTree(layout.buildDirectory.dir(getGeneratedKotlinReportPath(buildType = buildType))) {
                    exclude(JACOCO_GENERATED_EXCLUSION)
                }
            )
        )
        executionData.setFrom(
            fileTree(layout.buildDirectory) {
                include(JACOCO_INCLUDE_EXECUTION_DATA)
            }
        )

        reports {
            html.required.set(true)
            html.outputLocation.set(layout.buildDirectory.dir(JACOCO_HTML_REPORT_PATH).get())
            xml.required.set(true) // Required for Sonar
            xml.outputLocation.set(layout.buildDirectory.dir(JACOCO_XML_REPORT_PATH).get().asFile)
        }
    }
}