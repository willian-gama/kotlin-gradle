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
import java.io.File
import java.net.URL

class CodeAnalysisPlugin : Plugin<Project> {
    fun Any.getFileFromResource(fileName: String): File {
        val classLoader: ClassLoader = javaClass.classLoader
        val resource: URL? = classLoader.getResource(fileName)
        return if (resource == null) {
            throw IllegalArgumentException("file not found! $fileName")
        } else {
            val file: File = File.createTempFile("config", ".yml")
            val inputStream = resource.openStream()
            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        }
    }

    override fun apply(project: Project) {
        val codeAnalysisParams = project.extensions.create(
            CODE_ANALYSIS_PARAMS,
            CodeAnalysisParams::class.java
        )

        val file = getFileFromResource("linting/detekt/detekt.yml")

        project.setUpSonar(codeAnalysisParams = codeAnalysisParams)
        project.subprojects {
            setUpKtLint()
            setUpDetekt(file)
            setUpPaparazzi()
            afterEvaluate {
                setUpJacoco()
            }
        }
    }
}