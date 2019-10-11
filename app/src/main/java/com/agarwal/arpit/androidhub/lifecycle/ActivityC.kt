package com.agarwal.arpit.androidhub.lifecycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.agarwal.arpit.androidhub.R
import com.agarwal.arpit.androidhub.projectutils.BUNDLE_RECEIVED_STRING
import kotlinx.android.synthetic.main.activity_c.*
import timber.log.Timber

class ActivityC : AppCompatActivity(), FragmentCommunication {


    private val TAG = this.javaClass.name
    private var receivedString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)
        Timber.v("onCreate")

        setUpClickListener()
    }

    private fun setUpClickListener() {

        frag_a1_btn.setOnClickListener(View.OnClickListener {
            addFragment(frag_container.getId(), FragmentA1(), false, true)

        })

        frag_a2_btn.setOnClickListener(View.OnClickListener {
            val fragmentA2 = FragmentA2()
            if (!TextUtils.isEmpty(receivedString)) {
                val bundle = Bundle()
                bundle.putString(BUNDLE_RECEIVED_STRING, receivedString)
                fragmentA2.arguments = bundle
            }
            addFragment(frag_container!!.getId(), fragmentA2, true, true)
        })

        reset_btn?.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val i = fragmentManager.backStackEntryCount
            for (j in 0 until i) {
                fragmentManager.popBackStack()
            }
            receivedString = ""

        }

    }

    private fun addFragment(containerId: Int, fragment: Fragment, isReplace: Boolean, addToStack: Boolean) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (isReplace) {
            fragmentTransaction.replace(containerId, fragment)
        } else {
            fragmentTransaction.add(containerId, fragment)
        }

        if (addToStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        Timber.v("onStart")
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.v("onRestoreInstanceState")

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Timber.v("onPostCreate")
    }

    override fun onResume() {
        super.onResume()
        Timber.v("onResume")
    }

    override fun onPostResume() {
        super.onPostResume()
        Timber.v("onPostResume")


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Timber.v("onAttachedToWindow")

    }


    override fun onPause() {
        super.onPause()
        Timber.v("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.v("onStop")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.v("onSaveInstanceState")

    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy")

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val resultIntent = Intent()
            resultIntent.putExtra("result", "Returning from ActivityC")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onActionDone(string: String) {
        receivedString = string
    }

}