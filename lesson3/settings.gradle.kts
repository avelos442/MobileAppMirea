pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "lesson3"
include(":app")
include(":intentapp")
include(":sharer1")
include(":favoritebook")
include(":systemintentsapp")
include(":simplefragmentapp")
