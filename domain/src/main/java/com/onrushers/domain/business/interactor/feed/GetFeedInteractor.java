package com.onrushers.domain.business.interactor.feed;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.model.IFeed;

public interface GetFeedInteractor extends Interactor {

	void setFeed(IFeed feed);
}
