package com.willian.gama.kgp.plugin.test

import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_REPORT_PATH
import com.willian.gama.kgp.constants.SonarConstants
import com.willian.gama.kgp.model.CodeAnalysis
import com.willian.gama.kgp.model.DetektProperties
import com.willian.gama.kgp.model.JFrogProperties
import com.willian.gama.kgp.model.KtLintProperties
import com.willian.gama.kgp.model.MavenProperties
import com.willian.gama.kgp.model.SonarProperties
import java.io.File

object TestData {
    const val TEST_MAVEN_LIB_GROUP = "android-lib"
    const val TEST_MAVEN_LIB_VERSION = "0.0.1"
    const val TEST_MAVEN_LIB_ARTIFACT_ID = "core"
    const val TEST_MAJOR_VERSION = "123"
    const val TEST_MINOR_VERSION = "456"
    const val TEST_PATCH_VERSION = "789"
    const val TEST_DEBUG_VARIANT = "debug"
    const val TEST_FULL_DEBUG_VARIANT = "fullDebug"
    const val TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH = "reports/coverage/androidTest/$TEST_DEBUG_VARIANT/connected"
    const val TEST_FULL_DEBUG_DIRECTORY_PATH = "full/debug"
    const val TEST_FULL_DEBUG_ANDROID_TEST_DIRECTORY_PATH = "reports/coverage/androidTest/$TEST_FULL_DEBUG_DIRECTORY_PATH/connected"
    const val TEST_DEBUG_GENERATED_JAVA_REPORT_PATH = "intermediates/javac/$TEST_DEBUG_VARIANT/classes"
    const val TEST_FULL_DEBUG_GENERATED_JAVA_REPORT_PATH = "intermediates/javac/$TEST_FULL_DEBUG_VARIANT/classes"
    const val TEST_DEBUG_GENERATED_KOTLIN_REPORT_PATH = "tmp/kotlin-classes/$TEST_DEBUG_VARIANT"
    const val TEST_FULL_DEBUG_GENERATED_KOTLIN_REPORT_PATH = "tmp/kotlin-classes/$TEST_FULL_DEBUG_VARIANT"
    const val TEST_SONAR_TOKEN = "token"
    const val TEST_SONAR_PROJECT_KEY = "project"
    const val TEST_SONAR_ORGANIZATION = "organization"
    const val TEST_SONAR_PROJECT_NAME = "project name"
    const val TEST_KTLINT_CHECK_REPORT = "build/$KTLINT_REPORT_PATH/ktlintKotlinScriptCheck"
    const val TEST_KTLINT_FORMAT_REPORT = "build/$KTLINT_REPORT_PATH/ktlintKotlinScriptFormat"
    val TEST_DETEKT_CONFIG_FILE = File("detekt.yml")
    const val TEST_MAVEN_JFROG_REPO_KEY = "android-lib"
    const val TEST_MAVEN_REPO_URL = "https://test.jfrog.io/artifactory"
    const val TEST_MAVEN_REPO_USERNAME = "test@repo_usermane.com"
    const val TEST_MAVEN_REPO_ACCESS_TOKEN = "DY3ODM6N1NCZjZDazFHZUNKenVpbUxRdlNDQU1QN0RE"
    const val TEST_JAVA_PLUGIN = "java"
    const val TEST_ANDROID_COMPILE_SDK = 34
    const val TEST_NAMESPACE = "com.library.test"

    fun createCodeAnalysis(
        buildType: String = TEST_DEBUG_VARIANT,
        kotlinVersion: String = TEST_MAJOR_VERSION,
        jfrogRepoKey: String = TEST_MAVEN_JFROG_REPO_KEY
    ): CodeAnalysis = CodeAnalysis().also {
        it.buildType = buildType
        it.kotlinVersion = kotlinVersion
        it.jfrogRepoKey = jfrogRepoKey
    }

    fun createSonarProperties(
        token: String = TEST_SONAR_TOKEN,
        projectKey: String = TEST_SONAR_PROJECT_KEY,
        organizationKey: String = TEST_SONAR_ORGANIZATION,
        projectName: String = TEST_SONAR_PROJECT_NAME,
        buildType: String = TEST_DEBUG_VARIANT,
        kotlinVersion: String = TEST_MAJOR_VERSION,
        exclusions: String = SonarConstants.SONAR_EXCLUSIONS_VALUE,
    ) = SonarProperties(
        token = token,
        projectKey = projectKey,
        organizationKey = organizationKey,
        projectName = projectName,
        buildType = buildType,
        kotlinVersion = kotlinVersion,
        exclusions = exclusions
    )

    fun createDetektProperties(
        ignoreFailures: Boolean = false,
        file: File
    ) = DetektProperties(
        ignoreFailures = ignoreFailures,
        configFile = file
    )

    fun createMavenProperties(
        mavenGroupId: String = TEST_MAVEN_LIB_GROUP,
        mavenGroupVersion: String = TEST_MAVEN_LIB_VERSION,
        mavenArtifactId: String = TEST_MAVEN_LIB_ARTIFACT_ID
    ) = MavenProperties(
        mavenGroupId = mavenGroupId,
        mavenGroupVersion = mavenGroupVersion,
        mavenModule = mavenArtifactId
    )

    fun createJfrogProperties(
        repoUrl: String = TEST_MAVEN_REPO_URL,
        repoKey: String = TEST_MAVEN_JFROG_REPO_KEY,
        username: String = TEST_MAVEN_REPO_USERNAME,
        password: String = TEST_MAVEN_REPO_ACCESS_TOKEN
    ) = JFrogProperties(
        repoUrl = repoUrl,
        repoKey = repoKey,
        username = username,
        password = password
    )
}