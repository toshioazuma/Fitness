package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;

public interface GetUserWallInteractor extends Interactor {

	void setUserId(Integer userId);

	void setPage(int page);

	void setPageCount(int pageCount);
}
