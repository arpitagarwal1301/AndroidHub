package com.agarwal.arpit.androidhub

import android.content.Context
import com.agarwal.arpit.common.logging.HubLog
import com.google.android.play.core.splitcompat.SplitCompatApplication

class AppController : SplitCompatApplication() {

    companion object {
        private lateinit var mInstance: AppController
        private lateinit var sContext: Context


        @Synchronized
        fun getInstance(): AppController {
            return mInstance
        }

        fun getContext(): Context {
            return sContext
        }
    }


    override fun onCreate() {
        mInstance = this
        sContext = this

        super.onCreate()

        HubLog.init(BuildConfig.DEBUG)

    }



}