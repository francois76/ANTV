@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once https://github.com/gradle/gradle/issues/22797 is fixed (should be gradle 8.1)
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("dev.icerock.mobile.multiplatform-resources")
    id("kotlin-parcelize")
}

version = antvLibs.versions.version.get()

kotlin {
    android()
    jvm("desktop")
    ios()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared code for the sample"
        homepage = "https://github.com/JetBrains/compose-jb"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.moko.mvvm)
                implementation(libs.napier)
                implementation(libs.bundles.ktor.common)
                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)
                implementation(libs.bundles.xmlutil)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                api(libs.moko.resources)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val androidMain by getting {
            dependencies {
                // bundles

                implementation(libs.bundles.media3)
                implementation(libs.bundles.navigation)
                implementation(libs.bundles.accompanist)
                implementation(libs.bundles.compose)

                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.guava)
                implementation(libs.guava)
                implementation(libs.concurrent.futures)
                implementation(libs.lifecycle.process)
                implementation(libs.moko.resources.compose)
                implementation(libs.kotlinx.datetime)
                implementation(libs.coil.compose)
                implementation(libs.play.services.cast.framework)
                implementation(libs.core.ktx)
                implementation(libs.activity.compose)


            }
        }
        val iosMain by getting {
            dependencies {
                // Kotlin Coroutines 1.7.0 contains Dispatchers.IO
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }


        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.preview)
            }
        }
    }
}

android {
    namespace = "fr.fgognet.antv"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    compileSdk = antvLibs.versions.sdk.compile.get().toInt()
    sourceSets["main"].apply {
        assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }
    defaultConfig {
        minSdk = antvLibs.versions.sdk.min.get().toInt()
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "fr.fgognet.antv"
    disableStaticFrameworkWarning = true
}


