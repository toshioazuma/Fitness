package com.onrushers.app.feed.create;

import java.io.File;

public interface FeedCreateView {

	void setPicture(File pictureFile);

	void showLoading();

	void hideLoading();

	void showSuccessMessage(String success);

	void showErrorMessage(String error);
}
