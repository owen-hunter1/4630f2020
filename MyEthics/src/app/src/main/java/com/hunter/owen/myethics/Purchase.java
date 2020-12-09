package com.hunter.owen.myethics;

import com.google.gson.annotations.Expose;

import java.util.Calendar;

public class Purchase {
    @Expose
    private String product_name;
    @Expose
    private long purchase_date;
    @Expose
    private int score;

    public Purchase(String product_name, int date, int score){
        this.product_name = product_name;
        this.purchase_date = date;
        this.score = score;
    }

    public Calendar getPurchase_date() {
        Calendar result = Calendar.getInstance();
        result.setTimeInMillis(purchase_date);
        return result;
    }

    public int getScore() {
        return score;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setPurchase_date(Calendar purchase_date) {
        this.purchase_date = purchase_date.getTimeInMillis();
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
