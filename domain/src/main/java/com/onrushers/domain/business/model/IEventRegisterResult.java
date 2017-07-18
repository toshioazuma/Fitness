package com.onrushers.domain.business.model;

import android.os.Parcelable;

public interface IEventRegisterResult extends Parcelable {

	Integer getId();

	String getEmail();

	IEvent getEvent();

	IUser getUser();
}
