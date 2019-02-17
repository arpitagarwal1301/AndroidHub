package com.agarwal.arpit.androidhub;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

import static android.util.Log.INFO;

public class CustomApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialising the Timber Library
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }else {
            Timber.plant(new CustomLogTree());
        }
    }


    /*
    Class for sending crashes to Firebase Crashlytics in case non-debug builds
     */
    private static final class CustomLogTree extends Timber.Tree {

        @Override
        protected boolean isLoggable(@Nullable String tag, int priority) {
            return priority >= INFO;
        }

        @Override
        protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
            if (isLoggable(tag,priority)){
                if (null!=t){
                    Crashlytics.logException(t);
                }
                Crashlytics.log(priority,tag,message);
            }
        }


    }
}
