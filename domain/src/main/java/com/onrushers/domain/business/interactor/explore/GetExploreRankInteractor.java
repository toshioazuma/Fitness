package com.onrushers.domain.business.interactor.explore;

import com.onrushers.domain.business.interactor.Interactor;

public interface GetExploreRankInteractor extends Interactor {

	void setPage(int page);

	void setCount(int count);
}
