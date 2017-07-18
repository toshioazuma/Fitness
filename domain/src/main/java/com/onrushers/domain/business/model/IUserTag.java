package com.onrushers.domain.business.model;

import android.os.Parcelable;

public interface IUserTag extends Parcelable {

	Integer getId();

	IUser getUser();
}
