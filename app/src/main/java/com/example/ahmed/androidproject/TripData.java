package com.example.ahmed.androidproject;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class TripData extends FragmentActivity{
   ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_data);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
      viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
       viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
    }


}
