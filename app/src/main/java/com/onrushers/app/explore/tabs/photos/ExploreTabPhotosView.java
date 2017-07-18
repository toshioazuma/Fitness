package com.onrushers.app.explore.tabs.photos;

import com.onrushers.domain.business.model.IFeed;

import java.util.List;

public interface ExploreTabPhotosView {

	void showFeeds(List<IFeed> feeds, int page);
}
