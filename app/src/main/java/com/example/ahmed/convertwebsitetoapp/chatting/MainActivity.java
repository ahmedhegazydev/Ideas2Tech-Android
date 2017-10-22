package com.example.ahmed.convertwebsitetoapp.chatting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.UserManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.PrevOrdersActivity;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.fragments.AboutUs;
import com.example.ahmed.convertwebsitetoapp.fragments.ContactUs;
import com.example.ahmed.convertwebsitetoapp.fragments.FAQs;
import com.example.ahmed.convertwebsitetoapp.fragments.HomePage;
import com.example.ahmed.convertwebsitetoapp.fragments.OrderNow;
import com.example.ahmed.convertwebsitetoapp.fragments.OurProjects;
import com.example.ahmed.convertwebsitetoapp.fragments.Services;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.firebase.FirebaseModule;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.ui.InterfaceManager;


public class MainActivity extends AppCompatActivity {

    // urls to load navigation header background image
    // and profile image
    public static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    public static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    // tags used to attach the fragments
    public static final String TAG_HOME_PAGE = "TAG_HOME_PAGE";
    public static final String TAG_ABOUT_US = "TAG_ABOUT_US";
    public static final String TAG_OUR_PROJECTS = "TAG_OUR_PROJECTS";
    public static final String TAG_SERVICES = "TAG_SERVICES";
    public static final String TAG_ORDER_NOW = "TAG_ORDER_NOW";
    public static final String TAG_CONTACT_US = "TAG_CONTACT_US";
    public static final String TAG_FAQS = "TAG_FAQS";
    public static final String TAG_PREV_ORDERS = "TAG_PREV_ORDERS";
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME_PAGE;
    // Session Manager Class
    UserSessionManager session;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    int i = 0;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public View navHeader;
    public ImageView imgNavHeaderBg, imgProfile;
    public TextView txtName, txtWebsite;
    public Toolbar toolbar;
    public FloatingActionButton fabChatting;
    // toolbar titles respected to selected nav menu item
    public String[] activityTitles;
    // flag to load home fragment when user presses back key
    public boolean shouldLoadHomeFragOnBackPress = true;
    public Handler mHandler;

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

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/Ideas2Techs"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Ideas2Techs"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_final);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Session class instance
        session = new UserSessionManager(getApplicationContext());
        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.


//        if (session.checkLogin())
//            finish();


        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fabChatting = (FloatingActionButton) findViewById(R.id.fabChatting);
        // setAlphaAnimation(fabChatting);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);


        if (TextUtils.isEmpty(new UserSessionManager(getApplicationContext()).getUserDetails().get(UserSessionManager.KEY_USER_ID))) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_not_logged);
            // load toolbar titles from string resources
            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles_not_logged);

        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_logged);
            // load toolbar titles from string resources
            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles_logged);

        }


//        txtName = (TextView) navHeader.findViewById(R.id.name);
//        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
//        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
//        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);


        //ZopimChat.init(getResources().getString(R.string.CHATTING_APP_ID));
//        VisitorInfo visitorData = new VisitorInfo.Builder()
//                .name("Visitor name")
//                .email("visitor@example.com")
//                .phoneNumber("0123456789")
//                .build();
//        ZopimChat.setVisitorInfo(visitorData);

        ZendeskConfig.INSTANCE.init(this, "https://individual7623.zendesk.com", "277bf757e6b8dcc213d3633ff7f72a611ebfeab3a8b3095f",
                "mobile_sdk_client_651b0d149b179b0694c5");
        Identity identity = new AnonymousIdentity.Builder().build();
        ZendeskConfig.INSTANCE.setIdentity(identity);

        //SendBird.init(getResources().getString(R.string.APP_ID), this);
        //initChatSdk();
        fabChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

//                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity(new Intent(MainActivity.this, ZopimChatActivity.class));

                //startChatting();


                //InterfaceManager.shared().a.startLoginActivity(getApplicationContext(), true);


            }
        });
        //setAlphaAnimation(fabChatting);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME_PAGE;
            loadHomeFragment();
        }


        getSocialMedia(URLs.URL_SOCIAL_MEDIA);

    }


    private void initChatSdk() {
        //Enable multi-dexing
        MultiDex.install(getApplicationContext());
        Context context = getApplicationContext();
        // Create a new configuration
        Configuration.Builder builder = new Configuration.Builder(context);
//        builder.firebase(getActivity().getResources().getString(R.string.firebase_url),
//                getActivity().getResources().getString(R.string.firebase_root_path),
//                getActivity().getResources().getString(R.string.firebase_storage_url),
//                "CloudMessaging Api Key");
        // Perform any configuration steps
        // Initialize the Chat SDK
        ChatSDK.initialize(builder.build());
        // Activate the Firebase module
        FirebaseModule.activate(context);
        // File storage is needed for profile image upload and image messages
        FirebaseFileStorageModule.activate();
        // Activate any other modules you need.
        // ...


    }


    String urlFacebook = "", urlInstagram = "", urlYoutube = "", urlTwitter = "", urlGooglePlus = "", urlLikedin = "";

    private void getSocialMedia(String urlSocialMedia) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSocialMedia,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("socialMedia20130074", response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());
                            JSONObject jsonObject11 = jsonObject1.getJSONObject("data");

                            urlFacebook = jsonObject11.getString("facebook");
                            urlGooglePlus = jsonObject11.getString("googleplus");
                            urlTwitter = jsonObject11.getString("twitter");
                            urlLikedin = jsonObject11.getString("linkedin");
                            urlYoutube = jsonObject11.getString("youtube");
                            urlInstagram = jsonObject11.getString("instagram");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.tlMainContainer), "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void startChatting() {
        SendBird.connect("1", new SendBird.ConnectHandler() {
            @Override
            public void onConnected(com.sendbird.android.User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "connetcted1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        OpenChannel.createChannel(new OpenChannel.OpenChannelCreateHandler() {
            @Override
            public void onResult(OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "Connected2", Toast.LENGTH_SHORT).show();
                    enteringTheChannel();
                }
            }


        });


    }

    public void enteringTheChannel() {
        OpenChannel.getChannel("Ideas2Tech", new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }

                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            // Error.
                            return;
                        }
                    }
                });
            }
        });
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    public void loadNavHeader() {
        // name, website
//        txtName.setText("Ravi Tamada");
//        txtWebsite.setText("www.androidhive.info");

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);
//
//        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    public void loadHomeFragment() {

        if (TextUtils.isEmpty(new UserSessionManager(getApplicationContext()).getUserDetails().get(UserSessionManager.KEY_USER_ID))) {
            switch (session.getLastPressedFragment()) {
                case TAG_OUR_PROJECTS:
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_OUR_PROJECTS;
                    goToFragment(new OurProjects());
                    break;
                case TAG_ABOUT_US:
                    navItemIndex = 5;
                    CURRENT_TAG = TAG_ABOUT_US;
                    goToFragment(new AboutUs());
                    break;
                case TAG_CONTACT_US:
                    navItemIndex = 6;
                    CURRENT_TAG = TAG_CONTACT_US;
                    goToFragment(new ContactUs());
                    break;
                case TAG_FAQS:
                    navItemIndex = 7;
                    CURRENT_TAG = TAG_FAQS;
                    goToFragment(new FAQs());
                    break;
                case TAG_ORDER_NOW:
                    navItemIndex = 4;
                    CURRENT_TAG = TAG_ORDER_NOW;
                    goToFragment(new OrderNow());
                    break;
                case TAG_PREV_ORDERS:

                    break;
                case TAG_SERVICES:
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_SERVICES;
                    goToFragment(new Services());
                    break;
                default:
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME_PAGE;
                    goToFragment(new HomePage());
                    break;
            }
        } else {
            switch (session.getLastPressedFragment()) {
                case TAG_OUR_PROJECTS:
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_OUR_PROJECTS;
                    goToFragment(new OurProjects());
                    break;
                case TAG_ABOUT_US:
                    navItemIndex = 4;
                    CURRENT_TAG = TAG_ABOUT_US;
                    goToFragment(new AboutUs());
                    break;
                case TAG_CONTACT_US:
                    navItemIndex = 5;
                    CURRENT_TAG = TAG_CONTACT_US;
                    goToFragment(new ContactUs());
                    break;
                case TAG_FAQS:
                    navItemIndex = 6;
                    CURRENT_TAG = TAG_FAQS;
                    goToFragment(new FAQs());
                    break;
                case TAG_ORDER_NOW:
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_ORDER_NOW;
                    goToFragment(new OrderNow());
                    break;
                case TAG_PREV_ORDERS:

                    break;
                case TAG_SERVICES:
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_SERVICES;
                    goToFragment(new Services());
                    break;
                default:
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME_PAGE;
                    goToFragment(new HomePage());
                    break;
            }

        }
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            //toggleFab();
            return;
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }


    public void goToFragment(final Fragment fmt) {


        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fmt, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };


        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


    }

    public Fragment getHomeFragment() {
        Fragment fragment = null;
        switch (navItemIndex) {
            case 0:
                return new HomePage();
            case 1:
                return new OurProjects();
            case 2:
                return new Services();
            case 4:
                return new OrderNow();
            case 5:
                return new AboutUs();
            case 6:
                return new ContactUs();
            case 7:
                return new FAQs();
            case 8:
                signOut(MainActivity.this);
            default:
                return new HomePage();

        }
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

        new AlertDialog.Builder(context).setMessage(message)
                .setTitle(title)
                .setPositiveButton(titleOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Clear the session data
                        // This will clear all session data and
                        // redirect user to LoginActivity
                        session.logoutUser();

                    }
                })
                .setNegativeButton(titleCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();


    }

    public void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    public void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                if (!TextUtils.isEmpty(new UserSessionManager(getApplicationContext()).getUserDetails().get(UserSessionManager.KEY_USER_ID))) {
                    switch (menuItem.getItemId()) {
                        //Replacing the main content with ContentFragment Which is our Inbox View;
                        case R.id.nav_home_page:
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_HOME_PAGE;
                            session.postLastPressedFragment(MainActivity.TAG_HOME_PAGE);
                            break;
                        case R.id.nav_our_projects:
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_OUR_PROJECTS;
                            session.postLastPressedFragment(MainActivity.TAG_OUR_PROJECTS);
                            break;
                        case R.id.nav_services:
                            navItemIndex = 2;
                            CURRENT_TAG = TAG_SERVICES;
                            session.postLastPressedFragment(MainActivity.TAG_SERVICES);
                            break;
                        case R.id.nav_prev_services:
                            navItemIndex = 3;
                            CURRENT_TAG = TAG_PREV_ORDERS;
                            startActivity(new Intent(MainActivity.this, PrevOrdersActivity.class));
                            break;
                        case R.id.nav_order_now:
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_ORDER_NOW;
                            session.postLastPressedFragment(MainActivity.TAG_ORDER_NOW);
                            break;
                        case R.id.nav_about_us:
                            navItemIndex = 5;
                            CURRENT_TAG = TAG_ABOUT_US;
                            session.postLastPressedFragment(MainActivity.TAG_ABOUT_US);
                            break;
                        case R.id.nav_contact_us:
                            navItemIndex = 6;
                            CURRENT_TAG = TAG_CONTACT_US;
                            session.postLastPressedFragment(MainActivity.TAG_CONTACT_US);
                            break;
                        case R.id.nav_faqs:
                            navItemIndex = 7;
                            CURRENT_TAG = TAG_FAQS;
                            session.postLastPressedFragment(MainActivity.TAG_FAQS);
                            break;
                        case R.id.nav_sign_out:
                            //navItemIndex = 7;
                            signOut(MainActivity.this);
                            break;
                        default:
                            navItemIndex = 0;
                    }
                } else {
                    switch (menuItem.getItemId()) {
                        //Replacing the main content with ContentFragment Which is our Inbox View;
                        case R.id.nav_home_page:
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_HOME_PAGE;
                            session.postLastPressedFragment(MainActivity.TAG_HOME_PAGE);
                            break;
                        case R.id.nav_our_projects:
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_OUR_PROJECTS;
                            session.postLastPressedFragment(MainActivity.TAG_OUR_PROJECTS);
                            break;
                        case R.id.nav_services:
                            navItemIndex = 2;
                            CURRENT_TAG = TAG_SERVICES;
                            session.postLastPressedFragment(MainActivity.TAG_SERVICES);
                            break;
//                        case R.id.nav_prev_services:
//                            navItemIndex = 3;
//                            CURRENT_TAG = TAG_PREV_ORDERS;
//                            startActivity(new Intent(MainActivity.this, PrevOrdersActivity.class));
//                            break;
                        case R.id.nav_order_now:
                            navItemIndex = 3;
                            CURRENT_TAG = TAG_ORDER_NOW;
                            session.postLastPressedFragment(MainActivity.TAG_ORDER_NOW);
                            break;
                        case R.id.nav_about_us:
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_ABOUT_US;
                            session.postLastPressedFragment(MainActivity.TAG_ABOUT_US);
                            break;
                        case R.id.nav_contact_us:
                            navItemIndex = 5;
                            CURRENT_TAG = TAG_CONTACT_US;
                            session.postLastPressedFragment(MainActivity.TAG_CONTACT_US);
                            break;
                        case R.id.nav_faqs:
                            navItemIndex = 6;
                            CURRENT_TAG = TAG_FAQS;
                            session.postLastPressedFragment(MainActivity.TAG_FAQS);
                            break;
                        case R.id.nav_sign_out:
                            //navItemIndex = 7;
                            signOut(MainActivity.this);
                            break;
                        default:
                            navItemIndex = 0;
                    }
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar,
                R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME_PAGE;
                loadHomeFragment();
                return;
            }
        }

        //otherwise
        finish();
        System.exit(0);

        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
//        if (navItemIndex == 0) {
//            getMenuInflater().inflate(R.menu.main, menu);
//        }

        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }

        if (TextUtils.isEmpty(new UserSessionManager(getApplicationContext()).getUserDetails().get(UserSessionManager.KEY_USER_ID))) {
            getMenuInflater().inflate(R.menu.menu_social_media_not_logged, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_social_media_logged, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.socialFaceBook:
                /// Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityFacebook.class));
                //getOpenFacebookIntent(getApplicationContext());

                startActivityForViewing(urlFacebook);
                //startActivityForViewing("https://www.facebook.com/Ideas2Techs/");
                break;
            case R.id.socialInst:
                //Toast.makeText(this, "Instagram", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityInstagram.class));

                startActivityForViewing(urlInstagram);
                //startActivityForViewing("https://www.instagram.com/ideas2tech/");
                break;
            case R.id.socialTwitter:
                //Toast.makeText(this, "Twitter", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityTwitter.class));

                startActivityForViewing(urlTwitter);
                //startActivityForViewing("https://twitter.com/ideas2tech");
                break;
            case R.id.socialYoutube:
                //Toast.makeText(this, "Youtube", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityYoutube.class));

                startActivityForViewing(urlYoutube);
                break;
            case R.id.loginNow:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.signOut:
                signOut(MainActivity.this);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void startActivityForViewing(String url) {

        if (TextUtils.isEmpty(url.trim())) {
            new AlertDialog.Builder(this).setPositiveButton("No url supported yet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    // show or hide the fab
    public void toggleFab() {
        if (navItemIndex == 0)
            fabChatting.show();
        else
            fabChatting.hide();
    }
}
