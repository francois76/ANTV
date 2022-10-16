@file:Suppress("UnstableApiUsage")

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
        // application dependancies
        create("antvLibs") {
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
            version("coil-compose", "2.2.2") // https://coil-kt.github.io/coil/
            version("napier", "2.6.1") // https://github.com/AAkira/Napier
            version("ktor", "2.1.2") // https://ktor.io/docs/http-client-engines.html
            version("xmlutil", "0.84.3") // https://github.com/pdvrieze/xmlutil/releases
            version(
                "kotlinx-datetime",
                "0.4.0"
            ) // https://github.com/Kotlin/kotlinx-datetime/releases
            version(
                "kotlinx-serialization-json",
                "1.4.0"
            ) // https://github.com/Kotlin/kotlinx.serialization/releases
            version("junit", "4.13.2") // depend on intellij
            version(
                "accompanist-systemuicontroller",
                "0.25.1"
            ) // https://github.com/google/accompanist/releases
            version(
                "play-services-cast-framework",
                "21.2.0"
            ) // https://mvnrepository.com/artifact/com.google.android.gms/play-services-cast-framework?repo=google
            version(
                "core-ktx",
                "1.9.0"
            ) // https://developer.android.com/jetpack/androidx/releases/core
            version(
                "media3",
                "1.0.0-beta02"
            ) // https://developer.android.com/jetpack/androidx/releases/media3
            version(
                "navigation",
                "2.5.2"
            ) // https://developer.android.com/jetpack/androidx/releases/navigation
            version(
                "lifecycle",
                "2.5.1"
            ) // https://developer.android.com/jetpack/androidx/releases/lifecycle
            /*
            monoline repos
             */
            hashMapOf(
                "napier" to "io.github.aakira",
                "coil-compose" to "io.coil-kt",
                "accompanist-systemuicontroller" to "com.google.accompanist",
                "junit" to "junit",
                "play-services-cast-framework" to "com.google.android.gms",
                "core-ktx" to "androidx.core"
            ).forEach {
                library(it.key, it.value, it.key).versionRef(it.key)
            }

            /*
            Moko resource
             */
            listOf(
                "resources-generator",
                "resources-compose",
                "resources",
                "resources-test"
            ).forEach {
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
            val torCoreDependencies = arrayOf(
                "ktor-client-core",
                "ktor-client-json",
                "ktor-client-logging"
            )
            listOf(*torCoreDependencies, "ktor-client-okhttp").forEach {
                library(it, "io.ktor", it).versionRef("ktor")
            }
            bundle("ktor-common", torCoreDependencies.asList())
            /*
            xml util
             */
            val xmlUtilsDependencies = listOf("core", "serialization")
            xmlUtilsDependencies.forEach {
                library(it, "io.github.pdvrieze.xmlutil", it).versionRef("xmlutil")
            }
            bundle("xmlutil", xmlUtilsDependencies)
            /*
            Kotlinx
             */
            listOf("datetime", "serialization-json").forEach {
                library(
                    "kotlinx-$it",
                    "org.jetbrains.kotlinx",
                    "kotlinx-$it"
                ).versionRef("kotlinx-$it")
            }

            /*
            androidx media3
             */
            val media3Dependencies = listOf(
                "media3-exoplayer",
                "media3-exoplayer-hls",
                "media3-ui",
                "media3-cast",
                "media3-session"
            )
            media3Dependencies.forEach {
                library(it, "androidx.media3", it).versionRef("media3")
            }
            bundle("media3", media3Dependencies)

            // androidx navigation
            val navigationDependencies = arrayOf(
                "navigation-fragment-ktx",
                "navigation-ui-ktx",
                "navigation-dynamic-features-fragment",
                "navigation-compose"
            )
            listOf(*navigationDependencies, "navigation-testing").forEach {
                library(it, "androidx.navigation", it).versionRef("navigation")
            }
            bundle("navigation", navigationDependencies.asList())

            // androidx lifecycle
            listOf("lifecycle-process", "lifecycle-runtime").forEach {
                library(it, "androidx.lifecycle", it).versionRef("lifecycle")
            }
        }
    }
}
rootProject.name = "ANTV"
include(":shared")
include(":app")




