package com.onrushers.domain.business.model;

import java.util.List;

public interface IBoostPagination {

	int getCount();

	int getPages();

	List<? extends IBoost> getItems();
}
