package com.example.ahmed.convertwebsitetoapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ahmed.convertwebsitetoapp.DetailsActivtyServ;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.chatting.URLs;
import com.example.ahmed.convertwebsitetoapp.model.ServiceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by ahmed on 4/16/2017.
 */

public class Services extends Fragment implements ListView.OnItemClickListener {
    RecyclerView services;
    View viewRoot = null;
    ProgressDialog progressDialog = null;
    FloatingActionButton fab = null;
    ListView lvServices = null;
    ArrayList<ServiceItem> serviceItems = new ArrayList<ServiceItem>();
    ListAdapter listAdapter = null;

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
        viewRoot = inflater.inflate(R.layout.services, container, false);
        ///fab = (FloatingActionButton) viewRoot.findViewById(R.id.fabChatting);
       // setAlphaAnimation(fab);

//        services = (RecyclerView) view.findViewById(R.id.RecyclerView);
//        services.setHasFixedSize(true);
//        services.setItemViewCacheSize(20);
//        services.setDrawingCacheEnabled(true);
//        services.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
////
//
//        services.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        List<Uri> uriList = new ArrayList<>();
//        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service1"));
//        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service2"));
//        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service3"));
//        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service4"));
//        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service5"));
//        String[] titlesList = getActivity().getResources().getStringArray(R.array.titles);
//        services.setAdapter(new ImagesAdapter(uriList, titlesList));

        init();
        fetchData(URLs.URL_SERVICES);

        return viewRoot;
    }

    private void init() {
        lvServices = (ListView) viewRoot.findViewById(R.id.lvServices);
        listAdapter = new ListAdapter(getContext(), serviceItems);
        lvServices.setOnItemClickListener(this);
        lvServices.setAdapter(listAdapter);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Preparing");
        progressDialog.show();


    }

    private void fetchData(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(modifyJson(response.toString()));
                            //JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e("res33423", jsonObject.toString());
                            String image = jsonObject.getString("image");
                            String thumnail = jsonObject.getString("thumb");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                serviceItems.add(
                                        new ServiceItem(jsonObject1.getString("cgtitleen"),
                                                jsonObject1.getString("cgtitlear"),
                                                jsonObject1.getString("cgdescen"),
                                                jsonObject1.getString("cgdescar"),
                                                image + jsonObject1.getString("cgimg").toString(),
                                                thumnail + jsonObject1.getString("cgimg").toString()
                                        ));


                            }
                            listAdapter = new ListAdapter(getContext(), serviceItems);
                            lvServices.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
//

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ServiceItem serviceItem = serviceItems.get(i);

        startActivity(new Intent(getContext(), DetailsActivtyServ.class).putExtra("item", serviceItem));
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }


    public class ListAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<ServiceItem> serviceItems = null;

        public ListAdapter(Context context, ArrayList<ServiceItem> serviceItems) {
            this.context = context;
            this.serviceItems = serviceItems;
        }


        @Override
        public int getCount() {
            return this.serviceItems.size();
        }

        @Override
        public ServiceItem getItem(int i) {
            return this.serviceItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ServiceItem serviceItem = getItem(i);
            View view1 = null;
            String lan = "";
            lan = Locale.getDefault().getDisplayLanguage();
//            if (lan == "en") {
//                view1 = LayoutInflater.from(getActivity()).inflate(R.layout.img_service_item, null);
//            }else{
//                //ar
//                view1 = LayoutInflater.from(getActivity()).inflate(R.layout.img_service_item, null);
//            }
            view1 = LayoutInflater.from(getActivity()).inflate(R.layout.img_service_item, null);
            view1.setAlpha(.9f);
            ImageView imageView1 = (ImageView) view1.findViewById(R.id.iv);

//            ImageView imageView = new ImageView(context);
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 300));
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(serviceItem.getSerImageUrl()).into(imageView1);
            TextView tvImageTitle = (TextView) view1.findViewById(R.id.tvImageTitle);
            tvImageTitle.setText(serviceItem.getSerTitleEn());

            return view1;
        }
    }


    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

        private List<Uri> imagesList;
        private String[] listTitles = null;

        public ImagesAdapter(List<Uri> imagesList, String[] listTitles) {
            this.imagesList = imagesList;
            this.listTitles = listTitles;
        }
//

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_sample, parent, false);
            itemView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in));

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    //FragmentManager fm = getActivity().getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("imageUri", imagesList.get(position).toString());
//                    bundle.putString("imageTitle", listTitles[position]);
//                    Imageview imageview = new Imageview();
//                    imageview.setArguments(bundle);
//                    ft.replace(R.id.activity_main_content_fragment3, imageview);
//                    ft.addToBackStack(null);
//                    ft.commit();

                }
            });
            //Picasso.with(getContext()).load(imagesList.get(position)).into(holder.image);
            Glide.with(getContext()).load(imagesList.get(position)).into(holder.image);
            ////////-----------------------
            holder.imgTitle.setText(listTitles[position]);
        }

        @Override
        public int getItemCount() {
            return imagesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView image = null;
            public TextView imgTitle = null;

            public MyViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.imageView);
                image.setPadding(10, 10, 10, 10);
                //--------------
                imgTitle = (TextView) view.findViewById(R.id.textView);

            }
        }
    }

}
