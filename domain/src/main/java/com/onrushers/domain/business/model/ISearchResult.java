package com.onrushers.domain.business.model;

import android.os.Parcelable;

import com.onrushers.domain.business.type.SearchResultType;

public interface ISearchResult extends Parcelable {

	SearchResultType getType();

	Integer getId();

	String getName();

	Integer getPhoto();

	IUser toUser();
}
