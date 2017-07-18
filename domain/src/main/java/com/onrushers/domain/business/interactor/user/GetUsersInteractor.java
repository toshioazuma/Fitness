package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;

import java.util.List;

public interface GetUsersInteractor extends Interactor {

	void setUserIds(List<Integer> userIds);

	void setUserId(Integer userId);
}
