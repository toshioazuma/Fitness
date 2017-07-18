package com.onrushers.app.home;

import com.onrushers.app.feed.FeedsListPresenter;
import com.onrushers.domain.business.model.IFeed;

public interface HomePresenter extends FeedsListPresenter {

	void setView(HomeView view);

	void getPhotosSlider();

	void followUser(IFeed feed);
}
