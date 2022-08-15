
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android").version(Versions.kotlin)
}


android {
    compileSdk =  Versions.Android.compileSdk

    defaultConfig {
        applicationId = "fr.fgognet.antv"
        minSdk = Versions.Android.minSdk
        targetSdk = Versions.Android.targetSdk
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
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("com.google.android.material:material:1.6.1")
    implementation("com.google.android.gms:play-services-cast-framework:21.1.0")
    implementation("androidx.lifecycle:lifecycle-process:2.5.1")
    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // exoplayer
    implementation("com.google.android.exoplayer:exoplayer-core:${Versions.exoplayer}")
    implementation("com.google.android.exoplayer:exoplayer-hls:${Versions.exoplayer}")
    implementation("com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayer}")
    implementation("com.google.android.exoplayer:extension-cast:${Versions.exoplayer}")
    implementation("com.google.android.exoplayer:extension-mediasession:${Versions.exoplayer}")

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
    testImplementation("junit:junit:4.13.2")
}