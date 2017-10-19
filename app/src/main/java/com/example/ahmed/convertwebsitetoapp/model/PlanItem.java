package com.example.ahmed.convertwebsitetoapp.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hp on 10/12/2017.
 */

public class PlanItem implements Serializable {


    String id = "";
    String titleAr = "";
    String titleEn = "";
    String descAr = "";
    String descEn = "";
    String catAr = "";
    String catEn = "";
    String price = "";
    boolean checked = false;
    public PlanItem(String id, String titleAr, String titleEn, String descAr, String descEn, String catAr, String catEn, String price) {
        this.id = id;
        this.titleAr = titleAr;
        this.titleEn = titleEn;
        this.descAr = descAr;
        this.descEn = descEn;
        this.catAr = catAr;
        this.catEn = catEn;
        this.price = price;
    }


    public PlanItem(String id, String titleAr, String titleEn, String descAr, String descEn, String catAr, String catEn, String price, boolean checked) {
        this.id = id;
        this.titleAr = titleAr;
        this.titleEn = titleEn;
        this.descAr = descAr;
        this.descEn = descEn;
        this.catAr = catAr;
        this.catEn = catEn;
        this.price = price;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getDescAr() {
        return descAr;
    }

    public void setDescAr(String descAr) {
        this.descAr = descAr;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public String getCatAr() {
        return catAr;
    }

    public void setCatAr(String catAr) {
        this.catAr = catAr;
    }

    public String getCatEn() {
        return catEn;
    }

    public void setCatEn(String catEn) {
        this.catEn = catEn;
    }
}
