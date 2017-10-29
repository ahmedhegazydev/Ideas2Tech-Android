package com.example.ahmed.convertwebsitetoapp.chatting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.model.Person;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by ahmed on 30/09/17.
 */

public class LoginActivity extends AppCompatActivity {


    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    public static final String MyPREFERENCES = "MyPREFERENCES";
    public static final String USER_ID = "USER_ID";
    public final static int MOED = Context.MODE_PRIVATE;
    EditText etEmail = null, etPassword = null;
    EditText etNewUserEmail, etNewUserPass, etNewUserName, etNewUserConfPassword, etNewUserPhoneNumber, etNewUserAddress, etNewUserUserName;
    DrawerLayout drawerLayout = null;
    UserSessionManager session = null;
    ProgressDialog pdLogging = null;
    String email = "";
    ProgressDialog pdRegister = null;
    String from = "", to = "", message = "";
    // Response
    String responseServer;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    View view1 = null;
    ProgressDialog pdSendingEmail, pdCheckingEmailExist = null;
    String emailForgetPass = "";
    ProgressDialog progressDialog = null;
    TextWatcher txtWatcherEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable.toString()) && isValidEmail(editable.toString())) {
                etEmail.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.ok), null);
                ///Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //

        init();
        //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();


    }

    private void init() {


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        // Session Manager
        session = new UserSessionManager(getApplicationContext());


        etEmail = (EditText) findViewById(R.id.etUserEmail);
        etEmail.addTextChangedListener(txtWatcherEmail);
        etPassword = (EditText) findViewById(R.id.etUserPassword);


        //disable-the-swipe-gesture-that-opens-the-navigation-drawer-in-android
        drawerLayout = (DrawerLayout) findViewById(R.id.regDrawerLayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        NavigationView navigationView = (NavigationView) findViewById(R.id.regNavView);
        View view = navigationView.getHeaderView(0);


        etNewUserConfPassword = (EditText) view.findViewById(R.id.etNewConfPassword);
        etNewUserEmail = (EditText) view.findViewById(R.id.etNewEmail);
        etNewUserPass = (EditText) view.findViewById(R.id.etNewPassword);
        etNewUserName = (EditText) view.findViewById(R.id.etNewName);
        etNewUserPhoneNumber = (EditText) view.findViewById(R.id.etNewPhoneNumber);
        etNewUserAddress = (EditText) view.findViewById(R.id.etNewAddress);
        etNewUserUserName = (EditText) view.findViewById(R.id.etNewUsername);


        ((CheckBox) findViewById(R.id.cbShowPass)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    etPassword.setTransformationMethod(null);
                }
            }
        });


        //fit the navigationview all screen
        //change the width of the NavigationView programatically:
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        //params.height = metrics.heightPixels;
        navigationView.setLayoutParams(params);


//        register();
        //register2(URLs.URL_REGISTER_FINAL, new Person("Ahmed Mohammed", "Ahmed20130074", "123", "wowrar1234@gmail.com", "Haram", "01156749640"));
        //new AsyncT(new Person("Ahmed Mohammed", "Ahmed20130074", "123", "wowrar1234@gmail.com", "Haram", "01156749640")).execute();
//        register3();
//        new Signup2(getApplicationContext()).execute(
//                "Ahmed Mohammed",
//                "Ahmed20130074",
//                "wowrar1234@gmail.com",
//                "01156749640",
//                "Haram",
//                "123"
//        );

        if (getIntent().hasExtra("login")) {
            //Toast.makeText(this, getIntent().getStringExtra("login"), Toast.LENGTH_SHORT).show();
        } else {
            if (session.isSkipped() /*&& !getIntent().getStringExtra("login").toString().equalsIgnoreCase("login")*/) {
                startActivity(new Intent(this, MainActivity.class));
                finish();

            } else {

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void btnLogin(View view) {

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            Snackbar.make(findViewById(R.id.regDrawerLayout), "Please, Enter the email", Snackbar.LENGTH_SHORT).show();
            etEmail.setError("Enter email");
            etEmail.requestFocus();
            return;
        }

        if (!isValidEmail(etEmail.getText().toString())) {
            Snackbar.make(findViewById(R.id.regDrawerLayout), "Please, Enter valid email", Snackbar.LENGTH_SHORT).show();
            etEmail.setError("Enter valid email");
            etEmail.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            Snackbar.make(findViewById(R.id.regDrawerLayout), "Please, Enter the password", Snackbar.LENGTH_LONG).show();
            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return;
        }
        loginNow();
    }

    private void loginNow() {
        pdLogging = new ProgressDialog(this);
        pdLogging.setMessage("Logging ...");
        pdLogging.setCancelable(false);
        pdLogging.setCanceledOnTouchOutside(false);
        pdLogging.show();
        //if the user is already logged in we will directly start the profile activity
//        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
//            finish();
//            startActivity(new Intent(this, MainActivity.class));
//            return;
//        }
//
//        //if everything is fine
// Tag used to cancel the request
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN_FINAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res238135", response.toString());
                        String code = "";
                        String message = "";
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.toString());
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");
                            //Log.e("code12312", code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                            Snackbar.make(findViewById(R.id.regDrawerLayout), "User not exist", Snackbar.LENGTH_SHORT).show();
//                            drawerLayout.openDrawer(Gravity.RIGHT);
//                        } else {
//                            if (code == "1") {
//                                finish();
//                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                            } else {
//                                if (code == "6") {
//                                    new AlertDialog.Builder(LoginActivity.this)
//                                            .setMessage("Please, Activate your email address, That has been snt to u")
//                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                }
//                                            })
//                                            .show();
//                                }
//                            }
//                        }
                        //Log.e("message3723", message);
                        if (message.equalsIgnoreCase("user_not_exist")) {
                            Snackbar.make(findViewById(R.id.regDrawerLayout), "User not exist", Snackbar.LENGTH_SHORT).show();
                            drawerLayout.openDrawer(Gravity.RIGHT);
                        } else {
                            //engahmedali2022@gmail.com
                            if (message.equalsIgnoreCase("user_exist")) {
                                //Log.e("message3723", message)
                                String userId = null;
                                String userFullName = "";

                                try {
                                    userId = jsonObject.getJSONObject("data").getString("id");
                                    userFullName = jsonObject.getJSONObject("data").getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                Toast.makeText(LoginActivity.this, userId, Toast.LENGTH_SHORT).show();
//                                sharedPreferences = getSharedPreferences(MyPREFERENCES, MOED);
//                                editor.putString(USER_ID, userId);
//                                editor.commit();
//                                editor.apply();

                                session.createUserLoginSession(userFullName, etEmail.getText().toString(), userId);
                                // Starting MainActivity
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                // Add new Flag to start new Activity
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            } else {
                                if (message.equalsIgnoreCase("user_not_active")) {
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setMessage("Please, Activate your email address, That has been snt to u")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    if (message.equalsIgnoreCase("password_not_match")) {
                                        Snackbar.make(findViewById(R.id.regDrawerLayout), "Password is incorrect !!!", Snackbar.LENGTH_SHORT).show();
                                        etPassword.requestFocus();

                                    }
                                }
                            }
                        }
                        pdLogging.dismiss();
//
//                        Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                        if (response.equals("Login")) {
//                        // Creating user login session
//                        // For testing i am stroing name, email as follow
//                        // Use user real data
//                        session.createLoginSession(etEmail.getText().toString(), etPassword.getText().toString());
//                        finish();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    } else {
//                        Snackbar.make(findViewById(R.id.regDrawerLayout), "Check password or email, Something wrong !!!", Snackbar.LENGTH_LONG).show();
//                        pdLogging.dismiss();
//                        etPassword.requestFocus();
//                        drawerLayout.openDrawer(Gravity.RIGHT);
//                    }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, "onErrorResponse" + error.getMessage(), Toast.LENGTH_LONG).show();
                        //Log.d("ErrorResponse", error.getMessage());
                        Snackbar.make(findViewById(R.id.regDrawerLayout), "Network Error !!!!", Snackbar.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

        // Creating RequestQueue.
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        // Adding the StringRequest object into requestQueue.
//        requestQueue.add(strReq);

//        new AsyncLogin().execute(etEmail.getText().toString(),
//                etPassword.getText().toString());


        //new BackgroundWorker(this).execute(etEmail.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        } else {
            finish();
        }

        super.onBackPressed();


    }

    public void btnForgotPassword(View view) {

        view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_forget_pass, null);
        final EditText etEmail = (EditText) view1.findViewById(R.id.etUserEmail);
        view1.findViewById(R.id.llOptions).findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    etEmail.requestFocus();
                    etEmail.setError("Enter your email, As the password will be sent to this email");
                    Snackbar.make(findViewById(R.id.regDrawerLayout), "Please, Enter your email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(etEmail.getText().toString())) {
                    etEmail.requestFocus();
                    etEmail.setError("Enter valid email");
                    Snackbar.make(findViewById(R.id.regDrawerLayout), "Please, Enter valid email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //otherwise
                emailForgetPass = etEmail.getText().toString();
//                Toast.makeText(LoginActivity.this, emailForgetPass, Toast.LENGTH_SHORT).show();
//                drawerLayout.setFocusable(false);
//                drawerLayout.setEnabled(false);
//                drawerLayout.setVisibility(View.GONE);
                //rawerLayout.findViewById(R.id.layoutLogin).setVisibility(View.GONE);
                //showNext();
                checkingEmailExistanse(URLs.URL_FORGET_PASS);
            }


            private void showNext() {
                view1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up));
                drawerLayout.addView(view1);
                //drawerLayout.setEnabled(false);


                final View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_forget_pass_next, null);
                view2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up));
                drawerLayout.addView(view2);
                drawerLayout.setEnabled(false);


                final EditText etPass1 = (EditText) view2.findViewById(R.id.etPass1);
                final EditText etPass2 = (EditText) view2.findViewById(R.id.etPass2);
                view2.findViewById(R.id.llOptions).findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editPassword();
                    }

                    private void editPassword() {
                        if (TextUtils.isEmpty(etPass1.getText().toString())) {
                            etPass1.setError("This field is required");
                            return;
                        }
                        if (!TextUtils.equals(etPass1.getText().toString(), etPass2.getText().toString())) {
                            etPass2.setError("Passwords not matched");
                            return;
                        }
                        //otherwise
                        editPasswordNow();

                    }

                    private void editPasswordNow() {
                        //Edit password in the database

                    }
                });
                view2.findViewById(R.id.llOptions).findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //drawerLayout.setEnabled(true);
                        drawerLayout.removeView(view2);
                        view2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down));

                    }
                });


            }
        });
        view1.findViewById(R.id.llOptions).findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.removeView(view1);
                view1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_down));
                //drawerLayout.setEnabled(true);
                //drawerLayout.setFocusable(true);
                //drawerLayout.setVisibility(View.VISIBLE);

            }
        });
        view1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up));
        drawerLayout.addView(view1);
        //drawerLayout.setEnabled(false);


    }

    private void checkingEmailExistanse(String url) {

        pdSendingEmail = new ProgressDialog(this);
        pdSendingEmail.setMessage("Sending password RESET email ....");
        pdSendingEmail.setTitle("Please wait");
        ////////////////////////////
        pdCheckingEmailExist = new ProgressDialog(this);
        pdCheckingEmailExist.setMessage("Checking email existence....");
        pdCheckingEmailExist.setTitle("Please wait");
        pdCheckingEmailExist.show();
        //////////////////////////
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (pdCheckingEmailExist.isShowing()) {
                            pdCheckingEmailExist.dismiss();
                        }
                        Log.e("forgetPass1521", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            message = jsonObject.getString("message");
                            if (message.equalsIgnoreCase("email_not_exist")) {
                                Snackbar.make(findViewById(R.id.regDrawerLayout), "Email not exist !!!!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                if (message.equalsIgnoreCase("success")) {
                                    pdSendingEmail.show();

                                    drawerLayout.removeView(view1);
                                    view1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down));

                                    message = jsonObject.getString("data");
                                    from = "engahmedali2022@gmail.com";
                                    to = emailForgetPass;
                                    new RetreiveFeedTask2(LoginActivity.this).execute();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "onErrorResponse" + error.getMessage(), Toast.LENGTH_LONG).show();
                        //Log.d("ErrorResponse", error.getMessage());
                        Snackbar.make(findViewById(R.id.regDrawerLayout), "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
                        if (pdCheckingEmailExist.isShowing())
                            pdCheckingEmailExist.dismiss();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", emailForgetPass);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }

    public void btnNewUser(View view) {
        drawerLayout.openDrawer(Gravity.RIGHT);


    }

    public void Register(View view) {


        // Creating string request with post method.
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String ServerResponse) {
//
//                        // Hiding the progress dialog after all task complete.
//                        pdRegister.dismiss();
//
//                        // Showing Echo Response Message Coming From Server.
//                        Toast.makeText(LoginActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                        // Hiding the progress dialog after all task complete.
//                        pdRegister.dismiss();
//
//                        // Showing error message if something goes wrong.
//                        Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//
//                // Creating Map String Params.
//                Map<String, String> params = new HashMap<String, String>();
//
//                // Adding All values to Params.
//                // The firs argument should be same sa your MySQL database table columns.
//                params.put("UserName", etNewUserName.getText().toString());
//                params.put("UserEmail", etNewUserEmail.getText().toString());
//                params.put("UserPassword", etNewUserPass.getText().toString());
//                return params;
//            }
//
//        };
//
//        // Creating RequestQueue.
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        // Adding the StringRequest object into requestQueue.
//        requestQueue.add(stringRequest);


        registerCheckFieds();


    }

    public void registerCheckFieds() {
        if (TextUtils.isEmpty(etNewUserName.getText().toString())) {
            etNewUserName.setError("Enter full name");
            return;
        }
        if (TextUtils.isEmpty(etNewUserUserName.getText())) {
            etNewUserUserName.setError("Username is requires");
            return;
        }
        if (TextUtils.isEmpty(etNewUserPhoneNumber.getText())) {
            etNewUserPhoneNumber.setError("Phone number is required");
            return;
        }
        if (TextUtils.isEmpty(etNewUserEmail.getText().toString())) {
            etNewUserEmail.setError("Enter email");
            return;
        }
        if (!isValidEmail(etNewUserEmail.getText().toString())) {
            etNewUserEmail.setError("Enter valid email");
            return;
        }

        if (TextUtils.isEmpty(etNewUserAddress.getText())) {
            etNewUserAddress.setError("Address is required field");
            return;
        }

        if (TextUtils.isEmpty(etNewUserPass.getText().toString())) {
            etNewUserPass.setError("Enter password");
            return;
        }
        if (!TextUtils.equals(etNewUserPass.getText().toString(), etNewUserConfPassword.getText().toString())) {
            etNewUserConfPassword.setError("Passwords not confirmed");
            return;
        }
        // Showing progress dialog at user registration time.
//        pdRegister = new ProgressDialog(this);
//        pdRegister.setMessage("Please Wait, We are Inserting Your Data on Server");
//        pdRegister.setCanceledOnTouchOutside(false);
//        pdRegister.setCancelable(false);
//        pdRegister.show();


        register(URLs.URL_REGISTER_FINAL);
        //this is worked ok
//        new Signup(getApplicationContext()).execute(
//                etNewUserName.getText().toString(),
//                "Ahmed20130075",
//                etNewUserEmail.getText().toString(),
//                "01156749640",
//                "Haram",
//                etNewUserPass.getText().toString()
//        );

//
//        new Signup2(getApplicationContext()).execute(
//                etNewUserName.getText().toString(),
//                etNewUserUserName.getText().toString(),
//                etNewUserEmail.getText().toString(),
//                etNewUserPhoneNumber.getText().toString(),
//                etNewUserAddress.getText().toString(),
//                etNewUserPass.getText().toString()
//        );

//        new SendPostReqAsyncTask().execute(
//                etNewUserName.getText().toString(),
//                etNewUserEmail.getText().toString(),
//                etNewUserPass.getText().toString());


    }


    public void register(String url) {
        // Showing progress dialog at user registration time.
        pdRegister = new ProgressDialog(this);
        pdRegister.setMessage("Please Wait, We are Inserting Your Data on Server");
        pdRegister.setCanceledOnTouchOutside(false);
        pdRegister.setCancelable(false);
        pdRegister.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdRegister.dismiss();
                //Log.e("res32312111", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String code = jObj.getString("code");
                    if (!checkMessageCondition(code, jObj)) {
                        //new AsyncSendingEmail(LoginActivity.this).execute("engahmedali2022@gmail.com", etNewUserEmail.getText().toString(), "", jObj.getString("message"));
                        //Toast.makeText(LoginActivity.this, "Not error", Toast.LENGTH_SHORT).show();
                        to = etNewUserEmail.getText().toString();
                        from = "engahmedali2022@gmail.com";
                        message = jObj.getString("data");

                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Sending email confirmation....");
                        progressDialog.setTitle("Please wait");
                        progressDialog.show();

                        new RetreiveFeedTask2(getApplicationContext()).execute();

                        clearInputs();
                    } else {
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Parse Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            private boolean checkMessageCondition(String code, JSONObject jsonObject) {
                boolean error = false;
                switch (code) {
                    case "0":
                        try {
                            if (jsonObject.getString("message").equalsIgnoreCase("username_exist".trim())) {
                                Snackbar.make(findViewById(R.id.regDrawerLayout), "Username already exists", Snackbar.LENGTH_SHORT).show();
                            } else {
                                if (jsonObject.getString("message").equalsIgnoreCase("email_exist".trim())) {
                                    etEmail.setText(etNewUserEmail.getText().toString());
                                    drawerLayout.closeDrawer(Gravity.RIGHT);
                                    Snackbar.make(findViewById(R.id.regDrawerLayout), "Email already exists", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(findViewById(R.id.regDrawerLayout), "Phone already exists", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        error = true;
                        break;
                    case "1":
                        Snackbar.make(findViewById(R.id.regDrawerLayout), "Registered successfully", Snackbar.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        etEmail.setText(etNewUserEmail.getText().toString());
                        error = false;
                        break;
                    case "2":

                        error = true;
                        break;
                    case "3":
                        error = true;
                        break;
                    case "4":
                        error = true;
                        break;
                    case "5":
                        error = true;
                        break;
                    default:
                        break;


                }
                return error;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar.make(findViewById(R.id.regDrawerLayout), "Check internet connection", Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(),
//                        "onErrorResponse" + error.getMessage(), Toast.LENGTH_LONG).show();
                pdRegister.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", etNewUserName.getText().toString());
                params.put("email", etNewUserEmail.getText().toString());
                params.put("password", etNewUserPass.getText().toString());
                params.put("username", etNewUserUserName.getText().toString());
                params.put("mobile", etNewUserPhoneNumber.getText().toString());
                params.put("address", etNewUserAddress.getText().toString());

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(strReq);
    }

    public void sendEmail(String from, String to, String subject, String messageText) {
        try {
            String host = "smtp.gmail.com";
            String user = "engahmedali2022@gmail.com";
            String pass = "123456789(*&^%$#@!Aa!@#$";
            //String to = "Ahmedramzy_fcih@yahoo.com";
            //String from = "wowrar1234@gmail.com";
            //String subject = "Congratulations, Ahmed Ramzy";
            //String messageText = "Do not tell any one, I am Ahmed Mohammed Ali Ali";
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            //java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(messageText);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            System.out.println("message send successfully");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String register2(String url, Person person) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", person.getFullName());
            jsonObject.accumulate("email", person.getEmail());
            jsonObject.accumulate("password", person.getPassword());
            jsonObject.accumulate("address", person.getAddress());
            jsonObject.accumulate("mobile", person.getPhone());
            jsonObject.accumulate("username", person.getUsername());
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
        }

        Log.e("res32311", "result = " + result);
        // 11. return result
        return result;
    }

    public void register3() {

        Person person = new Person("Ahmed Mohammed", "Ahmed20130074", "123", "wowrar1234@gmail.com", "Haram", "01156749640");
        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("name", person.getFullName());
            jsonObject.accumulate("email", person.getEmail());
            jsonObject.accumulate("password", person.getPassword());
            jsonObject.accumulate("address", person.getAddress());
            jsonObject.accumulate("mobile", person.getPhone());
            jsonObject.accumulate("username", person.getUsername());
            new SendDeviceDetails().execute(URLs.URL_REGISTER_FINAL, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void skip(View view) {


        session.setAsSkipped();
        finish();
        startActivity(new Intent(this, MainActivity.class));


    }

    private void clearInputs() {
        etNewUserEmail.getText().clear();
        etNewUserPass.getText().clear();
        etNewUserConfPassword.getText().clear();
        etNewUserName.getText().clear();
        etNewUserPhoneNumber.getText().clear();
        etNewUserAddress.getText().clear();
        etNewUserUserName.getText().clear();


    }

    public static class InputStreamToStringExample {

        public static void main(String[] args) throws IOException {

            // intilize an InputStream
            InputStream is =
                    new ByteArrayInputStream("file content..blah blah".getBytes());

            String result = getStringFromInputStream(is);

            System.out.println(result);
            System.out.println("Done");

        }

        // convert InputStream to String
        private static String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }

    }

    class RetreiveFeedTask2 extends AsyncTask<String, Void, String> {

        Context context = null;

        public RetreiveFeedTask2(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            sendEmail(from, to, "Ideas2Tech", message);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //Toast.makeText(LoginActivity.this.getApplicationContext(), from + "\n" + to, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Email sent, please update your password", Toast.LENGTH_SHORT).show();
        }
    }

    public class AsyncSendingEmail extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = null;
        Context context = null;

        public AsyncSendingEmail(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Sending email confirmation....");
            progressDialog.setTitle("Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String from = strings[0];
            String to = strings[1];
            String subject = strings[2];
            String messageText = strings[3];

            try {
                String host = "smtp.gmail.com";
                String user = "engahmedali2022@gmail.com";
                String pass = "123456789(*&^%$#@!Aa!@#$";
                //String to = "Ahmedramzy_fcih@yahoo.com";
                //String from = "wowrar1234@gmail.com";
                //String subject = "Congratulations, Ahmed Ramzy";
                //String messageText = "Do not tell any one, I am Ahmed Mohammed Ali Ali";
                boolean sessionDebug = false;

                Properties props = System.getProperties();

                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.required", "true");

                //java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                Session mailSession = Session.getDefaultInstance(props, null);
                mailSession.setDebug(sessionDebug);
                Message msg = new MimeMessage(mailSession);
                msg.setFrom(new InternetAddress(from));
                InternetAddress[] address = {new InternetAddress(to)};
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setSubject(subject);
                msg.setSentDate(new Date());
                msg.setText(messageText);

                Transport transport = mailSession.getTransport("smtp");
                transport.connect(host, user, pass);
                transport.sendMessage(msg, msg.getAllRecipients());
                transport.close();


            } catch (Exception ex) {
                //System.out.println(ex);
                ex.printStackTrace();
                return new String("failed");
            }

            return new String("success");
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == "success") {
                Snackbar.make(findViewById(R.id.regDrawerLayout), "Sent successfully", Snackbar.LENGTH_SHORT).show();

            } else {
                Snackbar.make(findViewById(R.id.regDrawerLayout), "Network error !!!!", Snackbar.LENGTH_SHORT).show();

            }
            progressDialog.dismiss();

        }
    }

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("res27233", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

    /* Inner class to get response */
    class AsyncT extends AsyncTask<Void, Void, String> {
        Person person = null;

        public AsyncT(Person person) {
            this.person = person;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URLs.URL_REGISTER_FINAL);
            try {
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("name", person.getFullName());
                jsonobj.put("email", person.getEmail());
                jsonobj.put("password", person.getPassword());
                jsonobj.put("address", person.getAddress());
                jsonobj.put("mobile", person.getPhone());
                jsonobj.put("username", person.getUsername());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                //nameValuePairs.add(new BasicNameValuePair("req", jsonobj.toString()));
                nameValuePairs.add(new BasicNameValuePair("req", "{\"name\":\"Ahmed Abdallah\",\"username\":\"ahmed\",\"email\":\"aaaaa@ddd.com\",\"password\":\"123123\",\"mobile\":\"01234567890\",\"address\":\"25 ali street\"}"));
                Log.e("res234223424", jsonobj.toString());
//                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
                // Use UrlEncodedFormEntity to send in proper format which we need
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                InputStream inputStream = response.getEntity().getContent();
                InputStreamToStringExample str = new InputStreamToStringExample();
                responseServer = str.getStringFromInputStream(inputStream);
                Log.e("response", "response -----" + responseServer);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseServer;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("res312123232", s);


        }
    }

    public class Signup extends AsyncTask<String, Void, String> {

        private Context context;
//        private ProgressDialog progressDialog = null;

        public Signup(Context context) {
            this.context = context;
//            this.progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Signing Up....");
//            progressDialog.setTitle("Please wait");
//            progressDialog.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            String userFullName = arg0[0];
            String userName = arg0[1];
            String userEmail = arg0[2];
            String userMobile = arg0[3];
            String userAddress = arg0[4];
            String userPassword = arg0[5];


            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?name=" + URLEncoder.encode(userFullName, "UTF-8");
                data += "&username=" + URLEncoder.encode(userName, "UTF-8");
                data += "&email=" + URLEncoder.encode(userEmail, "UTF-8");
                data += "&mobile=" + URLEncoder.encode(userMobile, "UTF-8");
                data += "&address=" + URLEncoder.encode(userAddress, "UTF-8");
                data += "&password=" + URLEncoder.encode(userPassword, "UTF-8");


                link = URLs.URL_REGISTER + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            String jsonStr = result;
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    String query_result = jsonObj.getString("query_result");
//                    if (query_result.equals("SUCCESS")) {
//                        Toast.makeText(context, "Data inserted successfully. Signup successful.", Toast.LENGTH_SHORT).show();
////                        progressDialog.dismiss();
//                        LoginActivity.this.pdRegister.dismiss();
//                        LoginActivity.this.drawerLayout.closeDrawer(Gravity.RIGHT);
//                        etEmail.setText(etNewUserEmail.getText().toString());
//                        clearInputs();
//                    } else if (query_result.equals("FAILURE")) {
//                        Toast.makeText(context, "Data could not be inserted. Signup failed.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                //Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
//
//            }

            Toast.makeText(context, "Data inserted successfully. Signup successful.", Toast.LENGTH_SHORT).show();
            pdRegister.dismiss();
            LoginActivity.this.pdRegister.dismiss();
            LoginActivity.this.drawerLayout.closeDrawer(Gravity.RIGHT);

        }
    }

    public class Signup2 extends AsyncTask<String, Void, String> {

        private Context context;
//        private ProgressDialog progressDialog = null;

        public Signup2(Context context) {
            this.context = context;
//            this.progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Signing Up....");
//            progressDialog.setTitle("Please wait");
//            progressDialog.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            String userFullName = arg0[0];
            String userName = arg0[1];
            String userEmail = arg0[2];
            String userMobile = arg0[3];
            String userAddress = arg0[4];
            String userPassword = arg0[5];


            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?name=" + URLEncoder.encode(userFullName, "UTF-8");
                data += "&username=" + URLEncoder.encode(userName, "UTF-8");
                data += "&email=" + URLEncoder.encode(userEmail, "UTF-8");
                data += "&mobile=" + URLEncoder.encode(userMobile, "UTF-8");
                data += "&address=" + URLEncoder.encode(userAddress, "UTF-8");
                data += "&password=" + URLEncoder.encode(userPassword, "UTF-8");


                link = URLs.URL_REGISTER_FINAL + data;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.setDoOutput(true);
                //conn.setInstanceFollowRedirects( false );
                // conn.setRequestMethod("POST");
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {


            Log.e("res32311", result.toString());


//            Toast.makeText(context, "Data inserted successfully. Signup successful.", Toast.LENGTH_SHORT).show();
//            pdRegister.dismiss();
//            LoginActivity.this.pdRegister.dismiss();
//            LoginActivity.this.drawerLayout.closeDrawer(Gravity.RIGHT);

        }

    }

    class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String userName = params[0];
            String userPassword = params[1];
            String userEmail = params[2];


            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URLs.URL_REGISTER);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("userName", userName));
                nameValuePairs.add(new BasicNameValuePair("userPassword", userPassword));
                nameValuePairs.add(new BasicNameValuePair("userEmail", userEmail));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")); //for UTF-8 enconding.

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return new String("Inserted Successfully :)");


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


        }
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLogging.setCanceledOnTouchOutside(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(URLs.URL_LOGIN);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userEmail", params[0])
                        .appendQueryParameter("userPassword", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                boolean status = jsonObject.getBoolean("error");
                if (status == false) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.regDrawerLayout), "Check password and email", Snackbar.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (pdLogging.isShowing())
                pdLogging.dismiss();

        }


    }

    public class BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;
        ProgressDialog progressDialog = null;

        public BackgroundWorker(Context ctx) {
            context = ctx;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Logging........");
//            progressDialog.setTitle("Please wait...");
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String userFullName = arg0[0];
            String userName = arg0[1];
//            String userEmail = arg0[2];
//            String userMobile = arg0[3];
//            String userAddress = arg0[4];
//            String userPassword = arg0[5];


            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?userEmail=" + URLEncoder.encode(userFullName, "UTF-8");
                data += "&userPassword=" + URLEncoder.encode(userName, "UTF-8");
//                data += "&email=" + URLEncoder.encode(userEmail, "UTF-8");
//                data += "&mobile=" + URLEncoder.encode(userMobile, "UTF-8");
//                data += "&address=" + URLEncoder.encode(userAddress, "UTF-8");
//                data += "&password=" + URLEncoder.encode(userPassword, "UTF-8");


                link = URLs.URL_LOGIN + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                Toast.makeText(context, "result = " + result.toString(), Toast.LENGTH_SHORT).show();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(String result) {
//            alertDialog.setMessage(result);
//            alertDialog.show();
            Toast.makeText(context, "result = " + result.toString(), Toast.LENGTH_SHORT).show();
            Log.e("res", result);

            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                boolean status = jsonObject.getBoolean("error");
                if (status == false) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.regDrawerLayout), "Check password and email", Snackbar.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (pdLogging.isShowing())
                pdLogging.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}




