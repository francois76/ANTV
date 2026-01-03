// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    listOf(
        libs.plugins.jvm,
        libs.plugins.multiplatform,
        libs.plugins.android,
        libs.plugins.com.android.application,
        libs.plugins.com.android.library,
        libs.plugins.serialization,
        libs.plugins.google.services,
        libs.plugins.org.jetbrains.compose,
        libs.plugins.compose.compiler,
    ).forEach {
        alias(it).apply(false)
    }
}




buildscript {
    dependencies {
        classpath(libs.moko.resources.generator)
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
