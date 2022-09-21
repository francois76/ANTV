// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.plugin.serialization").version(Versions.kotlin)
    id("com.android.application").version(Versions.androidPlugin).apply(false)
    id("com.android.library").version(Versions.androidPlugin).apply(false)
    kotlin("jvm").version(Versions.kotlin).apply(false)
}

buildscript {
    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath(
            group = "dev.icerock.moko",
            name = "resources-generator",
            version = Versions.Moko.resources
        )
    }
}





task<Delete>("clean") {
    delete(rootProject.buildDir)
}
