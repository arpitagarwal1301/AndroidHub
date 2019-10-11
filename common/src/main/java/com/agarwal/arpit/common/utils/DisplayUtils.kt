package com.agarwal.arpit.common.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat


fun Context.showToast(msg:String){
    Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()
}

fun Context.getColorWrapper(colorInt : Int):Int{
    return ContextCompat.getColor(this.applicationContext,colorInt)
}

fun Context.getStringWrapper(stringInt : Int):String{
    return this.applicationContext.getString(stringInt)
}
