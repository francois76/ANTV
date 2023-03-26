// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once https://github.com/gradle/gradle/issues/22797 is fixed (should be gradle 8.1)
plugins {
    // kotlin plugins
    alias(libs.plugins.serialization).apply(false)
    kotlin("jvm").version(libs.versions.kotlin).apply(false)

    // android plugins
    alias(libs.plugins.com.android.application).apply(false)
    alias(libs.plugins.com.android.library).apply(false)
}

buildscript {
    dependencies {
        classpath(libs.moko.resources.generator)
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
