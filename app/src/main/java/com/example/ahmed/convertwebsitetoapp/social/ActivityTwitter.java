package com.example.ahmed.convertwebsitetoapp.social;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.ahmed.convertwebsitetoapp.R;

public class ActivityTwitter extends AppCompatActivity {

    WebView webViewFaceBook = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        webViewFaceBook = (WebView) findViewById(R.id.webViewTwitter);
        webViewFaceBook.loadUrl("https://twitter.com/ideas2tech");


    }
}
