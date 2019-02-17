package com.agarwal.arpit.androidhub.Utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    public static void showToast(Context screenContext, String msg){
        Toast.makeText(screenContext,msg,Toast.LENGTH_SHORT).show();
    }
}
