package com.onrushers.app.feed.boost;

import com.onrushers.domain.business.model.IFeed;

public interface FeedBoostListPresenter {

	void setView(FeedBoostListView view);

	void setFeed(IFeed feed);

	void loadBoosts();
}
