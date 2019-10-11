package com.agarwal.arpit.androidhub

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat

open class BaseSplitActivity : AppCompatActivity(){

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

}