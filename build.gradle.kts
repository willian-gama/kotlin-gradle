import java.io.FileInputStream
import java.util.*

private val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

group = "com.willian.gama"
version = "0.0.4"

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.3.2")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.1.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.6")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:5.0.0.4638")
}

gradlePlugin {
    plugins {
        create("linting") {
            id = "com.willian.gama.kgp.code.analysis"
            displayName = "Code analysis plugin"
            description = "Kotlin Gradle Plugin to scan code linting"
            implementationClass = "com.willian.gama.kgp.plugin.CodeAnalysisPlugin"
        }
    }
}

publishing {
    // Publish locally ./gradlew publishPluginMavenPublicationToMavenLocal publishLintingPluginMarkerMavenPublicationToMavenLocal
    publications {
        // https://docs.gradle.org/current/userguide/publishing_maven.html#sec:identity_values_in_the_generated_pom
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

publishing {
    // Publish on github packages ./gradlew publishPluginMavenPublicationToGitHubPackagesRepository publishLintingPluginMarkerMavenPublicationToGitHubPackagesRepository
    repositories {
        maven {
            name = "GitHubPackages"
            // Repository name MUST NOT have upper case it's also defined in settings.gradle.kts in rootProject.name="ksp" (it could be any name)
            url = uri("https://maven.pkg.github.com/${localProperties.getProperty("github_user_id")}/kgp")
            credentials {
                username = localProperties.getProperty("github_user_id")
                password = localProperties.getProperty("github_key")
            }
        }
    }
}