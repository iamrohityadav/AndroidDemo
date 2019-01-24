package com.mainli.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mainli.R;
import com.mainli.recyclerview.RViewHolder;
import com.mainli.recyclerview.RecyclerAdapter;

import java.util.Arrays;

/**
 * Created by lixiaoliang on 2018-5-3.
 */
public class CoordinatorLayoutActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new RecyclerAdapter<String>(Arrays.asList("aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh", "aaa", "bb", "ss", "asa", "jhhh")//
                , android.R.layout.activity_list_item) {
            @Override
            public void onBindObject2View(RViewHolder vh, String o, int position) {
                vh.get(android.R.id.icon, ImageView.class).setImageResource(R.mipmap.ic_launcher);
                vh.setText(android.R.id.text1, o);
            }
        });
    }
}
