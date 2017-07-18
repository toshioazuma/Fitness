package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;

public interface GetUserFansInteractor extends Interactor {

	void setUserId(Integer userId);

	void setPage(int page);

	void setCount(int count);
}
