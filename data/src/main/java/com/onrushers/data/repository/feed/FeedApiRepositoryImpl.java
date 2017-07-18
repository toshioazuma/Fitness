package com.onrushers.data.repository.feed;

import com.onrushers.data.api.service.FeedService;
import com.onrushers.data.models.Feed;
import com.onrushers.domain.boundaries.FeedRepository;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IGenericResult;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class FeedApiRepositoryImpl implements FeedRepository {

	private final FeedService mFeedService;

	@Inject
	public FeedApiRepositoryImpl(FeedService service) {
		mFeedService = service;
	}

	@Override
	public Observable<IFeed> createFeed(int type, int userId, String content, String place,
	                                    List<Integer> userIds, List<Integer> photos, String accessToken) {

		final Feed feed = new Feed();
		feed.type = type;
		feed.userId = userId;
		feed.content = content;
		feed.place = place;
		feed.userTags = userIds;
		feed.photos = photos;

		feed.rushed = null;
		feed.userIds = null;

		return mFeedService.postFeed(feed, accessToken).cast(IFeed.class);
	}

	@Override public Observable<IFeed> getFeed(int feedId, String accessToken) {
		return mFeedService.getFeed(feedId, accessToken).cast(IFeed.class);
	}

	@Override
	public Observable<IGenericResult> deleteFeed(int feedId, String accessToken) {
		return mFeedService.deleteFeed(feedId, accessToken).cast(IGenericResult.class);
	}
}
