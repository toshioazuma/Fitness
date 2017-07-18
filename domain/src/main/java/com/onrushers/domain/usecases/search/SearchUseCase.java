package com.onrushers.domain.usecases.search;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.SearchRepository;
import com.onrushers.domain.business.interactor.search.SearchInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.type.SearchResultType;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.PaginationUseCase;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class SearchUseCase extends PaginationUseCase implements SearchInteractor {

	private static final String TAG = "SearchUC";

	private final AuthSessionRepository mAuthSessionRepo;
	private final SearchRepository      mSearchRepo;

	private SearchResultType mType  = SearchResultType.Any;
	private String           mQuery = "";

	@Inject
	public SearchUseCase(AuthSessionRepository authSessionRepo, SearchRepository searchRepo,
	                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepo = authSessionRepo;
		mSearchRepo = searchRepo;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepo.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IPagination>>() {
				@Override
				public Observable<IPagination> call(IAuthSession authSession) {

					return mSearchRepo.getSearch(
						mQuery, mType, getPage(), getPageCount(), authSession.getToken());
				}
			});
	}

	@Override
	public void setType(SearchResultType type) {
		mType = type;
	}

	@Override
	public void setQuery(String query) {
		mQuery = query;
	}
}
