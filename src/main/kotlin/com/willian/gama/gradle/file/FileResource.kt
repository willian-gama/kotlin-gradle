package com.willian.gama.gradle.file

import java.io.File
import java.io.InputStream

private val DETEKT_CONFIG_FILE by lazy {
    File("config", ".yml")
}

object FileResource {
    fun getFileFromResource(fileName: String): File {
        return requireNotNull(javaClass.classLoader.getResource(fileName)).openStream().use {
            val file: File = File.createTempFile("config", ".yml")
            file.outputStream().use { output ->
                it.copyTo(output)
            }
            file
        }
    }

    fun getDetektConfigFile(inputStream: InputStream?): File {
        if (DETEKT_CONFIG_FILE.length() == 0L) {
            requireNotNull(inputStream).use {
                DETEKT_CONFIG_FILE.outputStream().use { output ->
                    it.copyTo(output)
                }
            }
        }
        println(DETEKT_CONFIG_FILE.name)
        return DETEKT_CONFIG_FILE
    }

//    private val DETEKT_CONFIG_FILE by lazy {
//        File("detekt.yml").run {
//            CodeAnalysisPlugin::class.java.javaClass.classLoader.getResourceAsStream("linting/detekt/detekt.yml")!!
//                .use {
//                    outputStream().use { output ->
//                        it.copyTo(output)
//                    }
//                }
//        }
//    }
}