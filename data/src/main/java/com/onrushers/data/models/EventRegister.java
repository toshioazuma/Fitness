package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.IUser;

public class EventRegister {

	@Expose
	public String email;

	@Expose
	@SerializedName("event")
	public Integer eventId;

	@Expose
	@SerializedName("user")
	public Integer userId;
}
