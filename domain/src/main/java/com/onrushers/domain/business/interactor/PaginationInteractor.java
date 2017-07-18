package com.onrushers.domain.business.interactor;

public interface PaginationInteractor extends Interactor {

	void setPage(int page);

	void setPageCount(int pageCount);
}
