package com.willian.gama.kgp.extension

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_APPLICATION
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_BASELINE_REPORT_PATH
import com.willian.gama.kgp.constants.AndroidConstants.ANDROID_LIBRARY
import org.gradle.api.Project

@Suppress("UnstableApiUsage")
fun Project.enableCodeCoverage(uiTestReportDir: String) {
    listOf(ANDROID_APPLICATION, ANDROID_LIBRARY).forEach { pluginId ->
        plugins.withId(pluginId) {
            fun <E : BuildFeatures, T : BuildType> CommonExtension<E, T, *, *, *, *>.toCommonExtension(uiTestReportDir: String) {
                // https://developer.android.com/reference/tools/gradle-api/7.4/com/android/build/api/dsl/Lint
                lint {
                    abortOnError = false
                    xmlReport = true // Required for sonar
                    baseline = layout.projectDirectory.dir(ANDROID_BASELINE_REPORT_PATH).asFile
                }
                testOptions {
                    reportDir = uiTestReportDir
                }
                buildTypes {
                    debug {
                        enableAndroidTestCoverage = true
                        enableUnitTestCoverage = true
                    }
                }
            }

            extensions.getByType(BaseExtension::class.java).let { extension ->
                when (extension) {
                    is ApplicationExtension -> extension.toCommonExtension(uiTestReportDir = uiTestReportDir)
                    is LibraryExtension -> extension.toCommonExtension(uiTestReportDir = uiTestReportDir)
                }
            }
        }
    }
}