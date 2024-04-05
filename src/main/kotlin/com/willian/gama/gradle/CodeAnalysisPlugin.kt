package com.willian.gama.gradle

import com.willian.gama.constants.ExtensionConstants.CODE_ANALYSIS_TOOL
import com.willian.gama.extension.*
import com.willian.gama.model.CodeAnalysisParams
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
            setUpUnitTest()
            setUpJacoco()
        }
    }
}