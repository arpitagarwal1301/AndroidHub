package com.agarwal.arpit.androidhub.lifecycle;

import android.os.Bundle;

import com.agarwal.arpit.androidhub.R;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
    }
}
