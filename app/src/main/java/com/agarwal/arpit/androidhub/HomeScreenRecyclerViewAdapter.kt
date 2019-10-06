package com.agarwal.arpit.androidhub

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.agarwal.arpit.androidhub.daos.FeatureEntity
import com.agarwal.arpit.androidhub.databinding.HomeScreenItemBinding

class HomeScreenRecyclerViewAdapter() : RecyclerView.Adapter<HomeScreenRecyclerViewAdapter.CustomViewHolder>(), View.OnClickListener {

    private var mRecyclerClickInterface: RecyclerClickInterface? = null
    private var mObjectList = listOf<FeatureEntity>();
    private var mContext: Context? = null

    constructor(objectList: List<FeatureEntity>) : this() {
        mObjectList = objectList;
    }

    constructor(objectList: List<FeatureEntity>, interfaceImplemention: RecyclerClickInterface) : this() {
        mObjectList = objectList;
        mRecyclerClickInterface = interfaceImplemention

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //parent.getContext()  -> return the activity context not application
        //Can be also done by passing in the constructor or using Dagger
        mContext = parent.context
        val binding = DataBindingUtil.inflate<HomeScreenItemBinding>(LayoutInflater.from(mContext), R.layout.home_screen_item, parent, false)
        //attachToParent false for recycler view as it is responsible for inflating and displaying views(as per the optimal conditions) not us .
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindingObject.homeScreenItemDao = mObjectList[position]
        holder.bindingObject.parentLayout?.let {
            it.setOnClickListener(this)
            it.tag = position
        }

    }

    override fun onClick(v: View) {
        mRecyclerClickInterface?.onClickAction(v)
    }


    class CustomViewHolder(val bindingObject: HomeScreenItemBinding) : RecyclerView.ViewHolder(bindingObject.root)

    override fun getItemCount() = mObjectList.size
}