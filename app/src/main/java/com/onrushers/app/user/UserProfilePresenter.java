package com.onrushers.app.user;

import com.onrushers.domain.business.model.IUser;

public interface UserProfilePresenter {

	void setView(UserProfileView view);

	void compareToAuthUser(Integer userId);

	void setPresentingLoggedInUser(boolean isPresentingLoggedInUser);

	boolean isPresentingLoggedInUser();

	void loadUserById(Integer userId);

	void presentUser(IUser user);

	boolean canShowHerosList();

	boolean canShowFansList();

	void toggleFollowUser();

	IUser getPresentedUser();
}
