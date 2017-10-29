package com.example.ahmed.convertwebsitetoapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.AddNewOrderActivity;
import com.example.ahmed.convertwebsitetoapp.OrderMoreActvity;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.chatting.LoginActivity;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.model.PlanItem;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Interfaces.OneClickPaymentListener;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.PayuConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//import com.example.ahmed.convertwebsitetoapp.activities.PayFortFactory;
//import com.payu.payuui.Activity.PayUBaseActivity;

//import com.example.ahmed.convertwebsitetoapp.RecordActivity;


/**
 * Created by ahmed on 3/25/2017.
 */

/**
 * This activity prepares PaymentParams, fetches hashes from server and send it to PayuBaseActivity.java.
 * <p>
 * Implement this activity with OneClickPaymentListener only if you are integrating One Tap payments.
 */
public class OrderNow extends Fragment implements OneClickPaymentListener, ImageView.OnClickListener {

    View viewRoot = null;
    Context context = null;
    //////////////////////////////////////
//    @BindView(R.id.btnOrderWeb)
//    Button btnOrderWeb;
//    @BindView(R.id.btnOrderApp)
//    Button btnOrderApp;
    View.OnClickListener myClkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }


    };
    FloatingActionButton fabChatting = null;
    ProgressDialog pdLoading = null;
    Spinner spinnerPlans = null;
    ArrayList<PlanItem> planItems = new ArrayList<PlanItem>();
    ArrayList<String> SPINNERLIST = new ArrayList<String>();
    boolean filter = false;
    boolean flag = true;
    Menu menu = null;
    boolean thereIsSelected = true;
    ListView listViewPlans = null;
    ListViewAdapter listAdapter = null;
    String cat = "";
    ArrayList<PlanItem> selectedPlanItem = new ArrayList<PlanItem>();
    TextView tvDataNotExist = null;
    ImageView ivAddNewOrder = null;
    RelativeLayout rlDataNotExist = null, rlMainOrderNow = null;
    //////////////////////////
    private String merchantKey, userCredentials;
    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;
    // This sets the configuration
    private PayuConfig payuConfig;
    private Spinner environmentSpinner;
    /////////////////////////////////////////
    // Used when generating hash from SDK
    private PayUChecksum checksum;
    private Unbinder unbinder;

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
        viewRoot = inflater.inflate(R.layout.order_now, container, false);
        rlMainOrderNow = (RelativeLayout) viewRoot.findViewById(R.id.rlMainOrderNow);

        init();
        setRetainInstance(true);
        fetchingData(URLs.URL_ORDER_NOW);


        ((com.example.ahmed.convertwebsitetoapp.chatting.MainActivity) getActivity())
                .setActionBarTitle(getActivity().getResources().getString(R.string.nav_order_now));


        ///preparePayment();
        return viewRoot;
    }

    private void fetchingData(String url) {


        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setMessage("Preparing...");
        pdLoading.setTitle("Please wait");
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
                            Log.e("plans2391", jsonObject.toString());
                            String message = "";
                            message = jsonObject.getString("message");


                            if (message.equalsIgnoreCase("data_exist")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                String price = null, titleEn = null, titleAr = null, descAr = null, descEn = null, catAr = null, catEn = null;
                                String planId = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    planId = jsonObject1.getString("plid");
                                    titleAr = jsonObject1.getString("pltitlear");
                                    titleEn = jsonObject1.getString("pltitleen");
                                    descAr = jsonObject1.getString("pldescar");
                                    descEn = jsonObject1.getString("pldescen");
                                    catAr = jsonObject1.getString("categoryar");
                                    catEn = jsonObject1.getString("categoryen");
                                    price = jsonObject1.getString("plprice");

                                    if (cat == "") {
                                        planItems.add(new PlanItem(planId, titleAr, titleEn, descAr, descEn, catAr, catEn, price));
                                    } else {
                                        if (cat == descEn) {
                                            planItems.add(new PlanItem(planId, titleAr, titleEn, descAr, descEn, catAr, catEn, price));
                                        }
                                    }

                                    if (!SPINNERLIST.contains(catEn)) {
                                        SPINNERLIST.add(catEn);
                                    }


                                }


                                listAdapter = new ListViewAdapter(getActivity(), planItems);
                                listViewPlans.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();


                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
                                spinnerPlans.setAdapter(arrayAdapter);
                            } else {

                                //data not exist
                                rlDataNotExist.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            //e.printStackTrace();
                            //Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            //Log.e("error343412", e.getMessage());
                            //Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();

                        } finally {
                            pdLoading.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "onErrorResponse" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/rlMainOrderNow, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
                Snackbar.make(viewRoot, "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

//    @OnClick(R.id.btnOrderWeb)
//    public void btnWantWebSire(View view) {
//        Toast.makeText(context, "want a website", Toast.LENGTH_SHORT).show();
//
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        if (!new UserSessionManager(getActivity()).isUserLoggedIn()) {
            getActivity().getMenuInflater().inflate(R.menu.menu_order_now, menu);
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (thereIsSelected) {
            this.menu.clear();
            getActivity().getMenuInflater().inflate(R.menu.menu_social_media_logged, this.menu);
        } else {
            this.menu.clear();
            getActivity().getMenuInflater().inflate(R.menu.menu_order_more, this.menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                //Toast.makeText(context, "Show the login page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.thereAreMoreSelected:
                Intent intent = new Intent(getActivity(), OrderMoreActvity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) selectedPlanItem);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);


                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void preparePayment() {
//        Wallet.Payments.isReadyToPay(mGoogleApiClient).setResultCallback(new ResultCallback<BooleanResult>() {
//            @Override
//            public void onResult(@NonNull BooleanResult booleanResult) {
//                if (booleanResult.getStatus().isSuccess()) {
//                    if (booleanResult.getValue()) {
//                        // Show Android Pay buttons alongside regular checkout button
//                        // ...
//                    } else {
//                        // Hide Android Pay buttons, show a message that Android Pay
//                        // cannot be used yet, and display a traditional checkout button
//                        // ...
//                    }
//                } else {
//                    // Error making isReadyToPay call
//                    Log.e(TAG, "isReadyToPay:" + booleanResult.getStatus());
//                }
//            }
//        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        btnOrderApp.setOnClickListener(myClkListener);
//        btnOrderWeb.setOnClickListener(myClkListener);
        ////////////////////////////////


    }

    public void init() {

        spinnerPlans = (Spinner) viewRoot.findViewById(R.id.ll).findViewById(R.id.spinnerPlans);
        spinnerPlans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat = (String) adapterView.getItemAtPosition(i);
                //Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                //fetchingData(URLs.URL_ORDER_NOW);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //fetchingData(URLs.URL_ORDER_NOW);
            }
        });

        ivAddNewOrder = (ImageView) viewRoot.findViewById(R.id.ll).findViewById(R.id.ivAddNewOrder);
        ivAddNewOrder.setOnClickListener(this);

        listViewPlans = (ListView) viewRoot.findViewById(R.id.listViewPlans);
        listAdapter = new ListViewAdapter(getActivity(), planItems);
        listViewPlans.setAdapter(listAdapter);

        rlDataNotExist = (RelativeLayout) viewRoot.findViewById(R.id.rlDataNotExist);
        rlDataNotExist.findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchingData(URLs.URL_ORDER_NOW);
            }
        });


        fabChatting = (FloatingActionButton) viewRoot.findViewById(R.id.fabChatting);
        //setAlphaAnimation(fabChatting);
//        fabChatting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().startActivity(new Intent(getActivity(), PayFortFactory.class));
//            }
//        });
//        ///////////////////
        context = getActivity();
        //////////////////////////////////////////
        ButterKnife.bind(getActivity(), viewRoot);
        ///////////////////////////////////////
        setHasOptionsMenu(true);
        setMenuVisibility(true);
        /////////////////////////////////////
//        //TODO Must write this code if integrating One Tap payments
//        OnetapCallback.setOneTapCallback(this);
//
//        //TODO Must write below code in your activity to set up initial context for PayU
//        Payu.setInstance(getActivity());


    }

    public void startActivityForViewing(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    /**
     * This method prepares all the payments params to be sent to PayuBaseActivity.java
     */
    public void navigateToBaseActivity(View view) {

//        merchantKey = ((EditText) findViewById(R.id.editTextMerchantKey)).getText().toString();
//        String amount = ((EditText) findViewById(R.id.editTextAmount)).getText().toString();
//        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();


        merchantKey = "gtkFFx";
        String amount = "10";
        String email = "finance@ideas2tech.net";

        String value = environmentSpinner.getSelectedItem().toString();
        int environment = PayuConstants.STAGING_ENV;
//        String TEST_ENVIRONMENT = getResources().getString(R.string.test);
//        if (value.equals(TEST_ENVIRONMENT))
//            environment = PayuConstants.STAGING_ENV;
//        else
//            environment = PayuConstants.PRODUCTION_ENV;

        userCredentials = merchantKey + ":" + email;

        //TODO Below are mandatory params for hash genetation
        mPaymentParams = new PaymentParams();
        /**
         * For Test Environment, merchantKey = "gtKFFx"
         * For Production Environment, merchantKey should be your live key or for testing in live you can use "0MQaQP"
         */
        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(amount);
        mPaymentParams.setProductInfo("product_info");
        mPaymentParams.setFirstName("firstname");
        mPaymentParams.setEmail("xyz@gmail.com");

        /*
        * Transaction Id should be kept unique for each transaction.
        * */
        mPaymentParams.setTxnId("" + System.currentTimeMillis());

        /**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         */
        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");

        /*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * */
        mPaymentParams.setUdf1("udf1");
        mPaymentParams.setUdf2("udf2");
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");

        /**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * */
        mPaymentParams.setUserCredentials(userCredentials);

        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);

        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        generateHashFromServer(mPaymentParams);

        /**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * */
        //String salt = "13p0PXZk";
        //generateHashFromSDK(mPaymentParams, salt);

    }

    /**
     * This method generates hash from server.
     *
     * @param mPaymentParams payments params used for hash generation
     */
    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This method prepares a HashMap of cardToken as key and merchantHash as value.
     *
     * @param merchantKey     merchant key used
     * @param userCredentials unique credentials of the user usually of the form key:userId
     */
    public HashMap<String, String> getAllOneClickHashHelper(String merchantKey, String userCredentials) {

        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        HashMap<String, String> cardTokens = new HashMap<String, String>();

        try {
            //TODO Replace below url with your server side file url.
            URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

            byte[] postParamsByte = postParams.getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postParamsByte);

            InputStream responseInputStream = conn.getInputStream();
            StringBuffer responseStringBuffer = new StringBuffer();
            byte[] byteContainer = new byte[1024];
            for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                responseStringBuffer.append(new String(byteContainer, 0, i));
            }

            JSONObject response = new JSONObject(responseStringBuffer.toString());

            JSONArray oneClickCardsArray = response.getJSONArray("data");
            int arrayLength;
            if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                for (int i = 0; i < arrayLength; i++) {
                    cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                }

            }
            // pass these to next activity

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cardTokens;
    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method deletes merchantHash and cardToken from server side file.
     *
     * @param cardToken cardToken of card whose merchantHash and cardToken needs to be deleted from merchant server
     */
    private void deleteMerchantHash(String cardToken) {

        final String postParams = "card_token=" + cardToken;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/delete_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * Returns a HashMap object of cardToken and one click hash from merchant server.
     * <p>
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * The function should return a cardToken and corresponding one click hash as a hashMap.
     *
     * @param userCreds a string giving the user credentials of user.
     * @return the Hash Map of cardToken and one Click hash.
     **/
    @Override
    public HashMap<String, String> getAllOneClickHash(String userCreds) {
        // 1. GET http request from your server
        // GET params - merchant_key, user_credentials.
        // 2. In response we get a
        // this is a sample code for fetching one click hash from merchant server.
        return getAllOneClickHashHelper(merchantKey, userCreds);
    }

    //TODO This method is used only if integrating One Tap Payments

    //TODO This method is used only if integrating One Tap Payments
    @Override
    public void getOneClickHash(String cardToken, String merchantKey, String userCredentials) {

    }

    /**
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * This function save the oneClickHash corresponding to its cardToken
     *
     * @param cardToken    a string containing the card token
     * @param oneClickHash a string containing the one click hash.
     **/
    @Override
    public void saveOneClickHash(String cardToken, String oneClickHash) {
        // 1. POST http request to your server
        // POST params - merchant_key, user_credentials,card_token,merchant_hash.
        // 2. In this POST method the oneclickhash is stored corresponding to card token in merchant server.
        // this is a sample code for storing one click hash on merchant server.

        storeMerchantHash(cardToken, oneClickHash);

    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method stores merchantHash and cardToken on merchant server.
     *
     * @param cardToken    card token received in transaction response
     * @param merchantHash merchantHash received in transaction response
     */
    private void storeMerchantHash(String cardToken, String merchantHash) {

        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials + "&card_token=" + cardToken + "&merchant_hash=" + merchantHash;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    //TODO Deploy a file on your server for storing cardToken and merchantHash nad replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/store_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }

    //TODO This method is used if integrating One Tap Payments

    /**
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * This function delete’s the oneClickHash from the merchant server
     *
     * @param cardToken       a string containing the card token
     * @param userCredentials a string containing the user credentials.
     **/
    @Override
    public void deleteOneClickHash(String cardToken, String userCredentials) {

        // 1. POST http request to your server
        // POST params  - merchant_hash.
        // 2. In this POST method the oneclickhash is deleted in merchant server.
        // this is a sample code for deleting one click hash from merchant server.

        deleteMerchantHash(cardToken);

    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
     *
     * @param payuHashes it contains all the hashes generated from merchant server
     */
    public void launchSdkUI(PayuHashes payuHashes) {

//        Intent intent = new Intent(getActivity(), PayUBaseActivity.class);
//        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
//        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
//        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
//
//        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

    }
    //Lets fetch all the one click card tokens first
    //fetchMerchantHashes(intent);


    /**
     * This method fetches merchantHash and cardToken already stored on merchant server.
     */
    private void fetchMerchantHashes(final Intent intent) {
        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        final Intent baseActivityIntent = intent;
        new AsyncTask<Void, Void, HashMap<String, String>>() {

            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());

                    HashMap<String, String> cardTokens = new HashMap<String, String>();
                    JSONArray oneClickCardsArray = response.getJSONArray("data");
                    int arrayLength;
                    if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                        for (int i = 0; i < arrayLength; i++) {
                            cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                        }
                        return cardTokens;
                    }
                    // pass these to next activity

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> oneClickTokens) {
                super.onPostExecute(oneClickTokens);

                baseActivityIntent.putExtra(PayuConstants.ONE_CLICK_CARD_TOKENS, oneClickTokens);
                startActivityForResult(baseActivityIntent, PayuConstants.PAYU_REQUEST_CODE);
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {

        if (view.equals(ivAddNewOrder)) {
            if (TextUtils.isEmpty(new UserSessionManager(getActivity()).getUserDetails().get(UserSessionManager.KEY_USER_ID))) {
                startActivity(new Intent(OrderNow.this.getActivity(), LoginActivity.class).putExtra("login", "login"));
                Toast.makeText(context, "Not logged in", Toast.LENGTH_SHORT).show();

            } else {
                startActivity(new Intent(OrderNow.this.getActivity(), AddNewOrderActivity.class));
                Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show();
            }
        }


    }

    class ListViewAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<PlanItem> planItems = new ArrayList<PlanItem>();

        public ListViewAdapter(Context context, ArrayList<PlanItem> planItems) {
            this.context = context;
            this.planItems = planItems;
        }


        @Override
        public int getCount() {
            return this.planItems.size();
        }

        @Override
        public PlanItem getItem(int position) {
            return this.planItems.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View v = null;

            v = LayoutInflater.from(context).inflate(R.layout.plan_list_item, null);
            v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
            v.setAlpha(.9f);

            final PlanItem planItem = getItem(position);


            TextView tvPlanTitle = null;
            TextView tvPlanOrderNow = null;
            TextView tvPlanDesc = null;
            TextView tvPlanPrice = null;
            CheckBox checkBox = null;

            tvPlanTitle = (TextView) v.findViewById(R.id.rlCheckBox).findViewById(R.id.tvPlanTitle);
            checkBox = (CheckBox) v.findViewById(R.id.rlCheckBox).findViewById(R.id.cbPlanItem);
            tvPlanOrderNow = (TextView) v.findViewById(R.id.tvPlanOrderNow);
            tvPlanDesc = (TextView) v.findViewById(R.id.llDescAndPrice).findViewById(R.id.tvPlanDesc);
            tvPlanPrice = (TextView) v.findViewById(R.id.llDescAndPrice).findViewById(R.id.tvPrice);

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                tvPlanDesc.setText(Html.fromHtml(planItem.getDescEn(), Html.FROM_HTML_MODE_LEGACY));
//            } else {
//                tvPlanDesc.setText(Html.fromHtml(planItem.getDescEn()));
//            }
            tvPlanDesc.setText(Html.fromHtml(planItem.getDescEn()).toString());
            tvPlanTitle.setText(planItem.getTitleEn());
            tvPlanOrderNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForViewing(URLs.PAYPAL);
                    //startActivityForViewing("https://www.paypal.com/cgi-bin/webscr?charset='utf-8'&cmd='_xclick'&business='info@ideas2tech.com'&item_name=''&item_number=''&amount=''&quantity=''&quantity=''&currency_code='USD'&no_shipping=1&handling=0&cancel_return='https://ideas2tech/pay_cancel.php'&success_return='https://ideas2tech/pay_success.php'");
                }
            });
            tvPlanPrice.setText(planItem.getPrice());


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Iterator<PlanItem> iterator = null;
                    if (compoundButton.isChecked()) {
                        planItem.setChecked(true);
                        if (!selectedPlanItem.contains(planItem)) {
                            selectedPlanItem.add(planItem);
                        }
                    } else {
                        if (selectedPlanItem.contains(planItem)) {
                            iterator = selectedPlanItem.iterator();
                            while (iterator.hasNext()) {
                                PlanItem planItem1 = iterator.next();
                                if (planItem.equals(planItem1)) {
                                    iterator.remove();
                                    break;
                                }
                            }
                        }
                        planItem.setChecked(false);
                    }
                    if (!selectedPlanItem.isEmpty()) {
                        thereIsSelected = false;
                        onPrepareOptionsMenu(OrderNow.this.menu);
                    } else {
                        thereIsSelected = true;
                        onPrepareOptionsMenu(OrderNow.this.menu);
                    }
                    //Toast.makeText(context, selectedPlanItem.size()+"", Toast.LENGTH_SHORT).show();
                }
            });
            if (planItem.isChecked()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }


            return v;
        }


    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature
                        /**
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        /**
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        /**
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key
                        /**
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         *
                         */
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }

}
