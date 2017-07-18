package com.onrushers.app.feed.adapters;

import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;

public interface OnFeedRushListener {

	void onRushFeed(IFeed feed);

	void onUnrushFeed(IFeed feed, IBoost boost);
}
