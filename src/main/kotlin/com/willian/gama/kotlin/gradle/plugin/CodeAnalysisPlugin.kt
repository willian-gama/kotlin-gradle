package com.willian.gama.kotlin.gradle.plugin

import com.willian.gama.kotlin.gradle.config.setUpDetekt
import com.willian.gama.kotlin.gradle.config.setUpJacoco
import com.willian.gama.kotlin.gradle.config.setUpKtLint
import com.willian.gama.kotlin.gradle.config.setUpPaparazzi
import com.willian.gama.kotlin.gradle.config.setUpSonar
import com.willian.gama.kotlin.gradle.constants.ExtensionConstants.CODE_ANALYSIS_PARAMS
import com.willian.gama.kotlin.gradle.file.FileResource.getFileFromResource
import com.willian.gama.kotlin.gradle.model.CodeAnalysisParams
import org.gradle.api.Plugin
import org.gradle.api.Project

class CodeAnalysisPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val codeAnalysisParams = project.extensions.create(
            CODE_ANALYSIS_PARAMS,
            CodeAnalysisParams::class.java
        )

        val detektConfigFile = getFileFromResource(fileName = "linting/detekt/detekt.yml")
        project.setUpSonar(codeAnalysisParams = codeAnalysisParams)
        project.subprojects {
            setUpKtLint()
            setUpDetekt(detektConfigFile)
            setUpPaparazzi()
            afterEvaluate {
                setUpJacoco()
            }
        }
    }
}