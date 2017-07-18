package com.onrushers.domain.business.interactor.authentication;

import com.onrushers.domain.business.interactor.Interactor;

public interface LogInInteractor extends Interactor {

	void setUsername(String username);

	void setPassword(String password);

	void setFacebookId(String facebookId);
}
