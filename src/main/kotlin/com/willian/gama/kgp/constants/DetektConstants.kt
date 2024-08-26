package com.willian.gama.kgp.constants

object DetektConstants {
    const val DETEKT_PLUGIN_IN = "io.gitlab.arturbosch.detekt"
    const val DETEKT_BASELINE_REPORT_PATH = "baseline/detekt/baseline.xml"
    val DETEKT_INCLUDE_PATTERN = listOf("**/*.kt", "**/*.kts")
    val DETEKT_EXCLUDE_PATTERN = listOf("**/build/**")
    const val DETEKT_REPORT_PATH = "reports/detekt"
    const val DETEKT_HTML_REPORT_PATH = "$DETEKT_REPORT_PATH/detekt.html"
    const val DETEKT_XML_REPORT_PATH = "$DETEKT_REPORT_PATH/detekt.xml"
    val DETEKT_IGNORED_BUILD_TYPES = listOf("release")
    val DETEKT_IGNORED_FLAVORS = listOf("snapshot")
}