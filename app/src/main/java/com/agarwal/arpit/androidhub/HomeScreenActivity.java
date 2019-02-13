package com.agarwal.arpit.androidhub;

import android.os.Bundle;
import android.view.View;

import com.agarwal.arpit.androidhub.daos.HomeScreenItemDao;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class HomeScreenActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<HomeScreenItemDao> mAdapterList  = new ArrayList<>();
    private HomeScreenRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        setUpController();
        sentDataRequest();
    }


    private void setUpController() {

        adapter = new HomeScreenRecyclerViewAdapter(mAdapterList, this, new RecyclerClickInterface() {
            @Override
            public void onClickAction(View view) {
                int position = (int)view.getTag();
                //Todo :: implement click functionality

            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);

    }


    private void sentDataRequest() {
        mAdapterList.clear();
        mAdapterList.add(new HomeScreenItemDao("Lifecycle","Demonstrates activity & fragment lifecycle"));
        adapter.notifyDataSetChanged();
    }


}
