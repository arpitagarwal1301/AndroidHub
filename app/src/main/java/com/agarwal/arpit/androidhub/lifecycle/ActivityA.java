package com.agarwal.arpit.androidhub.lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.agarwal.arpit.androidhub.R;
import com.agarwal.arpit.androidhub.Utils.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

public class ActivityA extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        Timber.v("onCreate");

        Intent intent = new Intent(this,ActivityB.class);
        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Timber.v("onActivityResult");
        if (true){
            if (requestCode== 0 && resultCode == Activity.RESULT_OK){
                String val = String.valueOf(data.getExtras().getString("result"));
                Utils.showToast(this,val);
            }else if (resultCode == Activity.RESULT_CANCELED){
                Utils.showToast(this,"No result returned for the activity with request code");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.v("onStart");
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Timber.v("onRestoreInstanceState");

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Timber.v("onPostCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.v("onResume");

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Timber.v("onPostResume");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Timber.v("onAttachedToWindow");

    }


    @Override
    protected void onPause() {
        super.onPause();
        Timber.v("onPause");


    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.v("onStop");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.v("onSaveInstanceState");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.v("onDestroy");

    }
}
