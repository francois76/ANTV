package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.darwin.NSObject


@Composable
actual fun OrientationWrapper(
    portrait: @Composable () -> Unit,
    landscape: @Composable () -> Unit
) {
    var isPortrait by remember { mutableStateOf(true) }
    isPortrait = when (UIDevice.currentDevice.orientation) {
        UIDeviceOrientation.UIDeviceOrientationPortrait,UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown ->true;


        UIDeviceOrientation.UIDeviceOrientationLandscapeLeft,UIDeviceOrientation.UIDeviceOrientationLandscapeRight ->false
        else->true;


    }
    if (isPortrait){
        portrait()
    }else{
        landscape()
    }
}