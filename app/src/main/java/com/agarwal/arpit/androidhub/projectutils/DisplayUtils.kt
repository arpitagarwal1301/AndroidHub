package com.agarwal.arpit.androidhub.projectutils

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.agarwal.arpit.androidhub.AppController


fun showToast(msg:String){
    Toast.makeText(AppController.getContext(), msg, Toast.LENGTH_SHORT).show()
}

fun getColorWrapper(colorInt : Int):Int{
    return ContextCompat.getColor(AppController.getContext(),colorInt)
}

fun getStringWrapper(stringInt : Int):String{
    return AppController.getContext().getString(stringInt)
}
