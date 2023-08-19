plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.serialization)
    id("dev.icerock.mobile.multiplatform-resources")
    id("kotlin-parcelize")
}

version = antvLibs.versions.antv.version.get()

kotlin {
    androidTarget()
    jvm(name = "desktop")
    ios()
    iosSimulatorArm64()

    jvmToolchain(17)

    cocoapods {
        summary = "Shared code for the sample"
        homepage = "https://github.com/JetBrains/compose-jb"
        ios.deploymentTarget = "16.2"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resource"] = "'build/cocoapods/framework/shared.framework/*.bundle'"
    }

    @Suppress("UnusedPrivateMember", "UNUSED_VARIABLE") // False positive
    sourceSets {
        val commonMain by getting {
            resources.srcDirs(File(buildDir, "generated/moko/commonMain/src"))
            dependencies {
                implementation(libs.bundles.moko.mvvm)
                implementation(libs.napier)
                implementation(libs.kamel)
                implementation(libs.navigation)
                implementation(libs.bundles.ktor.common)
                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)
                implementation(libs.bundles.xmlutil)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
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
            dependsOn(commonMain)
            dependencies {
                // bundles

                implementation(libs.bundles.media3)
                implementation(libs.bundles.accompanist)
                implementation(libs.bundles.compose)

                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.guava)
                implementation(libs.guava)
                implementation(libs.concurrent.futures)
                implementation(libs.lifecycle.process)
                implementation(libs.moko.resources.compose)
                implementation(libs.kotlinx.datetime)
                implementation(libs.play.services.cast.framework)
                implementation(libs.core.ktx)
                implementation(libs.activity.compose)


            }
        }
        val iosMain by getting {
            dependsOn(commonMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }


        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.macos_arm64)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(compose.preview)
            }
        }
    }
}

android {
    namespace = antvLibs.versions.antv.packagename.get()
    compileSdk = antvLibs.versions.android.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = antvLibs.versions.android.sdk.min.get().toInt()
    }
}
dependencies {
    implementation(libs.ui.text.android)
}


multiplatformResources {
    multiplatformResourcesPackage = antvLibs.versions.antv.packagename.get()
    disableStaticFrameworkWarning = false

}




