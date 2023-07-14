package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

enum class Platform {
    ANDROID,
    IOS,
    JAVA,
}

expect class PlatformContext{
    fun getPlatform() :Platform
}

@Composable
expect fun getPlatformContext(): PlatformContext