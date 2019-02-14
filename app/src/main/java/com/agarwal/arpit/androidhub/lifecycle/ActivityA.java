package com.agarwal.arpit.androidhub.lifecycle;

import android.content.Intent;
import android.os.Bundle;

import com.agarwal.arpit.androidhub.R;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        Intent intent = new Intent(this, ActivityB.class);
        startActivity(intent);
    }


}
