package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.MavenConstants.MAVEN_GROUP_ID
import com.willian.gama.kgp.constants.MavenConstants.MAVEN_GROUP_VERSION
import com.willian.gama.kgp.model.MavenProperties
import org.gradle.api.plugins.ExtraPropertiesExtension

fun ExtraPropertiesExtension.toMavenProperties(
    moduleName: String
) = MavenProperties(
    mavenGroupId = get(MAVEN_GROUP_ID).toString(),
    mavenGroupVersion = get(MAVEN_GROUP_VERSION).toString(),
    mavenModule = moduleName
)