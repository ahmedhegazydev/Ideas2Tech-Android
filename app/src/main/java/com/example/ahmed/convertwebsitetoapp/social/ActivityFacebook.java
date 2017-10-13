package com.example.ahmed.convertwebsitetoapp.social;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.ahmed.convertwebsitetoapp.R;

public class ActivityFacebook extends AppCompatActivity {

    WebView webViewFaceBook = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        webViewFaceBook = (WebView) findViewById(R.id.webViewFacebook);
        webViewFaceBook.loadUrl("https://www.facebook.com/Ideas2Techs/");


    }



    @Override
    public void onBackPressed() {
        if (webViewFaceBook.canGoBack() == true) {
            webViewFaceBook.goBack();
        } else {
            finish();
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (true)
//            finish();
//
//        super.onBackPressed();
//
//    }
}
