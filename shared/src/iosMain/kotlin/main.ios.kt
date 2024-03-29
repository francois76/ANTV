/*
 * Copyright 2020-2021 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import fr.fgognet.antv.utils.initCommonLogs
import fr.fgognet.antv.view.main.ANTVApp
import platform.UIKit.*
import platform.UIKit.UIViewController

@Suppress("FunctionNaming")
fun MainViewController(): UIViewController = ComposeUIViewController {
    ANTVApp(initialRoute = null,backHandler = {
        it()
    })
}

fun DebugBuild() {
    initCommonLogs()
}