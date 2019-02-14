package com.agarwal.arpit.androidhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agarwal.arpit.androidhub.daos.HomeScreenItemDao;
import com.agarwal.arpit.androidhub.databinding.HomeScreenItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class HomeScreenRecyclerViewAdapter extends RecyclerView.Adapter<HomeScreenRecyclerViewAdapter.CustomViewHolder> implements View.OnClickListener {

    private final List<HomeScreenItemDao> mObjectList ;
    private final RecyclerClickInterface mClickInterface;
    private Context mActivityContext = null;

    public HomeScreenRecyclerViewAdapter(List<HomeScreenItemDao> objectList) {
        mObjectList = objectList;
        mClickInterface = null;
    }

    public HomeScreenRecyclerViewAdapter(List<HomeScreenItemDao> objectList,RecyclerClickInterface interfaceImplementaion) {
        mObjectList = objectList;
        mClickInterface = interfaceImplementaion;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //parent.getContext()  -> return the activity context not application
        //Can be also done by passing in the constructor or using Dagger
        mActivityContext = parent.getContext();
        HomeScreenItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mActivityContext),R.layout.home_screen_item,parent,false);
        //attachToParent false for recycler view as it is responsible for inflating and displaying views(as per the optimal conditions) not us .
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        //Set the dao from which the data will be filled
        holder.mBindingObject.setHomeScreenItemDao(mObjectList.get(position));
        //holder.mBindingObject.itemName -- No need to set text here as we are doing by dataBinding

        //TODO :: implement this using dataBinding
        holder.mBindingObject.parentLayout.setOnClickListener(this);
        holder.mBindingObject.parentLayout.setTag(position);  //To get the list item from view

    }

    @Override
    public void onClick(View v) {
        //Problem : 2 different activity/fragment have different implementation on click ??
        //Sol : Get the implementation from the activity/fragment itself.
        mClickInterface.onClickAction(v);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        private final HomeScreenItemBinding mBindingObject;
        public CustomViewHolder(@NonNull HomeScreenItemBinding bindingObject) {
            super(bindingObject.getRoot());
            mBindingObject = bindingObject;
        }
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }


}
