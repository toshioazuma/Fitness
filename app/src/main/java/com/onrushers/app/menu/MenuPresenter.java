package com.onrushers.app.menu;

import com.onrushers.domain.business.model.IUser;

public interface MenuPresenter {

	void setView(MenuView view);

	void onViewCreated();

	void presentUser(IUser user);

	void representUser();

	void requestInvalidateSession();
}
