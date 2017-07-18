package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;

public interface GetUserHerosInteractor extends Interactor {

	void setUserId(Integer userId);

	void setCount(int count);

	void setPage(int page);
}
