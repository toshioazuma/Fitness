package com.onrushers.domain.usecases.search;

import com.onrushers.domain.boundaries.SearchRepository;
import com.onrushers.domain.business.interactor.search.SearchUserInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class SearchUserUseCase extends UseCase implements SearchUserInteractor {

	private static final String TAG = "SearchUserUC";

	private final SearchRepository mSearchRepository;

	private String mUserSlug;


	@Inject
	public SearchUserUseCase(SearchRepository searchRepository, ThreadExecutor threadExecutor,
	                         PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mSearchRepository = searchRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mSearchRepository.searchUser(mUserSlug);
	}

	@Override
	public void setSlug(String userSlug) {
		mUserSlug = userSlug;
	}
}
