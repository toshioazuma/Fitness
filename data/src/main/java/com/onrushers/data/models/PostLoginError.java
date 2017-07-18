package com.onrushers.data.models;

import com.onrushers.domain.business.model.ILoginUserResult;

import java.util.List;

public class PostLoginError implements ILoginUserResult {

	private Integer statusCode;


	public PostLoginError(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public Integer getStatusCode() {
		return statusCode;
	}

	@Override
	public List<String[]> getErrorMessages() {
		return null;
	}

	@Override
	public String getToken() {
		return null;
	}

	@Override
	public Integer getUserId() {
		return null;
	}
}
