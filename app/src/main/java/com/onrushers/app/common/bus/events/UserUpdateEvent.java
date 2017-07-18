package com.onrushers.app.common.bus.events;

import com.onrushers.domain.business.model.IUser;

public class UserUpdateEvent {

	private final IUser mUser;

	public UserUpdateEvent(IUser user) {
		mUser = user;
	}

	public IUser getUser() {
		return mUser;
	}
}
