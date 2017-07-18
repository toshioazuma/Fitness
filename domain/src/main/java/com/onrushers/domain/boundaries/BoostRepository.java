package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IBoostPagination;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IPagination;

import rx.Observable;

public interface BoostRepository {

	Observable<IBoost> addBoost(
		int number, int feedId, int userId, String accessToken);

	Observable<IGenericResult> deleteBoost(
		int boostId, String accessToken);

	Observable<IPagination> getFeedBoosts(
		int feedId, int page, int count, String accessToken);
}
