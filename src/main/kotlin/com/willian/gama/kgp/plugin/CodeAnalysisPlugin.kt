package com.willian.gama.kgp.plugin

import com.willian.gama.kgp.constants.CodeAnalysisConstants.CI_ENVIRONMENT
import com.willian.gama.kgp.constants.CodeAnalysisConstants.CODE_ANALYSIS
import com.willian.gama.kgp.constants.CodeAnalysisConstants.DETEKT_CONFIG_FILE_PATH
import com.willian.gama.kgp.constants.CodeAnalysisConstants.LOCAL_PROPERTIES
import com.willian.gama.kgp.extension.generateKtLintEditorConfig
import com.willian.gama.kgp.extension.setUpDetekt
import com.willian.gama.kgp.extension.setUpFrog
import com.willian.gama.kgp.extension.setUpJacoco
import com.willian.gama.kgp.extension.setUpKtLint
import com.willian.gama.kgp.extension.setUpSonar
import com.willian.gama.kgp.extension.toJfrogProperties
import com.willian.gama.kgp.extension.toSonarProperties
import com.willian.gama.kgp.model.CodeAnalysis
import com.willian.gama.kgp.model.DetektProperties
import com.willian.gama.kgp.util.FileUtil.copyFileFromResource
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties

class CodeAnalysisPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val codeAnalysis = project.extensions.create(
            CODE_ANALYSIS,
            CodeAnalysis::class.java
        )
        val properties = Properties().apply {
            load(project.file(LOCAL_PROPERTIES).inputStream())
        }
        val isCiEnvironment = project.providers
            .environmentVariable(CI_ENVIRONMENT)
            .getOrNull()?.toBooleanStrict() ?: false
        val detektProperties = DetektProperties(
            ignoreFailures = isCiEnvironment,
            configFile = copyFileFromResource(fileName = DETEKT_CONFIG_FILE_PATH)
        )

        project.afterEvaluate {
            generateKtLintEditorConfig()

            setUpSonar(
                properties = properties.toSonarProperties(
                    codeAnalysis = codeAnalysis,
                    isCiEnvironment = isCiEnvironment
                )
            )

            if (codeAnalysis.jfrogRepoKey.isNotBlank()) {
                setUpFrog(
                    properties = properties.toJfrogProperties(
                        codeAnalysis = codeAnalysis
                    )
                )
            }

            subprojects {
                setUpKtLint(isIgnoreFailures = isCiEnvironment)
                setUpDetekt(properties = detektProperties)
                setUpJacoco(buildType = codeAnalysis.buildType)
            }
        }
    }
}