package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.DetektConstants.DETEKT_BASELINE_REPORT_PATH
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_EXCLUDE_PATTERN
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_HTML_REPORT_PATH
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_IGNORED_BUILD_TYPES
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_IGNORED_FLAVORS
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_INCLUDE_PATTERN
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_PLUGIN_IN
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_XML_REPORT_PATH
import com.willian.gama.kgp.model.DetektProperties
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

fun Project.setUpDetekt(properties: DetektProperties) {
    val baselineFile = layout.projectDirectory.dir(DETEKT_BASELINE_REPORT_PATH).asFile

    // https://detekt.dev/docs/gettingstarted/gradle/#groovy-dsl-1
    pluginManager.apply(DETEKT_PLUGIN_IN)

    // https://detekt.dev/docs/gettingstarted/gradle/#kotlin-dsl-5
    tasks.withType<Detekt> {
        jvmTarget = JavaVersion.VERSION_17.toString()
        parallel = true
        allRules = true
        autoCorrect = true
        buildUponDefaultConfig = true
        ignoreFailures = properties.ignoreFailures
        config.setFrom(properties.configFile)
        baselineFile
            .takeIf { it.exists() }
            ?.let { file -> baseline.set(file) }

        setSource(files(projectDir))
        setIncludes(DETEKT_INCLUDE_PATTERN)
        setExcludes(DETEKT_EXCLUDE_PATTERN)

        reports(
            configure = {
                txt.required.set(false)
                sarif.required.set(false)
                md.required.set(false)
                html.required.set(true)
                html.outputLocation.set(layout.buildDirectory.dir(DETEKT_HTML_REPORT_PATH).get().asFile)
                xml.required.set(true) // it's required for Sonar
                xml.outputLocation.set(layout.buildDirectory.dir(DETEKT_XML_REPORT_PATH).get().asFile)
            }
        )
    }

    // https://detekt.dev/docs/gettingstarted/gradle/#kotlin-dsl-4
    tasks.withType<DetektCreateBaselineTask> {
        jvmTarget = JavaVersion.VERSION_17.toString()
        parallel.set(true)
        allRules.set(true)
        buildUponDefaultConfig.set(true)
        config.setFrom(properties.configFile)
        baseline.set(baselineFile)

        setSource(files(projectDir))
        setIncludes(DETEKT_INCLUDE_PATTERN)
        setExcludes(DETEKT_EXCLUDE_PATTERN)
    }

    // https://detekt.dev/docs/gettingstarted/gradle/#kotlin-dsl-3
    extensions.configure<DetektExtension> {
        ignoredBuildTypes = DETEKT_IGNORED_BUILD_TYPES
        ignoredFlavors = DETEKT_IGNORED_FLAVORS
    }
}