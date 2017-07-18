package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.PaginationInteractor;

public interface GetUserNotificationsInteractor extends PaginationInteractor {

	void setUserId(Integer userId);
}
