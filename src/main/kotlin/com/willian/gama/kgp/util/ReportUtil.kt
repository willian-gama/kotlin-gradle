package com.willian.gama.kgp.util

import com.willian.gama.kgp.extension.splitToDirectoryPath

object ReportUtil {
    fun getAndroidTestReportPath(buildType: String): String {
        return "reports/coverage/androidTest/${buildType.splitToDirectoryPath()}/connected" // Required for sonar
    }

    fun getGeneratedJavaReportPath(buildType: String): String = "intermediates/javac/$buildType/classes"

    fun getGeneratedKotlinReportPath(buildType: String): String = "tmp/kotlin-classes/$buildType"
}