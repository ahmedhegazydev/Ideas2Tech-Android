package com.example.ahmed.convertwebsitetoapp.model;

import java.io.Serializable;

/**
 * Created by hp on 10/4/2017.
 */

public class ServiceItem implements Serializable {
    String serTitleEn = "";
    String serTitleAr = "";
    String serDescEn = "";
    String serDescAr = "";
    String serImageUrl = "";
    String serThubUrl = "";

    public String getSerThubUrl() {
        return serThubUrl;
    }

    public void setSerThubUrl(String serThubUrl) {
        this.serThubUrl = serThubUrl;
    }

    public ServiceItem(String serTitleEn, String serTitleAr, String serDescEn, String serDescAr, String serImageUrl, String serThubUrl) {
        this.serTitleEn = serTitleEn;
        this.serTitleAr = serTitleAr;
        this.serDescEn = serDescEn;
        this.serDescAr = serDescAr;
        this.serImageUrl = serImageUrl;
        this.serThubUrl = serThubUrl;

    }


    public String getSerTitleEn() {
        return serTitleEn;
    }

    public void setSerTitleEn(String serTitleEn) {
        this.serTitleEn = serTitleEn;
    }

    public String getSerTitleAr() {
        return serTitleAr;
    }

    public void setSerTitleAr(String serTitleAr) {
        this.serTitleAr = serTitleAr;
    }

    public String getSerDescEn() {
        return serDescEn;
    }

    public void setSerDescEn(String serDescEn) {
        this.serDescEn = serDescEn;
    }

    public String getSerDescAr() {
        return serDescAr;
    }

    public void setSerDescAr(String serDescAr) {
        this.serDescAr = serDescAr;
    }

    public String getSerImageUrl() {
        return serImageUrl;
    }

    public void setSerImageUrl(String serImageUrl) {
        this.serImageUrl = serImageUrl;
    }
}
