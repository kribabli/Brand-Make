package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class RecordRegister {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;
    @SerializedName("email_verified_at")
    private String email_verified_at;

    @SerializedName("city")
    private String city;

    @SerializedName("image")
    private String image;

    @SerializedName("status")
    private String status;

    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("deleted_at")
    private String deleted_at;
    @SerializedName("company")
    private CompanyRecord company;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("sms_otp")
    private String sms_otp;
    @SerializedName("reset_otp")
    private String reset_otp;

    @SerializedName("contact_verify")
    private String contact_verify;


    @SerializedName("contact")
    private String contact;
    @SerializedName("plan_type")
    private String plan_type;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_verify() {
        return contact_verify;
    }

    public void setContact_verify(String contact_verify) {
        this.contact_verify = contact_verify;
    }

    /* public RecordRegister(String id,String api_token, String name, String email, String city,String image_url) {
            this.id = id;
            this.api_token = api_token;
            this.name = name;
            this.email = email;
            this.city = city;
            this.image_url = image_url;
        }*/
    public RecordRegister(String id, String name, String email, String city,String image_url,String image,String contact,String plan_type) {
        this.id = id;
       // this.api_token = api_token;
        this.name = name;
        this.email = email;
        this.city = city;
        this.image_url = image_url;
        this.image = image;
        this.contact = contact;
        this.plan_type = plan_type;


    }

    public CompanyRecord getCompany() {
        return company;
    }

    public void setCompany(CompanyRecord company) {
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getSms_otp() {
        return sms_otp;
    }

    public void setSms_otp(String sms_otp) {
        this.sms_otp = sms_otp;
    }

    public String getReset_otp() {
        return reset_otp;
    }

    public void setReset_otp(String reset_otp) {
        this.reset_otp = reset_otp;
    }
}
