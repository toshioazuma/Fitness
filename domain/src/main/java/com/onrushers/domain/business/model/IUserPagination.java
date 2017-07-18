package com.onrushers.domain.business.model;

import java.util.List;

public interface IUserPagination {

	int getCount();

	int getPages();

	List<? extends IUser> getItems();
}
