package com.agarwal.arpit.androidhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agarwal.arpit.androidhub.daos.FeatureEntity
import com.agarwal.arpit.androidhub.databinding.HomeScreenBinding
import com.agarwal.arpit.androidhub.lifecycle.ActivityA
import kotlinx.android.synthetic.main.home_screen.*
import java.util.*

class HomeScreenActivity : AppCompatActivity() {


    private val mAdapterList = ArrayList<FeatureEntity>()
    private lateinit var adapter: HomeScreenRecyclerViewAdapter
    private val bindingObject: HomeScreenBinding? = null //TODO :: get the object and access view directly

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        setUpController()
        sentDataRequest()
    }


    private fun setUpController() {

        adapter = HomeScreenRecyclerViewAdapter(mAdapterList, object : RecyclerClickInterface {
            override fun onClickAction(view: View) {
                val position = view.getTag() as Int
                //Todo :: implement click functionality

                when (position) {
                    0 -> demonstrateLifecycle()
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
        mAdapterList.add(FeatureEntity("Lifecycle1", "Demonstrates activity & fragment lifecycle1"))
        adapter.notifyDataSetChanged()
    }


}