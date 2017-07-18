package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.ICreateUserResult;

import java.util.ArrayList;
import java.util.List;

public class PostUserResult implements ICreateUserResult {

	@Expose
	public Boolean result;

	@Expose
	public List<String[]> messages;

	@Expose
	public List<String[]> message;

	@Expose
	public String token;

	@Expose
	@SerializedName("id")
	public Integer userId;

	@Override
	public boolean isSuccess() {
		return result;
	}

	@Override
	public List<String[]> getErrorMessages() {
		if (messages != null) {
			return messages;
		} else if (message != null) {
			return message;
		}
		return new ArrayList<>();
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
