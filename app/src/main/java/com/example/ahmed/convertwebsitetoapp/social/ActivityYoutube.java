package com.example.ahmed.convertwebsitetoapp.social;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.ahmed.convertwebsitetoapp.R;

public class ActivityYoutube extends AppCompatActivity {


    WebView webViewYou = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        webViewYou = (WebView) findViewById(R.id.webViewYoutube);
        webViewYou.loadUrl("");


    }
}
