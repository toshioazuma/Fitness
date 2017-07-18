package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;

public interface UpdateUserProfileInteractor extends Interactor {

	void setUsername(String username);

	void setDescription(String description);
}
