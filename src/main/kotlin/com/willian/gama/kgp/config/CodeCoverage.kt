package com.willian.gama.kgp.config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

fun Project.setUpCodeCoverage() {
    project.extensions.getByType(BaseExtension::class.java).run {
        setUpAndroidTestReport(testOptions)
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