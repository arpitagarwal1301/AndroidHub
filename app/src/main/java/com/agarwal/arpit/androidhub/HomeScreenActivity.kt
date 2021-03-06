package com.agarwal.arpit.androidhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agarwal.arpit.androidhub.databinding.ActivityHomeScreenBinding
import com.agarwal.arpit.androidhub.entities.FeatureEntity
import com.agarwal.arpit.common.utils.getStringWrapper
import com.agarwal.arpit.common.utils.showToast
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_home_screen.*
import java.util.*

private const val packageNameApp = "com.agarwal.arpit.androidhub"
private const val packageNameFlashLight = "com.agarwal.arpit.flashlight"
private const val packageNameFirebase = "com.agarwal.arpit.firebaseml"
private const val activityA = "$packageNameApp.lifecycle.ActivityA"
private const val mapsActivity = "$packageNameApp.googlemaps.MapsActivity"
private const val flashActivity = "$packageNameFlashLight.FlashActivity"
private const val firebaseActivity = "$packageNameApp.firebaseml.FirebaseMLActivity"

class HomeScreenActivity : AppCompatActivity() {

    private val mAdapterList = ArrayList<FeatureEntity>()
    private lateinit var mAdapter: HomeScreenRecyclerViewAdapter
    private lateinit var mBindingObject: ActivityHomeScreenBinding
    private lateinit var mSplitInstallManager: SplitInstallManager

    private val moduleFlashLight by lazy { getStringWrapper(R.string.module_feature_flash_light) }

    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->

        val multiInstall = state.moduleNames().size > 1
        val names = state.moduleNames().joinToString { "-" }
        when (state.status()) {

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                /*
             This may occur when attempting to download a sufficiently large module.

             In order to see this, the application has to be uploaded to the Play Store.
             Then features can be requested until the confirmation path is triggered.
            */
                startIntentSender(state.resolutionIntent()?.intentSender, null, 0, 0, 0)
            }

            SplitInstallSessionStatus.DOWNLOADING -> {
                //  In order to see this, the application has to be uploaded to the Play Store.

                showProgressBar()
                updateProgressMessage("Downloading : ${state.bytesDownloaded()}/${state.totalBytesToDownload()}")
            }

            SplitInstallSessionStatus.DOWNLOADED -> {
                showToast("DOWNLOADED")
                hideProgressBar()
            }

            SplitInstallSessionStatus.INSTALLED -> {
                showToast("INSTALLED")
                onSuccessfulLoad(names, launch = !multiInstall)
            }

            SplitInstallSessionStatus.INSTALLING -> {
                showProgressBar()
                updateProgressMessage("Installing : ${state.bytesDownloaded()}/${state.totalBytesToDownload()}")
            }



            SplitInstallSessionStatus.FAILED -> {
                showToast("Failed")
                updateProgressMessage("Failed")
                hideProgressBar()
            }

            SplitInstallSessionStatus.CANCELING -> {
                showProgressBar()
                updateProgressMessage("Cancelling")
            }

            SplitInstallSessionStatus.CANCELED -> {
                hideProgressBar()
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindingObject = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        mSplitInstallManager = SplitInstallManagerFactory.create(this)

        setUpController()
        sentDataRequest()
    }

    override fun onResume() {
        // Listener can be registered even without directly triggering a download.
        super.onResume()
        mSplitInstallManager.registerListener(listener)

    }

    private fun setUpController() {

        mAdapter = HomeScreenRecyclerViewAdapter(mAdapterList, object : RecyclerClickInterface {
            override fun onClickAction(view: View) {
                val position = view.getTag() as Int
                when (position) {
                    0 -> launchActivity(activityA)
                    1 -> loadAndLaunchModule(moduleFlashLight)
                    2 -> launchActivity(mapsActivity)
                    3 -> showToast("coming soon...")
                }
            }
        })


        recycler_view.layoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager?
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter

    }

    private fun sentDataRequest() {

        mAdapterList.clear()
        mAdapterList.add(FeatureEntity("Lifecycle", "Demonstrates activity & fragment lifecycle"))
        mAdapterList.add(FeatureEntity("Flash", "Pub Flash Light"))
        mAdapterList.add(FeatureEntity("Maps", "Google Maps"))
        mAdapterList.add(FeatureEntity("FirebaseML", "Face,Object,Text Detections"))
        mAdapter.notifyDataSetChanged()
    }

    /**
     * Load a feature by module name.
     * @param name The name of the feature module to load.
     */
    private fun loadAndLaunchModule(name: String) {
        updateProgressMessage("Loading module $name")
        // Skip loading if the module already is installed. Perform success action directly.
        if (mSplitInstallManager.installedModules.contains(name)) {
            updateProgressMessage("Already installed")
            onSuccessfulLoad(name, launch = true)
            return
        }

        // Create request to install a feature module by name.
        val request = SplitInstallRequest.newBuilder()
                .addModule(name)
                .build()

        // Load and install the requested feature module.
        mSplitInstallManager.startInstall(request)

        updateProgressMessage("Starting install for $name")
    }

    private fun onSuccessfulLoad(name: String, launch: Boolean) {
        if(launch){
            when(name){
                moduleFlashLight -> launchActivity(flashActivity)
            }
        }
        hideProgressBar()
    }

    override fun onPause() {
        mSplitInstallManager.unregisterListener(listener)
        super.onPause()
    }


    private fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    private fun updateProgressMessage(s: String) {
        if (progress_bar.visibility != View.VISIBLE)
            return
        progress_text.text = s;
    }

    private fun hideProgressBar() {
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
    }

    /** Launch an activity by its class name. */
    private fun launchActivity(className: String) {
        Intent().setClassName(packageName, className)
                .also {
                    startActivity(it)
                }
    }


}