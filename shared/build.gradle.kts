plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.7.10")
    id("org.jetbrains.kotlin.plugin.serialization").version("1.7.10")
    id("com.android.library")
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        sourceSets["commonMain"].dependencies {

            val napierVersion = "2.6.1"
            val xmlUtilsVersion = "0.84.2"
            val ktorVersion = "2.1.0"

            // for xml
            implementation("io.github.pdvrieze.xmlutil:core:$xmlUtilsVersion")
            implementation("io.github.pdvrieze.xmlutil:serialization:$xmlUtilsVersion")
            // rest
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0-RC")
            // logger
            implementation("io.github.aakira:napier:$napierVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            val korVersion = "3.0.0"
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
        val androidMain by getting
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(sourceSets["commonMain"])
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
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
