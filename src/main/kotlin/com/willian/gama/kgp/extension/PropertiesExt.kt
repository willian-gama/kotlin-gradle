package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.SonarConstants.SONAR_LOCALHOST_URL_VALUE
import com.willian.gama.kgp.constants.SonarConstants.SONAR_REMOTE_URL_VALUE
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

fun Properties.toSonarProperties(
    codeAnalysis: CodeAnalysis,
    isCiEnvironment: Boolean
) = SonarProperties(
    url = if (isCiEnvironment) SONAR_REMOTE_URL_VALUE else SONAR_LOCALHOST_URL_VALUE,
    token = getPropertySafely(USER_PROPERTY_SONAR_TOKEN_KEY),
    projectKey = getPropertySafely(USER_PROPERTY_SONAR_PROJECT_KEY),
    organizationKey = getPropertySafely(USER_PROPERTY_SONAR_ORGANIZATION_KEY),
    projectName = getPropertySafely(USER_PROPERTY_SONAR_PROJECT_NAME_KEY),
    buildType = codeAnalysis.buildType,
    kotlinVersion = codeAnalysis.kotlinVersion
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