//package com.example.ahmed.convertwebsitetoapp.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.ahmed.convertwebsitetoapp.R;
//import com.payfort.fort.android.sdk.base.FortSdk;
//import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
//import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
//import com.payfort.sdk.android.dependancies.base.FortInterfaces;
//import com.payfort.sdk.android.dependancies.models.FortRequest;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by hp on 10/11/2017.
// */
//
//public class PayFortFactory extends AppCompatActivity {
//
//    String deviceId = "", sdkToken = "";
//    Context context = null;
//    private FortCallBackManager fortCallback = null;
//
//
////    public PayFortFactory(Context context){
////        this.context = context;
////        init();
////    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.actvity_payfort);
//        init();
//
//
//    }
//
//    private void init() {
//        //init the context
//        context = this;
//        // create Fort callback instance
//        fortCallback = FortCallback.Factory.create();
//        // Generating deviceId
//        deviceId = FortSdk.getDeviceId(context);
//        Log.d("DeviceId", deviceId);
//        // prepare payment request
//        FortRequest fortrequest = new FortRequest();
//        fortrequest.setRequestMap(collectRequestMap("PASS_THE_GENERATED_SDK_TOKEN_HERE"));
//        fortrequest.setShowResponsePage(true); // to [display/use] the SDK response page
//        // execute payment request
//        callSdk(fortrequest);
//    }
//
//    private Map<String, String> collectRequestMap(String sdkToken) {
//        Map<String, String> requestMap = new HashMap<>();
//        requestMap.put("command", "PURCHASE");
//        requestMap.put("customer_email", "Sam@gmail.com");
//        requestMap.put("currency", "SAR");
//        requestMap.put("amount", "100");
//        requestMap.put("language", "en");
//        requestMap.put("merchant_reference", "ORD-0000007682");
//        requestMap.put("customer_name", "Sam");
//        requestMap.put("customer_ip", "172.150.16.10");
//        requestMap.put("payment_option", "VISA");
//        requestMap.put("eci", "ECOMMERCE");
//        requestMap.put("order_description", "DESCRIPTION");
//        requestMap.put("sdk_token", sdkToken);
//        return requestMap;
//    }
//
//    private void callSdk(FortRequest fortrequest) {
//
//        try {
//            FortSdk.getInstance().registerCallback(this, fortrequest, FortSdk.ENVIRONMENT.TEST, 5, fortCallback, new FortInterfaces.OnTnxProcessed() {
//                @Override
//                public void onCancel(Map<String, String> requestParamsMap, Map<String,
//                        String> responseMap) {
//                    Log.d("Cancelled ", responseMap.toString());
//                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onSuccess(Map<String, String> requestParamsMap, Map<String,
//                        String> fortResponseMap) {
//                    Log.i("Success ", fortResponseMap.toString());
//                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Map<String, String> requestParamsMap, Map<String,
//                        String> fortResponseMap) {
//                    Log.e("Failure ", fortResponseMap.toString());
//                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
//                }
//
//            });
//        } catch (Exception e) {
//            Log.e("execute Payment", "call FortSdk", e);
//        }
//
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        fortCallback.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//}
