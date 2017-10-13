package com.example.ahmed.convertwebsitetoapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//import com.example.ahmed.convertwebsitetoapp.ActivityMain;

import com.example.ahmed.convertwebsitetoapp.MainActivity;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.model.Drawer;


/**
 * Created by ahmed on 3/25/2017.
 */

public class HomeSkip extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepageskip, container, false);
        Drawer.title.setText("البراهيم للاستشارات الهندسية");
        MainActivity.option.setImageResource(0);
        Drawer.postionSelected = 0;
        Drawer.drawerAdapter.notifyDataSetChanged();

        return view;
    }
}
