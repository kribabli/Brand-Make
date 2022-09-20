package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class VideoHomeData {

    @SerializedName("id")
    private String id;

    @SerializedName("language")
    private String language;


    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("link")
    private String link;

   @SerializedName("status")
    private String status;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

   @SerializedName("deleted_at")
    private String deleted_at;

    @SerializedName("image_url")
    private String image_url;


    @SerializedName("business_category_id")
    private String business_category_id;

   @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

   @SerializedName("detail_display")
    private String detail_display;

    @SerializedName("detail_message")
    private String detail_message;
    @SerializedName("festival_date")
    public String festival_date;
    public String getDetail_display() {
        return detail_display;
    }

    public void setDetail_display(String detail_display) {
        this.detail_display = detail_display;
    }

    public String getDetail_message() {
        return detail_message;
    }

    public void setDetail_message(String detail_message) {
        this.detail_message = detail_message;
    }

    public String getBusiness_category_id() {
        return business_category_id;
    }

    public void setBusiness_category_id(String business_category_id) {
        this.business_category_id = business_category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFestival_date() {
        return festival_date;
    }

    public void setFestival_date(String festival_date) {
        this.festival_date = festival_date;
    }
}
