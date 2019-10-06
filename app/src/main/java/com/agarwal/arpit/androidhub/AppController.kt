package com.agarwal.arpit.androidhub

import android.app.Application
import android.content.Context
import android.util.Log.INFO
import com.crashlytics.android.Crashlytics
import com.google.android.play.core.splitcompat.SplitCompat
import timber.log.Timber

open class AppController : Application(){


    private lateinit var mInstance: AppController
    private lateinit var sContext: Context

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    @Synchronized
    fun getInstance(): AppController {
        return mInstance
    }

    fun getContext(): Context {
        return sContext
    }

    override fun onCreate() {
        mInstance = this
        sContext = this

        super.onCreate()

        //Initialising the Timber Library
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CustomLogTree())
        }
    }


    /*
    Class for sending crashes to Firebase Crashlytics in case non-debug builds
     */
    private class CustomLogTree : Timber.Tree() {

        override fun isLoggable(tag: String?, priority: Int): Boolean {
            return priority >= INFO
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (isLoggable(tag, priority)) {
                if (null != t) {
                    Crashlytics.logException(t)
                }
                Crashlytics.log(priority, tag, message)
            }
        }


    }



}