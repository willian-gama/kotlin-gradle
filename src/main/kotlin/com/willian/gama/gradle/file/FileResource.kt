package com.willian.gama.gradle.file

import java.io.File

object FileResource {
    private val DETEKT_FILE_CONFIG by lazy { File.createTempFile("config", ".yml") }

    fun getFileFromResource(fileName: String): File {
        return requireNotNull(javaClass.classLoader.getResource(fileName)).openStream().use {
            DETEKT_FILE_CONFIG.outputStream().use { output ->
                it.copyTo(output)
            }
            DETEKT_FILE_CONFIG
        }
    }
}