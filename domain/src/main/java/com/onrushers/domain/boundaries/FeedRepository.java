package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IGenericResult;

import java.util.List;

import rx.Observable;

public interface FeedRepository {

	Observable<IFeed> createFeed(int type, int userId, String content, String place,
	                             List<Integer> userIds, List<Integer> photos, String accessToken);

	Observable<IFeed> getFeed(int feedId, String accessToken);

	Observable<IGenericResult> deleteFeed(int feedId, String accessToken);
}
