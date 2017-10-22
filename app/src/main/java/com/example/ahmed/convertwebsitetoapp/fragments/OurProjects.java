package com.example.ahmed.convertwebsitetoapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.ActivityLogin;
//import com.example.ActivityMain;
//import com.example.ActivityProjectAdd;
//import com.example.ActivityProjectEdit;
//import com.example.ActivityShowProject;
//import com.example.handasy.R;
//import com.example.handasy.controller.GetData;
//import com.example.handasy.model.DataBase;
//import com.example.handasy.model.Drawer;
//import com.example.handasy.model.Image_Data;
//import com.example.handasy.model.ProjectData;
//import com.example.handasy.view.CustomButton;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ahmed.convertwebsitetoapp.DetailsActivtyProject;
import com.example.ahmed.convertwebsitetoapp.DetailsActivtyServ;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.model.ProjectItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by ahmed on 3/30/2017.
 */

public class OurProjects extends Fragment implements ViewPager.OnPageChangeListener, ListView.OnItemClickListener {

    View viewRoot = null;
    Context context = null;
    ViewPager viewPager = null;
    ActionBar actionBar = null;
    String[] indicators = null;
    TabLayout tabLayout = null;
    ListView lvServices = null;
    ArrayList<ProjectItem> serviceItems = new ArrayList<ProjectItem>();
    ListAdapter listAdapter = null;
    ProgressDialog progressDialog = null;
    private ArrayList<Fragment> listFragments = new ArrayList<Fragment>();
    FloatingActionButton fabChatting = null;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.my_projects, container, false);

        setRetainInstance(true);

        lvServices = (ListView) viewRoot.findViewById(R.id.lvServices);
        listAdapter = new ListAdapter(getContext(), serviceItems);
        lvServices.setOnItemClickListener(this);
        lvServices.setAdapter(listAdapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Preparing");
        progressDialog.show();

        fetchData(URLs.URL_PROJECTS);

        fabChatting = (FloatingActionButton) viewRoot.findViewById(R.id.fabChatting);
        //setAlphaAnimation(fabChatting);


//        tabLayout = (TabLayout) viewRoot.findViewById(R.id.tabs);
////        for (int i = 0; i < indicators.length; i++) {
////            tabLayout.addTab(tabLayout.newTab().setText(indicators[i]));
////        }
//
//
//        listFragments.add(new AllProjetcs());
//        listFragments.add(new Individuals());
//        listFragments.add(new Business());
//        listFragments.add(new Governments());
//
//        viewPager = (ViewPager) viewRoot.findViewById(R.id.viewpager);
//        viewPager.setAdapter(new ViewPagerAdapter(/*getChildFragmentManager()*/getFragmentManager(), listFragments,
//                new String[]{"All Projetcs", "Individuals", "Business", "Governments"}));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
////        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        return viewRoot;
    }


    public static void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, .3f);
        fadeOut.setDuration(300);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .3f, 1f);
        fadeIn.setDuration(300);
        final AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn).after(fadeOut);
        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void fetchData(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        serviceItems.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(modifyJson(response.toString()));
                            Log.e("res33423", jsonObject.toString());
                            String image = jsonObject.getString("image");
                            String thumnail = jsonObject.getString("thumb");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                serviceItems.add(
                                        new ProjectItem(jsonObject1.getString("prtitleen"),
                                                jsonObject1.getString("prtitlear"),
                                                jsonObject1.getString("prdescen"),
                                                jsonObject1.getString("prdescar"),
                                                jsonObject1.getString("categoryen"),
                                                jsonObject1.getString("categoryar"),
                                                image + jsonObject1.getString("primg"),
                                                thumnail + jsonObject1.getString("primg")
                                        )
                                );


                            }
                            listAdapter = new ListAdapter(getContext(), serviceItems);
                            lvServices.setAdapter(listAdapter);
                            //listAdapter.notifyDataSetChanged();
//

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error343412", e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity().getApplicationContext(), "onErrorResponse" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private String modifyJson(String s) {

        String s1 = "";
        boolean b = false, locker = true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '{' && locker) {
                b = true;
                locker = false;
            }
            if (b) {
                s1 += s.charAt(i);
            }
        }

        return s1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ProjectItem serviceItem = serviceItems.get(i);

        startActivity(new Intent(getContext(), DetailsActivtyProject.class).putExtra("item", serviceItem));
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }

    public class ListAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<ProjectItem> serviceItems = null;

        public ListAdapter(Context context, ArrayList<ProjectItem> serviceItems) {
            this.context = context;
            this.serviceItems = serviceItems;
        }


        @Override
        public int getCount() {
            return this.serviceItems.size();
        }

        @Override
        public ProjectItem getItem(int i) {
            return this.serviceItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ProjectItem serviceItem = getItem(i);

            View imageView = LayoutInflater.from(getActivity()).inflate(R.layout.project_item, null);
            imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            ImageView imageView1 = (ImageView) imageView.findViewById(R.id.iv);
            TextView textView = (TextView) imageView.findViewById(R.id.tvCategory);



            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English")){
                textView.setText(serviceItem.getCategoryEn());
            }else{
                textView.setText(serviceItem.getCategoryAr());
            }


//            ImageView imageView = new ImageView(context);
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 300));
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(serviceItem.getProjectImgUrl()).into(imageView1);

            return imageView;
        }
    }

}
