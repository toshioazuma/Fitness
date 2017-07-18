package com.onrushers.data.repository.relation;

import com.onrushers.data.api.service.RelationService;
import com.onrushers.data.models.Relation;
import com.onrushers.domain.boundaries.RelationRepository;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IRelation;

import javax.inject.Inject;

import rx.Observable;

public class RelationApiRepositoryImpl implements RelationRepository {

	private final RelationService mRelationService;

	@Inject
	public RelationApiRepositoryImpl(RelationService relationService) {
		mRelationService = relationService;
	}

	@Override
	public Observable<IRelation> createRelation(Integer fanId, Integer heroId, String accessToken) {

		Relation relation = new Relation();
		relation.fanId = fanId;
		relation.heroId = heroId;

		return mRelationService
			.postRelation(relation, accessToken)
			.cast(IRelation.class);
	}

	@Override
	public Observable<IGenericResult> deleteRelation(Integer relationId, String accessToken) {
		return mRelationService
			.deleteRelation(relationId, accessToken)
			.cast(IGenericResult.class);
	}
}
