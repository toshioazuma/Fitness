package com.onrushers.app.launch.signin;

public interface SignInView {

	void showUsernameAvailable();

	void showUsernameUnavailable();

	void showBirthDate(String formattedDate);

	void showLoading();

	void hideLoading();

	void showSignInErrorMessage(String errorMessage);

	void onSuccessfulSignIn();
}
