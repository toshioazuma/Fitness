package com.onrushers.data.repository.boost;

import com.onrushers.data.api.service.BoostService;
import com.onrushers.data.models.Boost;
import com.onrushers.domain.boundaries.BoostRepository;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IPagination;

import javax.inject.Inject;

import rx.Observable;

public class BoostApiRepositoryImpl implements BoostRepository {

	private final BoostService mBoostService;

	@Inject
	public BoostApiRepositoryImpl(BoostService boostService) {
		mBoostService = boostService;
	}

	@Override
	public Observable<IBoost> addBoost(
		int number, int feedId, int userId, String accessToken) {

		final Boost boost = new Boost();
		boost.number = number;
		boost.feedId = feedId;
		boost.userId = userId;

		return mBoostService
			.postBoost(boost, accessToken)
			.cast(IBoost.class);
	}

	@Override
	public Observable<IGenericResult> deleteBoost(
		int boostId, String accessToken) {

		return mBoostService
			.deleteBoost(boostId, accessToken)
			.cast(IGenericResult.class);
	}

	@Override
	public Observable<IPagination> getFeedBoosts(
		int feedId, int page, int count, String accessToken) {

		return mBoostService
			.getFeedBoosts(feedId, page, count, accessToken)
			.cast(IPagination.class);
	}
}
