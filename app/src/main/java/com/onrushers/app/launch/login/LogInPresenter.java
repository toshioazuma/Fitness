package com.onrushers.app.launch.login;

public interface LogInPresenter {

	void setView(LoginView view);

	void onDestroy();

	void setUsername(String username);

	void setPassword(String password);

	void logIn();
}
