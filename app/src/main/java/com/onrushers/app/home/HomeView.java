package com.onrushers.app.home;

import com.onrushers.app.feed.FeedsListView;
import com.onrushers.domain.business.model.IFeed;

import java.util.List;

public interface HomeView extends FeedsListView {

	void hideTopSlider();

	void showTopSlider(List<Integer> photos);
}
