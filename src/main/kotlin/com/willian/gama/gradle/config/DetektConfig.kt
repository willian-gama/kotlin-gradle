package com.willian.gama.gradle.config

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import java.io.File

fun Project.setUpDetekt(configFile: File) {
    pluginManager.apply("io.gitlab.arturbosch.detekt")

    tasks.withType<Detekt>().configureEach {
        include("**/*.kt")
        exclude("**/build/**")
        jvmTarget = JavaVersion.VERSION_17.toString()

        parallel = true
        allRules = true
        autoCorrect = true
        buildUponDefaultConfig = true
        setSource(files(projectDir))
        config.setFrom(configFile)

        reports {
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
            html.required.set(true)
            html.outputLocation.set(file("${project.layout.buildDirectory.get()}/reports/detekt/detekt.html"))
            xml.required.set(true) // It's required for Sonar
            xml.outputLocation.set(file("${project.layout.buildDirectory.get()}/reports/detekt/detekt.xml"))
        }
    }
}