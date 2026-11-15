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

rootProject.name = "ResponsiveLayout1"

// Part 1 - LinearLayout with weights
include(":linear-layout")

// Part 2 - ConstraintLayout with weights
include(":constraint-layout")
