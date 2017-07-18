package com.onrushers.app.common.bus.events;

import com.onrushers.domain.business.model.IFeed;

public class DeleteFeedEvent {

	private final int mFeedId;

	public DeleteFeedEvent(IFeed feed) {
		mFeedId = feed.getId();
	}

	public int getFeedId() {
		return mFeedId;
	}
}
