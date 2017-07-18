package com.onrushers.domain.business.model;

import android.os.Parcelable;

public interface ILocation extends Parcelable {

	int getId();

	double getLatitude();

	double getLongitude();

	String getCountry();

	String getCity();

	String getZipcode();

	String getAddress();

	String getRegion();
}
