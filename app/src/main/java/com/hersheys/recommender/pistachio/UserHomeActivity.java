package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hersheys.recommender.pistachio.NewRatings.OnFragmentInteractionListener;


public class UserHomeActivity extends AppCompatActivity implements GetRecommendationsTab.OnFragmentInteractionListener, RateTab.OnFragmentInteractionListener, UserTab.OnFragmentInteractionListener, MyRatings.OnFragmentInteractionListener, NewRatings.OnFragmentInteractionListener, WatchLater.OnFragmentInteractionListener, Recommended.OnFragmentInteractionListener {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter pAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
