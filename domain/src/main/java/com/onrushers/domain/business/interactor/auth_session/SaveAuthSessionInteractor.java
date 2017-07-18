package com.onrushers.domain.business.interactor.auth_session;

import com.onrushers.domain.business.interactor.Interactor;

public interface SaveAuthSessionInteractor extends Interactor {

	void setToken(String token);

	void setUserId(Integer userId);

}
