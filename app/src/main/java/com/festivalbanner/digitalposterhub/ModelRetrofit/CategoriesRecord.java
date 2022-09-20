package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoriesRecord {

    @SerializedName("data")
    private ArrayList<CategoriesData> data;

    @SerializedName("first_page_url")
    private String first_page_url;

    @SerializedName("next_page_url")
    private String next_page_url;

    public ArrayList<CategoriesData> getData() {
        return data;
    }

    public void setData(ArrayList<CategoriesData> data) {
        this.data = data;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }
}
