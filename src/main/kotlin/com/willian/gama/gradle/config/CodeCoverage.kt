package com.willian.gama.gradle.config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.setUpCodeCoverage() {
    configure<BaseExtension> {
        setUpAndroidTestReport(testOptions = testOptions)
        when (this) {
            is ApplicationExtension -> buildTypes {
                debug {
                    enableAndroidTestCoverage = true
                    enableUnitTestCoverage = true
                }
            }
            is LibraryExtension -> buildTypes {
                debug {
                    enableAndroidTestCoverage = true
                    enableUnitTestCoverage = true
                }
            }
        }
    }
}