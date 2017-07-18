package com.onrushers.app.explore;

public interface ExplorePresenter {

	void setView(ExploreView view);

	void setQuery(String query);

	void onDestroy();
}
