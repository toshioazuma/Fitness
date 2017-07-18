package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;

public interface UpdateUserAvatarInteractor extends Interactor {

	void setProfilePictureId(Integer profilePictureId);

	void setUserId(Integer userId);

	void setToken(String token);
}
