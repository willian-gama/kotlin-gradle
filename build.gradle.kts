import java.io.FileInputStream
import java.util.*

group = "com.willian.gama.gradle"
version = "0.0.1"

private val githubProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

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
    implementation("com.android.tools.build:gradle:8.3.1")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.1.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.6")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:5.0.0.4638")
}

gradlePlugin {
    plugins {
        create("linting") {
            id = "com.willian.gama.gradle.plugin.code-analysis"
            implementationClass = "com.willian.gama.gradle.plugin.CodeAnalysisPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${githubProperties.getProperty("github_user_id")}/WillianGamaGradle")
            credentials {
                username = githubProperties.getProperty("github_user_id")
                password = githubProperties.getProperty("github_key")
            }
        }
    }

    publications {
        // https://docs.gradle.org/current/userguide/publishing_maven.html#sec:identity_values_in_the_generated_pom
        create<MavenPublication>("gpr") {
            from(components["java"])
            afterEvaluate {
                artifactId = "plugin"
            }
//            groupId = project.group.toString()
//            artifactId = project.name
//            version = project.version.toString()
        }
    }
}