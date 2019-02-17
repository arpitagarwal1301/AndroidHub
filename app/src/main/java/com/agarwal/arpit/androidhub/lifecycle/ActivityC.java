package com.agarwal.arpit.androidhub.lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.agarwal.arpit.androidhub.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.agarwal.arpit.androidhub.Utils.StringConstants.RECEIVED_STRING;

public class ActivityC extends AppCompatActivity implements FragmentCommunication{

    @BindView(R.id.frag_container)
    FrameLayout frameLayoutContainer;

    @BindView(R.id.frag_a1_btn)
    Button fragA1Btn;

    @BindView(R.id.frag_a2_btn)
    Button fragA2Btn;

    @BindView(R.id.reset_btn)
    Button resetBtn;

    private final String TAG = this.getClass().getName();
    private String receivedString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        ButterKnife.bind(this);
        Timber.v("onCreate");

        setUpClickListener();
    }

    private void setUpClickListener() {
        fragA1Btn.setOnClickListener(v -> {
            addFragment(frameLayoutContainer.getId(),new FragmentA1(),false,true);
        });

        fragA2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentA2 fragmentA2 = new FragmentA2();
                if (!TextUtils.isEmpty(receivedString)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(RECEIVED_STRING, receivedString);
                    fragmentA2.setArguments(bundle);
                }
                addFragment(frameLayoutContainer.getId(), fragmentA2, true, true);
            }
        });

        resetBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int i = fragmentManager.getBackStackEntryCount();
            for (int j = 0; j < i; j++) {
                fragmentManager.popBackStack();
            }

        });
    }

    private void addFragment(final int containerId, final Fragment fragment,boolean isReplace,boolean addToStack){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isReplace){
            fragmentTransaction.replace(containerId,fragment);
        }else {
            fragmentTransaction.add(containerId,fragment);
        }

        if (addToStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.v("onStart");
    }


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result","Returning from ActivityC");
            setResult(Activity.RESULT_OK,resultIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActionDone(String string) {
        receivedString = string;
    }
}
