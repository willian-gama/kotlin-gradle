package com.willian.gama.gradle.config

import com.android.build.api.dsl.TestOptions
import org.gradle.api.Project

fun Project.setUpAndroidTestReport(testOptions: TestOptions) {
    testOptions.apply {
        reportDir = "${layout.buildDirectory.get()}/reports/coverage/androidTest/debug/connected"
    }
}