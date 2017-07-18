package com.onrushers.app.explore;

import com.onrushers.domain.business.model.ISearchResult;

import java.util.List;

public interface ExploreView {

	void showResultUsers(List<ISearchResult> results);
}
