pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        // application dependancies
        create("antvLibs") {
            version("version", "1.1.2")
            version("versionNumber", "11")
            version("sdk-compile", "33")
            version("sdk-min", "26")
            version("sdk-target", "33")
        }


        // libraries dependencies
        create("libs") {
            /*
            version definition
             */
            version("moko-resource", "0.22.0") // https://github.com/icerockdev/moko-resources
            version("moko-mvvm", "0.16.1") // https://github.com/icerockdev/moko-mvvm/releases
            version("coil-compose", "2.3.0") // https://coil-kt.github.io/coil/
            version("napier", "2.6.1") // https://github.com/AAkira/Napier
            version("ktor", "2.3.0") // https://github.com/ktorio/ktor/releases
            version("xmlutil", "0.85.0") // https://github.com/pdvrieze/xmlutil/releases
            version(
                "kotlinx-datetime",
                "0.4.0"
            ) // https://github.com/Kotlin/kotlinx-datetime/releases
            version(
                "kotlinx-serialization-json",
                "1.5.0"
            ) // https://github.com/Kotlin/kotlinx.serialization/releases
            version(
                "kotlinx-coroutines-guava",
                "1.6.4"
            ) // https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-guava/
            version("junit", "4.13.2") // depend on intellij
            version(
                "play-services-cast-framework",
                "21.3.0"
            ) // https://mvnrepository.com/artifact/com.google.android.gms/play-services-cast-framework?repo=google
            version(
                "core-ktx",
                "1.10.0"
            ) // https://developer.android.com/jetpack/androidx/releases/core
            version(
                "media3",
                "1.0.1"
            ) // https://developer.android.com/jetpack/androidx/releases/media3
            version(
                "navigation",
                "2.5.3"
            ) // https://developer.android.com/jetpack/androidx/releases/navigation
            version(
                "lifecycle",
                "2.6.1"
            ) // https://developer.android.com/jetpack/androidx/releases/lifecycle
            version(
                "material3",
                "1.0.1"
            ) // https://developer.android.com/jetpack/androidx/releases/compose-material3
            version("guava", "31.1-android") // https://github.com/google/guava/releases
            version(
                "concurrent-futures",
                "1.1.0"
            ) // https://developer.android.com/jetpack/androidx/releases/concurrent
            version(
                "activity-compose",
                "1.7.1"
            ) // https://developer.android.com/jetpack/androidx/releases/activity

            // compose stack: https://developer.android.com/jetpack/androidx/releases/compose
            version("compose", "1.4.2")
            version(
                "compose-multiplatform",
                "1.4.0"
            ) // https://github.com/JetBrains/compose-multiplatform/releases
            version(
                "compose-compiler",
                "1.4.6"
            ) // https://developer.android.com/jetpack/androidx/releases/compose-compiler
            version(
                "accompanist",
                "0.30.0" // WARNING! depend on version of compose
            ) // https://github.com/google/accompanist/releases
            version("kotlin", "1.8.20")
            version(
                "android-gradle-plugin",
                "8.0.0"
            ) // https://developer.android.com/studio/releases/gradle-plugin


            /*
            * plugins
             */

            plugin(
                "serialization",
                "org.jetbrains.kotlin.plugin.serialization"
            ).versionRef("kotlin")
            plugin(
                "com.android.application",
                "com.android.application"
            ).versionRef("android-gradle-plugin")
            plugin(
                "com.android.library",
                "com.android.library"
            ).versionRef("android-gradle-plugin")
            plugin(
                "org.jetbrains.compose",
                "org.jetbrains.compose"
            ).versionRef("compose-multiplatform")

            /*
            monoline repos
             */
            hashMapOf(
                "napier" to "io.github.aakira",
                "coil-compose" to "io.coil-kt",
                "guava" to "com.google.guava",
                "junit" to "junit",
                "play-services-cast-framework" to "com.google.android.gms",
                "kotlinx-coroutines-guava" to "org.jetbrains.kotlinx",
                "activity-compose" to "androidx.activity",
                "concurrent-futures" to "androidx.concurrent",
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

            val mokoMvvmDependencies = arrayOf(
                "mvvm-compose",
                "mvvm-core",
                "mvvm-livedata",
                "mvvm-state",
            )
            listOf(*mokoMvvmDependencies).forEach {
                library(it, "dev.icerock.moko", it).versionRef("moko-mvvm")
            }
            bundle("moko-mvvm", mokoMvvmDependencies.asList())

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

            // material3
            library(
                "material3-core",
                "androidx.compose.material3",
                "material3"
            ).versionRef("material3")
            library(
                "material3-window-size",
                "androidx.compose.material3",
                "material3-window-size-class"
            ).versionRef("material3")
            bundle("material3", listOf("material3-core", "material3-window-size"))

            // accompanist
            library(
                "accompanist-systemuicontroller",
                "com.google.accompanist",
                "accompanist-systemuicontroller"
            ).versionRef("accompanist")
            library(
                "accompanist-themeadapter-material3",
                "com.google.accompanist",
                "accompanist-themeadapter-material3"
            ).versionRef("accompanist")
            bundle(
                "accompanist",
                listOf("accompanist-systemuicontroller", "accompanist-themeadapter-material3")
            )

            // androidx compose
            val composeDependencies = hashMapOf(
                "runtime" to arrayListOf("runtime", "runtime-livedata"),
                "ui" to arrayListOf("ui", "ui-viewbinding", "ui-tooling-preview"),
                "foundation" to arrayListOf("foundation", "foundation-layout"),
            )
            composeDependencies.forEach {
                it.value.forEach { module ->
                    library(
                        module,
                        "androidx.compose.${it.key}",
                        module
                    ).versionRef("material3")
                }
            }
            library(
                "compose-ui-tooling",
                "androidx.compose.ui",
                "ui-tooling"
            ).versionRef("material3")
            bundle("compose", composeDependencies.values.flatten())
        }
    }
}

rootProject.name = "ANTV"

include(":androidApp")
include(":shared")
include(":desktopApp")
