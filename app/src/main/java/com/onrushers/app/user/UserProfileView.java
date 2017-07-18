package com.onrushers.app.user;

import android.text.SpannableString;

public interface UserProfileView {

	void showMyProfileControls(boolean isMyProfile);

	void showUserName(String userName);

	void showAvatarPictureUrl(String pictureUrl);

	void showCoverPictureUrl(String coverPictureUrl);

	void showHeroCount(SpannableString heroCountSpan);

	void showFanCount(SpannableString fanCountSpan);

	void showUserGrade(String grade);

	void showDescription(String description);

	void hideDescription();

	void showFollowState();

	void showFollowingState();

	void showLoading();

	void hideLoading();
}
