pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        // application dependancies
        create("antvLibs") {
            version("version", "1.1.2")
            version("versionNumber", "11")
            version("sdk-compile", "33")
            version("sdk-min", "26")
            version("sdk-target", "33")
        }

        
    }
}

rootProject.name = "ANTV"

include(":androidApp")
include(":shared")
include(":desktopApp")
