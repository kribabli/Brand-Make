package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserRegister {
    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("record")
    private RecordRegister record;

    @SerializedName("records")
    private ArrayList<BusinessListRecords> records;

    public ArrayList<BusinessListRecords> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<BusinessListRecords> records) {
        this.records = records;
    }

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

    public RecordRegister getRecord() {
        return record;
    }

    public void setRecord(RecordRegister record) {
        this.record = record;
    }
}
