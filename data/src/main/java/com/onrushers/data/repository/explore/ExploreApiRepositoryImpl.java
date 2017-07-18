package com.onrushers.data.repository.explore;

import com.onrushers.data.api.service.ExploreService;
import com.onrushers.domain.boundaries.ExploreRepository;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IRankPagination;

import javax.inject.Inject;

import rx.Observable;

public class ExploreApiRepositoryImpl implements ExploreRepository {

	private final ExploreService mExploreService;

	@Inject
	public ExploreApiRepositoryImpl(ExploreService exploreService) {
		mExploreService = exploreService;
	}

	@Override
	public Observable<IPagination> getPhotos(int page, int count, String accessToken) {
		return mExploreService.getPhotos(page, count, accessToken)
			.cast(IPagination.class);
	}

	@Override
	public Observable<IRankPagination> getRank(int page, int count, String accessToken) {
		return mExploreService.getRank(page, count, accessToken)
			.cast(IRankPagination.class);
	}
}
