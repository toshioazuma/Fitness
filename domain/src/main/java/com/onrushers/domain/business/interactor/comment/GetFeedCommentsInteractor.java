package com.onrushers.domain.business.interactor.comment;

import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.interactor.Interactor;

public interface GetFeedCommentsInteractor extends Interactor {

	void setFeed(IFeed feed);

    void setPage(int page);
}
