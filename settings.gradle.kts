@file:Suppress("UnstableApiUsage")

pluginManagement {
    val kotlin = "1.7.10"
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
        // application dependancies
        create("antv") {
            version("version", "0.0.6")
            version("versionNumber", "4")
            version("sdk-compile", "33")
            version("sdk-min", "26")
            version("sdk-target", "32")
        }

        // libraries dependencies
        create("libs") {
            // version definition
            version("moko-resource", "0.20.1")
            version("moko-mvvm", "0.14.0")
            version("kotlin", "1.7.10")

            // monoline repo with version
            library("coil", "io.coil-kt", "coil-compose").version("2.2.1")


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




