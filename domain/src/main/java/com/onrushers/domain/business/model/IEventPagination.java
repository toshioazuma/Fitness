package com.onrushers.domain.business.model;

import java.util.List;

public interface IEventPagination {

	int getCount();

	int getPages();

	List<? extends IEvent> getItems();
}
