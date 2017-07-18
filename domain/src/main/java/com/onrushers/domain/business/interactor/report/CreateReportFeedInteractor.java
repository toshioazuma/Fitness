package com.onrushers.domain.business.interactor.report;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.model.IFeed;

public interface CreateReportFeedInteractor extends Interactor {

	void setFeed(IFeed feed);
}
