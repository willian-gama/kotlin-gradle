package com.willian.gama.kgp.plugin.util

import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_GENERATED_JAVA_REPORT_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_GENERATED_KOTLIN_REPORT_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_DEBUG_VARIANT
import com.willian.gama.kgp.plugin.test.TestData.TEST_FULL_DEBUG_ANDROID_TEST_DIRECTORY_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_FULL_DEBUG_GENERATED_JAVA_REPORT_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_FULL_DEBUG_GENERATED_KOTLIN_REPORT_PATH
import com.willian.gama.kgp.plugin.test.TestData.TEST_FULL_DEBUG_VARIANT
import com.willian.gama.kgp.util.ReportUtil.getAndroidTestReportPath
import com.willian.gama.kgp.util.ReportUtil.getGeneratedJavaReportPath
import com.willian.gama.kgp.util.ReportUtil.getGeneratedKotlinReportPath
import org.junit.Assert.assertEquals
import org.junit.Test

class ReportUtilTest {
    @Test
    fun `test android test report path parsing with debug build variant`() {
        val directoryPath = getAndroidTestReportPath(buildType = TEST_DEBUG_VARIANT)
        assertEquals(TEST_DEBUG_ANDROID_TEST_DIRECTORY_PATH, directoryPath)
    }

    @Test
    fun `test android test report path parsing with full debug build variant`() {
        val directoryPath = getAndroidTestReportPath(buildType = TEST_FULL_DEBUG_VARIANT)
        assertEquals(TEST_FULL_DEBUG_ANDROID_TEST_DIRECTORY_PATH, directoryPath)
    }

    @Test
    fun `test generated java report path with debug build variant`() {
        val directoryPath = getGeneratedJavaReportPath(buildType = TEST_DEBUG_VARIANT)
        assertEquals(TEST_DEBUG_GENERATED_JAVA_REPORT_PATH, directoryPath)
    }

    @Test
    fun `test generated java report path with full debug build variant`() {
        val directoryPath = getGeneratedJavaReportPath(buildType = TEST_FULL_DEBUG_VARIANT)
        assertEquals(TEST_FULL_DEBUG_GENERATED_JAVA_REPORT_PATH, directoryPath)
    }

    @Test
    fun `test generated kotlin report path with debug build variant`() {
        val directoryPath = getGeneratedKotlinReportPath(buildType = TEST_DEBUG_VARIANT)
        assertEquals(TEST_DEBUG_GENERATED_KOTLIN_REPORT_PATH, directoryPath)
    }

    @Test
    fun `test generated kotlin report path with full debug build variant`() {
        val directoryPath = getGeneratedKotlinReportPath(buildType = TEST_FULL_DEBUG_VARIANT)
        assertEquals(TEST_FULL_DEBUG_GENERATED_KOTLIN_REPORT_PATH, directoryPath)
    }
}