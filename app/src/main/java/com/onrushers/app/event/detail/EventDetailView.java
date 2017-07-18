package com.onrushers.app.event.detail;

import android.text.SpannableString;

import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.EventStateType;
import com.onrushers.domain.business.type.Gender;

import java.util.List;

public interface EventDetailView {

	void showPictures(List<Integer> picturesIds);

	void showPrice(SpannableString priceSpan);

	void showAvatar(String avatarUrl);

	void showTitle(String title);

	void showHerosRelation(List<IUser> herosList, SpannableString herosCountSpan, String usersCountLeft);

	void showState(EventStateType stateType);

	void showDate(SpannableString dateSpan);

	void showPlacesLeft(SpannableString placesLeftSpan);

	void showLevel(SpannableString levelSpan);

	void showPublic(Gender gender);

	void showDescription(String description);

	void showLocation(String addressPrimary, String addressSecondary, Double lat, Double lng);

	void showLoading();

	void hideLoading();

	void showError(String message);

	void dismissView();
}
