package com.example.ahmed.convertwebsitetoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.model.OrderPrevItem;
import com.example.ahmed.convertwebsitetoapp.sessions.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrevOrdersActivity extends AppCompatActivity {

    Context context = null;
    ProgressDialog pdLoading = null;
    UserSessionManager userSessionManager = null;
    String userId = "";
    ListViewAdapter listViewAdapter = null;
    ArrayList<OrderPrevItem> orderPrevItems = new ArrayList<OrderPrevItem>();
    ListView lvPrevOrders = null;
    ArrayList<String> listPlanTitles = new ArrayList<String>();
    String currency = "";
    HashMap<String, String> hmPlanTitles = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_orders);


        init();
        fetchDaata(URLs.URL_PREV_ORDERS);


    }

    private void init() {
        context = this;

        //getting the user id
        userSessionManager = new UserSessionManager(getApplicationContext());
        userId = userSessionManager.getUserDetails().get(UserSessionManager.KEY_USER_ID);

        //init the listview
        lvPrevOrders = (ListView) findViewById(R.id.lvPrevOrders);


    }

    private void fetchDaata(String url) {

        pdLoading = new ProgressDialog(context);
        pdLoading.setMessage("Preparing...");
        pdLoading.setTitle("Please wait");
        pdLoading.show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        orderPrevItems.clear();
                        try {

                            //JSONObject jsonObject = new JSONObject(modifyJson(response.toString()));
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e("histPlans", jsonObject.toString());
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            JSONArray jsonArray = jsonObject1.getJSONArray("ordersnow");
                            Log.e("array", jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject11 = jsonArray.getJSONObject(i);
                                String orderNumber = "";
                                String orderStatus = "";
                                String orderPrice = "";
                                String planIds = "";
                                String orderName = "";
                                String currencyCode = "";
                                String orderDesc = jsonObject11.getString("ondesc");


                                orderNumber = jsonObject11.getString("onnumber");
                                orderPrice = jsonObject11.getString("onprice");
                                orderStatus = jsonObject11.getString("ontype");
                                //planIds = jsonObject11.getString("plans");
                                orderName = jsonObject11.getString("cgtitleen");
                                //currencyCode = jsonObject11.getString("");

                                orderPrevItems.add(
                                        new OrderPrevItem(orderNumber, orderPrice, orderStatus, planIds, orderName, orderDesc)
                                );


                            }

                            //Toast.makeText(getApplicationContext(), "Size = " + orderPrevItems.size() + "", Toast.LENGTH_SHORT).show();

                            listViewAdapter = new ListViewAdapter(context, orderPrevItems, hmPlanTitles);
                            lvPrevOrders.setAdapter(listViewAdapter);
                            //listViewAdapter.notifyDataSetChanged();
                            //gettingPlanTitlesByIds(URLs.URL_ORDER_NOW, orderPrevItems);


                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(context, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.e("error343412", e.getMessage());
                        } finally {
                            if (pdLoading.isShowing())
                                pdLoading.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "onErrorResponse" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/findViewById(R.id.consOrderHistory),
                        "Network Error !!!!", Snackbar.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userId);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void gettingPlanTitlesByIds(String url, final ArrayList<OrderPrevItem> orderPrevItems) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("plans39%%", response.toString());
                        JSONObject jsonObject = null;
                        hmPlanTitles.clear();
                        try {
                            jsonObject = new JSONObject(response.toString());

                            currency = jsonObject.getString("currency");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String planId = jsonObject1.getString("plid");
                                String planTitleEn = jsonObject1.getString("pltitleen");
                                hmPlanTitles.put(planId, planTitleEn);
                            }
                            //Log.e("hmPlantAndTitle", hmPlanTitles.toString());

                            listViewAdapter = new ListViewAdapter(context, orderPrevItems, hmPlanTitles);
                            lvPrevOrders.setAdapter(listViewAdapter);
                            listViewAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "onErrorResponse" + error.getMessage(), Toast.LENGTH_SHORT).show();
                //Snackbar.make(/*getActivity().findViewById(R.id.regDrawerLayout)*/findViewById(R.id.consOrderHistory),"Network Error !!!!", Snackbar.LENGTH_SHORT).show();
            }
        }
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    @Override
    public void onBackPressed() {

        if (true) {

            finish();
            startActivity(new Intent(PrevOrdersActivity.this, com.example.ahmed.convertwebsitetoapp.chatting.MainActivity.class));

        } else {
            super.onBackPressed();
        }


    }


    class ListViewAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<OrderPrevItem> orderPrevItems = new ArrayList<OrderPrevItem>();
        HashMap<String, String> hmPlanTitlesWithIds = null;

        public ListViewAdapter(Context context, ArrayList<OrderPrevItem> orderPrevItems, HashMap<String, String> hmPlanTitlesWithIds) {
            this.context = context;
            this.orderPrevItems = orderPrevItems;
            this.hmPlanTitlesWithIds = hmPlanTitlesWithIds;
        }


        @Override
        public int getCount() {
            return this.orderPrevItems.size();
        }

        @Override
        public OrderPrevItem getItem(int position) {
            return this.orderPrevItems.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View v = null;
            v = LayoutInflater.from(context).inflate(R.layout.hist_order_item, null);

            TextView tvOrderNumber = (TextView) v.findViewById(R.id.ll1).findViewById(R.id.ll2).findViewById(R.id.tvOrderNumber);
            TextView tvOrderStatus = (TextView) v.findViewById(R.id.ll1).findViewById(R.id.tvStatus);
            TextView tvOrderTotalPrice = (TextView) v.findViewById(R.id.ll1).findViewById(R.id.ll2).findViewById(R.id.tvOrderTotalPrice);
            TextView tvOrderName = (TextView) v.findViewById(R.id.ll1).findViewById(R.id.ll2).findViewById(R.id.tvOrderName);
            TextView tvOrderDesc = (TextView) v.findViewById(R.id.ll1).findViewById(R.id.ll2).findViewById(R.id.tvOrderDesc);


            OrderPrevItem orderPrevItem = getItem(position);


            tvOrderNumber.setText(orderPrevItem.getOrderNumber());
            tvOrderTotalPrice.setText(orderPrevItem.getOrderPrice() + " " + currency);
            if (orderPrevItem.getOrderStatus().equalsIgnoreCase("success_pay")) {
                //tvOrderStatus.setBackgroundColor(Color.parseColor("#4eff4e"));
                tvOrderStatus.setBackgroundResource(R.drawable.pay_success);
                tvOrderStatus.setText("Payed Success");

            }
            if (orderPrevItem.getOrderStatus().equalsIgnoreCase("waiting_pay")) {
                //tvOrderStatus.setBackgroundColor(Color.parseColor("#ffa500"));
                tvOrderStatus.setBackgroundResource(R.drawable.pay_waiting);
                tvOrderStatus.setText("Payed binding");

            }
            if (orderPrevItem.getOrderStatus().equalsIgnoreCase("cancel_pay")) {
                //tvOrderStatus.setBackgroundColor(Color.parseColor("#ff5133"));
                tvOrderStatus.setBackgroundResource(R.drawable.pay_cancelled);
                tvOrderStatus.setText("Payed Cancelled");
            }
            //----------------------------
//            List<String> elephantList = Arrays.asList(orderPrevItem.getPlanIds().split(","));
//            String compOrderName = "";
//            for (int i = 0; i < elephantList.size(); i++) {
//                compOrderName += hmPlanTitlesWithIds.get(elephantList.get(i)).concat("\n");
//            }
            //Log.e("hm", hmPlanTitlesWithIds.toString());
            //tvOrderName.setText(compOrderName);
            tvOrderName.setText(orderPrevItem.getOrderName());
            tvOrderDesc.setText(orderPrevItem.getOrderDesc());

            return v;
        }


    }

}
