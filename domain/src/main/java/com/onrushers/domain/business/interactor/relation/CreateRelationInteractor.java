package com.onrushers.domain.business.interactor.relation;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.model.IUser;

public interface CreateRelationInteractor extends Interactor {

	void setHero(IUser hero);
}
