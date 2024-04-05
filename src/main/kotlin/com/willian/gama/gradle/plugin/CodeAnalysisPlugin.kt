package com.willian.gama.gradle.plugin

import com.willian.gama.gradle.constants.ExtensionConstants.CODE_ANALYSIS_TOOL
import com.willian.gama.gradle.extension.setUpDetekt
import com.willian.gama.gradle.extension.setUpJacoco
import com.willian.gama.gradle.extension.setUpKtLint
import com.willian.gama.gradle.extension.setUpSonar
import com.willian.gama.gradle.model.CodeAnalysisParams
import org.gradle.api.Plugin
import org.gradle.api.Project

class CodeAnalysisPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val codeAnalysisParams = project.extensions.create(
            CODE_ANALYSIS_TOOL,
            CodeAnalysisParams::class.java
        )

        project.setUpSonar(codeAnalysisParams = codeAnalysisParams)
        project.subprojects {
            setUpKtLint()
            setUpDetekt()
            setUpJacoco()
        }
    }
}