package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class model_PaymntPlan_data {

    @SerializedName("id")
    private String id;
    @SerializedName("plan_image")
    private String plan_image;
    @SerializedName("payment_image")
    private String payment_image;
    @SerializedName("contact")
    private String contact;
    @SerializedName("email")
    private String email;
    @SerializedName("website")
    private String website;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("deleted_at")
    private String deleted_at;
    @SerializedName("plan_image_url")
    private String plan_image_url;
    @SerializedName("payment_image_url")
    private String payment_image_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlan_image() {
        return plan_image;
    }

    public void setPlan_image(String plan_image) {
        this.plan_image = plan_image;
    }

    public String getPayment_image() {
        return payment_image;
    }

    public void setPayment_image(String payment_image) {
        this.payment_image = payment_image;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getPlan_image_url() {
        return plan_image_url;
    }

    public void setPlan_image_url(String plan_image_url) {
        this.plan_image_url = plan_image_url;
    }

    public String getPayment_image_url() {
        return payment_image_url;
    }

    public void setPayment_image_url(String payment_image_url) {
        this.payment_image_url = payment_image_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
