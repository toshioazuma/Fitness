package com.onrushers.app.user.tabs.photos;

public interface UserTabPhotosPresenter {

	void setView(UserTabPhotosView view);

	void setUserId(Integer userId);

	void onViewCreated();

	void loadUserPhotosFeedsAtPage(int page);
}
