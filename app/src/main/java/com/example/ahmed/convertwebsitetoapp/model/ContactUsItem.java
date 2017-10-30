package com.example.ahmed.convertwebsitetoapp.model;

/**
 * Created by hp on 10/29/2017.
 */

public class ContactUsItem {


    String addressAr = "";
    String AddressEn = "";
    String mobile = "";
    String phone = "";
    String email = "";

    public ContactUsItem() {
    }

    public ContactUsItem() {
    }

    String map = "";

    public ContactUsItem(String addressAr, String addressEn, String mobile, String phone, String email, String map) {
        this.addressAr = addressAr;
        AddressEn = addressEn;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.map = map;
    }

    public String getAddressAr() {
        return addressAr;
    }

    public void setAddressAr(String addressAr) {
        this.addressAr = addressAr;
    }

    public String getAddressEn() {
        return AddressEn;
    }

    public void setAddressEn(String addressEn) {
        AddressEn = addressEn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
