package com.example.ahmed.convertwebsitetoapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.ahmed.convertwebsitetoapp.RecordActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.MainActivity;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.model.Drawer;
import com.example.ahmed.convertwebsitetoapp.model.ServiceItem;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by ahmed on 3/25/2017.
 */

public class AboutUs extends Fragment {

    View viewRoot = null;
    TextView tvAbout = null;
    WebView webView = null;

    private static void withinStyle(StringBuilder out, CharSequence text,
                                    int start, int end) {
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '<') {
                out.append("&lt;");
            } else if (c == '>') {
                out.append("&gt;");
            } else if (c == '&') {
                out.append("&amp;");
            } else if (c >= 0xD800 && c <= 0xDFFF) {
                if (c < 0xDC00 && i + 1 < end) {
                    char d = text.charAt(i + 1);
                    if (d >= 0xDC00 && d <= 0xDFFF) {
                        i++;
                        int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                        out.append("&#").append(codepoint).append(";");
                    }
                }
            } else if (c > 0x7E || c < ' ') {
                out.append("&#").append((int) c).append(";");
            } else if (c == ' ') {
                while (i + 1 < end && text.charAt(i + 1) == ' ') {
                    out.append("&nbsp;");
                    i++;
                }

                out.append(' ');
            } else {
                out.append(c);
            }
        }
    }

    FloatingActionButton fab = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.about, container, false);
        fab = (FloatingActionButton) viewRoot.findViewById(R.id.fabChatting);
        //setAlphaAnimation(fab);
        init();
        fetchData(URLs.URL_ABOUT_US);
        return viewRoot;
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


    private void init() {
        //tvAbout = (TextView) viewRoot.findViewById(R.id.sv).findViewById(R.id.ll).findViewById(R.id.tvAbout);
        //tvAbout.setMovementMethod(new ScrollingMovementMethod());
        webView = (WebView) viewRoot.findViewById(R.id.sv).findViewById(R.id.ll).findViewById(R.id.webViewAbout);
        webView.getSettings().setJavaScriptEnabled(true);
        setRetainInstance(true);
    }

    ProgressDialog pdLoading = null;

    private void fetchData(String url) {

        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setMessage("Preparing");
        pdLoading.setTitle("");
        pdLoading.show();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //JSONObject jsonObject = new JSONObject(modifyJson(response.toString()));
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e("aboutUs12812", jsonObject.toString());
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String dataEn = jsonObject1.getString("abdescen");
                            String dataAr = jsonObject1.getString("abdescar");
                            //Log.e("data", data);
                            //data = TextUtils.htmlEncode(data);

                            //StringBuilder out = new StringBuilder();
                            //withinStyle(out, data, 0, data.length());
                            //webView.loadData(out.toString(), "text/html; charset=UTF-8", null);

                            //tvAbout.setText(data);
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                                tvAbout.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_LEGACY));
//                            } else {
//                                tvAbout.setText(Html.fromHtml(data));
//                            }
                            webView.getSettings().setJavaScriptEnabled(true);
                            //webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
                            //webView.loadData(data, "text/html; charset=UTF-8", null);
                            //webView.loadData("<html><body>"+data+"</body></html>", "text/html; charset=utf-8", "UTF-8");
                            //webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");



                            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English")){
                                webView.loadData(URLEncoder.encode(dataEn).replaceAll("\\+", " "), "text/html", Xml.Encoding.UTF_8.toString());
                            }else{
                                webView.loadData(URLEncoder.encode(dataAr).replaceAll("\\+", " "), "text/html", Xml.Encoding.UTF_8.toString());
                            }

                            pdLoading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error343412", e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "onErrorResponse" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private String modifyJson(String s) {

        String s1 = "";
        boolean b = false, locker = true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '{' && locker) {
                b = true;
                locker = false;
            }
            if (b) {
                s1 += s.charAt(i);
            }
        }

        return s1;
    }


}
