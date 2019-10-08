package com.agarwal.arpit.androidhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.agarwal.arpit.androidhub.databinding.HomeScreenBinding
import com.agarwal.arpit.androidhub.entities.FeatureEntity
import com.agarwal.arpit.androidhub.lifecycle.ActivityA
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.home_screen.*
import java.util.*

private const val packageName = "com.agarwal.arpit.androidhub.flashit"
private const val flashActivity = "$packageName.FlashActivity"

class HomeScreenActivity : AppCompatActivity() {

    private val mAdapterList = ArrayList<FeatureEntity>()
    private lateinit var adapter: HomeScreenRecyclerViewAdapter
    private lateinit var bindingObject: HomeScreenBinding


    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->

            val multiInstall = state.moduleNames().size > 1
            val names = state.moduleNames().joinToString { "-" }
            when(state.status()){
                SplitInstallSessionStatus.DOWNLOADING -> {
                    //  In order to see this, the application has to be uploaded to the Play Store.
                    TODO()
                }
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingObject = DataBindingUtil.setContentView(this, R.layout.home_screen)

        setUpController()
        sentDataRequest()
    }


    private fun setUpController() {

        adapter = HomeScreenRecyclerViewAdapter(mAdapterList, object : RecyclerClickInterface {
            override fun onClickAction(view: View) {
                val position = view.getTag() as Int
                when (position) {
                    0 -> demonstrateLifecycle()
                    1 -> launchActivity(flashActivity)
                }
            }
        })


        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.setHasFixedSize(false)
        recycler_view.adapter = adapter

    }

    private fun demonstrateLifecycle() {
        val intent = Intent(this, ActivityA::class.java)
        startActivity(intent)
    }


    private fun sentDataRequest() {
        mAdapterList.clear()
        mAdapterList.add(FeatureEntity("Lifecycle", "Demonstrates activity & fragment lifecycle"))
        mAdapterList.add(FeatureEntity("Flash", "Pub Flash Light"))
        adapter.notifyDataSetChanged()
    }

    /** Launch an activity by its class name. */
    private fun launchActivity(className: String) {
        Intent().setClassName(packageName, className)
                .also {
                    startActivity(it)
                }
    }


}