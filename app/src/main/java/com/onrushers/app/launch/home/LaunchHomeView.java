package com.onrushers.app.launch.home;

public interface LaunchHomeView {

	void showRegistrationPageWithFacebook(String facebookId);

	void showErrorMessage(String[] messages);

	void showHome();

}
