import java.io.FileInputStream
import java.util.Properties

private val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

group = "com.willian.gama"
version = "1.0.39"

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    alias(libs.plugins.jfrog) apply true
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
    }
}

gradlePlugin {
    plugins {
        create("codeAnalysis") {
            id = "com.willian.gama.kgp.code.analysis"
            displayName = "Code analysis plugin"
            description = "Kotlin Gradle Plugin to scan code linting"
            implementationClass = "com.willian.gama.kgp.plugin.CodeAnalysisPlugin"
        }

        create("publishLib") {
            id = "com.willian.gama.kgp.publish.plugin"
            displayName = "Code analysis plugin"
            description = "Kotlin Gradle Plugin to scan code linting"
            implementationClass = "com.willian.gama.kgp.plugin.PublishLibPlugin"
        }
    }
}

// Github packages
publishing {
    // Publish on github packages ./gradlew publishPluginMavenPublicationToGitHubPackagesRepository publishCodeAnalysisPluginMarkerMavenPublicationToGitHubPackagesRepository publishPublishLibPluginMarkerMavenPublicationToGitHubPackagesRepository
    repositories {
        maven {
            name = "GitHubPackages"
            // Repository name MUST NOT have upper case it's also defined in settings.gradle.kts in rootProject.name="ksp" (it could be any name)
            url = uri("https://maven.pkg.github.com/${localProperties.getProperty("gpr_username")}/kgp")
            credentials {
                username = localProperties.getProperty("gpr_username")
                password = localProperties.getProperty("gpr_key") // generate Personal Access token - https://github.com/settings/tokens
            }
        }
    }
}

// Jfrog artifactory: https://github.com/jfrog/artifactory-gradle-plugin?tab=readme-ov-file#-installation
artifactory {
    setContextUrl(localProperties.getProperty("gpr_url"))

    publish {
        repository {
            repoKey = "android-lib-code-analysis"
            username = localProperties.getProperty("gpr_username")
            password = localProperties.getProperty("gpr_key")
        }

        defaults {
            setPublishArtifacts(true)
            setPublishPom(true)
            isPublishBuildInfo = false
            publications(
                "pluginMaven", // created automatically by "maven-publish"
                "codeAnalysisPluginMarkerMaven", // created by gradlePlugin extension in "codeAnalysis" task
                "publishLibPluginMarkerMaven" // created by gradlePlugin extension in "publishLib" task
            )
        }
    }
}

dependencies {
    implementation(libs.android)
    implementation(libs.ktlint)
    implementation(libs.detekt)
    implementation(libs.sonar)
    implementation(libs.jfrog)

    testImplementation(libs.junit)
}