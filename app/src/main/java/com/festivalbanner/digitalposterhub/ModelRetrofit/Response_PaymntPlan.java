package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class Response_PaymntPlan {

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("records")
    private model_PaymntPlan records;

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

    public model_PaymntPlan getRecords() {
        return records;
    }

    public void setRecords(model_PaymntPlan records) {
        this.records = records;
    }
}
