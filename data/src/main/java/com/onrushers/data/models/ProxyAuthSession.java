package com.onrushers.data.models;

import com.onrushers.domain.business.model.IAuthSession;

/**
 * Created by Ludovic on 06/09/16.
 */
public class ProxyAuthSession implements IAuthSession {

	private final String mToken;
	private final Integer mUserId;

	public ProxyAuthSession(IAuthSession authSession) {
		mToken = authSession.getToken();
		mUserId = authSession.getUserId();
	}

	public ProxyAuthSession(String token, Integer userId) {
		mToken = token;
		mUserId = userId;
	}

	@Override
	public String getToken() {
		return mToken;
	}

	@Override
	public Integer getUserId() {
		return mUserId;
	}
}
