package com.willian.gama.kgp.util

import java.io.File

object FileUtil {
    private val DETEKT_FILE_CONFIG = File.createTempFile("detekt_", ".yml")

    fun getFileFromResource(fileName: String): File {
        return requireNotNull(javaClass.classLoader.getResource(fileName)).openStream().use {
            DETEKT_FILE_CONFIG.apply {
                outputStream().use { fileOut ->
                    it.copyTo(fileOut)
                }
            }
        }
    }
}