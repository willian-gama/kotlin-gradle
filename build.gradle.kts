group = "com.willian.gama.gradle.plugin"
version = "1.0.0"

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    jacoco // Jacoco plugin: https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:jacoco_getting_started
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
    // implementation("app.cash.paparazzi:paparazzi-gradle-plugin:1.3.3")
}

gradlePlugin {
    plugins {
        create("linting") {
            id = "com.willian.gama.gradle.plugin.gradle.code-analysis"
            implementationClass = "com.willian.gama.gradle.plugin.CodeAnalysisPlugin"
        }
    }
}

publishing {
    publications {
        // https://docs.gradle.org/current/userguide/publishing_maven.html#sec:identity_values_in_the_generated_pom
        create<MavenPublication>("release") {
            from(components["java"])
            groupId = "com.willian.gama.gradle"
            artifactId = "plugin"
            version = "1.0.0"
        }
    }
}