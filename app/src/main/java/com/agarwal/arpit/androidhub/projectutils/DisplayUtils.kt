package com.agarwal.arpit.androidhub.projectutils

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.agarwal.arpit.androidhub.AppController

class DisplayUtils  {

    companion object {

        @JvmStatic
        fun showToast( msg: String) {
            Toast.makeText(AppController().getContext(), msg, Toast.LENGTH_SHORT).show()
        }

        @JvmStatic
        fun getColorWrapper(colorInt : Int) =
             ContextCompat.getColor(AppController().getContext(),colorInt)

        @JvmStatic
        fun getStringWrapper(stringInt : Int) =
                AppController().getContext().getString(stringInt)


    }

}