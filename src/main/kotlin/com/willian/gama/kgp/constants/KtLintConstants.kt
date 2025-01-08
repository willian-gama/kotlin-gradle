package com.willian.gama.kgp.constants

object KtLintConstants {
    const val KTLINT_PLUGIN_ID = "org.jlleitschuh.gradle.ktlint"
    const val KTLINT_VERSION = "1.5.0"
    const val KTLINT_BASELINE_REPORT_PATH = "baseline/ktlint/baseline.xml"
    const val KTLINT_REPORT_PATH = "reports/ktlint"
    private const val KTLINT_RULE_DISABLED = "disabled"
    const val KTLINT_EDITORCONFIG = ".editorconfig"
    const val KTLINT_FILE_PATTERNS = "[*.{kt,kts}]"
    val KTLINT_INCLUDE_PATTERN = listOf("**/*.kt", "**/*.kts")
    val KTLINT_EXCLUDE_PATTERN = listOf("**/build/**")
    const val KTLINT_EDITOR_CONFIG_COMMENT = "Code analysis plugin created $KTLINT_EDITORCONFIG with KtLint version $KTLINT_VERSION"
    val KTLINT_RULES = mapOf(
        "max_line_length" to "off",
        "insert_final_newline" to "false",
        "ktlint_standard_no-wildcard-imports" to KTLINT_RULE_DISABLED,
        "ktlint_standard_import-ordering" to KTLINT_RULE_DISABLED,
        "ktlint_standard_package-name" to KTLINT_RULE_DISABLED,
        "ktlint_standard_blank-line-before-declaration" to KTLINT_RULE_DISABLED,
        "ktlint_standard_multiline-expression-wrapping" to KTLINT_RULE_DISABLED,
        "ktlint_standard_function-signature" to KTLINT_RULE_DISABLED,
        "ktlint_standard_function-naming" to KTLINT_RULE_DISABLED,
        "ktlint_standard_blank-line-between-when-conditions" to KTLINT_RULE_DISABLED,
        "ktlint_standard_annotation" to KTLINT_RULE_DISABLED,
        "ktlint_standard_parameter-list-wrapping" to KTLINT_RULE_DISABLED,
        "ktlint_standard_trailing-comma-on-call-site" to KTLINT_RULE_DISABLED,
        "ktlint_standard_trailing-comma-on-declaration-site" to KTLINT_RULE_DISABLED,
        "ktlint_standard_function-expression-body" to KTLINT_RULE_DISABLED,
        "ktlint_standard_chain-method-continuation" to KTLINT_RULE_DISABLED,
        "ktlint_standard_class-signature" to KTLINT_RULE_DISABLED
    )
}