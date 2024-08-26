package com.willian.gama.kgp.extension

private const val MINOR_AND_PATCH = 2
private val SPLIT_FROM_UPPER_CASE_REGEX = "(?=\\p{Upper})".toRegex()

fun String.getMajorMinorVersion(): String {
    return if (count { it == '.' } == MINOR_AND_PATCH) {
        substringBeforeLast(delimiter = ".")
    } else {
        this
    }
}

fun String.splitToDirectoryPath(): String {
    return split(SPLIT_FROM_UPPER_CASE_REGEX)
        .joinToString(separator = "/") { it.lowercase() }
}