package com.onrushers.app.user.edit;

public interface UserEditView {

	void showCover(String coverUrl);

	void showAvatar(String avatarUrl);

	void showUsername(String username);

	void showDescription(String description);

	void showLoading();

	void hideLoading();

	void showSuccessMessage(int successResId);

	void showFailureMessage(String failureMessage);
}
