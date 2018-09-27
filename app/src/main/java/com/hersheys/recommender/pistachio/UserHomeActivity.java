package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabItem;
import android.widget.Toast;


public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        TabLayout tab_bar = new TabLayout(UserHomeActivity.this);
        tab_bar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        break;

                    case 1:
                        Intent RatingIntent = new Intent(UserHomeActivity.this, RatingPage.class);
                        Toast.makeText(UserHomeActivity.this,"Going To Ratings Page",Toast.LENGTH_LONG).show();

                        UserHomeActivity.this.startActivity(RatingIntent);
                        break;

                    case 2:
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
