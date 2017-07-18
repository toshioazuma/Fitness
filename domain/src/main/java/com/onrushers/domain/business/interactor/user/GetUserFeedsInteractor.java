package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.PaginationInteractor;
import com.onrushers.domain.business.type.FeedType;

public interface GetUserFeedsInteractor extends PaginationInteractor {

	void setUserId(Integer userId);

	void setType(FeedType type);
}
