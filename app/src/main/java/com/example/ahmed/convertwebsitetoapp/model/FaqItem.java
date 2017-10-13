package com.example.ahmed.convertwebsitetoapp.model;

/**
 * Created by ahmed on 02/10/17.
 */

public class FaqItem {

    String questionAr = "";
    String questionEn = "";
    String answerAr = "";
String answerEn = "";


    public FaqItem(String questionAr, String questionEn, String answerAr, String answerEn) {
        this.questionAr = questionAr;
        this.questionEn = questionEn;
        this.answerAr = answerAr;
        this.answerEn = answerEn;
    }


    public String getQuestionAr() {
        return questionAr;
    }

    public void setQuestionAr(String questionAr) {
        this.questionAr = questionAr;
    }

    public String getQuestionEn() {
        return questionEn;
    }

    public void setQuestionEn(String questionEn) {
        this.questionEn = questionEn;
    }

    public String getAnswerAr() {
        return answerAr;
    }

    public void setAnswerAr(String answerAr) {
        this.answerAr = answerAr;
    }

    public String getAnswerEn() {
        return answerEn;
    }

    public void setAnswerEn(String answerEn) {
        this.answerEn = answerEn;
    }
}
