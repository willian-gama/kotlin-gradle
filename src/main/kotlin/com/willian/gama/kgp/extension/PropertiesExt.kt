package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.SonarConstants.SONAR_EXCLUSIONS_VALUE
import com.willian.gama.kgp.constants.SonarConstants.getMissingPropertyErrorMessage
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_ACCESS_TOKEN
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_URL
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_MAVEN_REPO_USERNAME
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_ORGANIZATION_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_PROJECT_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_PROJECT_NAME_KEY
import com.willian.gama.kgp.constants.UserPropertiesConstants.USER_PROPERTY_SONAR_TOKEN_KEY
import com.willian.gama.kgp.model.CodeAnalysis
import com.willian.gama.kgp.model.JFrogProperties
import com.willian.gama.kgp.model.SonarProperties
import java.util.Properties

fun Properties.toSonarProperties(codeAnalysis: CodeAnalysis) = SonarProperties(
    token = getPropertySafely(USER_PROPERTY_SONAR_TOKEN_KEY),
    projectKey = getPropertySafely(USER_PROPERTY_SONAR_PROJECT_KEY),
    organizationKey = getPropertySafely(USER_PROPERTY_SONAR_ORGANIZATION_KEY),
    projectName = getPropertySafely(USER_PROPERTY_SONAR_PROJECT_NAME_KEY),
    buildType = codeAnalysis.buildType,
    kotlinVersion = codeAnalysis.kotlinVersion,
    exclusions = SONAR_EXCLUSIONS_VALUE
)

fun Properties.toJfrogProperties(codeAnalysis: CodeAnalysis) = JFrogProperties(
    repoUrl = getPropertySafely(USER_MAVEN_REPO_URL),
    repoKey = codeAnalysis.jfrogRepoKey,
    username = getPropertySafely(USER_MAVEN_REPO_USERNAME),
    password = getPropertySafely(USER_MAVEN_REPO_ACCESS_TOKEN)
)

fun Properties.getPropertySafely(param: String): String {
    return getProperty(param) ?: error(getMissingPropertyErrorMessage(param = param))
}