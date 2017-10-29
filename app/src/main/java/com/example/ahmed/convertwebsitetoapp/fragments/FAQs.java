package com.example.ahmed.convertwebsitetoapp.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.model.FaqItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

//import com.example.ahmed.convertwebsitetoapp.RecordActivity;


/**
 * Created by ahmed on 3/25/2017.
 */

public class FAQs extends Fragment {


    Context context = null;
    View viewRoot = null;
    ListView listViewFags = null;
    ArrayList<FaqItem> faqItems = new ArrayList<FaqItem>();
    ListAdapter listAdapter = null;
    ProgressDialog pdFetchingFaqs = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.faqs, container, false);
        listViewFags = (ListView) viewRoot.findViewById(R.id.lvFags);
        listAdapter = new ListAdapter(getActivity(), faqItems);
        listViewFags.setAdapter(listAdapter);
        //getFags();
        fetchData(URLs.URL_FAQS);

        ((com.example.ahmed.convertwebsitetoapp.chatting.MainActivity) getActivity())
                .setActionBarTitle(getActivity().getResources().getString(R.string.nav_faqs));


        return viewRoot;
    }

    @SuppressWarnings("unchecked")
    private void fetchData(String url) {

        pdFetchingFaqs = new ProgressDialog(getContext());
        pdFetchingFaqs.setMessage("Fetching the FAQs .....");
//        pdFetchingFaqs.setCancelable(false);
//        pdFetchingFaqs.setCanceledOnTouchOutside(false);
        pdFetchingFaqs.setTitle("Please wait");
        pdFetchingFaqs.show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("fagRes", response.toString());
//                        Log.e("oooohh", modifyJson(response.toString()));

                        faqItems.clear();
                        pdFetchingFaqs.dismiss();

                        try {
                            @SuppressWarnings("unchecked")
                            JSONObject jsonObject = new JSONObject(modifyJson(response.toString()));
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.e("res1313", jsonArray.toString());
//                            Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_SHORT).show();
//                            int size = jsonArray.length();
//                            ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
//                            for (int i = 0; i < size; i++) {
//                                JSONObject another_json_object = jsonArray.getJSONObject(i);
//                                //Blah blah blah...
//                                arrays.add(another_json_object);
//                            }
//                            JSONObject[] jsons = new JSONObject[arrays.size()];
//                            arrays.toArray(jsons);
                            //Toast.makeText(context, arrays.toArray(jsons).toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String questionAr = jsonObject1.getString("fatitlear");
                                String questionEn = jsonObject1.getString("fatitleen");
                                String ansAr = jsonObject1.getString("fadescar");
                                String ansEn = jsonObject1.getString("fadescen");

                                faqItems.add(new FaqItem(questionAr, questionEn, ansAr, ansEn));
                            }

//
                            listAdapter = new ListAdapter(getContext(), faqItems);
                            //listAdapter.notifyDataSetChanged();
                            listViewFags.setAdapter(listAdapter);

                        } catch (JSONException e) {
                            //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }

                        //Log.e("resFaqs", faqItems.toString());


                    }

                    private String modifyJson(String s) {
                        String s1 = "";
                        boolean b = false;
                        for (int i = 0; i < s.length(); i++) {
                            if (s.charAt(i) == '{') {
                                b = true;
                            }
                            if (b) {
                                s1 += s.charAt(i);
                            }
                        }

                        return s1;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getActivity().getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void getFags() {

    }

    public void setDataToListView(String resultJson) {


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        fetchData(URLs.URL_FAQS);

    }

    public class BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;
        ProgressDialog progressDialog = null;

        BackgroundWorker(Context ctx) {
            context = ctx;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Logging........");
            progressDialog.setTitle("Please wait...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            //String type = params[0];
            String login_url = "https://engahmedali2022.000webhostapp.com/select_user.php";
            //if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                String post_data = URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
//                        + URLEncoder.encode("userPassword", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
//                bufferedWriter.write(post_data);
//                bufferedWriter.flush();
//                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //}
            return null;
        }

        @Override
        protected void onPreExecute() {
//            alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("Login Status");
        }

        @Override
        protected void onPostExecute(String result) {
//            alertDialog.setMessage(result);
//            alertDialog.show();
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            setDataToListView(result);


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class ListAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<FaqItem> faqItems = null;

        public ListAdapter(Context context, ArrayList<FaqItem> faqItems) {
            this.context = context;
            this.faqItems = faqItems;
        }

        @Override
        public int getCount() {
            return this.faqItems.size();
        }

        @Override
        public FaqItem getItem(int position) {
            return this.faqItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;

            view = LayoutInflater.from(context).inflate(R.layout.faq_item, null);

            TextView tvQuestion = (TextView) view.findViewById(R.id.rl).findViewById(R.id.tvQuestion);
            TextView tvAnswer = (TextView) view.findViewById(R.id.tvAnswer);


            FaqItem faqItem = getItem(position);


            String lan = "";
            //lan = Locale.getDefault().getDisplayLanguage();
            lan = Locale.getDefault().getLanguage();
            //Log.e("lan364", lan);


            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English") || Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("en")) {
                tvQuestion.setText(faqItem.getQuestionEn());
                tvAnswer.setText(faqItem.getAnswerEn());
            } else {
                tvQuestion.setText(faqItem.getQuestionAr());
                tvAnswer.setText(faqItem.getAnswerAr());
            }


            return view;
        }
    }
}
