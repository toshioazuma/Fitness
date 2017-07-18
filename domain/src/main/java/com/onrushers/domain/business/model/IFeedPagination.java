package com.onrushers.domain.business.model;

import java.util.List;

public interface IFeedPagination {

	int getCount();

	int getPages();

	List<? extends IFeed> getItems();
}
