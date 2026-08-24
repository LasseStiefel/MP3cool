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

rootProject.name = "MP3cool"

include(
    ":android:app",
    ":android:core",
    ":android:domain",
    ":android:playback",
    ":android:library",
    ":android:database",
    ":android:spotify",
    ":android:hardware-api",
    ":android:hardware-mock",
    ":android:epaper-ui",
    ":android:design-system",
)
