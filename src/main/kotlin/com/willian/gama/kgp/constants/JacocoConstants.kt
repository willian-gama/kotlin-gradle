package com.willian.gama.kgp.constants

object JacocoConstants {
    const val JACOCO_PLUGIN_ID = "jacoco"
    const val JACOCO_TOOLS_VERSION = "0.8.12"
    const val JACOCO_GENERATE_CODE_COVERAGE = "generateCodeCoverage"
    const val JACOCO_HTML_REPORT_PATH = "reports/jacoco"
    const val JACOCO_XML_REPORT_PATH = "reports/jacoco/jacoco.xml"
    val JACOCO_EXCLUSION = listOf(
        "jdk.internal.*", // JDK 11 compatibility
        "android.*", // JDK 17 compatibility
        "coil.compose.*"
    )
    val JACOCO_INCLUDE_EXECUTION_DATA = listOf("**/*.exec", "**/*.ec")
    val JACOCO_GENERATED_EXCLUSION = listOf(
        "**/BuildConfig.*",
        "**/*$*",
        "**/Hilt_*.class",
        "hilt_**",
        "dagger/hilt/**",
        "**/*JsonAdapter.*",
        "androidx/**",
        "com/facebook/**",
        "io/realm/**"
    )
}