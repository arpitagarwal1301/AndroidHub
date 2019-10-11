package com.agarwal.arpit.androidhub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Intent(this, HomeScreenActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
