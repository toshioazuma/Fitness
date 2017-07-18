package com.onrushers.domain.business.model;

import java.util.List;

public interface ISearchPlaceResult {

	boolean hasResult();

	List<? extends IPlaceResultItem> getItems();
}
