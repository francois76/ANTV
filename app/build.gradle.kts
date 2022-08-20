plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android").version(Versions.kotlin)
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

            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                "$project.rootDir/tools/proguard-rules.pro"
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
    implementation("com.google.android.material:material:${Versions.Android.material}")
    implementation("com.google.android.gms:play-services-cast-framework:${Versions.Android.castFramework}")


    implementation("androidx.core:core-ktx:${Versions.Androidx.coreKtx}")

    implementation("androidx.lifecycle:lifecycle-process:${Versions.Androidx.lifecycle}")
    androidTestImplementation("androidx.lifecycle:lifecycle-runtime:${Versions.Androidx.lifecycle}")

    implementation("androidx.appcompat:appcompat:${Versions.Androidx.appCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.Androidx.constraintLayout}")
    listOf(
        "fragment",
        "ui",
        "fragment-ktx",
        "ui-ktx",
        "dynamic-features-fragment",
        "compose"
    ).forEach {
        implementation("androidx.navigation", "navigation-$it", Versions.Androidx.nav)
    }
    androidTestImplementation("androidx.navigation:navigation-testing:${Versions.Androidx.nav}")

    listOf(
        "exoplayer-core",
        "exoplayer-hls",
        "exoplayer-ui",
        "extension-cast",
        "extension-mediasession"
    ).forEach {
        implementation("com.google.android.exoplayer", it, Versions.Android.exoplayer)
    }

    implementation("com.soywiz.korlibs.korim:korim:${Versions.kor}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.datetime}")

    // Junit
    testImplementation("junit:junit:${Versions.junit}")
}