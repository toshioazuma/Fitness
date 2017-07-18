package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.ILoginUserResult;

import java.util.List;

public class PostLoginResult implements ILoginUserResult {

	@Expose
	public Boolean result;

	@Expose
	public List<String[]> messages;

	@Expose
	public String token;

	@Expose
	@SerializedName("id")
	public Integer userId;

	@Override
	public boolean isSuccess() {
		return result != null && result.booleanValue();
	}

	@Override
	public Integer getStatusCode() {
		return null;
	}

	@Override
	public List<String[]> getErrorMessages() {
		return messages;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public Integer getUserId() {
		return userId;
	}
}
