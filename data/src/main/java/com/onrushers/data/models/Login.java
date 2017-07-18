package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

	@Expose
	public String username;

	@Expose
	public String password;

	@Expose
	@SerializedName("facebook_id")
	public String facebookId;
}
