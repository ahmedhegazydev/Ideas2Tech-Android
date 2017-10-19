package com.example.ahmed.convertwebsitetoapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.example.ahmed.convertwebsitetoapp.ActivityMain;
import com.example.ahmed.convertwebsitetoapp.PrevOrdersActivity;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.adapters.ViewPagerAdapter;
import com.example.ahmed.convertwebsitetoapp.chatting.MainActivity;
import com.example.ahmed.convertwebsitetoapp.model.Drawer;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.support.SupportActivity;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by ahmed on 3/25/2017.
 */

public class HomePage extends Fragment implements View.OnClickListener {


    View viewRoot = null;
    Context context = null;
    ViewPager viewPager = null;
    ActionBar actionBar = null;
    String[] indicators = null;
    FloatingActionButton fabChatting = null;
    FrameLayout frameLayout = null;
    TextView tvOurProjects, tvServices, tvSignOut, tvFaqs, tvContactUs, tvOrderNow;
    Handler handler = null;
    Button btnBackToHome = null;
    LinearLayout linearLayout = null;
    TextView tvPrevOrders = null, tvAboutUs;
    UserSessionManager userSessionManager = null;
    private ArrayList<Fragment> listFragments = new ArrayList<Fragment>();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.home_page, container, false);


        linearLayout = (LinearLayout) viewRoot.findViewById(R.id.rlFrame);
        frameLayout = (FrameLayout) linearLayout.findViewById(R.id.frame);
        btnBackToHome = (Button) linearLayout.findViewById(R.id.bacToHome);
        btnBackToHome.setOnClickListener(this);


        ZendeskConfig.INSTANCE.init(getActivity(), "https://omniwear.zendesk.com",
                "23705744c16d8e0698b45920f18aa26e43d7",
                "mobile_sdk_client_b7fd695c0e9a6056");
        Identity identity = new AnonymousIdentity.Builder().build();
        ZendeskConfig.INSTANCE.setIdentity(identity);

        fabChatting = (FloatingActionButton) viewRoot.findViewById(R.id.fabChatting);
        //setAlphaAnimation(fabChatting);
        fabChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SupportActivity.Builder().show(getActivity());
            }
        });

        init();


//        listFragments.add(new FragmentContactsHotlist());
//        listFragments.add(new FragmentContactsGroups());
//
//        viewPager = (ViewPager) viewRoot.findViewById(R.id.viewpager);
//        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), listFragments, new String[]{"HOTLIST", "GROUPS"}));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//        tabLayout.setupWithViewPager(viewPager);
//        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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

    private void init() {
        tvContactUs = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.contactus);
        tvOurProjects = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.projects);
        tvServices = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.services);
        tvSignOut = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.signout);
        tvFaqs = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.faqs);
        tvOrderNow = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.ordernow);
        tvPrevOrders = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.prevOrders);
        tvAboutUs = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.llHomePage).findViewById(R.id.aboutUs);


        tvContactUs.setOnClickListener(this);
        tvOurProjects.setOnClickListener(this);
        tvServices.setOnClickListener(this);
        tvOrderNow.setOnClickListener(this);
        tvSignOut.setOnClickListener(this);
        tvFaqs.setOnClickListener(this);
        tvPrevOrders.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);



        userSessionManager = new UserSessionManager(getActivity());

    }

    @Override
    public void onClick(View view) {
        if (view.equals(tvSignOut)) {
            signOut(getActivity());

        }

        if (view.equals(tvServices)) {
            goToFragment(new Services());
            userSessionManager.postLastPressedFragment(MainActivity.TAG_SERVICES);
        }


        if (view.equals(tvOrderNow)) {
            goToFragment(new OrderNow());
            userSessionManager.postLastPressedFragment(MainActivity.TAG_ORDER_NOW);
        }

        if (view.equals(tvOurProjects)) {
            goToFragment(new OurProjects());
            userSessionManager.postLastPressedFragment(MainActivity.TAG_OUR_PROJECTS);
        }
        if (view.equals(tvFaqs)) {
            goToFragment(new FAQs());
            userSessionManager.postLastPressedFragment(MainActivity.TAG_FAQS);
        }
        if (view.equals(tvContactUs)) {
            goToFragment(new ContactUs());
            userSessionManager.postLastPressedFragment(MainActivity.TAG_CONTACT_US);
        }
        if (view.equals(tvAboutUs)) {
            goToFragment(new AboutUs());
            userSessionManager.postLastPressedFragment(MainActivity.TAG_ABOUT_US);
        }
        if (view.equals(tvPrevOrders)) {
            startActivity(new Intent(getActivity(), PrevOrdersActivity.class));
            userSessionManager.postLastPressedFragment(MainActivity.TAG_PREV_ORDERS);
        }

        if (view.equals(btnBackToHome)) {
            //frameLayout.setVisibility(View.GONE);
            goToFragment(new HomePage());

        }

    }

    public void goToFragment(final Fragment fragment) {

        linearLayout.setVisibility(View.VISIBLE);
        //btnBackToHome.setVisibility(View.VISIBLE);

        handler = new Handler();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "");
                fragmentTransaction.commitAllowingStateLoss();
            }
        };


        if (mPendingRunnable != null) {
            handler.post(mPendingRunnable);
        }

    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    public void signOut(Context context) {
        String message = "", titleOk = "", titleCancel = "";
        String title = "";
        if (Locale.getDefault().getLanguage().toString().equalsIgnoreCase("en")) {
            message = "Do u want to exit ?";
            titleOk = "Exit";
            titleCancel = "Cancel";
            title = "Our dear customer";
        } else {
            message = "هل تريد الخروج بالفعل؟";
            title = "عميلنا العزيز";
            titleCancel = "لا سيبني شويه";
            titleOk = "اه عايز أخرج";

        }
//
//        new AlertDialog.Builder(context).setMessage(message)
//                .setTitle(title)
//                .setPositiveButton(titleOk, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // Clear the session data
//                        // This will clear all session data and
//                        // redirect user to LoginActivity
//                        new UserSessionManager(getActivity()).logoutUser();
//
//                    }
//                })
//                .setNegativeButton(titleCancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .show();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }


}
