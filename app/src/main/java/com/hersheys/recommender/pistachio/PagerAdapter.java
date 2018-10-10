package com.hersheys.recommender.pistachio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int NoOfTabs;
    GetRecommendationsTab recommTab;
    RateTab rTab;
    UserTab uTab;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.NoOfTabs = NumberOfTabs;
        recommTab = null;
        rTab = null;
        uTab = null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if(recommTab==null)
                    recommTab = new GetRecommendationsTab();
                return recommTab;

            case 1:
                if(rTab==null)
                    rTab = new RateTab();
                return rTab;

            case 2:
                if(uTab==null)
                    uTab = new UserTab();
                return uTab;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }
}
