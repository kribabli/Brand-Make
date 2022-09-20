package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class model_PaymntPlan {

    @SerializedName("current_page")
    private String current_page;
    @SerializedName("data")
    private ArrayList<model_PaymntPlan_data> data;
    @SerializedName("first_page_url")
    private String first_page_url;
    @SerializedName("from")
    private String from;
    @SerializedName("last_page")
    private String last_page;
    @SerializedName("last_page_url")
    private String last_page_url;
    @SerializedName("next_page_url")
    private String next_page_url;
    @SerializedName("path")
    private String path;
    @SerializedName("per_page")
    private String per_page;
    @SerializedName("prev_page_url")
    private String prev_page_url;
    @SerializedName("to")
    private String to;
    @SerializedName("total")
    private String total;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public ArrayList<model_PaymntPlan_data> getData() {
        return data;
    }

    public void setData(ArrayList<model_PaymntPlan_data> data) {
        this.data = data;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
