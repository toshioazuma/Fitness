package com.onrushers.data.db.entity;

import com.onrushers.domain.business.model.IAuthSession;

import io.realm.RealmObject;

public class AuthSessionEntity extends RealmObject implements IAuthSession {

	private String token;

	private Integer userId;

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String getToken() {
		return token;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public Integer getUserId() {
		return userId;
	}
}
