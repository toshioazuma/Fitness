package com.onrushers.domain.business.model;

import java.util.List;

public interface IPagination<T> {

	int getCount();

	int getPages();

	List<T> getItems();
}
