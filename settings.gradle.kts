pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "StickerApp"
include(":app")
include(":camera")
include(":camera:camera_presentation")
include(":collections")
include(":core")
include(":collections:collections_presentation")
include(":camera:camera_domain")
include(":camera:camera_data")
include(":collections:collections_domain")
include(":collections:collections_data")
