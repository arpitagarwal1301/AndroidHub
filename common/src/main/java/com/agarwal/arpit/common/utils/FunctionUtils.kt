package com.agarwal.arpit.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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