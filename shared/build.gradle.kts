plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.android.library")
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
                // for xml
                implementation("io.github.pdvrieze.xmlutil:core:${Versions.xmlUtils}")
                implementation("io.github.pdvrieze.xmlutil:serialization:${Versions.xmlUtils}")
                // rest
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlinx.serialization}")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.datetime}")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        val macosArm64Main by getting {
            dependencies {
            }
        }
        val iosArm64Main by getting {
            dependencies {
            }
        }
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            macosArm64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val macosArm64Test by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            macosArm64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
        val androidTest by getting {
            dependencies {

            }
        }

    }
}

android {
    namespace = "fr.fgognet.antv"
    compileSdk = antv.versions.sdk.compile.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = antv.versions.sdk.min.get().toInt()
        targetSdk = antv.versions.sdk.target.get().toInt()
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "fr.fgognet.antv"
    disableStaticFrameworkWarning = true
}
dependencies {
    commonMainApi(libs.moko.resources)
    commonTestImplementation(libs.moko.resources.test)
}

