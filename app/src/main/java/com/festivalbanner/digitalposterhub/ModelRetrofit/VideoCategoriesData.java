package com.festivalbanner.digitalposterhub.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class VideoCategoriesData {

    @SerializedName("id")
    private String id;
    @SerializedName("video_category_id")
    private String video_category_id;
    @SerializedName("video")
    public String video;
    @SerializedName("title")
    public String title;
    @SerializedName("video_url")
    public String video_url;
    @SerializedName("language")
    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_category_id() {
        return video_category_id;
    }

    public void setVideo_category_id(String video_category_id) {
        this.video_category_id = video_category_id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
