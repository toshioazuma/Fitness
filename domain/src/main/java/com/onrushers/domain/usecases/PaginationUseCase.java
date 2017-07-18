package com.onrushers.domain.usecases;

import com.onrushers.domain.business.interactor.PaginationInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;

public abstract class PaginationUseCase extends UseCase implements PaginationInteractor {

	private int mPage      = 1;
	private int mPageCount = 20;

	protected PaginationUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
	}

	@Override
	public void setPage(int page) {
		mPage = page;
	}

	@Override
	public void setPageCount(int pageCount) {
		mPageCount = pageCount;
	}

	protected int getPage() {
		return mPage;
	}

	protected int getPageCount() {
		return mPageCount;
	}
}
