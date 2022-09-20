package com.festivalbanner.digitalposterhub.Utills;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BusinessesResponse{

	@SerializedName("error")
	private String error;

	@SerializedName("message")
	private List<BusinessItem> message;

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setMessage(List<BusinessItem> message){
		this.message = message;
	}

	public List<BusinessItem> getMessage(){
		return message;
	}
}