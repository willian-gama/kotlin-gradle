package com.willian.gama.kgp.config

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import java.io.File

fun Project.setUpDetekt(configFile: File) {
    pluginManager.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        ignoredBuildTypes = listOf("release")
        ignoredFlavors = listOf("snapshot")
    }

    tasks.withType<Detekt>().configureEach {
        include("**/*.kt", "**/*.kts")
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
            html.outputLocation.set(file(project.layout.buildDirectory.dir("reports/detekt/detekt.html").get()))
            xml.required.set(true) // It's required for Sonar
            xml.outputLocation.set(file(project.layout.buildDirectory.dir("reports/detekt/detekt.xml").get()))
        }
    }
}