package com.onrushers.app.event.register;

import android.text.SpannableString;

public interface EventRegisterView {

	void showDate(SpannableString dateSpan);

	void showPlaces(SpannableString placesSpan);

	void showAvatar(String avatarUrl);

	void showTitle(String title);

	void showUserAvatar(String avatarUrl);

	void showUserInfo(String username, String grade);

	void showUserForm(String firstName, String lastName, String email);

	void showPrices(String unitPrice, String totalPrice);

	void showLoading();

	void hideLoading();

	void showError(String message);

	void showSuccess(String message);
}
