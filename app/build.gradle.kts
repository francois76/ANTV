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
        versionCode = 1
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
    implementation("com.google.android.material:material:${Versions.Android.material}")
    implementation("com.google.android.gms:play-services-cast-framework:${Versions.Android.castFramework}")
    implementation("com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanish}")

    listOf(
        "fragment",
        "ui",
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
    androidTestImplementation(
        group = "androidx.navigation",
        name = "navigation-testing",
        version = Versions.Androidx.nav
    )

    listOf(
        "exoplayer-core",
        "exoplayer-hls",
        "exoplayer-ui",
        "extension-cast",
        "extension-mediasession"
    ).forEach {
        implementation(
            group = "com.google.android.exoplayer",
            name = it,
            version = Versions.Android.exoplayer
        )
    }

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.datetime}")

    // Junit
    testImplementation("junit:junit:${Versions.junit}")

    // Androidx
    implementation("androidx.core:core-ktx:${Versions.Androidx.coreKtx}")
    implementation("androidx.lifecycle:lifecycle-process:${Versions.Androidx.lifecycle}")
    implementation("androidx.appcompat:appcompat:${Versions.Androidx.appCompat}")
    implementation("androidx.activity:activity-compose:${Versions.Androidx.composeActivity}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.Androidx.constraintLayoutCompose}")
    implementation("androidx.compose.runtime:runtime:${Versions.Androidx.compose}")
    implementation("androidx.compose.ui:ui:${Versions.Androidx.compose}")
    implementation("androidx.compose.foundation:foundation:${Versions.Androidx.compose}")
    implementation("androidx.compose.foundation:foundation-layout:${Versions.Androidx.compose}")
    implementation("androidx.compose.ui:ui-viewbinding:${Versions.Androidx.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.Androidx.compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.Androidx.compose}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Androidx.compose}")
    implementation("com.google.android.material:compose-theme-adapter:${Versions.Androidx.composeThemeAdapter}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.Androidx.compose}")
    implementation("androidx.compose.material3:material3:${Versions.Androidx.material3}")
    implementation("androidx.compose.material3:material3-window-size-class:${Versions.Androidx.material3}")
    implementation("io.coil-kt:coil-compose:${Versions.coil}")
    androidTestImplementation("androidx.lifecycle:lifecycle-runtime:${Versions.Androidx.lifecycle}")

}
