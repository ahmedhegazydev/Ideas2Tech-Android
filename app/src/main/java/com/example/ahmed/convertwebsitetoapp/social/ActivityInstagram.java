package com.example.ahmed.convertwebsitetoapp.social;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.ahmed.convertwebsitetoapp.R;

public class ActivityInstagram extends AppCompatActivity {

    WebView webViewInsta = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        webViewInsta = (WebView) findViewById(R.id.webViewInstagram);
        webViewInsta.loadUrl("https://www.instagram.com/ideas2tech/");


    }
}
