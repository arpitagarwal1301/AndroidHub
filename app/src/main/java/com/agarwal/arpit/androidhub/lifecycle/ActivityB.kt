package com.agarwal.arpit.androidhub.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agarwal.arpit.androidhub.R


class ActivityB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)
    }


}
