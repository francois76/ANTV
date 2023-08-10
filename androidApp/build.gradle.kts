plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

kotlin {
    android()
    @Suppress("UnusedPrivateMember", "UNUSED_VARIABLE") // False positive
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
    }
}

android {
    compileSdk = antvLibs.versions.android.sdk.compile.get().toInt()
    namespace = antvLibs.versions.antv.packagename.get()

    defaultConfig {
        applicationId = antvLibs.versions.antv.packagename.get()
        minSdk = antvLibs.versions.android.sdk.min.get().toInt()
        targetSdk = antvLibs.versions.android.sdk.target.get().toInt()
        versionCode = antvLibs.versions.android.version.number.get().toInt()
        versionName = antvLibs.versions.antv.version.get()

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
            )
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    packaging {
        resources {
            excludes.addAll(
                arrayListOf(
                    "META-INF/DEPENDENCIES",
                    "META-INF/LICENSE",
                    "META-INF/LICENSE.txt",
                    "META-INF/license.txt",
                    "META-INF/LICENSE.md",
                    "META-INF/LICENSE-notice.md",
                    "META-INF/NOTICE",
                    "META-INF/NOTICE.md",
                    "META-INF/NOTICE.txt",
                    "META-INF/notice.txt",
                    "META-INF/ASL2.0",
                    "META-INF/mimetypes.default",
                    "META-INF/mailcap.default",
                    "META-INF/*.kotlin_module"
                )
            )
        }
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    
}
dependencies {
    implementation(libs.play.services.cast.framework)
    implementation(libs.bundles.media3)
    implementation(libs.activity.compose)
    implementation(libs.napier)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)

    // testing
    testImplementation(libs.junit)
    testImplementation(libs.lifecycle.runtime)
    debugImplementation(libs.compose.ui.tooling)
}

