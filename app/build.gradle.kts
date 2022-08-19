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
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
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
    implementation("androidx.core:core-ktx:${Versions.Androidx.coreKtx}")
    implementation("com.google.android.material:material:${Versions.Android.material}")
    implementation("com.google.android.gms:play-services-cast-framework:${Versions.Android.castFramework}")
    implementation("androidx.lifecycle:lifecycle-process:${Versions.Androidx.lifecycle}")
    implementation("androidx.appcompat:appcompat:${Versions.Androidx.appCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.Androidx.constraintLayout}")
    // exoplayer
    implementation("com.google.android.exoplayer:exoplayer-core:${Versions.Android.exoplayer}")
    implementation("com.google.android.exoplayer:exoplayer-hls:${Versions.Android.exoplayer}")
    implementation("com.google.android.exoplayer:exoplayer-ui:${Versions.Android.exoplayer}")
    implementation("com.google.android.exoplayer:extension-cast:${Versions.Android.exoplayer}")
    implementation("com.google.android.exoplayer:extension-mediasession:${Versions.Android.exoplayer}")

    // navigation
    implementation("androidx.navigation:navigation-fragment:${Versions.Androidx.nav}")
    implementation("androidx.navigation:navigation-ui:${Versions.Androidx.nav}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.Androidx.nav}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.Androidx.nav}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${Versions.Androidx.nav}")
    androidTestImplementation("androidx.navigation:navigation-testing:${Versions.Androidx.nav}")
    implementation("androidx.navigation:navigation-compose:${Versions.Androidx.nav}")

    implementation("com.soywiz.korlibs.korim:korim:${Versions.kor}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.datetime}")

    // Junit
    testImplementation("junit:junit:${Versions.junit}")
}