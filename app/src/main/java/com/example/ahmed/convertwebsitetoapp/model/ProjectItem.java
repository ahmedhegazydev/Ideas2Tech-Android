package com.example.ahmed.convertwebsitetoapp.model;

import java.io.Serializable;

/**
 * Created by hp on 10/4/2017.
 */

public class ProjectItem implements Serializable {

    String titleEn = "";
    String titleAr = "";
    String descEn = "";
    String descAr = "";
    String categoryEn = "";
    String categoryAr = "";

    String projectImgUrl = "";
    String projectImgThumb = "";


    public ProjectItem(String titleEn, String titleAr, String descEn, String descAr, String categoryEn, String categoryAr, String projectImgUrl, String projectImgThumb) {
        this.titleEn = titleEn;
        this.titleAr = titleAr;
        this.descEn = descEn;
        this.descAr = descAr;
        this.categoryEn = categoryEn;
        this.categoryAr = categoryAr;
        this.projectImgUrl = projectImgUrl;
        this.projectImgThumb = projectImgThumb;
    }

    public String getProjectImgUrl() {
        return projectImgUrl;
    }

    public void setProjectImgUrl(String projectImgUrl) {
        this.projectImgUrl = projectImgUrl;
    }

    public String getProjectImgThumb() {
        return projectImgThumb;
    }

    public void setProjectImgThumb(String projectImgThumb) {
        this.projectImgThumb = projectImgThumb;
    }

    public String getTitleEn() {
        return titleEn;

    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public String getDescAr() {
        return descAr;
    }

    public void setDescAr(String descAr) {
        this.descAr = descAr;
    }

    public String getCategoryEn() {
        return categoryEn;
    }

    public void setCategoryEn(String categoryEn) {
        this.categoryEn = categoryEn;
    }

    public String getCategoryAr() {
        return categoryAr;
    }

    public void setCategoryAr(String categoryAr) {
        this.categoryAr = categoryAr;
    }
}
