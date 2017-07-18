package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IRankPagination;

import rx.Observable;

public interface ExploreRepository {

	Observable<IPagination> getPhotos(int page, int count, String accessToken);

	Observable<IRankPagination> getRank(int page, int count, String accessToken);
}
