package com.willian.gama.gradle.extension

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

fun Project.setUpDetekt() {
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
        config.setFrom("/Users/android_dev_engineer/IdeaProjects/WillianGamaCI/src/main/resources/linting/detekt/detekt.yml")

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