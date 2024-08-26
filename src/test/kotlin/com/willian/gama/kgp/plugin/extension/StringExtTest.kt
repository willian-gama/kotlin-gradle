package com.willian.gama.kgp.plugin.extension

import com.willian.gama.kgp.extension.getMajorMinorVersion
import com.willian.gama.kgp.extension.splitToDirectoryPath
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_VARIANT
import com.willian.gama.kgp.plugin.test.TestData.TEST_FULL_DEBUG_DIRECTORY_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_FULL_DEBUG_VARIANT
import com.willian.gama.kgp.plugin.test.TestData.TEST_MAJOR_VERSION
import com.willian.gama.kgp.plugin.test.TestData.TEST_MINOR_VERSION
import com.willian.gama.kgp.plugin.test.TestData.TEST_PATCH_VERSION
import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtTest {
    @Test
    fun `test get major and minor version with patch version`() {
        val version = "$TEST_MAJOR_VERSION.$TEST_MINOR_VERSION.$TEST_PATCH_VERSION"
        val majorMinorVersion = version.getMajorMinorVersion()
        assertEquals("$TEST_MAJOR_VERSION.$TEST_MINOR_VERSION", majorMinorVersion)
    }

    @Test
    fun `test get major and minor version without patch version`() {
        val version = "$TEST_MAJOR_VERSION.$TEST_MINOR_VERSION"
        val majorMinorVersion = version.getMajorMinorVersion()
        assertEquals("$TEST_MAJOR_VERSION.$TEST_MINOR_VERSION", majorMinorVersion)
    }

    @Test
    fun `test split debug build variant to directory path`() {
        val directoryPath = TEST_DEBUG_VARIANT.splitToDirectoryPath()
        assertEquals(TEST_DEBUG_VARIANT, directoryPath)
    }

    @Test
    fun `test split full debug build variant to directory path`() {
        val directoryPath = TEST_FULL_DEBUG_VARIANT.splitToDirectoryPath()
        assertEquals(TEST_FULL_DEBUG_DIRECTORY_PATH, directoryPath)
    }
}