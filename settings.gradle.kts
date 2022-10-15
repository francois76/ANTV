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

            /*
            version definition
             */
            version("moko-resource", "0.20.1") // https://github.com/icerockdev/moko-resources
            version("moko-mvvm", "0.14.0") // https://github.com/icerockdev/moko-mvvm/releases
            version("coil", "2.2.2") // https://coil-kt.github.io/coil/
            version("napier", "2.6.1") // https://github.com/AAkira/Napier
            version("ktor", "2.1.2") // https://ktor.io/docs/http-client-engines.html

            /*
            monoline repos
             */
            library("coil", "io.coil-kt", "coil-compose").versionRef("coil")
            library(
                "napier",
                "io.github.aakira",
                "napier"
            ).versionRef("napier")


            /*
            Moko resource
             */
            listOf("resources-generator", "resources-compose", "resources").forEach {
                library("moko-$it", "dev.icerock.moko", it).versionRef("moko-resource")
            }

            /*
            Moko MVVM
             */
            val mokoMvvmAndroidDependencies = arrayOf(
                "mvvm-livedata-material",
                "mvvm-livedata-glide",
                "mvvm-livedata-swiperefresh",
                "mvvm-databinding",
                "mvvm-viewbinding"
            )
            val mokoMvvmCoreDependencies = arrayOf(
                "mvvm-core",
                "mvvm-livedata",
                "mvvm-state",
            )
            listOf(*mokoMvvmAndroidDependencies, *mokoMvvmCoreDependencies).forEach {
                library(it, "dev.icerock.moko", it).versionRef("moko-mvvm")
            }
            bundle("moko-mvvm-android", mokoMvvmAndroidDependencies.asList())
            bundle("moko-mvvm-core", mokoMvvmCoreDependencies.asList())

            /*
            Ktor
             */
            val ktorCoreDependancies = arrayOf(
                "ktor-client-core",
                "ktor-client-json",
                "ktor-client-logging"
            )
            listOf(*ktorCoreDependancies, "ktor-client-okhttp").forEach {
                library(it, "io.ktor", it).versionRef("ktor")
            }
            bundle("ktor-common", ktorCoreDependancies.asList())

        }
    }
}
rootProject.name = "ANTV"
include(":shared")
include(":app")




