plugins {
    id("org.jetbrains.kotlin.plugin.serialization").version(Versions.kotlin)
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
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

            // for xml
            implementation("io.github.pdvrieze.xmlutil:core:${Versions.xmlUtils}")
            implementation("io.github.pdvrieze.xmlutil:serialization:${Versions.xmlUtils}")
            // rest
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlinx.serialization}")
            // logger
            implementation("io.github.aakira:napier:${Versions.napier}")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.datetime}")
            implementation("com.soywiz.korlibs.korim:korim:${Versions.kor}")
            implementation("com.soywiz.korlibs.korio:korio:${Versions.kor}")
            // ktor
            implementation("io.ktor:ktor-client-core:${Versions.ktor}")
            implementation("io.ktor:ktor-client-json:${Versions.ktor}")
            implementation("io.ktor:ktor-client-logging:${Versions.ktor}")
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
            implementation("com.soywiz.korlibs.korio:korio-macosarm64:${Versions.kor}")
        }
        val iosArm64Main by getting
        iosArm64Main.dependencies {
            implementation("com.soywiz.korlibs.korio:korio-iosarm64:${Versions.kor}")
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
    compileSdk = Versions.Sdk.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.Sdk.minSdk
        targetSdk = Versions.Sdk.targetSdk
    }
}
