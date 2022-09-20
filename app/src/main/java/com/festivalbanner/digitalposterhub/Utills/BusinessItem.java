package com.festivalbanner.digitalposterhub.Utills;

import com.google.gson.annotations.SerializedName;

public class BusinessItem {

	@SerializedName("businessname")
	private String businessname;

	@SerializedName("id")
	private String id;

	@SerializedName("businessimage")
	private String businessimage;

	public void setBusinessname(String businessname){
		this.businessname = businessname;
	}

	public String getBusinessname(){
		return businessname;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setBusinessimage(String businessimage){
		this.businessimage = businessimage;
	}

	public String getBusinessimage(){
		return businessimage;
	}
}