package com.willian.gama.kgp.model

data class SonarProperties(
    val url: String,
    val token: String,
    val projectKey: String,
    val organizationKey: String,
    val projectName: String,
    val buildType: String,
    val kotlinVersion: String
)