@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.serialization)
    alias(libs.plugins.com.android.library)
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform-resources")
}


kotlin {
    android()
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.moko.mvvm.core)
                implementation(libs.napier)
                implementation(libs.bundles.ktor.common)
                implementation(libs.moko.resources)
                implementation(libs.bundles.xmlutil)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                api(libs.moko.resources)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.bundles.media3)
                implementation(libs.play.services.cast.framework)
                implementation(libs.bundles.navigation)
                implementation(libs.kotlinx.coroutines.guava)
                implementation("com.google.guava:guava:31.0.1-android")

                // To use CallbackToFutureAdapter
                implementation("androidx.concurrent:concurrent-futures:1.1.0")
            }
        }
        val macosArm64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            macosArm64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "fr.fgognet.antv"
    compileSdk = antvLibs.versions.sdk.compile.get().toInt()
    sourceSets["main"].apply {
        assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }
    defaultConfig {
        minSdk = antvLibs.versions.sdk.min.get().toInt()
        targetSdk = antvLibs.versions.sdk.target.get().toInt()
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "fr.fgognet.antv"
    disableStaticFrameworkWarning = true
}


