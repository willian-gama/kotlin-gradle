package com.willian.gama.gradle.constants

object JacocoConstants {
    val KOTLIN_CLASSES by lazy {
        listOf(
            "**/BuildConfig.*",
            "**/*$*",
            "**/Hilt_*.class",
            "hilt_**",
            "dagger/hilt/**",
            "**/*JsonAdapter.*"
        )
    }
    val EXECUTION_DATA by lazy {
        listOf(
            "**/*.exec",
            "**/*.ec"
        )
    }
}