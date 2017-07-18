package com.onrushers.app.feed;

import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;

public interface FeedsListPresenter {

	void getFeedsAtPage(int page);

	void addRush(IFeed feed);

	void removeRush(IFeed feed, IBoost boost);

	void deleteFeed(IFeed feed);

	void reportFeed(IFeed feed);

	void onDestroy();
}
