package com.onrushers.app.user.edit;

import com.onrushers.domain.business.model.IUser;

import java.io.File;

public interface UserEditPresenter {

	void setView(UserEditView view);

	void setUser(IUser user);

	void updateInfo(String username, String description);

	void postAvatar(File avatarFile);

	void postCover(File coverFile);
}
