package com.onrushers.domain.usecases.explore;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.ExploreRepository;
import com.onrushers.domain.business.interactor.explore.GetExploreRankInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IRankPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetExploreRankUseCase extends UseCase implements GetExploreRankInteractor {

	private final ExploreRepository     mExploreRepository;
	private final AuthSessionRepository mAuthSessionRepository;

	private int mPage      = 1;
	private int mPageCount = 20;

	@Inject
	public GetExploreRankUseCase(ExploreRepository exploreRepository,
	                             AuthSessionRepository authSessionRepository,
	                             ThreadExecutor threadExecutor,
	                             PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mExploreRepository = exploreRepository;
		mAuthSessionRepository = authSessionRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IRankPagination>>() {
				@Override
				public Observable<IRankPagination> call(IAuthSession authSession) {
					return mExploreRepository.getRank(mPage, mPageCount, authSession.getToken());
				}
			});
	}

	@Override
	public void setPage(int page) {
		mPage = page;
	}

	@Override
	public void setCount(int count) {
		mPageCount = count;
	}
}
