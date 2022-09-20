package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class CompanyDetails {
    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("record")
    private CompanyRecord record;

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

    public CompanyRecord getRecord() {
        return record;
    }

    public void setRecord(CompanyRecord record) {
        this.record = record;
    }
}
