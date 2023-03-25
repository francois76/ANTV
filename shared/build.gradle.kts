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
        iosSimulatorArm64(),
        iosArm64(),
        macosArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    // See https://youtrack.jetbrains.com/issue/KT-55751
    val myAttribute = Attribute.of("myOwnAttribute", String::class.java)

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
    configurations.named("releaseFrameworkIosFat").configure {
        attributes {
            // put a unique attribute
            attribute(myAttribute, "release-all")
        }
    }

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
    configurations.named("debugFrameworkIosFat").configure {
        attributes {
            attribute(myAttribute, "debug-all")
        }
    }

    // replace releaseFrameworkIosFat by the name of the first configuration that conflicts
    configurations.named("releaseFrameworkOsxFat").configure {
        attributes {
            // put a unique attribute
            attribute(myAttribute, "release-all-osx")
        }
    }

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
    configurations.named("releaseFrameworkMacosArm64").configure {
        attributes {
            attribute(myAttribute, "release-all-macos")
        }
    }
    // replace debugFrameworkIosFat by the name of the second configuration that conflicts
    configurations.named("debugFrameworkMacosArm64").configure {
        attributes {
            attribute(myAttribute, "debug-all-macos")
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
                implementation(libs.guava)
                implementation(libs.concurrent.futures)
            }
        }
        val macosArm64Main by getting
        // val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            macosArm64Main.dependsOn(this)
            // iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
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
        targetSdk = antvLibs.versions.sdk.target.get().toInt()
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "fr.fgognet.antv"
    disableStaticFrameworkWarning = true
}


