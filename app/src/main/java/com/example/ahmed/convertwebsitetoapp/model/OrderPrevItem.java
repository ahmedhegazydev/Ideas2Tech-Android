package com.example.ahmed.convertwebsitetoapp.model;

/**
 * Created by hp on 10/16/2017.
 */

public class OrderPrevItem {

    String orderNumber = "";
    String orderPrice = "";
    String orderStatus = "";
    String planIds = "";
    String orderName = "";
String orderDesc  = "";

    public OrderPrevItem(String orderNumber, String orderPrice, String orderStatus, String planIds, String orderName, String orderDesc) {
        this.orderNumber = orderNumber;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
        this.planIds = planIds;
        this.orderName = orderName;
        this.orderDesc = orderDesc;
    }


    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }




    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPlanIds() {
        return planIds;
    }

    public void setPlanIds(String planIds) {
        this.planIds = planIds;
    }
}
