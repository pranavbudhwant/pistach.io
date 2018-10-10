package com.hersheys.recommender.pistachio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RecommedationAdapter extends FragmentPagerAdapter {
    int NoOfTabs;
    Recommended recomm;
    WatchLater wl;
    public RecommedationAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.NoOfTabs = NumberOfTabs;
        recomm = null;
        wl = null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if(recomm==null)
                    recomm = new Recommended();
                return recomm;
            case 1:
                if(wl==null)
                    wl = new WatchLater();
                return wl;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }
}
