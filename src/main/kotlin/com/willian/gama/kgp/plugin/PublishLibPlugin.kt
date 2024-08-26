package com.willian.gama.kgp.plugin

import com.willian.gama.kgp.extension.setUpMavenPublish
import com.willian.gama.kgp.extension.toMavenProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

class PublishLibPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            setUpMavenPublish(
                properties = rootProject.extra.toMavenProperties(
                    moduleName = project.name
                )
            )
        }
    }
}
