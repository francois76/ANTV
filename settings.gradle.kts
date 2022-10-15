pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("libs") {
            version("moko-resource", "0.20.1")
            version("moko-mvvm", "0.14.0")

            val mokoMvvmAndroidDependencies = listOf(
                "mvvm-livedata-material",
                "mvvm-livedata-glide",
                "mvvm-livedata-swiperefresh",
                "mvvm-databinding",
                "mvvm-viewbinding"
            )
            val mokoMvvmCoreDependencies = listOf(
                "mvvm-core",
                "mvvm-livedata",
                "mvvm-state",
            )
            listOf(mokoMvvmAndroidDependencies, mokoMvvmCoreDependencies).flatten().forEach {
                library(it, "dev.icerock.moko", it).versionRef("moko-mvvm")
            }
            bundle("moko-mvvm-android", mokoMvvmAndroidDependencies)
            bundle("moko-mvvm-core", mokoMvvmCoreDependencies)
        }
    }
}
rootProject.name = "ANTV"
include(":shared")
include(":app")




