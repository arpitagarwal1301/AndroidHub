package com.agarwal.arpit.common.logging

import androidx.annotation.Nullable
import com.crashlytics.android.Crashlytics
import timber.log.Timber


class HubLog {

    companion object {
        private val DEFAULT_TAG = "HubLog"

        private lateinit var mLogFacade: LoggerFacade
        private var mIsDebug : Boolean = true

        fun init(b: Boolean) {
            mLogFacade = LoggerFacade(mIsDebug)
            mIsDebug = b
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


    class LoggerFacade internal constructor(mIsDebug: Boolean) {

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

            if (mIsDebug){
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