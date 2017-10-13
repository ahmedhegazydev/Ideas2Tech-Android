package com.example.ahmed.convertwebsitetoapp.fragments_ourprojetcs;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmed.convertwebsitetoapp.R;
import com.example.ahmed.convertwebsitetoapp.fragments.Imageview;

import java.util.ArrayList;
import java.util.List;

//import com.example.ahmed.convertwebsitetoapp.RecordActivity;


/**
 * Created by ahmed on 3/25/2017.
 */

public class Business extends Fragment {

    RecyclerView services;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_business, container, false);
        services = (RecyclerView) view.findViewById(R.id.RecyclerView);
        services.setHasFixedSize(true);
        services.setItemViewCacheSize(20);
        services.setDrawingCacheEnabled(true);
        services.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//

        services.setLayoutManager(new GridLayoutManager(getContext(), 1));
        List<Uri> uriList = new ArrayList<>();
        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service1"));
        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service2"));
        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service3"));
        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service4"));
        uriList.add(Uri.parse("android.resource://com.example.ahmed.convertwebsitetoapp/drawable/service5"));
        //String[] titlesList = getActivity().getResources().getStringArray(R.array.titles);
        services.setAdapter(new ImagesAdapter(uriList, new String[]{"Project 1", "project 2", "Project 3", "Project 4", "Project 5"}));

        return view;
    }

    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

        private List<Uri> imagesList;
        private String[] listTitles = null;

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
//

        public ImagesAdapter(List<Uri> imagesList, String[] listTitles) {
            this.imagesList = imagesList;
            this.listTitles = listTitles;
        }

        @Override
        public ImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_sample, parent, false);
            itemView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in));

            return new ImagesAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ImagesAdapter.MyViewHolder holder, final int position) {


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("imageUri", imagesList.get(position).toString());
                    bundle.putString("imageTitle", listTitles[position]);
                    Imageview fragment = new Imageview();
                    fragment.setArguments(bundle);
                    ft.replace(R.id.activity_main_content_fragment3, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

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
    }
}
