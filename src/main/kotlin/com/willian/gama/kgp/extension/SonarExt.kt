package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.DetektConstants.DETEKT_REPORT_PATH
import com.willian.gama.kgp.constants.KtLintConstants.KTLINT_REPORT_PATH
import com.willian.gama.kgp.constants.SonarConstants.SONAR_COVERAGE_JACOCO_XML_REPORT_PATHS_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_DESCRIPTION
import com.willian.gama.kgp.constants.SonarConstants.SONAR_HOST_URL_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_JACOCO_REPORT_PATH
import com.willian.gama.kgp.constants.SonarConstants.SONAR_KOTLIN_DETEKT_REPORT_PATHS_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_KOTLIN_KTLINT_REPORT_PATHS_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_KOTLIN_SOURCE_VERSION_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_KTLINT_FILE_PATH
import com.willian.gama.kgp.constants.SonarConstants.SONAR_ORGANIZATION_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_PLUGIN_ID
import com.willian.gama.kgp.constants.SonarConstants.SONAR_PROJECT_KEY_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_PROJECT_NAME_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_SOURCE_ENCODING_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_SOURCE_ENCODING_VALUE
import com.willian.gama.kgp.constants.SonarConstants.SONAR_TOKEN_KEY_PARAM
import com.willian.gama.kgp.constants.SonarConstants.SONAR_XML_FILE_PATH
import com.willian.gama.kgp.model.SonarProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarExtension

fun Project.setUpSonar(properties: SonarProperties) {
    // https://docs.sonarsource.com/sonarqube/latest/analyzing-source-code/scanners/sonarscanner-for-gradle/#analyzing
    pluginManager.apply(SONAR_PLUGIN_ID)

    extensions.configure<SonarExtension> {
        description = SONAR_DESCRIPTION
        // https://docs.sonarsource.com/sonarqube/latest/analyzing-source-code/scanners/sonarscanner-for-gradle/#additional-defaults-for-android-projects
        setAndroidVariant(properties.buildType)

        properties {
            properties(
                mapOf(
                    SONAR_HOST_URL_PARAM to properties.url,
                    SONAR_TOKEN_KEY_PARAM to properties.token,
                    SONAR_PROJECT_KEY_PARAM to properties.projectKey,
                    SONAR_ORGANIZATION_PARAM to properties.organizationKey,
                    SONAR_PROJECT_NAME_PARAM to properties.projectName,
                    SONAR_KOTLIN_SOURCE_VERSION_PARAM to properties.kotlinVersion.getMajorMinorVersion(),
                    SONAR_SOURCE_ENCODING_PARAM to SONAR_SOURCE_ENCODING_VALUE,
                    SONAR_KOTLIN_DETEKT_REPORT_PATHS_PARAM to getReportPaths(
                        directoryPath = DETEKT_REPORT_PATH,
                        filePattern = SONAR_XML_FILE_PATH
                    ),
                    SONAR_KOTLIN_KTLINT_REPORT_PATHS_PARAM to getReportPaths(
                        directoryPath = KTLINT_REPORT_PATH,
                        filePattern = SONAR_KTLINT_FILE_PATH
                    ),
                    SONAR_COVERAGE_JACOCO_XML_REPORT_PATHS_PARAM to getReportPaths(
                        directoryPath = SONAR_JACOCO_REPORT_PATH,
                        filePattern = SONAR_XML_FILE_PATH
                    )
                )
            )
        }
    }
}

fun Project.getReportPaths(
    directoryPath: String,
    filePattern: String
): String = subprojects
    .mapNotNull { fileTree(it.layout.buildDirectory.dir(directoryPath)) }
    .flatMap { it.matching { include(filePattern) }.files }
    .joinToString(separator = ",")