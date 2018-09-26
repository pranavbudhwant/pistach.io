package com.hersheys.recommender.pistachio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RatingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_page);
        Toast.makeText(RatingPage.this,"On Ratings Page",Toast.LENGTH_LONG).show();
    }
}
