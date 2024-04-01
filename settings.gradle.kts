pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal() // https://docs.gradle.org/current/userguide/plugins.html#sec:custom_plugin_repositories
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}

plugins {
    /* Required for Circleci: https://app.circleci.com/pipelines/circleci/A5yyywdwJzMPnUc2dxVrDW/HrsAhkavEsbtRm2Sa87CZg/4/workflows/ec5662a1-1af9-4a0f-b923-5d5ee659a249/jobs/7?invite=true#step-104-4336_91
       No matching toolchains found for requested specification: {languageVersion=11, vendor=any, implementation=vendor-specific} for LINUX on x86_64.
       No locally installed toolchains match and toolchain download repositories have not been configured.
    */
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "WillianGamaCI"

