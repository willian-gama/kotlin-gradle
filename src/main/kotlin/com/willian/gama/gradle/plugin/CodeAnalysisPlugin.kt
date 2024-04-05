package com.willian.gama.gradle.plugin

import com.willian.gama.gradle.config.setUpDetekt
import com.willian.gama.gradle.config.setUpJacoco
import com.willian.gama.gradle.config.setUpKtLint
import com.willian.gama.gradle.config.setUpPaparazzi
import com.willian.gama.gradle.config.setUpSonar
import com.willian.gama.gradle.constants.ExtensionConstants.CODE_ANALYSIS_PARAMS
import com.willian.gama.gradle.model.CodeAnalysisParams
import org.gradle.api.Plugin
import org.gradle.api.Project

class CodeAnalysisPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val codeAnalysisParams = project.extensions.create(
            CODE_ANALYSIS_PARAMS,
            CodeAnalysisParams::class.java
        )

        project.setUpSonar(codeAnalysisParams = codeAnalysisParams)
        project.subprojects {
            setUpKtLint()
            setUpDetekt()
            setUpPaparazzi()
            afterEvaluate {
                setUpJacoco()
            }
        }
    }
}