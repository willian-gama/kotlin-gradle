package com.willian.gama.kgp.constants

object JacocoConstants {
    val JACOCO_EXCLUSION by lazy {
        listOf(
            "**/BuildConfig.*",
            "**/*$*",
            "**/Hilt_*.class",
            "hilt_**",
            "dagger/hilt/**",
            "**/*JsonAdapter.*"
        )
    }
    val JACOCO_EXECUTION_DATA by lazy {
        listOf(
            "**/*.exec",
            "**/*.ec"
        )
    }
}