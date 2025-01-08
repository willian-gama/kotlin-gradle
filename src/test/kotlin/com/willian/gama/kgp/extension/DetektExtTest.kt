package com.willian.gama.kgp.extension

import com.willian.gama.kgp.constants.DetektConstants.DETEKT_BASELINE_REPORT_PATH
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_EXCLUDE_PATTERN
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_HTML_REPORT_PATH
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_IGNORED_BUILD_TYPES
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_IGNORED_FLAVORS
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_INCLUDE_PATTERN
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_PLUGIN_IN
import com.willian.gama.kgp.constants.DetektConstants.DETEKT_XML_REPORT_PATH
import com.willian.gama.kgp.test.TestData.TEST_DETEKT_CONFIG_FILE
import com.willian.gama.kgp.test.TestData.createDetektProperties
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private val TEST_DETEKT_PROPERTIES = createDetektProperties(
    ignoreFailures = true,
    file = TEST_DETEKT_CONFIG_FILE
)

class DetektExtTest {
    @Test
    fun `test detekt plugin is applied`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        assertTrue(project.pluginManager.hasPlugin(DETEKT_PLUGIN_IN))
    }

    @Test
    fun `test detekt task with jmvTarget set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(JavaVersion.VERSION_17.toString(), jvmTarget)
        }
    }

    @Test
    fun `test detekt task with parallel set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(true, parallel)
        }
    }

    @Test
    fun `test detekt task with allRules set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(true, allRules)
        }
    }

    @Test
    fun `test detekt task with autoCorrect set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(true, autoCorrect)
        }
    }

    @Test
    fun `test detekt task with buildUponDefaultConfig set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(true, buildUponDefaultConfig)
        }
    }

    @Test
    fun `test detekt task with ignoreFailures set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(ignoreFailures = true))
        project.tasks.withType<Detekt>().first().run {
            assertEquals(true, ignoreFailures)
        }
    }

    @Test
    fun `test detekt task with txt report set as not required`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(false, reports.txt.required.get())
        }
    }

    @Test
    fun `test detekt task with sarif report set as not required`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(false, reports.sarif.required.get())
        }
    }

    @Test
    fun `test detekt task with md report set as not required`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(false, reports.md.required.get())
        }
    }

    @Test
    fun `test detekt task with html report set as required`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(true, reports.html.required.get())
        }
    }

    @Test
    fun `test detekt task with html outputLocation set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(
                project.layout.buildDirectory.dir(DETEKT_HTML_REPORT_PATH).get().asFile,
                reports.html.outputLocation.get().asFile
            )
        }
    }

    @Test
    fun `test detekt task with xml report set as required`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(true, reports.xml.required.get())
        }
    }

    @Test
    fun `test detekt task with xml outputLocation set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<Detekt>().first().run {
            assertEquals(
                project.layout.buildDirectory.dir(DETEKT_XML_REPORT_PATH).get().asFile,
                reports.xml.outputLocation.get().asFile
            )
        }
    }

    @Test
    fun `test detekt task with config file set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<Detekt>().first().run {
            assertEquals(TEST_DETEKT_CONFIG_FILE, config.from.first())
        }
    }

    @Test
    fun `test detekt task with baseline set`() {
        val project = ProjectBuilder.builder().build()
        val file = project.layout.projectDirectory.dir(DETEKT_BASELINE_REPORT_PATH).asFile.apply {
            parentFile.mkdirs()
            createNewFile()
            deleteOnExit()
        }
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<Detekt>().first().run {
            assertEquals(file, baseline.get().asFile)
        }
    }

    @Test
    fun `test detekt task with baseline not set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<Detekt>().first().run {
            assertFalse(baseline.isPresent)
        }
    }

    @Test
    fun `test detekt task with inclusion set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<Detekt>().first().run {
            assertEquals(
                DETEKT_INCLUDE_PATTERN.toHashSet(),
                includes
            )
        }
    }

    @Test
    fun `test detekt task with exclusion set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<Detekt>().first().run {
            assertEquals(
                DETEKT_EXCLUDE_PATTERN.toHashSet(),
                excludes
            )
        }
    }

    @Test
    fun `test detekt extension with ignored build types set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.extensions.getByType<DetektExtension>().run {
            assertEquals(DETEKT_IGNORED_BUILD_TYPES, ignoredBuildTypes)
        }
    }

    @Test
    fun `test detekt extension with ignored flavors set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.extensions.getByType<DetektExtension>().run {
            assertEquals(DETEKT_IGNORED_FLAVORS, ignoredFlavors)
        }
    }

    @Test
    fun `test detekt-baseline task with jmvTarget set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(JavaVersion.VERSION_17.toString(), jvmTarget)
        }
    }

    @Test
    fun `test detekt-baseline task with parallel set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(true, parallel.get())
        }
    }

    @Test
    fun `test detekt-baseline task with allRules set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(true, allRules.get())
        }
    }

    @Test
    fun `test detekt-baseline task with buildUponDefaultConfig set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES)
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(true, buildUponDefaultConfig.get())
        }
    }

    @Test
    fun `test detekt-baseline task with config file set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(TEST_DETEKT_CONFIG_FILE, config.from.first())
        }
    }

    @Test
    fun `test detekt-baseline task with baseline set`() {
        val project = ProjectBuilder.builder().build()
        val file = project.layout.projectDirectory.dir(DETEKT_BASELINE_REPORT_PATH).asFile.apply {
            parentFile.mkdirs()
            createNewFile()
            deleteOnExit()
        }
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(file, baseline.get().asFile)
        }
    }

    @Test
    fun `test detekt-baseline task with inclusion set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(
                DETEKT_INCLUDE_PATTERN.toHashSet(),
                includes
            )
        }
    }

    @Test
    fun `test detekt-baseline task with exclusion set`() {
        val project = ProjectBuilder.builder().build()
        project.setUpDetekt(properties = TEST_DETEKT_PROPERTIES.copy(configFile = TEST_DETEKT_CONFIG_FILE))
        project.tasks.withType<DetektCreateBaselineTask>().first().run {
            assertEquals(
                DETEKT_EXCLUDE_PATTERN.toHashSet(),
                excludes
            )
        }
    }
}