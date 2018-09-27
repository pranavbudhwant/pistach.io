package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RatingPage extends AppCompatActivity {
    ImageButton tick;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_page);
        Toast.makeText(RatingPage.this, "On Ratings Page", Toast.LENGTH_LONG).show();

        //set status bar background to transparent

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //set recyclerview with adapter

        RecyclerView recyclerView = findViewById(R.id.card_list);
        List<item> mList = new ArrayList<>();
        mList.add(new item(R.drawable.daredevil, "Daredevil"));
        mList.add(new item(R.drawable.daredevil, "Daredevil"));
        mList.add(new item(R.drawable.daredevil, "Daredevil"));
        Adapter adapter = new Adapter(this, mList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
