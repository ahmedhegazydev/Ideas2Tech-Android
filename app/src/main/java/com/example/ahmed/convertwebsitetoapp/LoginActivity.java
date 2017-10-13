package com.example.ahmed.convertwebsitetoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ahmed.convertwebsitetoapp.fragments.LoginHome;

public class LoginActivity extends FragmentActivity {

    public static String C_ID = "", name = "", mobile = "", userId = "";
    public static boolean login;
    public static String logo = "";
    SharedPreferences mPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginHome();

    }

    private void loginHome() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginHome fragment = new LoginHome();
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.commit();
    }


    public void skip(View view) {
        Toast.makeText(this, "skip", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, MainActivity.class).putExtra("kind", "skip"));
    }

    public void signup(View view) {
        Toast.makeText(this, "sign up", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, MainActivity.class).putExtra("kind", "skip"));
    }

    //xml onclick
    public void login(View view) {
        Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, MainActivity.class).putExtra("kind", "skip"));
    }




}
