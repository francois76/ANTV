import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    jvm {
    }
    @Suppress("UnusedPrivateMember", "UNUSED_VARIABLE") // False positive
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "fr.fgognet.antv.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ANTV"
            packageVersion = "1.0.0"

            macOS {
                dockName = "ANTV"
                bundleID = "fr.fgognet.antv.antv"
            }

            windows {
                menuGroup = "ANTV"
                // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "5ac63736-d8c7-4a65-a66b-6870df88ddfe"
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = antvLibs.versions.antv.packagename.get()
    disableStaticFrameworkWarning = true
}
