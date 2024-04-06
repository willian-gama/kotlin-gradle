package com.willian.gama.gradle.plugin

import com.willian.gama.gradle.config.*
import com.willian.gama.gradle.constants.ExtensionConstants.CODE_ANALYSIS_PARAMS
import com.willian.gama.gradle.file.FileResource.getFileFromResource
import com.willian.gama.gradle.model.CodeAnalysisParams
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