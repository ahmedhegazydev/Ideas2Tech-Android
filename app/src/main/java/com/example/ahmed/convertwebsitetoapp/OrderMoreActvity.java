package com.example.ahmed.convertwebsitetoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.chatting.*;
import com.example.ahmed.convertwebsitetoapp.chatting.LoginActivity;
import com.example.ahmed.convertwebsitetoapp.chatting.MainActivity;
import com.example.ahmed.convertwebsitetoapp.model.PlanItem;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderMoreActvity extends AppCompatActivity {

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AaceqhNh__GoqDwQ3p2tOyhpu0KHsUcQTwj5LnedN2OZwloxt2H4mqlI69twVZtURENhrQ8K2V16fOC1";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private static final String TAG = "paymentExample";
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    ArrayList<PlanItem> planIdsItems = null;
    double price = 0d;
    String planIds = "";
    String userId = "";
    UserSessionManager userSessionManager = null;
    HashMap<String, String> hashMap = null;
    ArrayList<String> ids = new ArrayList<String>();
    ProgressDialog progressDialog = null;
    String id = "", orderCode = "", payEmail = "", currenyCode = "";
    ArrayList<String> plansTitles = new ArrayList<String>();
    ProgressDialog pdPayment = null;
    String orderNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_more);


        init();
        getOrderNumber(URLs.URL_PAY);


    }

    private void init() {

        //fetching the planIdss that have been selected by user before
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        planIdsItems = (ArrayList<PlanItem>) args.getSerializable("ARRAYLIST");
        //Toast.makeText(this, planIdsItems.size()+"", Toast.LENGTH_SHORT).show();


        for (int i = 0; i < planIdsItems.size(); i++) {
            PlanItem planIdsItem = planIdsItems.get(i);
            //then calculate the sum of prcies for all selected planIdss
            price += Double.valueOf(planIdsItem.getPrice());
            //adding the id to arraylist for future processing
            ids.add(planIdsItem.getId());
            //adding the plans titles for setting them into PayPalPayment object as order name
            plansTitles.add(planIdsItem.getTitleEn());


        }
        Log.e("ids32312", ids.toString());
        //setting up the planIds ids
        String separator = "";  // separator here is your ","
        for (String s : ids) {
            planIds = planIds.concat(separator.concat(s));
            separator = ",";
        }
        //price = new DecimalFormat("##.##").format(price);
        price = Double.parseDouble(new DecimalFormat("##.##").format(price));


        //getting the userid
        userSessionManager = new UserSessionManager(getApplicationContext());
        hashMap = userSessionManager.getUserDetails();
        userId = hashMap.get(UserSessionManager.KEY_USER_ID);


        //test
//        Log.e("userId", userId);
//        Log.e("price", price + "");
//        Log.e("planIds", planIds);


    }

    private void getOrderNumber(String url) {

        progressDialog = new ProgressDialog(OrderMoreActvity.this);
        progressDialog.setMessage("Processing");
        progressDialog.setTitle("");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("pays127126", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e("pays127126", jsonObject.toString());

                            id = jsonObject.getString("data");
                            currenyCode = jsonObject.getString("currency");
                            payEmail = jsonObject.getString("payemail");
                            orderNumber = jsonObject.getString("order_number");
                            orderCode = jsonObject.getString("order_code");

//                            Log.e("id", id);
                            //Log.e("currencyCode", currenyCode);
//                            Log.e("payEmail", payEmail);
//                            Log.e("orderNumber", orderCode);
                            Log.e("orderCode3123", orderCode);


                            if (id != "") {
                                //success
                                Log.e("data723", id);


//                                new SendDataToPaypal(OrderMoreActvity.this).execute(
//                                        URLs.PAYPAL,
//                                        "utf-8",
//                                        "_xclick",
//                                        payEmail,
//                                        "",
//                                        "",
//                                        String.valueOf(price),
//                                        "1",
//                                        currenyCode,
//                                        "1",
//                                        "0",
//                                        "https://ideas2tech.000webhostapp.com/ideas2tech/pay_cancel-en/".concat(id),
//                                        "https://ideas2tech.000webhostapp.com/ideas2tech/pay_success-en/".concat(id)
//
//                                );
                                onBuyPressed(null);

                            } else {
                                //failure

                            }

                        } catch (Exception e) {
                            Toast.makeText(OrderMoreActvity.this, "getOrderNumber", Toast.LENGTH_LONG).show();
                        } finally {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Snackbar.make(findViewById(R.id.rlPayActionResult), "Network Error !!!", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userId);
                params.put("plan", planIds);
                params.put("price", String.valueOf(price));
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void onBuyPressed(View pressed) {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(OrderMoreActvity.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
//        return new PayPalPayment(new BigDecimal("0.01"), "USD", "sample item",
//                paymentIntent);

        //adding new line after each plan title
        String orderName = "";
        for (PlanItem planItem : planIdsItems) {
            orderName = orderName.concat(planItem.getTitleEn()).concat("\n");

        }
        //currenyCode = "EUR";
        return new PayPalPayment(new BigDecimal(price), currenyCode, orderName,
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        //displayResultText("PaymentConfirmation info received from PayPal");
                        paymentSuccessed(URLs.PAYPAL2);

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

    }

    protected void displayResultText(String result) {
        Toast.makeText(
                getApplicationContext(),
                result, Toast.LENGTH_LONG)
                .show();
    }

    public void paymentSuccessed(String url) {
        //if everything is fine
        // Tag used to cancel the request


        pdPayment = new ProgressDialog(OrderMoreActvity.this);
        pdPayment.setMessage("Sending data .....");
        pdPayment.setTitle("Please wait");
        pdPayment.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("pay4825q2$$", response.toString());
                        if (pdPayment.isShowing())
                            pdPayment.dismiss();


                        Intent intent = new Intent(OrderMoreActvity.this, PrevOrdersActivity.class);
                        startActivity(intent);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, "onErrorResponse" + error.getMessage(), Toast.LENGTH_LONG).show();
                        //Log.d("ErrorResponse", error.getMessage());
                        Snackbar.make(findViewById(R.id.regDrawerLayout), "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
                        if (!pdPayment.isShowing()) {
                            pdPayment.dismiss();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userId);
                params.put("code", orderCode);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    ;

    public class SendDataToPaypal extends AsyncTask<String, Void, String> {

        private Context context;
        private ProgressDialog progressDialog = null;

        public SendDataToPaypal(Context context) {
            this.context = context;
            this.progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Dealing with paypal....");
            progressDialog.setTitle("Please wait");
            progressDialog.show();
            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            int i = 0;
            String payUrl = params[i];
            String charSet = params[++i];
            String cmd = params[++i];
            String business = params[++i];
            String itemName = params[++i];
            String itemNumber = params[++i];
            String amount = params[++i];
            String quantity = params[++i];
            String currency_code = params[++i];
            String no_shipping = params[++i];
            String handling = params[++i];
            String cancel_return = params[++i];
            String success_return = params[++i];


            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?charset=" + URLEncoder.encode(charSet, "UTF-8");
                data += "&cmd=" + URLEncoder.encode(cmd, "UTF-8");
                data += "&business=" + URLEncoder.encode(business, "UTF-8");
                data += "&item_name=" + URLEncoder.encode(itemName, "UTF-8");
                data += "&item_number=" + URLEncoder.encode(itemNumber, "UTF-8");
                data += "&amount=" + URLEncoder.encode(amount, "UTF-8");
                data += "&quantity=" + URLEncoder.encode(quantity, "UTF-8");
                data += "&currency_code=" + URLEncoder.encode(currency_code, "UTF-8");
                data += "&no_shipping=" + URLEncoder.encode(no_shipping, "UTF-8");
                data += "&handling=" + URLEncoder.encode(handling, "UTF-8");
                data += "&cancel_return=" + URLEncoder.encode(cancel_return, "UTF-8");
                data += "&success_return=" + URLEncoder.encode(success_return, "UTF-8");

                link = payUrl + data;
                Log.e("completeLink", link);
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            String jsonStr = result;
            Toast.makeText(context, "Dealt with paypal successfully", Toast.LENGTH_SHORT).show();
            Log.e("dealing_result277", jsonStr);


        }
    }

}
