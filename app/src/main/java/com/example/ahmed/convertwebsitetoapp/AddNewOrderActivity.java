package com.example.ahmed.convertwebsitetoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AddNewOrderActivity extends AppCompatActivity {

    String userId = "", userFullName = "", userEmail = "", userPhoneNumber = "", whatsAppNumber = "",
            serviceId = "";
    ProgressDialog pdLogging = null;
    UserSessionManager userSessionManager = null;
    EditText etFirstName, etLastName, etUserEmail, etPhoneNumber = null, etDesc, etWhatsAppNumber;
    Spinner spinnerServiceType = null;
    ArrayList<String> SPINNERLIST = new ArrayList<String>();
    Context context = null;
    View viewRoot = null;
    HashMap<String, String> hmCatWithIds = new HashMap<String, String>();
    String spinnerValue = "";
    String fullName = "";
    String desc = "";
    String phone = "", email = "";
    String from = "", to = "", messageBody = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);


        context = this;
        viewRoot = findViewById(R.id.llAddNewOrder);

        getUserId();
        initFields();
        getAllCatToSpinner(URLs.URL_SERVICES);


    }

    private void getAllCatToSpinner(String url) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //JSONObject jsonObject = new JSONObject(modifyJson(response.toString()));
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e("plans2391", jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String catId = "";
                                String catTitleEn = "";
                                String catTitleAr = "";

                                catId = jsonObject1.getString("cgid");
                                catTitleAr = jsonObject1.getString("cgtitlear");
                                catTitleEn = jsonObject1.getString("cgtitleen");

                                hmCatWithIds.put(catId, catTitleEn);


                                if (!SPINNERLIST.contains(catTitleEn)) {
                                    SPINNERLIST.add(catTitleEn);
                                }
                            }


//
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
                            spinnerServiceType.setAdapter(arrayAdapter);


                        } catch (JSONException e) {
                            //e.printStackTrace();
                            //Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            //Log.e("error343412", e.getMessage());
                            Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();

                        } finally {
                            //pdLoading.dismiss();
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

    private void initFields() {
        etUserEmail = ((TextInputLayout) findViewById(R.id.etEmail)).getEditText();
        etFirstName = ((TextInputLayout) findViewById(R.id.etFirstName)).getEditText();
        etLastName = ((TextInputLayout) findViewById(R.id.etLastNane)).getEditText();
        etDesc = ((TextInputLayout) findViewById(R.id.etWhatDoUWant)).getEditText();
        etWhatsAppNumber = ((TextInputLayout) findViewById(R.id.etWhatsNumber)).getEditText();
        etPhoneNumber = ((TextInputLayout) findViewById(R.id.etPhoneNumber)).getEditText();

        spinnerServiceType = (Spinner) findViewById(R.id.spinnerServiceTpe);
        spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                spinnerValue = spinnerServiceType.getSelectedItem().toString();
                for (String key : hmCatWithIds.keySet()) {
                    if (hmCatWithIds.get(key).equalsIgnoreCase(spinnerValue)) {
                        serviceId = key;
                        break;
                    }
                }
                Toast.makeText(context, serviceId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getUserId() {

        userSessionManager = new UserSessionManager(getApplicationContext());
        userId = userSessionManager.getUserDetails().get(UserSessionManager.KEY_USER_ID);
        //check if the id is empty = the user didn't logged in
        //otherwise the user logged in already
        if (userId.toString().length() == 0) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            userId = "0";


        } else {
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
        }
        //=-----------------------------------------
    }

    public void btnSendMyOrder(View view) {

        //the user not logged in
        if (userId.equalsIgnoreCase("0")) {

            if (TextUtils.isEmpty(etFirstName.getText())) {
                etFirstName.setError("Enter FirstName");
                etFirstName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(etLastName.getText())) {
                etLastName.setError("Enter LastName");
                etLastName.requestFocus();
                return;

            }
            if (TextUtils.isEmpty(etUserEmail.getText())) {
                etUserEmail.setError("Enter Email");
                etUserEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(etPhoneNumber.getText())) {
                etPhoneNumber.setError("Enter Phone");
                etPhoneNumber.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(etDesc.getText())) {
                etDesc.setError("Enter the description");
                etDesc.requestFocus();
                return;
            }
        } else {
            if (TextUtils.isEmpty(etDesc.getText())) {
                etDesc.setError("Enter the description");
                etDesc.requestFocus();
                return;
            }

        }


        spinnerValue = spinnerServiceType.getSelectedItem().toString();
        for (String key : hmCatWithIds.keySet()) {
            if (hmCatWithIds.get(key).equalsIgnoreCase(spinnerValue)) {
                serviceId = key;
                break;
            }
        }


        whatsAppNumber = String.valueOf(etWhatsAppNumber.getText());
        userFullName = etFirstName.getText().toString() + " " + etLastName.getText().toString();
        desc = etDesc.getText().toString();
        phone = etPhoneNumber.getText().toString();
        email = etUserEmail.getText().toString();


        Log.e("whatsNumber", whatsAppNumber);
        Log.e("servId", serviceId);
        Log.e("desc", desc);
        Log.e("phone", phone);
        Log.e("email", email);
        Log.e("spinner", spinnerValue);


        sendData(URLs.URL_ADD_NEW_ORDER);


    }

    private void sendData(String url) {

        pdLogging = new ProgressDialog(this);
        pdLogging.setMessage("Saving data ...");
        pdLogging.setTitle("Please wait");
        pdLogging.setCancelable(false);
        pdLogging.setCanceledOnTouchOutside(false);
        pdLogging.show();


        // if everything is fine
        //Tag used to cancel the request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("newOrder", response.toString());

                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());
                            String message = "";
                            message = jsonObject1.getString("message");
                            if (message.equalsIgnoreCase("success")) {
                                //sending the email

                                String orderNumber = "";
                                String payEmail = "";

                                orderNumber = jsonObject1.getString("order_number");
                                payEmail = jsonObject1.getString("payemail");

                                from = to = payEmail;
                                messageBody = "The adasdasd from "+orderNumber;


                                new AsyncSendingEmail(context).execute();


                            } else {
                                Snackbar.make(viewRoot, message, Snackbar.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {
                            Snackbar.make(viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
                        } finally {
                            pdLogging.dismiss();

                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, "onErrorResponse" + error.getMessage(), Toast.LENGTH_LONG).show();
                        //Log.e("ErrorResponse", error.getMessage().toString());
                        //error.printStackTrace();
                        Snackbar.make(viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (userId == "0") {
                    userId = "";
                    params.put("id", userId);
                    params.put("name", userFullName);
                    params.put("email", email);
                    params.put("mobile", phone);
                    params.put("wsup", whatsAppNumber);
                    params.put("service", serviceId);
                    params.put("description", desc);

                } else {
                    params.put("wsup", whatsAppNumber);
                    params.put("service", serviceId);
                    params.put("description", desc);

                    params.put("id", userId);// = ""
                    params.put("name", "");
                    params.put("email", "");
                    params.put("mobile", "");
                    params.put("wsup", "");


                }
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
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

    class AsyncSendingEmail extends AsyncTask<String, Void, String> {


        ProgressDialog pdSeningEmailToAdmin = null;
        Context context = null;

        public AsyncSendingEmail(Context context) {

            this.context = context;
            pdSeningEmailToAdmin = new ProgressDialog(context);
            pdSeningEmailToAdmin.setMessage("Sending email to admin");
            pdSeningEmailToAdmin.setTitle("Please wait");
            pdSeningEmailToAdmin.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            sendEmail(from, to, "Ideas2Tech", messageBody);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (pdSeningEmailToAdmin.isShowing()) {
                pdSeningEmailToAdmin.dismiss();
            }


        }
    }


}
