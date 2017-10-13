package com.example.ahmed.convertwebsitetoapp.view;

/**
 * Created by ahmed on 3/25/2017.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends/* android.support.v7.widget.AppCompatEditText*/EditText{


    //Overloaded Constructor
    public CustomEditText(Context context) {
        super(context);
        Typeface face= Typeface.createFromAsset(context.getAssets(), "font.ttf");
        this.setTypeface(face);
    }

    //Overloaded Constructor
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face= Typeface.createFromAsset(context.getAssets(), "font.ttf");
        this.setTypeface(face);
    }

    //Overloaded Constructor
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face= Typeface.createFromAsset(context.getAssets(), "font.ttf");
        this.setTypeface(face);
    }
//
//    protected void onDraw (Canvas canvas) {
//        super.onDraw(canvas);
//
//
//    }

}
