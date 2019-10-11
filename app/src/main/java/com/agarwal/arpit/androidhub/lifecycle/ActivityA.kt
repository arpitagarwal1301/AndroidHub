package com.agarwal.arpit.androidhub.lifecycle


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agarwal.arpit.androidhub.R
import com.agarwal.arpit.common.logging.HubLog
import com.agarwal.arpit.common.utils.showToast
import kotlinx.android.synthetic.main.activity_a.*

class ActivityA : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        HubLog.v("onCreate")


        activitya_btn.setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            startActivity(intent)
        }

        activityb_btn.setOnClickListener {
            val intent = Intent(this, ActivityB::class.java)
            startActivityForResult(intent, 0)
        }

        activityc_btn.setOnClickListener {
            val intent = Intent(this, ActivityC::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        HubLog.v("onActivityResult")
        if (true) {
            if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
                val `val` = data!!.extras!!.getString("result").toString()
                showToast(`val`)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showToast("No result returned for the activity with request code")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        HubLog.v("onStart")
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        HubLog.v("onRestoreInstanceState")

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        HubLog.v("onPostCreate")
    }

    override fun onResume() {
        super.onResume()
        HubLog.v("onResume")

    }

    override fun onPostResume() {
        super.onPostResume()
        HubLog.v("onPostResume")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        HubLog.v("onAttachedToWindow")

    }


    override fun onPause() {
        super.onPause()
        HubLog.v("onPause")


    }

    override fun onStop() {
        super.onStop()
        HubLog.v("onStop")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        HubLog.v("onSaveInstanceState")

    }

    override fun onDestroy() {
        super.onDestroy()
        HubLog.v("onDestroy")

    }
}
