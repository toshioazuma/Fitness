package com.onrushers.app.feed;

import com.onrushers.domain.business.model.IFeed;

import java.util.List;

public interface FeedsListView {

	void showFeeds(List<IFeed> feeds, int page);

	void reloadView();

	void showReportSent(boolean sent);

	void showError(String errorMessage);
}
