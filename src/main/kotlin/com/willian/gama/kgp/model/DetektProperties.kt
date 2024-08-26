package com.willian.gama.kgp.model

import java.io.File

data class DetektProperties(
    val ignoreFailures: Boolean,
    val configFile: File
)