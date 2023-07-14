/*
 * Copyright 2020-2021 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

import androidx.compose.ui.window.ComposeUIViewController
import fr.fgognet.antv.view.main.ANTVApp
import platform.UIKit.UIViewController
import platform.UIKit.UINavigationController
import platform.UIKit.UIInterfaceOrientation

fun MainViewController(): UIViewController = ComposeUIViewController {
    ANTVApp(backHandler = {

    })
}