package com.example.ahmed.androidproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by amany on 2/16/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    private String []tabTitle ={"Upcomming","Done","Cancelled"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UpcommingFrag();
            case 1:
                return new DoneFrag();
            case 2:
                return new CancelledFrag();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
