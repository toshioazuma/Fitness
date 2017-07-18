package com.onrushers.domain.usecases.boost;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.BoostRepository;
import com.onrushers.domain.business.interactor.boost.GetFeedBoostsInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IBoostPagination;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetFeedBoostsUseCase extends UseCase implements GetFeedBoostsInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final BoostRepository       mBoostRepository;

	private Integer mFeedId;
	private int mPage      = 1;
	private int mPageCount = 50;

	@Inject
	public GetFeedBoostsUseCase(AuthSessionRepository authSessionRepository,
	                            BoostRepository boostRepository, ThreadExecutor threadExecutor,
	                            PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mBoostRepository = boostRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IPagination>>() {
				@Override
				public Observable<IPagination> call(IAuthSession authSession) {

					final String accessToken = authSession.getToken();

					return mBoostRepository.getFeedBoosts(mFeedId, mPage, mPageCount, accessToken);
				}
			});
	}

	@Override
	public void setFeed(IFeed feed) {
		mFeedId = feed.getId();
	}

	@Override
	public void setPage(int page) {
		mPage = page;
	}
}
