package com.onrushers.domain.business.interactor.boost;

import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.interactor.Interactor;

public interface AddBoostInteractor extends Interactor {

	void setNumber(int number);

	void setFeed(IFeed feed);
}
