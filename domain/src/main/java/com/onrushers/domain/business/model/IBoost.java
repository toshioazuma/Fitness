package com.onrushers.domain.business.model;

import android.os.Parcelable;

import java.util.Date;

public interface IBoost extends Parcelable {

	Integer getId();

	Date getDate();

	int getNumber();

	Integer getFeedId();

	Integer getUserId();
}
