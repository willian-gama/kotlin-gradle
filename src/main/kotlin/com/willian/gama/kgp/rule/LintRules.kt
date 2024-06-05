package com.willian.gama.kgp.rule

val KTLINT_RULES by lazy {
    mapOf(
        "max_line_length" to "off",
        "insert_final_newline" to "false",
        "ktlint_standard_no-wildcard-imports" to "disabled",
        "ktlint_standard_import-ordering" to "disabled",
        "ktlint_standard_package-name" to "disabled",
        "ktlint_standard_blank-line-before-declaration" to "disabled",
        "ktlint_standard_multiline-expression-wrapping" to "disabled",
        "ktlint_standard_function-signature" to "disabled",
        "ktlint_standard_function-naming" to "disabled",
        "ktlint_standard_blank-line-between-when-conditions" to "disabled",
        "ktlint_standard_annotation" to "disabled",
        "ktlint_standard_parameter-list-wrapping" to "disabled",
        "ktlint_standard_trailing-comma-on-call-site" to "disabled",
        "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
        "ktlint_standard_function-expression-body" to "disabled",
        "ktlint_standard_chain-method-continuation" to "disabled",
        "ktlint_standard_class-signature" to "disabled"
    )
}