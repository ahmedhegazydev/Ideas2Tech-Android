package com.example.ahmed.convertwebsitetoapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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
import com.example.ahmed.convertwebsitetoapp.RecordActivity;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;
import com.mancj.slideup.SlideUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//import com.example.ahmed.convertwebsitetoapp.RecordActivity;


/**
 * Created by ahmed on 3/25/2017.
 */

public class ContactUs extends Fragment {

    Context context = null;
    FloatingActionButton floatingActionButton = null;
    EditText etName, etEmail, etMessage = null;
    Button btnSendInfo = null;
    View viewRoot = null;
    Session session = null;
    ProgressDialog pdialog = null;
    String from = "";
    String message = "", to = "", userName = "", title = "", email = "";
    EditText etTitle;
    ProgressDialog progressDialog = null;
    String userId = "";
    FloatingActionButton fabChatting = null;
    View viewMain = null;
    private SlideUp slideUp;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        context = getContext();
        setHasOptionsMenu(true);
        setMenuVisibility(true);


        userId = new UserSessionManager(getActivity()).getUserDetails().get(UserSessionManager.KEY_USER_ID);
        if (TextUtils.isEmpty(userId)) {
            viewRoot = inflater.inflate(R.layout.contact_main_notloggedin, container, false);
            Toast.makeText(context, "not logged in", Toast.LENGTH_SHORT).show();
            initFieldsNotLoggedIn();
        } else {
            viewRoot = inflater.inflate(R.layout.contact_main_loggedin, container, false);
            initFieldaLoggedIn();
            Toast.makeText(context, "logged in", Toast.LENGTH_SHORT).show();

        }

        init();

        //fetching the contact info from json into textviews
        getContactInfo(URLs.URL_CONTACT_INFO);


        return viewRoot;
    }

    private void getContactInfo(String url) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("contactInfo20130074", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void initFieldaLoggedIn() {

        viewMain = viewRoot.findViewById(R.id.ll1).findViewById(R.id.ll2);

        etMessage = (EditText) viewMain.findViewById(R.id.llMessageContent).findViewById(R.id.etMessage);
        etTitle = (EditText) viewMain.findViewById(R.id.etMessageTitle);

        viewMain.findViewById(R.id.btnSendInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void initFieldsNotLoggedIn() {

        viewMain = viewRoot.findViewById(R.id.ll1).findViewById(R.id.ll2);
        etName = (EditText) viewMain.findViewById(R.id.llName).findViewById(R.id.etName);
        etEmail = (EditText) viewMain.findViewById(R.id.llEmail).findViewById(R.id.etEmail);
        etMessage = (EditText) viewMain.findViewById(R.id.llMessageContent).findViewById(R.id.etMessage);
        etTitle = (EditText) viewMain.findViewById(R.id.etMessageTitle);
        viewMain.findViewById(R.id.btnSendInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void init() {


//        final ScrollView sv = (ScrollView) viewRoot.findViewById(R.id.sv);

        WebView webView = new WebView(getActivity());
        //webView.loadUrl(getResources().getString(R.string.our_location));
        webView.loadUrl("https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d610.2222548032782!2d31.321557043330273!3d30.095951766972572!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x14581588413eaea7%3A0x948d33764f8cd050!2s7+Al+Bahreen%2C+El-Montaza%2C+Heliopolis%2C+Cairo+Governorate%2C+Egypt!5e0!3m2!1sen!2s!4v1506301732320");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new webChromeClient());
        webView.getSettings().setGeolocationEnabled(true);
        webView.setWebViewClient(new webViewClient());


//        slideUp = new SlideUp(webView);
//        slideUp.hideImmediately();
//        slideUp.setSlideListener(new SlideUp.SlideListener() {
//            @Override
//            public void onSlideDown(float v) {
//                sv.setAlpha(1 - (v / 100));
//            }
//
//            @Override
//            public void onVisibilityChanged(int i) {
//                if (i == View.GONE) {
//                    floatingActionButton.show();
//                }
//
//            }
//        });


        floatingActionButton = (FloatingActionButton) viewRoot.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //slideUp.animateIn();
                floatingActionButton.hide();
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/place/Al+Bahreen,+El-Montaza,+Heliopolis,+Cairo+Governorate,+Egypt/@30.0957698,31.3193086,17z/data=!3m1!4b1!4m5!3m4!1s0x1458158842ca8929:0x5707fab78edc89cb!8m2!3d30.0957698!4d31.3214973");
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                // Attempt to start an activity that can handle the Intent
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });

        fabChatting = (FloatingActionButton) viewRoot.findViewById(R.id.fabChatting);
        fabChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showingTheChattingView();
            }
        });
        // setAlphaAnimation(fabChatting);


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

    private void checkValidation() {
        if (TextUtils.isEmpty(userId)) {
            if (TextUtils.isEmpty(etName.getText().toString())) {
                etName.setError("Please, Enter your name");
                etName.requestFocus();
                Toast.makeText(context, "Please, Enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(etEmail.getText())) {
                etEmail.setError("Please, Enter your email");
                etEmail.requestFocus();
                Toast.makeText(context, "Please, Enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Please, Enter valid email");
                etEmail.requestFocus();
                //Toast.makeText(context, "Please, Enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(etTitle.getText().toString())) {
                etTitle.setError("Please, Enter your message title");
                etTitle.requestFocus();
                Toast.makeText(context, "Please, Enter your message title", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(etMessage.getText())) {
                etMessage.setError("Please, Enter your message");
                etMessage.requestFocus();
                Toast.makeText(context, "Please, Enter your message", Toast.LENGTH_SHORT).show();
                return;
            }


        } else {

            if (TextUtils.isEmpty(etTitle.getText().toString())) {
                etTitle.setError("Please, Enter your message title");
                etTitle.requestFocus();
                Toast.makeText(context, "Please, Enter your message title", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(etMessage.getText())) {
                etMessage.setError("Please, Enter your message");
                etMessage.requestFocus();
                Toast.makeText(context, "Please, Enter your message", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);
        new RetreiveFeedTask2().execute();
    }

    private void sendInfo() {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("engahmedali2022@gmail.com", "123456789(*&^%$#@!Aa!@#$");
            }
        });


        pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

        from = etEmail.getText().toString();
        message = etMessage.getText().toString();


        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }

    protected void sendEmail() {
        //Log.i("Send email", "");
        String[] TO = {"engahmedali2022@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, TO);
//        emailIntent.putExtra(Intent.EXTRA_BCC, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Order");
        emailIntent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
        //need this to prompts email client only
        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            //Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertDataIntoDatabase(String url) {


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving data into database");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        //from = "";
        //from = etEmail.getText().toString();
        from = "engahmedali2022@gmail.com";
        to = "engahmedali2022@gmail.com";
        if (TextUtils.isEmpty(userId)) {
            title = etTitle.getText().toString();
            message = etMessage.getText().toString();
            email = etEmail.getText().toString();
            userName = etName.getText().toString();
        } else {
            title = etTitle.getText().toString();
            message = etMessage.getText().toString();
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("res238135", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String code = jsonObject.getString("code");
                            switch (code) {
                                case "0":
                                    break;
                                case "1":
                                    Toast.makeText(getActivity(), "Inserted success", Toast.LENGTH_SHORT).show();
                                    break;
                                case "2":
                                    break;
                                case "3":
                                    break;
                                case "4":
                                    break;
                                case "5":
                                    break;
                                case "6":
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "onErrorResponse" + error.getMessage(), Toast.LENGTH_LONG).show();
                        //Log.d("ErrorResponse", error.getMessage());
                        Snackbar.make(viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (TextUtils.isEmpty(userId)) {
                    params.put("name", userName);
                    params.put("email", email);
                    params.put("body", message);
                    params.put("title", title);
                    params.put("id", "");
                } else {
                    params.put("name", "");
                    params.put("email", "");
                    params.put("body", message);
                    params.put("title", title);
                    params.put("id", userId);
                }


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat_contactus, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.chatting:
                showingTheChattingView();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showingTheChattingView() {
        Intent intent = new Intent(getActivity(), RecordActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    class webChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
            Log.d("RecordActivity", "onGeolocationPermissionHidePromt");
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, true);
            Log.d("RecordActivity", "onGeolocationPermissionShowPromt");
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("engahmedali2022@gmail.com"));
                message.setSubject("Customer Order");
                message.setContent(etMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            pdialog.dismiss();
//            etName.setText("");
//            etEmail.setText("");
//            etMessage.setText("");
            Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }

    class RetreiveFeedTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            sendEmail(from, to, "Ideas2Tech", "From : " + userName + "\n" + message);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
//            etName.setText("");
//            etEmail.setText("");
//            etMessage.setText("");
            //Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_LONG).show();
            insertDataIntoDatabase(URLs.URL_CONTACTUS);

        }
    }
}
