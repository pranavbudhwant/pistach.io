package com.hersheys.recommender.pistachio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class RatePagerAdapter extends FragmentStatePagerAdapter {
    int NoOfTabs;
    public RatePagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.NoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                NewRatings nr = new NewRatings();
                return nr;
            case 1:
                MyRatings mr = new MyRatings();
                return mr;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }
}
