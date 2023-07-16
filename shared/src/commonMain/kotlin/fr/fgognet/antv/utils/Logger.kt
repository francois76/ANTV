package fr.fgognet.antv.utils

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier


fun initCommonLogs() {
    Napier.base(DebugAntilog())
}

fun resetLogs() {
    Napier.takeLogarithm()
}