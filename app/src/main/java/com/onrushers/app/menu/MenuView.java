package com.onrushers.app.menu;

public interface MenuView {

	void showUserName(String username);

	void showUserGrade(String grade);

	void showUserAvatar(String avatarUrl);

	void reloadView();

	void showLaunchPage();
}
