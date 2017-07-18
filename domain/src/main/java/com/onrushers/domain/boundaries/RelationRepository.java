package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IRelation;

import rx.Observable;

public interface RelationRepository {

	Observable<IRelation> createRelation(Integer fanId, Integer heroId, String accessToken);

	Observable<IGenericResult> deleteRelation(Integer relationId, String accessToken);
}
