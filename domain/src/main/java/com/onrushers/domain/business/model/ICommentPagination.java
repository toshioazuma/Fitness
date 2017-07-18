package com.onrushers.domain.business.model;

import java.util.List;

public interface ICommentPagination {

	int getCount();

	int getPages();

	List<? extends IComment> getItems();
}
