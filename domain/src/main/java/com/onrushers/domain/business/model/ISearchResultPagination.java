package com.onrushers.domain.business.model;

import java.util.List;

@Deprecated
public interface ISearchResultPagination {

	int getCount();

	int getPages();

	List<? extends ISearchResult> getItems();
}
