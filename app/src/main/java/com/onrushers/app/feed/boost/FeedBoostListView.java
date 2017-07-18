package com.onrushers.app.feed.boost;

import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;

import java.util.List;

public interface FeedBoostListView {

	void setFeed(IFeed feed);

	void showBoosts(List<IBoost> boosts, int page);
}
