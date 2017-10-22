package com.example.ahmed.convertwebsitetoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmed.convertwebsitetoapp.chatting.*;
import com.example.ahmed.convertwebsitetoapp.model.ServiceItem;

import java.util.Locale;

public class DetailsActivtyServ extends AppCompatActivity {

    ImageView imageView = null;
    TextView textView = null;
    WebView webView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activty);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Your toolbar is now an action bar and you can use it like you always do, for example:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        Intent intent = getIntent().getParcelableExtra("item");
//        String imageUrl = intent.getStringExtra("imgUrl");
//        String imageDesc = intent.getStringExtra("imgDesc");
//        String imageTitle = intent.getStringExtra("imgTitle");

//        ServiceItem serviceItem = getIntent().getParcelableExtra("item");
        ServiceItem serviceItem = (ServiceItem) getIntent().getSerializableExtra("item");


        imageView = (ImageView) findViewById(R.id.iv);
        //textView = (TextView) findViewById(R.id.tvImageDesc);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);


        Glide.with(getApplicationContext()).load(serviceItem.getSerImageUrl()).into(imageView);
        //textView.setText(serviceItem.getSerDescEn());

        if (Locale.getDefault().getLanguage() == "en") {
            webView.loadDataWithBaseURL("", serviceItem.getSerDescEn(), "text/html", "UTF-8", "");
            getSupportActionBar().setTitle(serviceItem.getSerTitleEn());
        } else {
            webView.loadDataWithBaseURL("", serviceItem.getSerDescAr(), "text/html", "UTF-8", "");
            getSupportActionBar().setTitle(serviceItem.getSerTitleAr());

        }


    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                startActivity(new Intent(this, com.example.ahmed.convertwebsitetoapp.chatting.MainActivity.class));
                overridePendingTransitionExit();
                break;
        }

        return true;

    }
}
