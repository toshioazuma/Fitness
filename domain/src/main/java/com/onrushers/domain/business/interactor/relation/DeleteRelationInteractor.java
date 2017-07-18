package com.onrushers.domain.business.interactor.relation;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.model.IRelation;

public interface DeleteRelationInteractor extends Interactor {

	void setRelation(IRelation relation);
}
