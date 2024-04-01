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
        languageVersion.set(JavaLanguageVersion.of("17"))
    }
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.withType<Test>().configureEach {
    extensions.configure(JacocoTaskExtension::class) {
        isIncludeNoLocationClasses = true // Robolectric support
        excludes = listOf(
            "jdk.internal.*",
            "coil.compose.*"
        )
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.3.1")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.1.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.6")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
}

gradlePlugin {
    plugins {
        create("linting") {
            id = "com.willian.gama.plugin.gradle.code-analysis"
            implementationClass = "com.willian.gama.gradle.CodeAnalysisPlugin"
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