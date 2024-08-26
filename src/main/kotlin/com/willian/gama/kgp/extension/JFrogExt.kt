package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.MavenConstants.MAVEN_FULL_RELEASE
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_JFROG_PLUGIN_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_PUBLISH_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_SNAPSHOT
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_SNAPSHOT_RELEASE
import com.willian.gama.kgp.model.JFrogProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention

fun Project.setUpFrog(properties: JFrogProperties) {
    pluginManager.apply(MAVEN_JFROG_PLUGIN_ID)
    pluginManager.apply(MAVEN_PUBLISH_ID)

    configure<ArtifactoryPluginConvention> {
        setContextUrl(properties.repoUrl)
        publish {
            repository {
                repoKey = properties.repoKey
                username = properties.username
                password = properties.password
            }

            defaults {
                isPublishBuildInfo = false
                isSkip = true
                setPublishArtifacts(true)
                setPublishPom(true)
                publications(if (project.hasProperty(MAVEN_SNAPSHOT)) MAVEN_SNAPSHOT_RELEASE else MAVEN_FULL_RELEASE)
            }
        }
    }
}