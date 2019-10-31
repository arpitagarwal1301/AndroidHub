package com.agarwal.arpit.common.logging

import androidx.annotation.Nullable
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/*
Supplying tag instead of taking tag of the class/fragment because :
1. It is done using reflection which is a heavy operation
2. During obfuscation the name change so instead we should provide string tag or put tag in the message itself

In case this is the requirement then iterate the stack trace and if class name is not equal to  "HubLog,LoggerFacade,Thread"  then return the class .
Also you can use Pattern matcher to check for abnormal class names .

StackTrace :

This Highly depends on what you are looking for... But this should get the class and method that called this method within this object directly.

index 0 = Thread
index 1 = this
index 2 = direct caller, can be self.
index 3 ... n = classes and methods that called each other to get to the index 2 and below.
 */

private const val DEFAULT_TAG = "HubLog"

class HubLog {

    companion object {

        private lateinit var mLogFacade: LoggerFacade

        fun init(b: Boolean) {
            mLogFacade = LoggerFacade(b)
        }

        /**
         * Error
         */
        fun e(message: String, vararg args: Any) {
            mLogFacade.e(message, *args)
        }

        fun e(t: Throwable, message: String, vararg args: Any) {
            mLogFacade.e(t, message, *args)
        }

        /**
         * Warn
         */
        fun w(message: String, vararg args: Any) {
            mLogFacade.w(message, *args)
        }

        fun w(t: Throwable, message: String, vararg args: Any) {
            mLogFacade.w(t, message, *args)
        }

        /**
         * Info
         */
        fun i(message: String, vararg args: Any) {
            mLogFacade.i(message, *args)
        }

        fun i(t: Throwable, message: String, vararg args: Any) {
            mLogFacade.i(t, message, *args)
        }

        /**
         * Debug
         */
        fun d(message: String, vararg args: Any) {
            mLogFacade.d(message, *args)
        }

        fun d(t: Throwable, message: String, vararg args: Any) {
            mLogFacade.d(t, message, *args)
        }

        /**
         * Verbose
         */
        fun v(message: String, vararg args: Any) {
            mLogFacade.v(message, *args)
        }

        fun v(t: Throwable, message: String, vararg args: Any) {
            mLogFacade.v(t, message, *args)
        }

        fun tag(tag: String): LoggerFacade {
            mLogFacade.tag = tag
            return mLogFacade
        }

    }


    class LoggerFacade internal constructor(isDebug: Boolean) {

        private val providedTag = ThreadLocal<String>()

        internal var tag: String
            @Nullable
            get() {
                var tag: String? = providedTag.get()
                if (tag != null) {
                    providedTag.remove()
                } else {
                    tag = DEFAULT_TAG
                }
                return tag
            }
            set(tag) = providedTag.set(tag)

        init {

            if (isDebug){
                Timber.plant(Timber.DebugTree())
            }else{
                Timber.plant(CrashlyticsLogTree())
            }
        }

        fun e(message: String, vararg args: Any) {
            Timber.tag(tag).e(message, *args)
        }

        fun e(t: Throwable, message: String, vararg args: Any) {
            Timber.tag(tag).e(t, message, *args)
        }

        fun w(message: String, vararg args: Any) {
            Timber.tag(tag).w(message, *args)
        }

        fun w(t: Throwable, message: String, vararg args: Any) {
            Timber.tag(tag).w(t, message, *args)
        }

        fun i(message: String, vararg args: Any) {
            Timber.tag(tag).i(message, *args)
        }

        fun i(t: Throwable, message: String, vararg args: Any) {
            Timber.tag(tag).i(t, message, *args)
        }

        fun d(message: String, vararg args: Any) {
            Timber.tag(tag).d(message, *args)
        }

        fun d(t: Throwable, message: String, vararg args: Any) {
            Timber.tag(tag).d(t, message, *args)
        }

        fun v(message: String, vararg args: Any) {
            Timber.tag(tag).v(message, *args)
        }

        fun v(t: Throwable, message: String, vararg args: Any) {
            Timber.tag(tag).v(t, message, *args)
        }


    }


    private class CrashlyticsLogTree : Timber.Tree() {

        override fun isLoggable(tag: String?, priority: Int): Boolean {
            return priority >= android.util.Log.WARN
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