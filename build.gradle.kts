// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.plugin.serialization").version("1.7.10")
    id("com.android.application").version("7.2.2").apply(false)
    id("com.android.library").version ("7.2.2"). apply (false)
}


task<Delete>("clean") {
    delete(rootProject.buildDir)
}