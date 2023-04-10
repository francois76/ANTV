// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once https://github.com/gradle/gradle/issues/22797 is fixed (should be gradle 8.1)
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("jvm").version(libs.versions.kotlin).apply(false)
    kotlin("multiplatform").version(libs.versions.kotlin).apply(false)
    kotlin("android").version(libs.versions.kotlin).apply(false)
    alias(libs.plugins.com.android.application).apply(false)
    alias(libs.plugins.com.android.library).apply(false)
    alias(libs.plugins.serialization).apply(false)
    alias(libs.plugins.org.jetbrains.compose).apply(false)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

buildscript {
    dependencies {
        classpath(libs.moko.resources.generator)
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
