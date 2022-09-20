package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class List_of_Categories_name {

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("records")
    private CategoriesRecord records;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CategoriesRecord getRecords() {
        return records;
    }

    public void setRecords(CategoriesRecord records) {
        this.records = records;
    }
}
