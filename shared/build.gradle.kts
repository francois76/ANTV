plugins {
    kotlin("multiplatform")
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    id("dev.icerock.mobile.multiplatform-resources")
    id("kotlin-parcelize")
}


version = antvLibs.versions.antv.version.get()

kotlin {
    androidTarget()
    jvm(name = "desktop")

    applyDefaultHierarchyTemplate()

    // iOS targets declared for compilation but framework generation is commented due to Gradle 9 incompatibility
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    /* XCFramework configuration - Currently incompatible with Gradle 9.0
     * Uncomment when Kotlin plugin supports Gradle 9 for binaries.framework
     *
     * val xcframeworkName = "shared"
     * val xcframework = XCFramework(xcframeworkName)
     *
     * iosX64 {
     *     binaries.framework {
     *         baseName = xcframeworkName
     *         isStatic = true
     *         xcframework.add(this)
     *         export(libs.moko.resources)
     *     }
     * }
     * // ... same for iosArm64 and iosSimulatorArm64
     */

    // CocoaPods temporarily disabled for Gradle 9 migration
    // XCFramework also disabled - waiting for Kotlin plugin update for Gradle 9 support

    @Suppress("UnusedPrivateMember", "UNUSED_VARIABLE") // False positive
    sourceSets {
        val commonMain by getting {
            resources.srcDirs(
                rootProject.layout.buildDirectory.dir("generated/moko-resources/commonMain/src")
            )
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
            dependencies {
                // bundles

                implementation(libs.bundles.media3)
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

kotlin {
    jvmToolchain(21)
}

android {
    namespace = antvLibs.versions.antv.packagename.get()
    compileSdk = antvLibs.versions.android.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = antvLibs.versions.android.sdk.min.get().toInt()
    }
}


multiplatformResources {
    resourcesPackage.set(antvLibs.versions.antv.packagename.get())
}

// Task to assemble XCFramework for debug
tasks.register("assembleSharedXCFramework") {
    dependsOn("assembleSharedDebugXCFramework")
    group = "build"
    description = "Assemble debug XCFramework for all iOS targets"
}




