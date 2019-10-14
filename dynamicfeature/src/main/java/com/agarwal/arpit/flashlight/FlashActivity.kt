package com.agarwal.arpit.flashlight

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.agarwal.arpit.androidhub.BaseSplitActivity
import com.agarwal.arpit.androidhub.flashit.Time
import com.agarwal.arpit.common.utils.getStringWrapper
import com.agarwal.arpit.common.utils.showToast
import kotlinx.android.synthetic.main.activity_flash_it.*

private const val VALUE_ZERO = "0"

class FlashActivity : BaseSplitActivity() {

    private var isFlashOn = false
    private var freq: Int = 0
    private var t: Thread? = null
    private var stroboRunner: StroboRunner? = null
    private var stopFlicker = false
    lateinit var mContext: Context

    private var mCameraManager: CameraManager? = null
    private var cameraId: String? = null
    private val timeVar = Time()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_it)
        mContext = this

        checkForResources()
        seekBarMethod()
    }


    private fun checkForResources() {
        val pm = mContext.packageManager

        /*Checking availability of required camera hardware*/
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            showToast(getStringWrapper(R.string.NO_CAMERA_ERROR))
            return
        } else {
            //            initialiseResources();
            initialiseResourcesCamera2()
            setToggleButtonBehaviour()
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initialiseResourcesCamera2() {

        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        //        mCameraManager.registerTorchCallback(torchCallback, null);// (callback, handler)
        try {
            cameraId = mCameraManager!!.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        //        checkForCameraPermission();
    }


    private fun setFlashOn(enable: Boolean?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager!!.setTorchMode(cameraId!!, enable!!)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }

        }
    }


    private fun setToggleButtonBehaviour() {

        //Using toggle button with background image
        imageToggleButton.setOnCheckedChangeListener { _, isChecked ->
            isFlashOn = isChecked
            if (!isChecked) {
                // The toggle is enabled
                Log.i("info", "torch is turned off!")
                setFlashOn(false)
                isFlashOn = false
                seekBar.progress = 0
                textView_progress.text = VALUE_ZERO
                if (t != null) {
                    stopFlicker = true
                    t = null
                }
            } else {
                // The toggle is disabled
                Log.i("info", "torch is turned on!")
                setFlashOn(true)
                isFlashOn = true
                stopFlicker = false
            }
        }
    }

    private fun seekBarMethod() {
        /*SeekBar which will indicate value of brightness of torch*/
        seekBar.max = 10
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            internal var progressValue: Int = 0

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressValue = progress
                textView_progress.text = progressValue.toString() + ""

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                textView_progress.text = progressValue.toString() + ""
                flashFlicker()
            }
        })
    }

    private fun flashFlicker() {
        if (isFlashOn) {
            freq = seekBar.progress
            timeVar.setSleepTime(freq)
            stroboRunner = StroboRunner()
            t = Thread(stroboRunner)
            t!!.start()
            return
        } else {
            showToast(getStringWrapper(R.string.SWICH_FLASH_ON))
            seekBar.progress = 0
            textView_progress.text = VALUE_ZERO
        }
    }

    private inner class StroboRunner : Runnable {

        override fun run() {
            try {
                while (!stopFlicker) {

                    if (freq != 0) {
                        setFlashOn(true)
                        Thread.sleep(timeVar.sleepTime)
                        setFlashOn(false)
                        Thread.sleep(timeVar.sleepTime)
                    } else {
                        setFlashOn(true)
                    }
                }

            } catch (e: Exception) {
                e.stackTrace
            }

        }
    }

    /*Releasing camera resources*/
    private fun releaseCamera() {
        setFlashOn(false)
        seekBar.progress = 0
        isFlashOn = false
        textView_progress.text = VALUE_ZERO
        imageToggleButton.isChecked = false
    }


    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }


}