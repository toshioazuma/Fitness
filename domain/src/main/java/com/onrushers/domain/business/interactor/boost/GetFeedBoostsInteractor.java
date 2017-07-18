package com.onrushers.domain.business.interactor.boost;

import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.interactor.Interactor;

public interface GetFeedBoostsInteractor extends Interactor {

	void setFeed(IFeed feed);

	void setPage(int page);
}
