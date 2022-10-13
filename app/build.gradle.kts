plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dev.icerock.mobile.multiplatform-resources")
}


android {
    compileSdk = Versions.Sdk.compileSdk

    defaultConfig {
        applicationId = "fr.fgognet.antv"
        minSdk = Versions.Sdk.minSdk
        targetSdk = Versions.Sdk.targetSdk
        versionCode = Versions.antvNumber
        versionName = Versions.antv

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

    // moko
    listOf(
        "livedata-material",
        "livedata-glide",
        "livedata-swiperefresh",
        "databinding",
        "viewbinding"
    ).forEach {
        implementation(
            group = "dev.icerock.moko",
            name = "mvvm-$it",
            version = Versions.Moko.mvvm
        )
    }
    implementation(
        group = "dev.icerock.moko",
        name = "resources-compose",
        version = Versions.Moko.resources
    )

    // navigation
    listOf(
        "fragment-ktx",
        "ui-ktx",
        "dynamic-features-fragment",
        "compose"
    ).forEach {
        implementation(
            group = "androidx.navigation",
            name = "navigation-$it",
            version = Versions.Androidx.nav
        )
    }

    // exoplayer
    listOf(
        "media3-exoplayer",
        "media3-exoplayer-hls",
        "media3-ui",
        "media3-cast",
        "media3-session"
    ).forEach {
        implementation(
            group = "androidx.media3",
            name = it,
            version = Versions.Androidx.media3
        )
    }

    // Androidx
    implementation("androidx.core:core-ktx:${Versions.Androidx.coreKtx}")
    implementation("androidx.lifecycle:lifecycle-process:${Versions.Androidx.lifecycle}")
    implementation("androidx.appcompat:appcompat:${Versions.Androidx.appCompat}")
    implementation("androidx.activity:activity-compose:${Versions.Androidx.Compose.activity}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.Androidx.constraintLayoutCompose}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Androidx.Compose.compose}")
    androidTestImplementation("androidx.lifecycle:lifecycle-runtime:${Versions.Androidx.lifecycle}")

    // compose
    hashMapOf(
        "runtime" to arrayListOf("runtime", "runtime-livedata"),
        "ui" to arrayListOf("ui", "ui-viewbinding", "ui-tooling-preview"),
        "foundation" to arrayListOf("foundation", "foundation-layout"),
    ).forEach {
        it.value.forEach { module ->
            implementation(
                group = "androidx.compose.${it.key}",
                name = module,
                version = Versions.Androidx.Compose.compose
            )
        }
    }

    // material3
    implementation("androidx.compose.material3:material3:${Versions.Androidx.material3}")
    implementation("androidx.compose.material3:material3-window-size-class:${Versions.Androidx.material3}")
    implementation("com.google.android.material:compose-theme-adapter-3:${Versions.Androidx.Compose.themeAdapter}")

    // misc
    implementation("com.google.android.gms:play-services-cast-framework:${Versions.Android.castFramework}")
    implementation("com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.datetime}")
    implementation("io.coil-kt:coil-compose:${Versions.coil}")

    // testing
    debugImplementation(
        group = "androidx.compose.ui",
        name = "ui-tooling",
        version = Versions.Androidx.Compose.compose
    )
    androidTestImplementation(
        group = "androidx.navigation",
        name = "navigation-testing",
        version = Versions.Androidx.nav
    )
    testImplementation(
        group = "junit",
        name = "junit",
        version = Versions.junit
    )

}
