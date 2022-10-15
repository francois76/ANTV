// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // kotlin plugins
    id("org.jetbrains.kotlin.plugin.serialization").version("1.7.10").apply(false)
    kotlin("jvm").version("1.7.10").apply(false)

    // android plugins
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
}

buildscript {
    dependencies {
        classpath(libs.moko.resources.generator)
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
