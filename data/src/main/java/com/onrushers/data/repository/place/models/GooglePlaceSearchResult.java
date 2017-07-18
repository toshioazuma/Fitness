package com.onrushers.data.repository.place.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IPlaceResultItem;
import com.onrushers.domain.business.model.ISearchPlaceResult;

import java.util.List;

public class GooglePlaceSearchResult implements ISearchPlaceResult {

	@Expose
	List<GooglePlaceResultItem> results;

	@Override
	public boolean hasResult() {
		if (results != null && !results.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public List<? extends IPlaceResultItem> getItems() {
		return results;
	}
}
