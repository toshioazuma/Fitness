package com.onrushers.app.user.tabs.feeds;

import com.onrushers.app.feed.FeedsListPresenter;

public interface UserTabFeedsPresenter extends FeedsListPresenter {

	void setView(UserTabFeedsView view);

	void setUserId(Integer userId);
}
