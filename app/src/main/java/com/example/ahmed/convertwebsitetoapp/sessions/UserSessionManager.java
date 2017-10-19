package com.example.ahmed.convertwebsitetoapp.sessions;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.ahmed.convertwebsitetoapp.chatting.LoginActivity;

public class UserSessionManager {

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";
    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String IS_SKIPPPED = "IS_SKIPPPED";
    //user id from login
    public static final String KEY_USER_ID = "KEY_USER_ID";
    //the last fragment has been pressed
    public static final String KEY_LAST_FGMT_PRESSED = "KEY_LAST_FGMT_PRESSED";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void postLastPressedFragment(String lastPressedFmtName) {
        editor.putString(KEY_LAST_FGMT_PRESSED, lastPressedFmtName);
        editor.commit();
        editor.apply();

    }

    public String getLastPressedFragment() {
        return new String(pref.getString(KEY_LAST_FGMT_PRESSED, ""));
    }

    //Create login session
    public void createUserLoginSession(String name, String email, String userId) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_NAME, name);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        //storing user id in pref
        editor.putString(KEY_USER_ID, userId);
        // commit changes
        editor.commit();
    }


    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        //user id
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));


        // return user
        return user;
    }


    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    // Check for login
    public boolean isSkipped() {
        return pref.getBoolean(IS_SKIPPPED, false);
    }

    public void setAsSkipped() {
        editor.putBoolean(IS_SKIPPPED, true);
        // commit changes
        editor.commit();
    }

    public boolean checkSkip() {
        // Check skip
        if (!this.isSkipped()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }
}