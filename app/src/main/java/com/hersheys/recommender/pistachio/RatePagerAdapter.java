package com.hersheys.recommender.pistachio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class RatePagerAdapter extends FragmentStatePagerAdapter {
    int NoOfTabs;
    NewRatings nr;
    MyRatings mr;

    public RatePagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.NoOfTabs = NumberOfTabs;
        mr = null;
        nr = null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if(nr==null)
                    nr = new NewRatings();
                return nr;
            case 1:
                if(mr==null)
                    mr = new MyRatings();
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
