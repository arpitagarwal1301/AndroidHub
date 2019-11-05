package com.agarwal.arpit.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import androidx.core.app.ActivityCompat.startActivityForResult


fun Context.openSettingPage(permissionInt: Int) {

    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {

        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        val uri: Uri = Uri.fromParts("package", getPackageName(), null);
        data = uri
        startActivityForResult(this@openSettingPage as Activity, this, permissionInt, null)
    }

}


fun Context.getScreenHeight(): Int {
    val wm = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val point = Point()
    try {
        display.getSize(point)
    } catch (ignore: NoSuchMethodError) { // Older device
        point.x = display.width
        point.y = display.height
    }

    return point.y
}

fun Context.getScreenWidth(): Int {
    val wm = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val point = Point()
    try {
        display.getSize(point)
    } catch (ignore: NoSuchMethodError) { // Older device
        point.x = display.width
        point.y = display.height
    }

    return point.x
}

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = applicationContext.getResources().getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = applicationContext.getResources().getDimensionPixelSize(resourceId)
    }
    return result
}
