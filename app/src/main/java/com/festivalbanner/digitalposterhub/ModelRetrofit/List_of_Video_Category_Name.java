package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class List_of_Video_Category_Name {

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("records")
    private VideoCategoriesRecord records;

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

    public VideoCategoriesRecord getRecords() {
        return records;
    }

    public void setRecords(VideoCategoriesRecord records) {
        this.records = records;
    }
}
