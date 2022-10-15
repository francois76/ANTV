plugins {
    id("org.jetbrains.kotlin.plugin.serialization").version(Versions.kotlin)
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
        sourceSets["commonMain"].dependencies {
            implementation(libs.bundles.moko.mvvm.core)
            // for xml
            implementation("io.github.pdvrieze.xmlutil:core:${Versions.xmlUtils}")
            implementation("io.github.pdvrieze.xmlutil:serialization:${Versions.xmlUtils}")
            // rest
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlinx.serialization}")
            // logger
            implementation("io.github.aakira:napier:${Versions.napier}")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.datetime}")
            // ktor
            implementation("io.ktor:ktor-client-core:${Versions.ktor}")
            implementation("io.ktor:ktor-client-json:${Versions.ktor}")
            implementation("io.ktor:ktor-client-logging:${Versions.ktor}")
            implementation("dev.icerock.moko:resources:0.20.1")
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        sourceSets["androidMain"].dependencies {
            implementation("io.ktor:ktor-client-okhttp:${Versions.ktor}")
        }
        val androidTest by getting
        val macosArm64Main by getting
        macosArm64Main.dependencies {
        }
        val iosArm64Main by getting
        iosArm64Main.dependencies {
        }
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(sourceSets["commonMain"])
            macosArm64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val macosArm64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            macosArm64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "fr.fgognet.antv"
    compileSdk = Versions.Sdk.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.Sdk.minSdk
        targetSdk = Versions.Sdk.targetSdk
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "fr.fgognet.antv"
    disableStaticFrameworkWarning = true
}
dependencies {
    commonMainApi("dev.icerock.moko:resources:0.20.1")
    commonTestImplementation("dev.icerock.moko:resources-test:0.20.1")
}

