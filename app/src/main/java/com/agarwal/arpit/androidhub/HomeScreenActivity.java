package com.agarwal.arpit.androidhub;

import android.content.Intent;
import android.os.Bundle;

import com.agarwal.arpit.androidhub.daos.HomeScreenItemDao;
import com.agarwal.arpit.androidhub.databinding.HomeScreenBinding;
import com.agarwal.arpit.androidhub.lifecycle.ActivityA;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<HomeScreenItemDao> mAdapterList  = new ArrayList<>();
    private HomeScreenRecyclerViewAdapter adapter;
    private HomeScreenBinding bindingObject ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        ButterKnife.bind(this);

        setUpController();
        sentDataRequest();
    }


    private void setUpController() {

        adapter = new HomeScreenRecyclerViewAdapter(mAdapterList, view -> {
            int position = (int)view.getTag();
            //Todo :: implement click functionality

            switch (position){
                case 0:demonstrateLifecycle();
                    break;
            }

        });

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

    }

    private void demonstrateLifecycle() {
        Intent intent = new Intent(this, ActivityA.class);
        startActivity(intent);
    }


    private void sentDataRequest() {
        mAdapterList.clear();
        mAdapterList.add(new HomeScreenItemDao("Lifecycle","Demonstrates activity & fragment lifecycle"));
        mAdapterList.add(new HomeScreenItemDao("Lifecycle1","Demonstrates activity & fragment lifecycle1"));
        adapter.notifyDataSetChanged();
    }


}
