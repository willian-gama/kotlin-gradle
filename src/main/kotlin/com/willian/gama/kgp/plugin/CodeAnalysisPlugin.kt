package com.willian.gama.kgp.plugin

import com.willian.gama.kgp.config.setUpDetekt
import com.willian.gama.kgp.config.setUpJacoco
import com.willian.gama.kgp.config.setUpKtLint
import com.willian.gama.kgp.config.setUpPaparazzi
import com.willian.gama.kgp.config.setUpSonar
import com.willian.gama.kgp.constants.ExtensionConstants.CODE_ANALYSIS_PARAMS
import com.willian.gama.kgp.file.FileResource.getFileFromResource
import com.willian.gama.kgp.model.CodeAnalysisParams
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