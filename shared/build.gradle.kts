plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.7.10")
    id("org.jetbrains.kotlin.plugin.serialization").version("1.7.10")
    id("com.android.library")
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
    val napierVersion = "2.6.1"
    val xmlUtilsVersion = "0.84.2"
    val ktorVersion = "2.1.0"
    val korVersion = "3.0.0-Beta7"
    sourceSets {
        sourceSets["commonMain"].dependencies {

            // for xml
            implementation("io.github.pdvrieze.xmlutil:core:$xmlUtilsVersion")
            implementation("io.github.pdvrieze.xmlutil:serialization:$xmlUtilsVersion")
            // rest
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
            // logger
            implementation("io.github.aakira:napier:$napierVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            implementation("com.soywiz.korlibs.korim:korim:$korVersion")
            implementation("com.soywiz.korlibs.korio:korio:$korVersion")
            // ktor
            implementation("io.ktor:ktor-client-core:${ktorVersion}")
            implementation("io.ktor:ktor-client-json:${ktorVersion}")
            implementation("io.ktor:ktor-client-logging:${ktorVersion}")
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        sourceSets["androidMain"].dependencies {
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
        }
        val androidTest by getting
        val macosArm64Main by getting
        macosArm64Main.dependencies {
            implementation("com.soywiz.korlibs.korio:korio-macosarm64:$korVersion")
        }
        val iosArm64Main by getting
        iosArm64Main.dependencies {
            implementation("com.soywiz.korlibs.korio:korio-iosarm64:$korVersion")
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
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 32
    }
}
