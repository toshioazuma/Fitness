package com.onrushers.app.user.tabs.photos;

import com.onrushers.domain.business.model.IFeed;

import java.util.List;

public interface UserTabPhotosView {

	void showFeeds(List<IFeed> feedsList, int page);
}
