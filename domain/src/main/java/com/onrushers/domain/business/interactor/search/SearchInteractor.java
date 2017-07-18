package com.onrushers.domain.business.interactor.search;

import com.onrushers.domain.business.interactor.PaginationInteractor;
import com.onrushers.domain.business.type.SearchResultType;

public interface SearchInteractor extends PaginationInteractor {

	void setType(SearchResultType type);

	void setQuery(String query);
}
