package com.onrushers.app.explore.tabs.photos;

public interface ExploreTabPhotosPresenter {

	void setView(ExploreTabPhotosView view);

	void onViewCreated();

	void getPhotosAtPage(int page);

	void onDestroy();
}
