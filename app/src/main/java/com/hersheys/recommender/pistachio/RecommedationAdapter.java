package com.hersheys.recommender.pistachio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RecommedationAdapter extends FragmentPagerAdapter {
    int NoOfTabs;
    public RecommedationAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.NoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Recommended nr = new Recommended();
                return nr;
            case 1:
                WatchLater mr = new WatchLater();
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
