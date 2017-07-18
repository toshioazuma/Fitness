package com.onrushers.domain.business.interactor.feed;

import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.interactor.Interactor;

public interface DeleteFeedInteractor extends Interactor {

	void setFeed(IFeed feed);
}
