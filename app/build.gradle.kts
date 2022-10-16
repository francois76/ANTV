plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dev.icerock.mobile.multiplatform-resources")
}


android {
    compileSdk = antvLibs.versions.sdk.compile.get().toInt()
    namespace = "fr.fgognet.antv"

    defaultConfig {
        applicationId = "fr.fgognet.antv"
        minSdk = antvLibs.versions.sdk.min.get().toInt()
        targetSdk = antvLibs.versions.sdk.target.get().toInt()
        versionCode = antvLibs.versions.versionNumber.get().toInt()
        versionName = antvLibs.versions.version.get()

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
    packagingOptions {
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
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":shared"))
    // bundles
    implementation(libs.bundles.moko.mvvm.android)
    implementation(libs.bundles.media3)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.material3)
    implementation(libs.bundles.compose)
    // libs
    implementation(libs.moko.resources.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.compose)
    implementation(libs.play.services.cast.framework)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.process)
    implementation(libs.compose.theme.adapter.get3())
    implementation(libs.activity.compose)

    // testing
    testImplementation(libs.junit)
    testImplementation(libs.navigation.testing)
    testImplementation(libs.lifecycle.runtime)
    debugImplementation(libs.compose.ui.tooling)


}
