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
        maven {
            url = uri("https://repo.repsy.io/mvn/chrynan/public")
        }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        // application dependencies
        create("antvLibs") {
            from(files("../ANTV/gradle/antv.versions.toml"))
        }


    }
}

rootProject.name = "ANTV"

include(":androidApp")
include(":shared")
include(":desktopApp")
