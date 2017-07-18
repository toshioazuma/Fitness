package com.onrushers.app.launch.login;

public interface LoginView {

	void showLoading();

	void hideLoading();

	void showLogInErrorMessage(String errorMessage);

	void onSuccessfulLogIn();

}
