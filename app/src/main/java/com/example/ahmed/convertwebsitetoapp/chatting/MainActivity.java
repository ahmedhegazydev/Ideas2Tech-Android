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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    // tags used to attach the fragments
    private static final String TAG_HOME_PAGE = "TAG_HOME_PAGE";
    private static final String TAG_ABOUT_US = "TAG_ABOUT_US";
    private static final String TAG_OUR_PROJECTS = "TAG_OUR_PROJECTS";
    private static final String TAG_SERVICES = "TAG_SERVICES";
    private static final String TAG_ORDER_NOW = "TAG_ORDER_NOW";
    private static final String TAG_CONTACT_US = "TAG_CONTACT_US";
    private static final String TAG_FAQS = "TAG_FAQS";
    private static final String TAG_PREV_ORDERS = "TAG_PREV_ORDERS";
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME_PAGE;
    // Session Manager Class
    UserSessionManager session;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    int i = 0;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fabChatting;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

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
        if (session.checkLogin())
            finish();


        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fabChatting = (FloatingActionButton) findViewById(R.id.fabChatting);
        // setAlphaAnimation(fabChatting);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
//        txtName = (TextView) navHeader.findViewById(R.id.name);
//        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
//        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
//        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        //ZopimChat.init(getResources().getString(R.string.CHATTING_APP_ID));
//        VisitorInfo visitorData = new VisitorInfo.Builder()
//                .name("Visitor name")
//                .email("visitor@example.com")
//                .phoneNumber("0123456789")
//                .build();
//        ZopimChat.setVisitorInfo(visitorData);
        ZendeskConfig.INSTANCE.init(this, "https://individual7623.zendesk.com", "277bf757e6b8dcc213d3633ff7f72a611ebfeab3a8b3095f",
                "mobile_sdk_client_651b0d149b179b0694c5");
        //Identity identity = new AnonymousIdentity.Builder().build();
        //ZendeskConfig.INSTANCE.setIdentity(identity);
        //SendBird.init(getResources().getString(R.string.APP_ID), this);
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


    }

    private void startChatting() {
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

    private void enteringTheChannel() {
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
    private void loadNavHeader() {
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
    private void loadHomeFragment() {
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
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
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

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home_page:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME_PAGE;
                        break;
                    case R.id.nav_our_projects:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_OUR_PROJECTS;
                        break;
                    case R.id.nav_services:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SERVICES;
                        break;
                    case R.id.nav_prev_services:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_PREV_ORDERS;
                        startActivity(new Intent(MainActivity.this, PrevOrdersActivity.class));
                        break;
                    case R.id.nav_order_now:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ORDER_NOW;
                        break;
                    case R.id.nav_about_us:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_ABOUT_US;
                        break;
                    case R.id.nav_contact_us:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_CONTACT_US;
                        break;
                    case R.id.nav_faqs:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_FAQS;
                        break;
                    case R.id.nav_sign_out:
                        //navItemIndex = 7;
                        signOut(MainActivity.this);
                        break;
//                    case R.id.nav_about_us:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
//                        drawer.closeDrawers();
//                        return true;
//                    case R.id.nav_privacy_policy:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
//                        drawer.closeDrawers();
//                        return true;
                    default:
                        navItemIndex = 0;
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

        getMenuInflater().inflate(R.menu.menu_social_media, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.socialFaceBook:
                /// Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityFacebook.class));
                //getOpenFacebookIntent(getApplicationContext());
                startActivityForViewing("https://www.facebook.com/Ideas2Techs/");
                break;
            case R.id.socialInst:
                //Toast.makeText(this, "Instagram", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityInstagram.class));
                startActivityForViewing("https://www.instagram.com/ideas2tech/");
                break;
            case R.id.socialTwitter:
                //Toast.makeText(this, "Twitter", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityTwitter.class));
                startActivityForViewing("https://twitter.com/ideas2tech");
                break;
            case R.id.socialYoutube:
                //Toast.makeText(this, "Youtube", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, ActivityYoutube.class));

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
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fabChatting.show();
        else
            fabChatting.hide();
    }
}
