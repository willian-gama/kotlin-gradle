package com.willian.gama.kgp.model

data class SonarProperties(
    val token: String,
    val projectKey: String,
    val organizationKey: String,
    val projectName: String,
    val projectVersion: String,
    val buildType: String,
    val kotlinVersion: String,
    val exclusions: String
)