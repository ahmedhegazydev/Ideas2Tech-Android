package com.example.ahmed.convertwebsitetoapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

//    Context context = null;
    ArrayList<Fragment> fragments = null;
    String[] tabTitles = null;

    public ViewPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> fragments) {
        super(fm);
//        this.context = context;
        this.fragments = fragments;
    }

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] tabTitles) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;

    }

    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }



}